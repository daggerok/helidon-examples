package com.github.daggerok;

import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

@Log4j2
public class Main {
    public static void main(String[] args) {

        ServerConfiguration config = ServerConfiguration.builder()
                                                        .port(8080)
                                                        .build();
        Routing routes = Routing.builder()
                                .any((req, res) -> res.send("It works!"))
                                .build();

        CompletableFuture<WebServer> server = WebServer.create(config, routes)
                                                       .start()
                                                       .toCompletableFuture();
        Try.of(() -> server.get(5, SECONDS))
           .onFailure(throwable -> log.error("Cannot start server {}", throwable.getLocalizedMessage()))
           .onSuccess(webServer -> log.info("WebServer started on {} port...", webServer.port()));
    }
}
