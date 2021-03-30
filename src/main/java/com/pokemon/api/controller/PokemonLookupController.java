package com.pokemon.api.controller;

import com.pokemon.api.dto.ApiError;
import com.pokemon.api.dto.PokemonDto;
import com.pokemon.service.PokemonLookupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pokemon")
public class PokemonLookupController {

    private final PokemonLookupService pokemonLookupService;

    @Operation(summary = "Get pokemon by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found pokemon",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PokemonDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid pokemon name provided",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Pokemon not found"),
            @ApiResponse(responseCode = "500", description = "An unexpected error was detected, contact techops@truelayer.com"),
    })
    @GetMapping(path = "{name}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<PokemonDto> getPokemon(@PathVariable String name) {
        log.info("looking up information on the pokemon: {}", name);
        final var possiblePokemon = pokemonLookupService.getPokemonByName(name);

        if (possiblePokemon.isPresent()) {
            var pokemon = possiblePokemon.get();
            log.info("returning {} to client", name);

            return ok(PokemonDto
                    .builder()
                    .name(name)
                    .description(pokemon.getDescription())
                    .build());
        }
        return ResponseEntity.notFound().build();
    }
}
