package com.pokemon.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pokemon-maker")
public class PokemonSetupController {

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> post(@RequestBody String body) {
        log.debug("creating pokemon {}", body);
        return ResponseEntity.created(URI.create("pokemon/1")).build();
    }
}