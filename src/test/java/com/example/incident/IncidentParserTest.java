package com.example.incident;

import com.example.incident.model.Incident;
import com.example.incident.parser.IncidentParser;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class IncidentParserTest {

    @Test
    void parsesCorrectLineAndComputesDuration() {
        Incident inc = IncidentParser.parse(
                "INC-1;billing;2024-03-30T22:30:00Z;2024-03-31T01:00:00Z");

        assertEquals("INC-1", inc.id());
        assertEquals("billing", inc.service());
        assertEquals(Duration.ofMinutes(150), inc.duration());
    }

    @Test
    void rejectsInvalidTimestamp() {
        assertThrows(IllegalArgumentException.class, () ->
                IncidentParser.parse("INC-1;billing;not-a-date;2024-03-31T01:00:00Z"));
    }

    @Test
    void rejectsEmptyId() {
        assertThrows(IllegalArgumentException.class, () ->
                IncidentParser.parse(";billing;2024-03-30T22:30:00Z;2024-03-31T01:00:00Z"));
    }

    @Test
    void rejectsResolvedBeforeStarted() {
        assertThrows(IllegalArgumentException.class, () ->
                IncidentParser.parse("INC-1;billing;2024-03-31T01:00:00Z;2024-03-30T22:30:00Z"));
    }

    @Test
    void rejectsWrongFieldCount() {
        assertThrows(IllegalArgumentException.class, () ->
                IncidentParser.parse("INC-1;billing;2024-03-30T22:30:00Z"));
    }
}