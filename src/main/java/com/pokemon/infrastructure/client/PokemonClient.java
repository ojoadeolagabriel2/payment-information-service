package com.pokemon.infrastructure.client;

import com.pokemon.infrastructure.data.PokemonEntity;

import java.util.Optional;

public interface PokemonClient {
    /**
     * Retrieves pokemon information by name
     * @param name name of pokemon e.g. charizard
     * @return pokemon
     */
    Optional<PokemonEntity> getPokemonByName(String name);
}