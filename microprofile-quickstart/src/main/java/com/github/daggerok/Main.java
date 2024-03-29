package com.github.daggerok;

import io.helidon.config.Config;
import io.helidon.microprofile.server.Server;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.time.ZonedDateTime;

import static io.helidon.config.ConfigSources.classpath;
import static java.time.ZonedDateTime.now;

@Log4j2
public class Main {

    public static void main(String[] args) {

        ZonedDateTime startedAt = now();
        Try.of(() -> Server.builder()
                           .config(Config.builder()
                                         .sources(classpath("application.conf"))
                                         .disableEnvironmentVariablesSource()
                                         .disableSystemPropertiesSource()
                                         .disableCaching()
                                         .build())
                           .addApplication(JaxRsApplication.class)
                           .build()
                           .start())
           .onSuccess(server -> log.info("Helidon MicroProfile {}:{} server started in {} s.",
                                         server.host(), server.port(), Duration.between(startedAt, now())
                                                                               .toMillis() / 1000.0))
           .onFailure(throwable -> log.error(throwable.getLocalizedMessage(), throwable));

        // io.helidon.microprofile.server.Main.main(args);

        // Try.of(() -> WebServer.create(ServerConfiguration.builder()
        //                                                  .port(8080)
        //                                                  .build(),
        //                               Routing.builder()
        //                                      .register("/", JsonSupport.create())
        //                                      .register("/",
        //                                                JerseySupport.builder(new JaxRsApplication())
        //                                                             .build())
        //                                      .build())
        //                       .start()
        //                       .toCompletableFuture()
        //                       .get(10, SECONDS))
        //    .onSuccess(webServer -> log.info("server started on {} port.", webServer.port()))
        //    .onFailure(throwable -> log.error("oops! {}", throwable.getLocalizedMessage()));
    }
}
