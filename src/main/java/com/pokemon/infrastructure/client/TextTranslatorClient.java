package com.pokemon.infrastructure.client;

import com.pokemon.infrastructure.client.data.TranslationResult;

import java.util.Optional;

public interface TextTranslatorClient {
    /**
     * Translates input text
     * @param text text to translate
     * @return translation result
     */
    Optional<TranslationResult> translate(String text);
}