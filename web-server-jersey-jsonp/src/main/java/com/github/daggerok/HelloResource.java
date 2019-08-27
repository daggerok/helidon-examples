package com.github.daggerok;

import lombok.extern.log4j.Log4j2;
import org.glassfish.jersey.process.internal.RequestScoped;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static java.util.Collections.singletonMap;

@Log4j2
@Path("")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelloResource {

    @Context
    UriInfo uriInfo;

    @POST
    @Path("")
    public Response jsonify(JsonObject request) {
        log.debug("consuming {}", request.toString());
        return Response.accepted()
                       .entity(singletonMap("jsonify", request.toString()))
                       .build();
    }

    @GET
    @Path("")
    public JsonObject hello() {
        log.info("hello!");

        return Json.createObjectBuilder()
                   .add("message", "Hello, World!")
                   .add("_links", Json.createObjectBuilder()
                                      .add("baseUrl", Hateoas.baseUrl(uriInfo))
                                      .build())
                   .build();
    }
}
