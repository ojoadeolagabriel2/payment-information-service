package com.pokemon.domain.error;

import lombok.Getter;

@Getter
public class InvalidPokemonNameException extends RuntimeException {
    private final String description;

    public InvalidPokemonNameException(String description) {
        super(description);
        this.description = description;
    }
}