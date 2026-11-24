package com.example.incident;

import com.example.incident.legacy.LegacyDateAdapter;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LegacyDateAdapterTest {

    @Test
    void roundTripDateAndInstant() {
        Instant original = Instant.parse("2024-03-30T12:00:00.123Z");
        Date date = LegacyDateAdapter.toDate(original);
        Instant restored = LegacyDateAdapter.toInstant(date);
        assertEquals(original, restored);
    }

    @Test
    void roundTripCalendarAndZonedDateTime() {
        ZonedDateTime original = ZonedDateTime.of(
                2024, 3, 30, 12, 0, 0, 0, ZoneId.of("Europe/Berlin"));

        GregorianCalendar cal = LegacyDateAdapter.toGregorianCalendar(original);
        ZonedDateTime restored = LegacyDateAdapter.toZonedDateTime(cal);

        assertEquals(original, restored);
        assertEquals(ZoneId.of("Europe/Berlin"), cal.getTimeZone().toZoneId());
    }
}