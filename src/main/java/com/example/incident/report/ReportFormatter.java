package com.example.incident.report;

import com.example.incident.model.MaintenanceWindow;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public final class ReportFormatter {
    private ReportFormatter() {}

    // dd.MM.uuuu HH:mm XXX (VV)
    // Скобки экранируем одинарными кавычками — литералы.
    private static final String PATTERN = "dd.MM.uuuu HH:mm XXX '('VV')'";

    public static String formatWindow(MaintenanceWindow window, ZoneId zone, Locale locale) {
        Objects.requireNonNull(window, "window must not be null");
        Objects.requireNonNull(zone, "zone must not be null");
        Objects.requireNonNull(locale, "locale must not be null");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(PATTERN, locale);

        ZonedDateTime start = window.start().atZone(zone);
        ZonedDateTime end   = window.end().atZone(zone);

        return start.format(fmt) + " – " + end.format(fmt);
    }
}