/* Copyright (C) 2025 OpenCQRS and contributors */
package com.opencqrs.esdb.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Event data structure retrieved from an event store, conforming to the <a
 * href="https://github.com/cloudevents/spec">Cloud Events Specification</a>.
 *
 * @param source identifies the originating source of publication
 * @param subject an absolute path identifying the subject that the event is related to
 * @param type uniquely identifies the event type, specifically for being able to interpret the contained data structure
 * @param data a generic map structure containing the event payload
 * @param specVersion cloud events specification version
 * @param id a unique event identifier with respect to the originating event store
 * @param time the publication time-stamp
 * @param dataContentType the data content-type, always {@code application/json}
 * @param hash the hash of this event
 * @param predecessorHash the hash of the preceding event in the event store
 * @param traceParent the event's 'traceparent' header, according to the W3C Trace Context standard
 * @param traceState the event's 'tracestate' header, according to the W3C Trace Context standard
 * @see EventCandidate
 * @see EsdbClient#read(String, Set)
 * @see EsdbClient#read(String, Set, Consumer)
 * @see EsdbClient#observe(String, Set, Consumer)
 */
public record Event(
        @NotBlank String source,
        @NotBlank String subject,
        @NotBlank String type,
        @NotNull Map<String, ?> data,
        @NotBlank String specVersion,
        @NotBlank String id,
        @NotNull Instant time,
        @NotBlank String dataContentType,
        String hash,
        @NotBlank String predecessorHash,
        String traceParent,
        String traceState)
        implements Marshaller.ResponseElement {

    /**
     * Convenience constructor with no tracing information available.
     *
     * @param source
     * @param subject
     * @param type
     * @param data
     * @param specVersion
     * @param id
     * @param time
     * @param dataContentType
     * @param hash
     * @param predecessorHash
     */
    public Event(
            @NotBlank String source,
            @NotBlank String subject,
            @NotBlank String type,
            @NotNull Map<String, ?> data,
            @NotBlank String specVersion,
            @NotBlank String id,
            @NotNull Instant time,
            @NotBlank String dataContentType,
            String hash,
            @NotBlank String predecessorHash) {
        this(source, subject, type, data, specVersion, id, time, dataContentType, hash, predecessorHash, null, null);
    }
}
