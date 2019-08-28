package com.github.daggerok;

import lombok.extern.log4j.Log4j2;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static java.util.Collections.singletonMap;

@Log4j2
@Path("")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelloResource {

    @Inject
    Links links;

    @GET
    @Path("")
    public JsonObject hello() {
        if (log.isDebugEnabled()) log.debug("...");
        return Json.createObjectBuilder()
                   .add("message", "Hello, World!")
                   .add("_links", links.create())
                   .build();
    }

    @POST
    @Path("")
    public Response jsonify(JsonObject request) {
        if (log.isDebugEnabled()) log.debug(request.toString());
        return Response.accepted()
                       .entity(singletonMap("jsonify", request.toString()))
                       .build();
    }
}
