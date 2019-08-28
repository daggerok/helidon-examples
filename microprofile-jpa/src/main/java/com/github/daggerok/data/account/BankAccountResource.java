package com.github.daggerok.data.account;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.stream.JsonCollectors;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Transactional
@RequestScoped
@Path("/bank-account")
public class BankAccountResource {

    @Inject
    BankAccountRepository repository;

    @GET
    @Path("{id}")
    public JsonObject register(@PathParam("id") UUID id) {
        return toJsonObject(repository.findBy(id.toString()));
    }

    @POST
    @Path("")
    public BackAccount register(JsonObject request) {
        String username = request.getString("username", "anonymous");
        BigDecimal balance = Optional.ofNullable(request.getJsonNumber("balance"))
                                     .map(JsonNumber::bigDecimalValue)
                                     .orElse(new BigDecimal("0.00"));
        // BigDecimal balance = Optional.of(request.getString("balance", "0.00"))
        //                              .map(BigDecimal::new)
        //                              .orElse(new BigDecimal("0.00"));
        return repository.save(BackAccount.of(username, balance));
    }

    @GET
    @Path("")
    public JsonArray getAll() {
        return toJsonArray(repository.findAll());
    }

    private static JsonArray toJsonArray(Collection<BackAccount> backAccounts) {
        return backAccounts.stream()
                           .map(jsonObjectMapper())
                           .collect(JsonCollectors.toJsonArray());
    }

    private static JsonObject toJsonObject(BackAccount backAccount) {
        return Optional.ofNullable(backAccount)
                       .map(jsonObjectMapper())
                       .orElse(Json.createObjectBuilder()
                                   .build());
    }

    private static Function<BackAccount, JsonObject> jsonObjectMapper() {
        return ba -> Json.createObjectBuilder()
                         .add("id", ba.getId())
                         .add("username", ba.getUsername())
                         .add("balance", ba.getBalance())
                         .build();
    }
}
