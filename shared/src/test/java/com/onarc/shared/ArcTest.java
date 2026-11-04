// Tests for Arc.java.

package com.onarc.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalTime;

import org.junit.Test;

public class ArcTest {

    // Helper so every test doesn't have to spell out lunch by hand.
    private static Arc lunch() {
        return new Arc("Lunch", LocalTime.of(12, 0), LocalTime.of(12, 45));
    }

    // ── Building an arc ──────────────────────────────────────

    @Test
    public void keepsNameStartAndEnd() {
        Arc arc = lunch();

        assertEquals("Lunch", arc.getName());
        assertEquals(LocalTime.of(12, 0), arc.getStart());
        assertEquals(LocalTime.of(12, 45), arc.getEnd());
    }

    @Test
    public void nameIsStoredTrimmed() {
        // Expected to FAIL until Arc saves name.trim() instead of name.
        Arc arc = new Arc("  Lunch  ", LocalTime.of(12, 0), LocalTime.of(12, 45));

        assertEquals("Lunch", arc.getName());
    }

    // ── Bad input is rejected ────────────────────────────────

    @Test
    public void nullNameIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Arc(null, LocalTime.of(12, 0), LocalTime.of(12, 45)));
    }

    @Test
    public void emptyNameIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Arc("", LocalTime.of(12, 0), LocalTime.of(12, 45)));
    }

    @Test
    public void blankNameIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Arc("   ", LocalTime.of(12, 0), LocalTime.of(12, 45)));
    }

    @Test
    public void nullStartIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Arc("Lunch", null, LocalTime.of(12, 45)));
    }

    @Test
    public void nullEndIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Arc("Lunch", LocalTime.of(12, 0), null));
    }

    @Test
    public void endBeforeStartIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Arc("Lunch", LocalTime.of(12, 45), LocalTime.of(12, 0)));
    }

    @Test
    public void startEqualToEndIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Arc("Lunch", LocalTime.of(12, 0), LocalTime.of(12, 0)));
    }

    // ── Duration ─────────────────────────────────────────────

    @Test
    public void lunchLasts45Minutes() {
        assertEquals(45, lunch().getDurationMinutes());
    }

    @Test
    public void oneMinuteArcLasts1Minute() {
        Arc arc = new Arc("Blink", LocalTime.of(9, 0), LocalTime.of(9, 1));

        assertEquals(1, arc.getDurationMinutes());
    }

    @Test
    public void almostWholeDayArcDurationIsCorrect() {
        // 00:00 to 23:59 is the longest arc allowed until midnight-crossing exists.
        Arc arc = new Arc("Everything", LocalTime.MIDNIGHT, LocalTime.of(23, 59));

        assertEquals(23 * 60 + 59, arc.getDurationMinutes());
    }

    // ── contains(): start is IN, end is OUT ──────────────────

    @Test
    public void containsStartTime() {
        assertTrue(lunch().contains(LocalTime.of(12, 0)));
    }

    @Test
    public void containsMiddleTime() {
        assertTrue(lunch().contains(LocalTime.of(12, 30)));
    }

    @Test
    public void containsLastMinute() {
        assertTrue(lunch().contains(LocalTime.of(12, 44)));
    }

    @Test
    public void doesNotContainEndTime() {
        // 12:45 belongs to whatever comes after lunch.
        assertFalse(lunch().contains(LocalTime.of(12, 45)));
    }

    @Test
    public void doesNotContainTimeBeforeStart() {
        assertFalse(lunch().contains(LocalTime.of(11, 59)));
    }

    @Test
    public void doesNotContainTimeAfterEnd() {
        assertFalse(lunch().contains(LocalTime.of(13, 0)));
    }

    @Test
    public void containsNullIsRejected() {
        Arc arc = lunch();

        assertThrows(IllegalArgumentException.class, () -> arc.contains(null));
    }
}
