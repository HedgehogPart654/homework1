package com.example.incident;

import com.example.incident.model.MaintenanceWindow;
import com.example.incident.model.OverlapPolicy;
import com.example.incident.scheduler.MaintenanceScheduler;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaintenanceSchedulerTest {

    private static final ZoneId BERLIN = ZoneId.of("Europe/Berlin");

    @Test
    void rejectsGapTimeWithDateTimeException() {
        Clock clock = Clock.fixed(Instant.parse("2024-03-30T00:00:00Z"), ZoneOffset.UTC);
        MaintenanceScheduler scheduler = new MaintenanceScheduler(clock);

        assertThrows(DateTimeException.class, () -> scheduler.nextWindow(
                LocalDate.of(2024, 3, 31),
                LocalTime.of(2, 30),
                Duration.ofMinutes(30),
                BERLIN,
                OverlapPolicy.EARLIER));
    }

    @Test
    void overlapEarlierAndLaterDifferByOneHour() {
        Clock clock = Clock.fixed(Instant.parse("2024-10-26T00:00:00Z"), ZoneOffset.UTC);
        MaintenanceScheduler scheduler = new MaintenanceScheduler(clock);

        MaintenanceWindow earlier = scheduler.nextWindow(
                LocalDate.of(2024, 10, 27), LocalTime.of(2, 30),
                Duration.ofMinutes(30), BERLIN, OverlapPolicy.EARLIER);
        MaintenanceWindow later = scheduler.nextWindow(
                LocalDate.of(2024, 10, 27), LocalTime.of(2, 30),
                Duration.ofMinutes(30), BERLIN, OverlapPolicy.LATER);

        assertEquals(ZoneOffset.ofHours(2), earlier.start().atZone(BERLIN).getOffset());
        assertEquals(ZoneOffset.ofHours(1), later.start().atZone(BERLIN).getOffset());
        assertEquals(Duration.ofHours(1),
                Duration.between(earlier.start(), later.start()));
    }

    @Test
    void upcomingWindowsAcrossDstTransition() {
        Clock clock = Clock.fixed(Instant.parse("2024-03-30T00:00:00Z"), ZoneOffset.UTC);
        MaintenanceScheduler scheduler = new MaintenanceScheduler(clock);

        List<MaintenanceWindow> windows = scheduler.upcomingWindows(
                2, LocalTime.of(3, 30), Duration.ofMinutes(30),
                BERLIN, OverlapPolicy.EARLIER);

        assertEquals(2, windows.size());

        ZonedDateTime w1 = windows.get(0).start().atZone(BERLIN);
        ZonedDateTime w2 = windows.get(1).start().atZone(BERLIN);

        assertEquals(LocalTime.of(3, 30), w1.toLocalTime());
        assertEquals(LocalTime.of(3, 30), w2.toLocalTime());

        assertEquals(Duration.ofHours(23),
                Duration.between(windows.get(0).start(), windows.get(1).start()));
    }

    @Test
    void fixedClockIsDeterministic() {
        Clock clock = Clock.fixed(Instant.parse("2024-03-30T00:00:00Z"), ZoneOffset.UTC);
        MaintenanceScheduler scheduler = new MaintenanceScheduler(clock);

        List<MaintenanceWindow> first = scheduler.upcomingWindows(
                2, LocalTime.of(3, 30), Duration.ofMinutes(30),
                BERLIN, OverlapPolicy.EARLIER);
        List<MaintenanceWindow> second = scheduler.upcomingWindows(
                2, LocalTime.of(3, 30), Duration.ofMinutes(30),
                BERLIN, OverlapPolicy.EARLIER);

        assertEquals(first, second);
    }

    @Test
    void rejectsNonPositiveDuration() {
        Clock clock = Clock.fixed(Instant.parse("2024-03-30T00:00:00Z"), ZoneOffset.UTC);
        MaintenanceScheduler scheduler = new MaintenanceScheduler(clock);

        assertThrows(IllegalArgumentException.class, () -> scheduler.nextWindow(
                LocalDate.of(2024, 3, 30), LocalTime.of(3, 30),
                Duration.ZERO, BERLIN, OverlapPolicy.EARLIER));
        assertThrows(IllegalArgumentException.class, () -> scheduler.nextWindow(
                LocalDate.of(2024, 3, 30), LocalTime.of(3, 30),
                Duration.ofMinutes(-1), BERLIN, OverlapPolicy.EARLIER));
    }

    @Test
    void rejectsNegativeDays() {
        Clock clock = Clock.fixed(Instant.parse("2024-03-30T00:00:00Z"), ZoneOffset.UTC);
        MaintenanceScheduler scheduler = new MaintenanceScheduler(clock);

        assertThrows(IllegalArgumentException.class, () -> scheduler.upcomingWindows(
                -1, LocalTime.of(3, 30), Duration.ofMinutes(30),
                BERLIN, OverlapPolicy.EARLIER));
        assertEquals(List.of(), scheduler.upcomingWindows(
                0, LocalTime.of(3, 30), Duration.ofMinutes(30),
                BERLIN, OverlapPolicy.EARLIER));
    }
}