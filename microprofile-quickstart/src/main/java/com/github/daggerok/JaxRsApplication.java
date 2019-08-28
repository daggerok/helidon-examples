package com.github.daggerok;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import io.vavr.collection.HashSet;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

@ApplicationScoped
@ApplicationPath("/")
public class JaxRsApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return HashSet.of(Links.class,
                          HelloResource.class,
                          GlobalExceptionMapper.class,
                          JacksonJaxbJsonProvider.class) // requires jackson-jaxrs-json-provider
                      .toJavaSet();
    }
}
