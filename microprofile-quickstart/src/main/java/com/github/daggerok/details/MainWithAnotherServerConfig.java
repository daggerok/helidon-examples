package com.github.daggerok.details;

import com.github.daggerok.JaxRsApplication;
import io.helidon.config.Config;
import io.helidon.microprofile.server.Server;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import static io.helidon.config.ConfigSources.classpath;

@Log4j2
public class MainWithAnotherServerConfig {

    public static void main(String[] args) {

        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");

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
           .onSuccess(server -> log.info("Server listening {}:{}", server.host(), server.port()))
           .onFailure(throwable -> log.error(throwable.getLocalizedMessage(), throwable));
    }
}
