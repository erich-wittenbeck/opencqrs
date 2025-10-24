/* Copyright (C) 2025 OpenCQRS and contributors */
package com.opencqrs.framework.eventhandler.tracing;

import com.opencqrs.esdb.client.Event;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;

public class OpenTelemetryEventTracingContextExtractor implements EventTracingContextExtractor {

    private final TextMapPropagator propagator;
    private final TextMapGetter<Event> textMapGetter;

    public OpenTelemetryEventTracingContextExtractor(OpenTelemetry openTelemetry, TextMapGetter<Event> textMapGetter) {
        this(openTelemetry.getPropagators().getTextMapPropagator(), textMapGetter);
    }

    protected OpenTelemetryEventTracingContextExtractor(
            TextMapPropagator propagator, TextMapGetter<Event> textMapGetter) {
        this.propagator = propagator;
        this.textMapGetter = textMapGetter;
    }

    @Override
    public Scope extractAndRestoreContextFromEvent(Context ctx, Event event) {
        return propagator.extract(ctx, event, textMapGetter).makeCurrent();
    }
}
