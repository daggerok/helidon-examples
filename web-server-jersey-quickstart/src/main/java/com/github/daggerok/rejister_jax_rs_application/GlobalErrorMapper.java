package com.github.daggerok.rejister_jax_rs_application;

import lombok.extern.log4j.Log4j2;
import org.glassfish.jersey.process.internal.RequestScoped;

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
        log.error(message);

        return Response.ok(Jsonp.objectBuilder()
                                .add("error", String.format("Fallback! %s", message))
                                .add("_links", Jsonp.objectBuilder()
                                                    .add("baseUrl", uriInfo.getBaseUri().toASCIIString())
                                                    .build())
                                .build())
                       .type(MediaType.APPLICATION_JSON)
                       // .header("oops", "^_^")
                       .build();
    }
}
