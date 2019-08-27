package com.github.daggerok.services;

import io.helidon.webserver.Routing;
import io.helidon.webserver.Service;
import lombok.extern.log4j.Log4j2;

import java.util.function.Supplier;

@Log4j2
public class EnService implements Service, Supplier<EnService> {

    @Override
    public void update(Routing.Rules rules) {
        log.debug("rules: {}", rules);
        rules.get("/en", (serverRequest, serverResponse) -> serverResponse.send("English!"));
    }

    @Override
    public EnService get() {
        log.debug("en service get");
        return this;
    }
}
