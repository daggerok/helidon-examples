package com.github.daggerok;

import io.vavr.collection.HashMap;
import lombok.extern.log4j.Log4j2;
import org.glassfish.jersey.process.internal.RequestScoped;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.net.URI;

import static java.util.Collections.singletonMap;

@Log4j2
@Provider
@RequestScoped
public class GlobalErrorMapper implements ExceptionMapper<Throwable> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable throwable) {
        log.info("fallback!");
        return Response.ok(HashMap.of("result", "Fallback!",
                                      "_links", singletonMap("baseUrl", getBaseUrl()))
                                  .toJavaMap())
                       .type(MediaType.APPLICATION_JSON)
                       .header("oops", "^_^")
                       .build();
    }

    private String getBaseUrl() {
        URI uri = uriInfo.getAbsolutePath();
        return String.format("%s://%s", uri.getScheme(), uri.getAuthority());
    }
}
