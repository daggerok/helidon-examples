package com.github.daggerok.data;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.interceptor.Interceptor;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@ApplicationScoped
@Priority(Interceptor.Priority.APPLICATION)
public class EntityManagerProvider {

    @PersistenceUnit(unitName = "DDD")
    private EntityManagerFactory entityManagerFactory;

    @Default
    @Produces
    // @javax.enterprise.context.RequestScoped
    @ApplicationScoped // both scopes works fine
    public EntityManager create() {
        return this.entityManagerFactory.createEntityManager();
    }

    public void dispose(@Disposes @Default EntityManager entityManager) {
        if (entityManager.isOpen()) entityManager.close();
    }
}
