package com.example.incident;

import com.example.incident.model.MaintenanceWindow;
import com.example.incident.report.ReportFormatter;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReportFormatterTest {

    @Test
    void formatsWindowExactly() {
        MaintenanceWindow window = new MaintenanceWindow(
                Instant.parse("2024-03-31T01:00:00Z"),
                Instant.parse("2024-03-31T02:00:00Z"));

        String formatted = ReportFormatter.formatWindow(
                window, ZoneId.of("Europe/Berlin"), Locale.GERMANY);

        assertEquals(
                "31.03.2024 03:00 +02:00 (Europe/Berlin) – 31.03.2024 04:00 +02:00 (Europe/Berlin)",
                formatted);
    }
}