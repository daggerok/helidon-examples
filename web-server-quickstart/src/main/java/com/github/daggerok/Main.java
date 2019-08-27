package com.github.daggerok;

import io.helidon.common.http.MediaType;
import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.webserver.RequestPredicate;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import sun.security.x509.IPAddressName;

import java.net.InetAddress;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

@Log4j2
public class Main {
    public static void main(String[] args) {

        ServerConfiguration configuration = ServerConfiguration.builder()
                                                               .port(8080)
                                                               .build();
        Routing routes = Routing.builder()
                                .get((req, res) -> res.send("Hola! POST something to me!"))
                                .any(RequestPredicate.create()
                                                     .hasContentType(MediaType.APPLICATION_JSON)
                                                     .containsHeader("Called-By", "human")
                                                     .thenApply((req, res) -> res.send("Hey!"))
                                                     .otherwise((req, res) -> res.send("R-O-B-O-t@...!")))
                                .build();

        CompletableFuture<WebServer> server = WebServer.create(configuration, routes)
                                                       .start()
                                                       .toCompletableFuture();
        Try.of(() -> server.get(5, SECONDS))
           .onFailure(throwable -> log.error("Cannot start server {}", throwable.getLocalizedMessage()))
           .onSuccess(webServer -> log.info("WebServer started on {} port...", webServer.port()));
    }
}
