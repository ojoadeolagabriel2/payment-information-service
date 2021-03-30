package com.pokemon.service.impl;

import com.pokemon.domain.data.Pokemon;
import com.pokemon.domain.error.InvalidPokemonNameException;
import com.pokemon.infrastructure.data.PokemonEntity;
import com.pokemon.service.PokemonCachedRepositoryService;
import com.pokemon.service.PokemonLookupService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class PokemonLookupServiceTest {

    private static final String EMPTY_NAME = "";
    private static final String NULL_NAME = null;

    @Autowired
    PokemonLookupService pokemonLookupService;

    @MockBean
    PokemonCachedRepositoryService pokemonCachedRepositoryService;

    @Test
    public void getByPokemonName_ValidName_ReturnPokemon() {
        // given
        String validName = "charizard";
        String description = "can shoot fire";

        // when
        when(pokemonCachedRepositoryService.getPokemonByName(validName)).thenReturn(
                Optional.of(PokemonEntity.builder().name(validName).description(description).build())
        );

        // then
        final Pokemon pokemon = pokemonLookupService.getPokemonByName(validName).get();
        assertEquals(validName, pokemon.getName());
        assertEquals(description, pokemon.getDescription());
    }

    @Test
    public void getByPokemonName_EmptyName_ThrowsInvalidPokemonNameException() {
        assertThrows(InvalidPokemonNameException.class, () -> pokemonLookupService.getPokemonByName(EMPTY_NAME));
    }

    @Test
    public void getByPokemonName_NullName_ThrowsInvalidPokemonNameException() {
        assertThrows(InvalidPokemonNameException.class, () -> pokemonLookupService.getPokemonByName(NULL_NAME));
    }
}