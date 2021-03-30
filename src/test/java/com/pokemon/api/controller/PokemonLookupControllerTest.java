package com.pokemon.api.controller;

import com.pokemon.domain.data.Pokemon;
import com.pokemon.domain.error.InvalidPokemonNameException;
import com.pokemon.service.PokemonLookupService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Optional.of;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class PokemonLookupControllerTest {

    private static final String VALID_POKEMON_NAME = "charizard";
    private static final String BLANK_POKEMON_NAME = "  ";
    private static final String EMPTY_POKEMON_NAME = "";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PokemonLookupService mockPokemonLookupService;

    @Test
    public void getByPokemonName_Valid_ReturnSuccess200() throws Exception {
        //when
        when(mockPokemonLookupService.getPokemonByName(VALID_POKEMON_NAME)).thenReturn(
                of(aPokemon(VALID_POKEMON_NAME, "pits fire that\\nis hot enough to\\nmelt boulders"))
        );

        //then
        mockMvc.perform(get("/pokemon/{name}", VALID_POKEMON_NAME).contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getByPokemonName_EmptyName_ReturnFailed404() throws Exception {
        //when
        when(mockPokemonLookupService.getPokemonByName(BLANK_POKEMON_NAME)).thenReturn(
                of(aPokemon(EMPTY_POKEMON_NAME, "pits fire that\\\\nis hot enough to\\\\nmelt boulders\""))
        );

        //then
        mockMvc.perform(get("/pokemon/{name}", EMPTY_POKEMON_NAME).contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getByPokemonName_BlankName_ReturnFailed400() throws Exception {
        //when
        when(mockPokemonLookupService.getPokemonByName(BLANK_POKEMON_NAME)).thenThrow(new InvalidPokemonNameException("pokemon name is black"));

        //then
        mockMvc.perform(get("/pokemon/{name}", BLANK_POKEMON_NAME).contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private Pokemon aPokemon(String name, String description) {
        return Pokemon.builder()
                .name(name)
                .description(description)
                .build();
    }
}