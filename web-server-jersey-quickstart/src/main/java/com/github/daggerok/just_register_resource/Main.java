package com.github.daggerok.just_register_resource;

import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.jersey.JerseySupport;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.util.concurrent.TimeUnit;

@Log4j2
public class Main {
    public static void main(String[] args) {

        val server = WebServer.create(ServerConfiguration.builder()
                                                         .port(8080)
                                                         .build(),
                                      Routing.builder()
                                             .register("/", JerseySupport.builder()
                                                                         .register(HelloResource.class)
                                                                         .build())
                                             .build())
                              .start()
                              .toCompletableFuture();

        Try.of(() -> server.get(10, TimeUnit.SECONDS))
           .onSuccess(webServer -> log.info("server started on {} port.", webServer.port()))
           .onFailure(throwable -> log.error("oops! {}", throwable.getLocalizedMessage()));
    }
}
