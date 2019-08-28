package com.github.daggerok;

import io.helidon.common.http.Http;
import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.media.jsonp.common.JsonProcessing;
import io.helidon.media.jsonp.server.JsonSupport;
import io.helidon.webserver.Handler;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonCollectors;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.Collections.singletonList;

@Log4j2
public class Main {

    public static void main(String[] args) {

        Config config = Config.builder()
                              // // this is how we can make our proprs prefixed: xxx.yyy=zzz -> prefixed.xxx.yyy=zzz
                              // .sources(ConfigSources.prefixed("webserver", ConfigSources.classpath("conf/application.conf")))

                              // // any of these props are working in a same way:
                              .sources(ConfigSources.classpath("conf/application.conf"))
                              // .sources(ConfigSources.classpath("conf/application.json"))
                              // .sources(ConfigSources.classpath("conf/application.properties"))
                              // .sources(ConfigSources.classpath("conf/application.yaml"))
                              .disableEnvironmentVariablesSource()
                              .disableSystemPropertiesSource()
                              .build();

        // if ("Mac OS X".equalsIgnoreCase(System.getProperty("os.name"))) // local (only) debug
        config.asMap().ifPresent(map -> map.forEach((k, v) -> log.debug("{}='{}'", k, v)));

        ServerConfiguration configuration = ServerConfiguration.create(config);

        Handler jsonpHandler = Handler.create(JsonObject.class, (req, res, jsonObject) -> {
            log.debug("received jsonObject: {}", jsonObject);
            res.send(Json.createObjectBuilder()
                         .add("received", jsonObject)
                         .add("jsonArray",
                              config.asMap()
                                    .orElse(new HashMap<>())
                                    .entrySet()
                                    .stream()
                                    .map(entry -> Json.createObjectBuilder()
                                                      .add(entry.getKey(), entry.getValue())
                                                      .build())
                                    .collect(JsonCollectors.toJsonArray()))
                         .add("jsonObject",
                              config.asMap()
                                    .orElse(new HashMap<>())
                                    .entrySet()
                                    .stream() // <-- also worked with .parallelStream()
                                    .collect(FoldLibrary.entriesToConcurrentHashMap)
                                    .entrySet()
                                    .stream() // <-- also worked with .parallelStream() too!
                                    .reduce(Json.createObjectBuilder(),
                                            // <-- initial state
                                            // // add -> merge into same type as initial state
                                            // // NOTE: this variant isn't worked at all!
                                            // (jsonBuilder, entry) -> Json.createObjectBuilder()
                                            //                             .add(entry.getKey(),
                                            //                                  entry.getValue()),
                                            // // mutate same jsonBuilder object to make it work:
                                            (jsonBuilder, entry) -> jsonBuilder.add(entry.getKey(), entry.getValue()),
                                            // combine -> merge (needed only when running in parallel)
                                            (jsonBuilder1, jsonBuilder2) -> Json.createObjectBuilder()
                                                                                .addAll(jsonBuilder1)
                                                                                .addAll(jsonBuilder2))
                                    .build())
                         .build());
        });

        // Handler fallback = Handler.create(JsonObject.class,
        //                                   (req, res, whatever) ->
        //                                           Json.createObjectBuilder()
        //                                               .add("GET", "/conf")
        //                                               .build());

        Handler oops = (req, res) -> {
            // res.headers().add("Content-Type", "application/json");
            res.status(Http.ResponseStatus.create(200))
               .send(String.format(
                       "curl -XPOST http://%s/conf -d'{\"k\":\"v\"}' -H'Content-Type:application/json' | jq '.'",
                       req.headers()
                          .toMap()
                          .getOrDefault("Host", singletonList("http://127.0.0.1:8080"))
                          .iterator()
                          .next()));
        };

        Routing routing = Routing.builder()
                                 .register(JsonSupport.create(JsonProcessing.create()))
                                 .post("/conf", jsonpHandler)
                                 .any("/{+}", oops)
                                 .build();

        WebServer webServer = WebServer.create(configuration, routing);

        Try.of(() -> webServer.start()
                              .toCompletableFuture()
                              .get(10, TimeUnit.SECONDS))

           .onSuccess(server -> log.info("Server started on {} port ({})", webServer.port(), webServer == server))
           .onFailure(throwable -> log.error("oops! {}", throwable.getLocalizedMessage()));
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class FoldLibrary {

        /*
        private static final <U> Collection<U> combine(Collection<U> a, Collection<U> b) {
            CopyOnWriteArrayList<U> result = new CopyOnWriteArrayList<>(a);
            result.addAll(new CopyOnWriteArrayList<>(b));
            return result;
        }

        private static final <U> Collection<U> add(Collection<U> list, U i) {
            CopyOnWriteArrayList<U> result = new CopyOnWriteArrayList<>(list);
            result.add(i);
            return result;
        }
        */

        private static final Collector<Map.Entry<String, String>, ConcurrentHashMap<String, String>, Map<String, String>> entriesToConcurrentHashMap =
                new Collector<Map.Entry<String, String>, ConcurrentHashMap<String, String>, Map<String, String>>() {

                    @Override
                    public Supplier<ConcurrentHashMap<String, String>> supplier() {
                        return ConcurrentHashMap::new;
                    }

                    @Override
                    public BiConsumer<ConcurrentHashMap<String, String>, Map.Entry<String, String>> accumulator() {
                        return (map, entry) -> map.put(entry.getKey(), entry.getValue());
                    }

                    @Override
                    public BinaryOperator<ConcurrentHashMap<String, String>> combiner() {
                        return (map1, map2) -> {
                            map1.putAll(map2);
                            return map1;
                        };
                    }

                    @Override
                    public Function<ConcurrentHashMap<String, String>, Map<String, String>> finisher() {
                        // return a -> a; // can work with String -> List<String>
                        // return Function.identity(); // can work with String -> List<String>
                        return ConcurrentHashMap::new;
                    }

                    @Override
                    public Set<Characteristics> characteristics() {
                        return Collections.unmodifiableSet(
                                EnumSet.of(
                                        Characteristics.IDENTITY_FINISH,
                                        Characteristics.CONCURRENT
                                )
                        );
                    }
                };
    }
}
