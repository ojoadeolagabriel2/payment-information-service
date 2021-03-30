package com.pokemon.domain.error;


import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final String description;

    public ApplicationException(String description, Exception e) {
        super(description, e);
        this.description = description;
    }
}
