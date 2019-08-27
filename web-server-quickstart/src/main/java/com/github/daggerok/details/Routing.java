package com.github.daggerok.details;

import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

@Log4j2
public class Routing {
    public static void main(String[] args) {

        ServerConfiguration configuration = ServerConfiguration.builder()
                                                               .port(8080)
                                                               .build();
        io.helidon.webserver.Routing routes = io.helidon.webserver.Routing.builder()
                                                                          // path patterns...
                                                                          .get("/foo/bar/baz",
                                                                               (req, res) -> res.send("Exact path match against resolved path even with non-usual characters"))
                                                                          .get("/foo/{}/baz",
                                                                               (req, res) -> res.send("{} Unnamed regular expression segment ([^/]+)"))
                                                                          .get("/foo/{var}/baz",
                                                                               (req, res) -> res.send("Named regular expression segment ([^/]+)"))
                                                                          .get("/foo/{var:\\d+}",
                                                                               (req, res) -> res.send("Named regular expression segment with a specified expression"))
                                                                          .get("/foo/{:\\d+}",
                                                                               (req, res) -> res.send("Unnamed regular expression segment with a specified expression"))
                                                                          .get("/foo/{+var}",
                                                                               (req, res) -> res.send("Convenience shortcut for {var:.+}. A matcher is not a true URI template (as defined by RFC) but this convenience is in sync with the Apiary templates"))
                                                                          .get("/foo/{+}",
                                                                               (req, res) -> res.send("Convenience shortcut for unnamed segment with regular expression {:.+}"))
                                                                          .get("/foo[/bar]",
                                                                               (req, res) -> res.send("An optional block, which translates to the /foo(/bar)? regular expression"))
                                                                          // just methods...
                                                                          .get((req, res) -> res.send("GET method"))
                                                                          .post((req, res) -> res.send("POST method"))
                                                                          // or fallback...
                                                                          .any((req, res) -> res.send("This is a fallback route!"))
                                                                          .build();

        CompletableFuture<WebServer> server = WebServer.create(configuration, routes)
                                                       .start()
                                                       .toCompletableFuture();
        Try.of(() -> server.get(5, SECONDS))
           .onFailure(throwable -> log.error("Cannot start server {}", throwable.getLocalizedMessage()))
           .onSuccess(webServer -> log.info("WebServer started on {} port...", webServer.port()));
    }
}
