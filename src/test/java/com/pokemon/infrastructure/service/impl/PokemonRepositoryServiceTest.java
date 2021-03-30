package com.pokemon.infrastructure.service.impl;

import com.pokemon.infrastructure.client.PokemonClient;
import com.pokemon.infrastructure.client.TextTranslatorClient;
import com.pokemon.infrastructure.client.data.TranslationResult;
import com.pokemon.infrastructure.client.data.TranslationResultContent;
import com.pokemon.infrastructure.client.data.TranslationResultSuccessInformation;
import com.pokemon.infrastructure.data.PokemonEntity;
import com.pokemon.infrastructure.service.PokemonRepositoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class PokemonRepositoryServiceTest {

    @Autowired
    PokemonRepositoryService pokemonRepositoryService;

    @MockBean
    PokemonClient pokemonClient;

    @MockBean
    TextTranslatorClient textTranslatorClient;

    @Test
    public void getByPokemonName_ValidName_ReturnPokemon() {
        // given
        var validName = "chidori";
        var translatedText = "Spits fire yond is hot enow to melt boulders";
        var original = "Spits fire that is hot enough to melt boulders";

        // when
        when(pokemonClient.getPokemonByName(validName)).thenReturn(
                Optional.of(PokemonEntity.builder().name(validName).description(original).build())
        );

        // and
        when(textTranslatorClient.translate(original)).thenReturn(
                Optional.of(TranslationResult
                        .builder()
                        .success(TranslationResultSuccessInformation.builder().total(1).build())
                        .contents(TranslationResultContent.builder().text(original).translated(translatedText).translation("shakespeare").build())
                        .build())
        );

        var result = pokemonRepositoryService.getPokemonByName(validName).get();
        assertEquals(validName, result.getName());
        assertEquals(translatedText, result.getDescription());
    }
}