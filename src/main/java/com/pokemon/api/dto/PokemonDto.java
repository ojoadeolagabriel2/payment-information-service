package com.pokemon.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class PokemonDto {
    private final String name;
    private final String description;
}
