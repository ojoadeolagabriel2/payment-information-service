package com.pokemon.api.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Endpoint(id = "version")
public class VersionHealthIndicator {
    private final ObjectMapper objectMapper;
    private final BuildProperties buildProperties;

    @ReadOperation
    public String version() throws JsonProcessingException {
        return objectMapper.writeValueAsString(Map.of("version", buildProperties.getVersion()));
    }
}