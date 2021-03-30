package com.pokemon.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.pokemon.domain.error.ApplicationException;
import com.pokemon.infrastructure.data.PokemonEntity;
import com.pokemon.infrastructure.service.PokemonRepositoryService;
import com.pokemon.service.PokemonCachedRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
public class PokemonCachedRepositoryServiceImpl implements PokemonCachedRepositoryService {

    private static final PokemonEntity EMPTY_POKEMON = null;
    private final PokemonRepositoryService pokemonRepositoryService;
    private final Cache<String, PokemonEntity> cache;

    @Override
    public Optional<PokemonEntity> getPokemonByName(String name) {
        try{
            return ofNullable(cache.get(name, this::retrievePokemonByName));
        } catch (Exception exception) {
            throw new ApplicationException(exception.getMessage(), exception);
        }
    }

    private PokemonEntity retrievePokemonByName(String name) {
        Optional<PokemonEntity> possiblePokemon = pokemonRepositoryService.getPokemonByName(name);

        if (possiblePokemon.isPresent()) {
            var summary = possiblePokemon.get();
            return PokemonEntity
                    .builder()
                    .name(summary.getName())
                    .description(summary.getDescription())
                    .build();
        }
        return EMPTY_POKEMON;
    }
}
