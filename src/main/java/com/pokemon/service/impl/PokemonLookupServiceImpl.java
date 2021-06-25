package com.pokemon.service.impl;

import com.pokemon.domain.data.Pokemon;
import com.pokemon.domain.error.InvalidPokemonNameException;
import com.pokemon.infrastructure.entity.PokemonEntity;
import com.pokemon.service.PokemonCachedRepositoryService;
import com.pokemon.service.PokemonLookupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Optional.of;
import static org.junit.platform.commons.util.StringUtils.isBlank;

@Slf4j
@Service
@RequiredArgsConstructor
public class PokemonLookupServiceImpl implements PokemonLookupService {

    private final PokemonCachedRepositoryService repositoryService;

    public Optional<Pokemon> getPokemonByName(String name) {
        validateName(name);
        Optional<PokemonEntity> possiblePokemon = repositoryService.getPokemonByName(name);

        if (possiblePokemon.isPresent()) {
            var pokemon = possiblePokemon.get();
            return of(Pokemon
                    .builder()
                    .name(name)
                    .description(pokemon.getDescription())
                    .build());
        }
        return Optional.empty();
    }

    private void validateName(String name) {
        if (isBlank(name)) {
            log.error("missing pokemon name!");
            throw new InvalidPokemonNameException("pokemon name cannot be empty");
        }
    }
}