package com.pokemon.service;

import com.pokemon.domain.data.Pokemon;

import java.util.Optional;

public interface PokemonLookupService {
    Optional<Pokemon> getPokemonByName(String name);
}