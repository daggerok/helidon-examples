package com.github.daggerok.just_register_resource;

import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
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
    private ServerRequest request;

    @Context
    private ServerResponse response;

    @Context
    UriInfo uriInfo;

    @GET
    @Path("")
    public Response hello() {
        // return Response.ok(singletonMap("message", request.remotePort()))
        //                .build();
        URI uri = uriInfo.getAbsolutePath();
        String basePath = String.format("%s://%s", uri.getScheme(), uri.getAuthority());

        log.info("base path: {}", basePath);
        return Response.ok(HashMap.of("message", "Hello, World!",
                                      "_links", singletonMap("baseUrl", basePath))
                                  .toJavaMap())
                       .build();
    }
}
