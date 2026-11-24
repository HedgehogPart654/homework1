package com.example.incident.scheduler;

import com.example.incident.model.MaintenanceWindow;
import com.example.incident.model.OverlapPolicy;

import java.time.*;
import java.time.zone.ZoneRules;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class MaintenanceScheduler {
    private final Clock clock;

    public MaintenanceScheduler(Clock clock) {
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
    }

    public MaintenanceWindow nextWindow(
            LocalDate date, LocalTime localStart, Duration duration,
            ZoneId zone, OverlapPolicy policy) {

        Objects.requireNonNull(date, "date must not be null");
        Objects.requireNonNull(localStart, "localStart must not be null");
        Objects.requireNonNull(duration, "duration must not be null");
        Objects.requireNonNull(zone, "zone must not be null");
        Objects.requireNonNull(policy, "policy must not be null");

        if (duration.isNegative() || duration.isZero()) {
            throw new IllegalArgumentException("duration must be positive");
        }

        LocalDateTime localDateTime = LocalDateTime.of(date, localStart);
        ZoneRules rules = zone.getRules();

        if (rules.getValidOffsets(localDateTime).isEmpty()) {
            throw new DateTimeException(
                    "Local time " + localDateTime + " does not exist in zone " + zone + " (DST gap)");
        }

        ZonedDateTime zdt = ZonedDateTime.ofLocal(localDateTime, zone, null);

        zdt = switch (policy) {
            case EARLIER -> zdt.withEarlierOffsetAtOverlap();
            case LATER   -> zdt.withLaterOffsetAtOverlap();
        };

        Instant start = zdt.toInstant();
        Instant end = start.plus(duration);
        return new MaintenanceWindow(start, end);
    }

    public List<MaintenanceWindow> upcomingWindows(
            int days, LocalTime localStart, Duration duration,
            ZoneId zone, OverlapPolicy policy) {

        if (days < 0) {
            throw new IllegalArgumentException("days must not be negative");
        }
        if (days == 0) return List.of();

        LocalDate today = LocalDate.now(clock.withZone(zone));

        List<MaintenanceWindow> result = new ArrayList<>(days);
        for (int i = 0; i < days; i++) {
            result.add(nextWindow(today.plusDays(i), localStart, duration, zone, policy));
        }
        return Collections.unmodifiableList(result);
    }
}
