package com.pokemon.infrastructure.data;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class PokemonEntity {
    private final String name;
    private final String description;
}
