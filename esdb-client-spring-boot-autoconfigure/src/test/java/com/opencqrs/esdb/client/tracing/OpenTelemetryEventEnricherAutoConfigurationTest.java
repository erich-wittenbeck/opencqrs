/* Copyright (C) 2025 OpenCQRS and contributors */
package com.opencqrs.esdb.client.tracing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import io.opentelemetry.api.OpenTelemetry;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

public class OpenTelemetryEventEnricherAutoConfigurationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner();

    @Test
    public void otelTracingEventEnricherUsed() {
        runner.withConfiguration(AutoConfigurations.of(OpenTelemetryEventEnricherAutoConfiguration.class))
                .withBean(OpenTelemetry.class, OpenTelemetryEventEnricherAutoConfigurationTest::mockOtel)
                .run(context -> {
                    assertThat(context).hasNotFailed().hasSingleBean(OpenTelemetryTracingEventEnricher.class);
                });
    }

    @Test
    public void otherTracingEventEnricherFoundAndUsed() {
        runner.withConfiguration(AutoConfigurations.of(OpenTelemetryEventEnricherAutoConfiguration.class))
                .withBean(OpenTelemetry.class, OpenTelemetryEventEnricherAutoConfigurationTest::mockOtel)
                .withBean(
                        TracingEventEnricher.class,
                        OpenTelemetryEventEnricherAutoConfigurationTest::mockOtherEventEnricher)
                .run(context -> {
                    assertThat(context).hasNotFailed().doesNotHaveBean(OpenTelemetryTracingEventEnricher.class);
                });
    }

    @Test
    public void noOtelBeanFoundAndOtherTracingEventEnricherUsed() {
        runner.withConfiguration(AutoConfigurations.of(OpenTelemetryEventEnricherAutoConfiguration.class))
                .withBean(
                        TracingEventEnricher.class,
                        OpenTelemetryEventEnricherAutoConfigurationTest::mockOtherEventEnricher)
                .run(context -> {
                    assertThat(context).hasNotFailed().doesNotHaveBean(OpenTelemetryTracingEventEnricher.class);
                });
    }

    private static OpenTelemetry mockOtel() {
        return mock(OpenTelemetry.class, Mockito.RETURNS_DEEP_STUBS);
    }

    private static TracingEventEnricher mockOtherEventEnricher() {
        return mock(TracingEventEnricher.class, Mockito.RETURNS_DEEP_STUBS);
    }
}
