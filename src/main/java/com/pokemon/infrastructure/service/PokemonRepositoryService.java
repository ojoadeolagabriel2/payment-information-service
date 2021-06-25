package com.pokemon.infrastructure.service;

import com.pokemon.infrastructure.entity.PokemonEntity;

import java.util.Optional;

public interface PokemonRepositoryService {
    Optional<PokemonEntity> getPokemonByName(String name);
}