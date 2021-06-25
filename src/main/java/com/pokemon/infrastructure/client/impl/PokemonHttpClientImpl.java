package com.pokemon.infrastructure.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemon.infrastructure.client.PokemonClient;
import com.pokemon.infrastructure.client.data.PokemonSpecie;
import com.pokemon.infrastructure.client.data.PokemonSpecieFlavor;
import com.pokemon.infrastructure.entity.PokemonEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import static com.pokemon.infrastructure.utils.UrlBuilder.getPokemonApiUrl;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Component
public class PokemonHttpClientImpl implements PokemonClient {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final String gameVersionName;
    private final String pokemonApiBaseV2Url;

    public PokemonHttpClientImpl(
            @Value("${app.pokemon_io_host_url}") String pokemonApiBaseV2Url,
            @Value("${app.game_version_name}") String gameVersionName
    ) {
        this.gameVersionName = gameVersionName;
        this.pokemonApiBaseV2Url = pokemonApiBaseV2Url;
    }

    @Override
    public Optional<PokemonEntity> getPokemonByName(String name) {
        try {
            final HttpGet httpGet = new HttpGet(getPokemonApiUrl(name, pokemonApiBaseV2Url));

            CloseableHttpResponse response = httpClient().execute(httpGet);
            if (response.getStatusLine().getStatusCode() == OK.value()) {
                var responseBody = EntityUtils.toString(response.getEntity());

                final PokemonSpecie specie = objectMapper.readValue(responseBody, PokemonSpecie.class);
                final Optional<PokemonSpecieFlavor> possibleFlavor = specie
                        .getFlavorTextEntries()
                        .stream()
                        .filter(c -> c.getVersion().getName().equals(gameVersionName))
                        .findFirst();

                if (possibleFlavor.isPresent()) {
                    final PokemonSpecieFlavor flavor = possibleFlavor.get();
                    return Optional.of(PokemonEntity.builder().name(name).description(flavor.getFlavorText()).build());
                }
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private CloseableHttpClient httpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(5000)
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .build();

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .build();
    }
}
