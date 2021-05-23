package com.pokemon.infrastructure.client.impl;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.pokemon.infrastructure.client.PokemonClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.Options.DYNAMIC_PORT;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class PokemonHttpClientTest {

    private static final String TEST_HOST = "http://localhost";
    private static final String DEFAULT_GAME_NAME = "ruby";
    private static final String VALID_POKEMON_NAME = "charizard";
    private static final String UNKNOWN_POKEMON_NAME = "charizardx";

    PokemonClient pokemonClient;
    static WireMockServer wireMockServer = new WireMockServer(DYNAMIC_PORT);

    @Before
    public void setup() {
        wireMockServer.start();
        pokemonClient = new PokemonHttpClientImpl(getTestHost(), DEFAULT_GAME_NAME);
    }

    @SneakyThrows
    @Test
    public void getPokemonByName_ValidName_ReturnPokemonDescription() {
        // given
        wireMockServer.stubFor(get(urlEqualTo("/api/v2/pokemon-species/" + VALID_POKEMON_NAME))
                .willReturn(aResponse().withStatus(OK.value()).withBodyFile("chirizard_200.json")));

        // when
        var pokemon = pokemonClient.getPokemonByName(VALID_POKEMON_NAME).orElseThrow();

        //then
        assertEquals(VALID_POKEMON_NAME, pokemon.getName());
        assertEquals("CHARIZARD flies around the sky in\n" +
                "search of powerful opponents.\n" +
                "It breathes fire of such great heat\f" +
                "that it melts anything. However, it\n" +
                "never turns its fiery breath on any\n" +
                "opponent weaker than itself.", pokemon.getDescription());
    }

    @SneakyThrows
    @Test
    public void getPokemonByName_UnKnownPokemonName_ReturnPokemonNotFound() {
        // given
        wireMockServer.stubFor(get(urlEqualTo("/api/v2/pokemon-species/" + UNKNOWN_POKEMON_NAME))
                .willReturn(aResponse().withStatus(NOT_FOUND.value()).withBodyFile("chirizard_404.txt")));

        // when
        var possiblePokemon = pokemonClient.getPokemonByName(UNKNOWN_POKEMON_NAME);

        //then
        assertFalse(possiblePokemon.isPresent());
    }

    private String getTestHost() {
        return format("%s:%d", TEST_HOST, wireMockServer.port());
    }
}