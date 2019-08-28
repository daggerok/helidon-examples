package com.github.daggerok.data.account;

import org.apache.deltaspike.data.api.FullEntityRepository;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.jpa.api.entitymanager.EntityManagerConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.FlushModeType;

@Repository
@ApplicationScoped
@EntityManagerConfig(flushMode = FlushModeType.COMMIT)
public interface BankAccountRepository extends FullEntityRepository<BackAccount, String> {
}
