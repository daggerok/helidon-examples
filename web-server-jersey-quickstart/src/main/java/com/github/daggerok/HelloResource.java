package com.github.daggerok;

import lombok.extern.log4j.Log4j2;
import org.glassfish.jersey.process.internal.RequestScoped;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Log4j2
@Path("")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HelloResource {

    @Context
    UriInfo uriInfo;

    @GET
    @Path("")
    public JsonObject hello() {
        log.info("hello!");
        return Jsonp.objectBuilder()
                    .add("message", "Hello, World!")
                    .add("_links", Jsonp.objectBuilder()
                                        .add("baseUrl", uriInfo.getBaseUri().toString())
                                        .build())
                    .build();
    }
}
