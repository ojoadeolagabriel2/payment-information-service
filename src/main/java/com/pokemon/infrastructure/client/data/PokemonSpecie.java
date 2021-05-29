package com.pokemon.infrastructure.client.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonSpecie {
    @JsonProperty("flavor_text_entries")
    private List<PokemonSpecieFlavor> flavorTextEntries;
}
