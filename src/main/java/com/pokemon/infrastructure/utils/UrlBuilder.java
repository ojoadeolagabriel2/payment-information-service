package com.pokemon.infrastructure.utils;

import java.net.URI;
import java.net.URISyntaxException;

import static java.net.URLEncoder.encode;
import static java.nio.charset.Charset.defaultCharset;

public class UrlBuilder {

    public static String getPokemonApiUrl(String name, String host) throws URISyntaxException {
        return new URI(host + "/api/v2/pokemon-species/" + name).toString();
    }

    public URI getShakeSpeareApiUrl(String text, String host) throws URISyntaxException {
        return new URI(host + "/translate/shakespeare.json?text=" + encode(text.replaceAll("\\r|\\n", " "), defaultCharset()));
    }
}
