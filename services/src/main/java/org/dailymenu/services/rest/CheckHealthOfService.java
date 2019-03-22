package org.dailymenu.services.rest;

import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;

@Health
@ApplicationScoped
public class CheckHealthOfService implements HealthCheck {

    @Override
    public HealthCheckResponse call() {

        HealthCheckResponseBuilder healthCheckResponseBuilder =
                HealthCheckResponse.named(CheckHealthOfService.class.getSimpleName());

        if (isHealthy()) {
            return healthCheckResponseBuilder.up().withData("How are you feeling?", "Good :)").build();
        } else {
            return healthCheckResponseBuilder.down().build();
        }
    }

    private boolean isHealthy(){
        //for now
        return true;
    }

}