package com.github.daggerok.details;

import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * 1) Working for me:
 * - create src/main/resources/application.properties file
 * - use it as ConfigSource.classpath resource to create server configuration
 *   (see 33 line)
 *
 * 2) This format is not working for me...
 * webserver {
 *   port: 8080,
 *   bind-address: "0.0.0.0",
 * }
 */
@Log4j2
public class WebServerConfigurationFile {
    public static void main(String[] args) {

        Config config = Config.builder()
                              .sources(ConfigSources.classpath("application.properties"))
                              .build();
        ServerConfiguration configuration = ServerConfiguration.create(config);
        Routing routes = Routing.builder()
                                .any((req, res) -> res.send(
                                        "WebServer configuration should be taken from application.properties file!"
                                ))
                                .build();

        CompletableFuture<WebServer> server = WebServer.create(configuration, routes)
                                                       .start()
                                                       .toCompletableFuture();
        Try.of(() -> server.get(5, SECONDS))
           .onFailure(throwable -> log.error("Cannot start server {}", throwable.getLocalizedMessage()))
           .onSuccess(webServer -> log.info("WebServer started on {} port...", webServer.port()));
    }
}
