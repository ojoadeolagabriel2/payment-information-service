package com.pokemon.api.support;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VersionHealthIndicator implements HealthIndicator {

    private final BuildProperties buildProperties;

    @Override
    public Health health() {
        Health.Builder status = Health.up();
        return status.withDetail("version", buildProperties.getVersion()).build();
    }
}