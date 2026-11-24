package com.example.incident.analytics;

import com.example.incident.model.Incident;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public final class IncidentAnalytics {
    private IncidentAnalytics() {}

    public static Duration totalDowntime(List<Incident> incidents) {
        Objects.requireNonNull(incidents, "incidents must not be null");
        if (incidents.isEmpty()) return Duration.ZERO;

        long totalNanos = 0L;
        for (Incident inc : incidents) {
            totalNanos = Math.addExact(totalNanos, inc.duration().toNanos());
        }
        return Duration.ofNanos(totalNanos);
    }

    public static Duration averageDowntime(List<Incident> incidents) {
        Objects.requireNonNull(incidents, "incidents must not be null");
        if (incidents.isEmpty()) return Duration.ZERO;

        long totalNanos = 0L;
        for (Incident inc : incidents) {
            totalNanos = Math.addExact(totalNanos, inc.duration().toNanos());
        }
        long avgNanos = totalNanos / incidents.size();
        return Duration.ofNanos(avgNanos);
    }

    public static Optional<Incident> longestIncident(List<Incident> incidents) {
        Objects.requireNonNull(incidents, "incidents must not be null");
        if (incidents.isEmpty()) return Optional.empty();

        Comparator<Incident> cmp = Comparator
                .comparing(Incident::duration)
                .thenComparing(Comparator.comparing(Incident::startedAt).reversed())
                .thenComparing(Comparator.comparing(Incident::id).reversed());

        return incidents.stream().max(cmp);
    }

    public static Map<LocalDate, List<Incident>> groupByStartDate(
            List<Incident> incidents, ZoneId zone) {
        Objects.requireNonNull(incidents, "incidents must not be null");
        Objects.requireNonNull(zone, "zone must not be null");

        TreeMap<LocalDate, List<Incident>> grouped = new TreeMap<>();
        for (Incident inc : incidents) {
            LocalDate date = inc.startedAt().atZone(zone).toLocalDate();
            grouped.computeIfAbsent(date, k -> new ArrayList<>()).add(inc);
        }

        Comparator<Incident> inner = Comparator
                .comparing(Incident::startedAt)
                .thenComparing(Incident::id);
        for (List<Incident> list : grouped.values()) {
            list.sort(inner);
        }

        Map<LocalDate, List<Incident>> result = new LinkedHashMap<>();
        for (Map.Entry<LocalDate, List<Incident>> e : grouped.entrySet()) {
            result.put(e.getKey(), Collections.unmodifiableList(new ArrayList<>(e.getValue())));
        }
        return Collections.unmodifiableMap(result);
    }
}