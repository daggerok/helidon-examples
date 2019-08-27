package com.github.daggerok.details;

import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * You can start server without configuration,
 * (see line 25), so server port will take
 * free available port randomly...
 */
@Log4j2
public class RandomWebServerPort {
    public static void main(String[] args) {

        Routing routes = Routing.builder()
                                .any((req, res) -> res.send("Port should be assigned randomly!"))
                                .build();

        CompletableFuture<WebServer> server = WebServer.create(routes)
                                                       .start()
                                                       .toCompletableFuture();
        Try.of(() -> server.get(5, SECONDS))
           .onFailure(throwable -> log.error("Cannot start server {}", throwable.getLocalizedMessage()))
           .onSuccess(webServer -> log.info("WebServer started on {} port...", webServer.port()));
    }
}
