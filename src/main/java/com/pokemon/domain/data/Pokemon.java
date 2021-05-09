package com.pokemon.domain.data;

import lombok.*;

@Data
@Builder
public class Pokemon {
    private final String name;
    private final String description;
}