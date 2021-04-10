package com.pokemon.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemon.api.dto.ApiError;
import com.pokemon.domain.error.ApplicationException;
import com.pokemon.domain.error.InvalidPokemonNameException;
import com.pokemon.domain.error.PokemonNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.time.LocalDateTime.now;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ErrorHandlerConfiguration extends ResponseEntityExceptionHandler {
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @ExceptionHandler(value = {ApplicationException.class})
    protected ResponseEntity<?> handleGeneral(ApplicationException ex, WebRequest request) {
        String logMessageHash = md5Hex(now() + ex.getMessage());
        log.error(String.format("Error processing request %s, see: %s", ex.getMessage(), logMessageHash), ex);

        var message = objectMapper
                .writeValueAsString(ApiError
                        .builder()
                        .message_id(logMessageHash)
                        .message(ex.getMessage())
                        .build());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", APPLICATION_JSON_VALUE);
        return handleExceptionInternal(ex, message, httpHeaders, INTERNAL_SERVER_ERROR, request);
    }

    @SneakyThrows
    @ExceptionHandler(value = {PokemonNotFoundException.class})
    protected ResponseEntity<?> handlePokemonNotFound(PokemonNotFoundException ex, WebRequest request) {
        String logMessageHash = md5Hex("PokemonNotFoundException" + now() + ex.getMessage());
        log.error(String.format("check input as %s, see: %s", ex.getMessage(), logMessageHash), ex);

        var message = objectMapper
                .writeValueAsString(ApiError
                        .builder()
                        .message_id(logMessageHash)
                        .message(ex.getMessage())
                        .build());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", APPLICATION_JSON_VALUE);
        return handleExceptionInternal(ex, message, httpHeaders, NOT_FOUND, request);
    }

    @SneakyThrows
    @ExceptionHandler(value = {InvalidPokemonNameException.class})
    protected ResponseEntity<?> handlePokemonNotFound(InvalidPokemonNameException ex, WebRequest request) {
        String logMessageHash = md5Hex("InvalidPokemonNameException" + now() + ex.getMessage());
        log.error(String.format("%s, see: %s", ex.getMessage(), logMessageHash), ex);

        var message = objectMapper
                .writeValueAsString(ApiError
                        .builder()
                        .message_id(logMessageHash)
                        .message(ex.getMessage())
                        .build());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", APPLICATION_JSON_VALUE);
        return handleExceptionInternal(ex, message, httpHeaders, BAD_REQUEST, request);
    }
}