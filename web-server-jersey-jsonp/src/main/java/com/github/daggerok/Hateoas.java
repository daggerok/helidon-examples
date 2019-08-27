package com.github.daggerok;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Hateoas {

    public static String baseUrl(UriInfo uriInfo) {
        URI uri = uriInfo.getAbsolutePath();
        return String.format("%s://%s", uri.getScheme(), uri.getAuthority());
    }
}
