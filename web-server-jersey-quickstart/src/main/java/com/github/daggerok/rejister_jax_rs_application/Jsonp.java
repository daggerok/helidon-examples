package com.github.daggerok.rejister_jax_rs_application;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import java.util.Collections;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Jsonp {

    private static final JsonBuilderFactory jsonBuilderFactory = Json.createBuilderFactory(Collections.emptyMap());

    public static JsonObjectBuilder objectBuilder() {
        return jsonBuilderFactory.createObjectBuilder();
    }
}
