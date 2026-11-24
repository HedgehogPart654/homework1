package com.example.incident.parser;

import com.example.incident.model.Incident;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public final class IncidentParser {
    private IncidentParser() {}

    public static Incident parse(String line) {
        if (line == null) throw new IllegalArgumentException("line must not be null");

        String[] parts = line.split(";", -1);
        if (parts.length != 4) {
            throw new IllegalArgumentException(
                    "Expected 4 fields separated by ';', got " + parts.length);
        }

        String id = parts[0];
        String service = parts[1];
        String startedStr = parts[2];
        String resolvedStr = parts[3];

        if (id.isBlank()) throw new IllegalArgumentException("id must not be blank");
        if (service.isBlank()) throw new IllegalArgumentException("service must not be blank");
        if (startedStr.isBlank()) throw new IllegalArgumentException("startedAt must not be blank");
        if (resolvedStr.isBlank()) throw new IllegalArgumentException("resolvedAt must not be blank");

        Instant startedAt;
        Instant resolvedAt;
        try {
            startedAt = Instant.parse(startedStr);
            resolvedAt = Instant.parse(resolvedStr);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid ISO-8601 timestamp", e);
        }

        return new Incident(id, service, startedAt, resolvedAt);
    }
}