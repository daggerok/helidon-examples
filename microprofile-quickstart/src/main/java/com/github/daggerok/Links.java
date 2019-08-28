package com.github.daggerok;

import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@RequestScoped
public class Links {

    @Context
    UriInfo uriInfo;

    public JsonObject create() {
        return Json.createObjectBuilder()
                   .add("hello GET", joinAll())
                   .add("jsongify POST", joinAll())
                   .add("openapi GET", joinAll("openapi"))
                   .add("health GET", joinAll("health"))
                   .add("metrics GET", joinAll("metrics"))
                   .add("static-resources GET",
                        joinAll("static", "microprofile-quickstart-integration-tests.http"))
                   .build();
    }

    private String joinAll(String... paths) {
        String baseUrl = uriInfo.getBaseUri().toASCIIString();
        return baseUrl + String.join("/", paths);
    }
}
