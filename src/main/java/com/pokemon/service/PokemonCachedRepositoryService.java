package com.pokemon.service;

import com.pokemon.infrastructure.entity.PokemonEntity;

import java.util.Optional;

public interface PokemonCachedRepositoryService {
    /**
     * Get pokemon by name
     * @param name name of pokemon
     * @return cached pokemon information per name, based on cache interval
     */
    Optional<PokemonEntity> getPokemonByName(String name);
}
