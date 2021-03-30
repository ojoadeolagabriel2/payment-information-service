package com.pokemon.domain.error;


import lombok.Getter;

@Getter
public class PokemonNotFoundException extends RuntimeException {
    private final String description;

    public PokemonNotFoundException(String description, Exception e) {
        super(description, e);
        this.description = description;
    }
}
