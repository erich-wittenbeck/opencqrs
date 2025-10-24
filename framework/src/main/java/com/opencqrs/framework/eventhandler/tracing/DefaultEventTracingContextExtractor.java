/* Copyright (C) 2025 OpenCQRS and contributors */
package com.opencqrs.framework.eventhandler.tracing;

import com.opencqrs.esdb.client.Event;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;

public class DefaultEventTracingContextExtractor implements EventTracingContextExtractor {
    @Override
    public Scope extractAndRestoreContextFromEvent(Context ctx, Event event) {
        return ctx.makeCurrent();
    }
}
