/* Copyright (C) 2025 OpenCQRS and contributors */
package com.opencqrs.esdb.client.tracing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import io.opentelemetry.api.OpenTelemetry;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.actuate.autoconfigure.opentelemetry.OpenTelemetryAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

public class NoTracingEventEnricherAutoConfigurationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner();

    @Test
    public void noTracingEventEnricherUsed() {
        runner.withConfiguration(AutoConfigurations.of(NoTracingEventEnricherAutoConfiguration.class))
                .withClassLoader(new FilteredClassLoader(OpenTelemetry.class, OpenTelemetryAutoConfiguration.class))
                .run(context -> {
                    assertThat(context).hasNotFailed().hasSingleBean(NoTracingEventEnricher.class);
                });
    }

    @Test
    public void otherTracingEventEnricherFoundAndUsed() {
        runner.withConfiguration(AutoConfigurations.of(OpenTelemetryEventEnricherAutoConfiguration.class))
                .withClassLoader(new FilteredClassLoader(OpenTelemetry.class, OpenTelemetryAutoConfiguration.class))
                .withBean(
                        TracingEventEnricher.class, NoTracingEventEnricherAutoConfigurationTest::mockOtherEventEnricher)
                .run(context -> {
                    assertThat(context).hasNotFailed().doesNotHaveBean(NoTracingEventEnricher.class);
                });
    }

    private static TracingEventEnricher mockOtherEventEnricher() {
        return mock(TracingEventEnricher.class, Mockito.RETURNS_DEEP_STUBS);
    }
}
