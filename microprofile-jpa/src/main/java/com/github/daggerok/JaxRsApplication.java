package com.github.daggerok;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.github.daggerok.data.EntityManagerProvider;
import com.github.daggerok.data.account.BackAccount;
import com.github.daggerok.data.account.BankAccountRepository;
import com.github.daggerok.data.account.BankAccountResource;
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
        return HashSet.of(// Infrastructure
                          Links.class,
                          HelloResource.class,
                          GlobalExceptionMapper.class,
                          // JSON-P JsonObject
                          JacksonJaxbJsonProvider.class, // requires jackson-jaxrs-json-provider
                          // JPA
                          BackAccount.class,
                          BankAccountResource.class,
                          BankAccountRepository.class,
                          EntityManagerProvider.class)
                      .toJavaSet();
    }
}
