package com.github.daggerok.rejister_jax_rs_application;

import io.vavr.collection.HashSet;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

@ApplicationPath("/")
public class JaxRsApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return HashSet.of(HelloResource.class,
                          GlobalErrorMapper.class)
                      .toJavaSet();
    }
}