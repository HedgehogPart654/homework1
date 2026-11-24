package com.example.incident;

import com.example.incident.analytics.IncidentAnalytics;
import com.example.incident.model.Incident;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class IncidentAnalyticsTest {

    private static Incident inc(String id, String start, String end) {
        return new Incident(id, "svc", Instant.parse(start), Instant.parse(end));
    }

    @Test
    void totalAndAverageDowntime() {
        List<Incident> incidents = List.of(
                inc("1", "2024-01-01T00:00:00Z", "2024-01-01T00:10:00Z"),
                inc("2", "2024-01-01T01:00:00Z", "2024-01-01T01:20:00Z"),
                inc("3", "2024-01-01T02:00:00Z", "2024-01-01T02:30:00Z")
        );
        assertEquals(Duration.ofMinutes(60), IncidentAnalytics.totalDowntime(incidents));
        assertEquals(Duration.ofMinutes(20), IncidentAnalytics.averageDowntime(incidents));
    }

    @Test
    void averageRoundsDown() {
        List<Incident> incidents = List.of(
                new Incident("1", "s",
                        Instant.parse("2024-01-01T00:00:00Z"),
                        Instant.parse("2024-01-01T00:00:00.000000001Z")),
                new Incident("2", "s",
                        Instant.parse("2024-01-01T01:00:00Z"),
                        Instant.parse("2024-01-01T01:00:00.000000002Z"))
        );
        assertEquals(Duration.ofNanos(1), IncidentAnalytics.averageDowntime(incidents));
    }

    @Test
    void longestIncidentSelectsCorrectly() {
        List<Incident> incidents = List.of(
                inc("B", "2024-01-01T00:00:00Z", "2024-01-01T00:01:40Z"),
                inc("A", "2024-01-01T01:00:00Z", "2024-01-01T01:00:50Z")
        );
        assertEquals("B", IncidentAnalytics.longestIncident(incidents).orElseThrow().id());
    }

    @Test
    void longestIncidentTieBreaker() {
        Instant t0 = Instant.parse("2024-01-01T00:00:00Z");
        List<Incident> incidents = List.of(
                new Incident("B", "s", t0, t0.plusSeconds(100)),
                new Incident("A", "s", t0.plusSeconds(50), t0.plusSeconds(150)),
                new Incident("C", "s", t0, t0.plusSeconds(100))
        );
        Optional<Incident> longest = IncidentAnalytics.longestIncident(incidents);
        assertTrue(longest.isPresent());
        assertEquals("B", longest.get().id());
    }

    @Test
    void emptyListBehavior() {
        assertEquals(Duration.ZERO, IncidentAnalytics.totalDowntime(List.of()));
        assertEquals(Duration.ZERO, IncidentAnalytics.averageDowntime(List.of()));
        assertEquals(Optional.empty(), IncidentAnalytics.longestIncident(List.of()));
    }

    @Test
    void groupByStartDateInBerlin() {
        ZoneId berlin = ZoneId.of("Europe/Berlin");
        List<Incident> incidents = List.of(
                inc("1", "2024-03-30T22:00:00Z", "2024-03-30T23:00:00Z"),
                inc("2", "2024-03-30T23:30:00Z", "2024-03-31T00:30:00Z")
        );

        Map<LocalDate, List<Incident>> grouped =
                IncidentAnalytics.groupByStartDate(incidents, berlin);

        assertEquals(
                List.of(LocalDate.of(2024, 3, 30), LocalDate.of(2024, 3, 31)),
                List.copyOf(grouped.keySet()));
        assertEquals(List.of(incidents.get(0)), grouped.get(LocalDate.of(2024, 3, 30)));
        assertEquals(List.of(incidents.get(1)), grouped.get(LocalDate.of(2024, 3, 31)));
    }
}