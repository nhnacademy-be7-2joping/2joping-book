package com.nhnacademy.bookstore.global.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomHealthIndicator implements HealthIndicator {

    private final ApplicationStatus applicationStatus;

    /**
     * @param includeDetails
     * @return
     */
    @Override
    public Health getHealth(boolean includeDetails) {
        return HealthIndicator.super.getHealth(includeDetails);
    }

    /**
     * @return
     */
    @Override
    public Health health() {
        if (!applicationStatus.isStatus()) {
            return Health.down().build();
        }

        return Health.up().withDetail("service", "start").build();
    }
}
