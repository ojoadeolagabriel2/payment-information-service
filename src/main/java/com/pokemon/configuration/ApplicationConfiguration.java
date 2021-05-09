package com.pokemon.configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.pokemon.infrastructure.data.PokemonEntity;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.concurrent.TimeUnit.MINUTES;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

    private static final Integer MAX_POKEMON_IN_CACHE = 1000;

    @Bean
    public Cache<String, PokemonEntity> pokemonCache(@Value("${app.max_cache_expiry_in_minutes}") Integer maxCacheExpiryInMinutes) {
        return Caffeine.newBuilder()
                .expireAfterWrite(maxCacheExpiryInMinutes, MINUTES)
                .maximumSize(MAX_POKEMON_IN_CACHE)
                .build();
    }

    @Bean
    public CloseableHttpClient httpClient(
            @Value("${app.conn_req_timeout:5000}") Integer connectionRequestTimeOut,
            @Value("${app.conn_timeout:5000}") Integer connectionTimeOut,
            @Value("${app.socket_timeout:5000}") Integer socketTimeOut
    ) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(connectionRequestTimeOut)
                .setConnectTimeout(connectionTimeOut)
                .setSocketTimeout(socketTimeOut)
                .build();

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .build();
    }
}