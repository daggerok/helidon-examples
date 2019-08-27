package com.github.daggerok.rejister_jax_rs_application;

import io.vavr.collection.HashMap;
import lombok.extern.log4j.Log4j2;
import org.glassfish.jersey.process.internal.RequestScoped;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static java.util.Collections.singletonMap;

@Log4j2
@Path("")
@RequestScoped // required if you need @Context
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelloResource {

    @Context
    UriInfo uriInfo;

    @GET
    @Path("")
    public Response hello() {
        log.info("hello!");
        return Response.ok(HashMap.of("message", "Hello, World!",
                                      "_links", singletonMap("baseUrl", getBaseUrl()))
                                  .toJavaMap())
                       .build();
    }

    private String getBaseUrl() {
        URI uri = uriInfo.getAbsolutePath();
        return String.format("%s://%s", uri.getScheme(), uri.getAuthority());
    }
}
