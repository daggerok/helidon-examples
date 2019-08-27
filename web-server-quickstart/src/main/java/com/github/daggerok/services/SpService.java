package com.github.daggerok.services;

import io.helidon.webserver.Routing;
import io.helidon.webserver.Service;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SpService implements Service {

    @Override
    public void update(Routing.Rules rules) {
        log.debug("rules: {}", rules);
        rules.get("/sp", (serverRequest, serverResponse) -> serverResponse.send("Spain!"));
    }
}
