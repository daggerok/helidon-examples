package com.github.daggerok;

import lombok.extern.log4j.Log4j2;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Log4j2
@Provider
@RequestScoped
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Inject
    Links links;

    @PostConstruct
    public void initialization() {
        log.info("Register global error handler!");
    }

    @Override
    public Response toResponse(Exception e) {

        String error = e.getLocalizedMessage();
        if (log.isDebugEnabled()) log.debug(error, e);
        else log.error(error);

        return Response.status(BAD_REQUEST)
                       .entity(Json.createObjectBuilder()
                                   .add("error", error)
                                   .add("_links", links.create())
                                   .build())
                       .type(MediaType.APPLICATION_JSON)
                       .header("oops", "^_^")
                       .build();
    }
}
