package com.pokemon.infrastructure.client.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TranslationResultContent {
    private String translated;
    private String text;
    private String translation;
}
