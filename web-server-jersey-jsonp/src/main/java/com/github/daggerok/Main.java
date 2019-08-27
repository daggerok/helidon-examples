package com.github.daggerok;

import io.helidon.media.jsonp.server.JsonSupport;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.jersey.JerseySupport;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import static java.util.concurrent.TimeUnit.SECONDS;

@Log4j2
public class Main {

    public static void main(String[] args) {

        Try.of(() -> WebServer.create(ServerConfiguration.builder()
                                                         .port(8080)
                                                         .build(),
                                      Routing.builder()
                                             .register("/", JsonSupport.create())
                                             .register("/",
                                                       JerseySupport.builder(new JaxRsApplication())
                                                                    .build())
                                             .build())
                              .start()
                              .toCompletableFuture()
                              .get(10, SECONDS))
           .onSuccess(webServer -> log.info("server started on {} port.", webServer.port()))
           .onFailure(throwable -> log.error("oops! {}", throwable.getLocalizedMessage()));
    }
}
