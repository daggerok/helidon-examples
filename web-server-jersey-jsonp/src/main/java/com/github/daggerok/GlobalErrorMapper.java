package com.github.daggerok;

import lombok.extern.log4j.Log4j2;
import org.glassfish.jersey.process.internal.RequestScoped;

import javax.json.Json;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Log4j2
@Provider
@RequestScoped
public class GlobalErrorMapper implements ExceptionMapper<Throwable> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable throwable) {

        String message = throwable.getLocalizedMessage();
        String error = String.format("Fallback %s", message);

        log.info(message);
        return Response.ok(Json.createObjectBuilder()
                               .add("error", error)
                               .add("_links", Json.createObjectBuilder()
                                                  .add("baseUrl", Hateoas.baseUrl(uriInfo))
                                                  .build())
                               .build())
                       .type(MediaType.APPLICATION_JSON)
                       // .header("oops", "^_^")
                       .build();
    }
}
