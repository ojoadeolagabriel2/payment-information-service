package com.pokemon.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.pokemon.infrastructure.entity.PokemonEntity;
import com.pokemon.infrastructure.service.PokemonRepositoryService;
import com.pokemon.service.PokemonCachedRepositoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class PokemonCachedRepositoryServiceTest {

    private static final Integer CACHED_CALL_COUNTER = 1;
    private static final Integer UNCACHED_CALL_COUNTER = 0;
    private static final String TEST_POKEMON_NAME = "pikachu";

    @MockBean
    PokemonRepositoryService pokemonRepositoryService;

    @Autowired
    Cache<String, PokemonEntity> cache;

    PokemonCachedRepositoryService pokemonCachedRepositoryService;

    @Before
    public void setup() {
        pokemonCachedRepositoryService = new PokemonCachedRepositoryServiceImpl(pokemonRepositoryService, cache);
    }

    @Test
    public void getByPokemonName_ValidName_UnCachedThenCached() {
        // when repo service is called with pokemon name
        when(pokemonRepositoryService.getPokemonByName(TEST_POKEMON_NAME)).thenReturn(Optional.of(PokemonEntity.builder().name(TEST_POKEMON_NAME).build()));

        // and cached repo service is calls getPokemonByName
        pokemonCachedRepositoryService.getPokemonByName(TEST_POKEMON_NAME).get();

        // verify uncached call is made
        verify(pokemonRepositoryService, times(CACHED_CALL_COUNTER)).getPokemonByName(TEST_POKEMON_NAME);

        reset(pokemonRepositoryService);

        // and when cached repo is called subsequently
        pokemonCachedRepositoryService.getPokemonByName(TEST_POKEMON_NAME).get();

        // verify cached call is made
        verify(pokemonRepositoryService, times(UNCACHED_CALL_COUNTER)).getPokemonByName(TEST_POKEMON_NAME);
    }
}