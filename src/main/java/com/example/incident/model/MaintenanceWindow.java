package com.example.incident.model;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public final class MaintenanceWindow {
    private final Instant start;
    private final Instant end;

    public MaintenanceWindow(Instant start, Instant end) {
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(end, "end must not be null");
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("end must not be before start");
        }
        this.start = start;
        this.end = end;
    }

    public Instant start() { return start; }
    public Instant end()   { return end; }

    public Duration duration() {
        return Duration.between(start, end);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MaintenanceWindow that)) return false;
        return start.equals(that.start) && end.equals(that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "MaintenanceWindow{start=" + start + ", end=" + end + '}';
    }
}