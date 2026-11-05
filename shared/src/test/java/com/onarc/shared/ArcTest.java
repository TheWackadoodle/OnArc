// Made by Claude

package com.onarc.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalTime;

import org.junit.Test;

/*
 * =====================================================================
 *  ArcTest: what's covered in Arc.java
 * =====================================================================
 *
 *  Update this list whenever Arc.java changes or tests are added.
 *
 *  CONSTRUCTOR: Arc(name, start, end)
 *    [x] keeps name, start and end as given
 *    [x] trims spaces off the ends of the name
 *    [x] keeps spaces in the middle of the name ("Lunch Break")
 *    [x] rejects null name
 *    [x] rejects empty name ""
 *    [x] rejects blank name "   "
 *    [x] rejects null start
 *    [x] rejects null end
 *    [x] rejects end before start
 *    [x] rejects end equal to start (zero-length arc)
 *    [x] accepts the earliest possible start (00:00)
 *
 *  getName() / getStart() / getEnd()
 *    [x] return what the arc was built with (see constructor tests)
 *
 *  getDurationMinutes()
 *    [x] normal arc (12:00-12:45 = 45)
 *    [x] shortest arc (1 minute)
 *    [x] longest arc (00:00-23:59 = 1439)
 *    [x] arc spanning several hours with odd minutes (09:10-11:35 = 145)
 *
 *  contains(time)   rule: start is IN, end is OUT
 *    [x] start time         -> true
 *    [x] middle time        -> true
 *    [x] last minute        -> true
 *    [x] exact end time     -> false
 *    [x] minute before start-> false
 *    [x] time after end     -> false
 *    [x] null time          -> throws
 *
 *  isOverlapping(other)   rule: touching ends do NOT count
 *    [x] other starts inside this          -> true
 *    [x] other ends inside this            -> true
 *    [x] other fully inside this           -> true
 *    [x] this fully inside other           -> true
 *    [x] exact same times                  -> true
 *    [x] back-to-back, other right after   -> false
 *    [x] back-to-back, other right before  -> false
 *    [x] other completely later            -> false
 *    [x] other completely earlier          -> false
 *    [x] same answer both ways (a vs b == b vs a)
 *    [x] null other         -> throws
 *
 *  NEVER (by design)
 *    [-] arcs that cross midnight: each dial is one self-contained
 *        24h day (00:00-23:59), so 23:00-01:00 is just rejected as
 *        end-before-start (see endBeforeStartIsRejected)
 *
 *  NOT COVERED YET (waiting on features that don't exist)
 *    [ ] seconds / nanoseconds in times (we only use whole minutes so far)
 *    [ ] colour, icon, warnings, etc. (fields not added yet)
 *    [ ] equals() / hashCode() / toString() (not written yet)
 * =====================================================================
 */
public class ArcTest {

    // The standard arc most tests use: Lunch, 12:00-12:45.
    private static Arc lunch() {
        return new Arc("Lunch", LocalTime.of(12, 0), LocalTime.of(12, 45));
    }

    // ── Constructor: good input ──────────────────────────────

    @Test
    public void keepsNameStartAndEnd() {
        Arc arc = lunch();

        assertEquals("Lunch", arc.getName());
        assertEquals(LocalTime.of(12, 0), arc.getStart());
        assertEquals(LocalTime.of(12, 45), arc.getEnd());
    }

    @Test
    public void nameIsStoredTrimmed() {
        Arc arc = new Arc("  Lunch  ", LocalTime.of(12, 0), LocalTime.of(12, 45));

        assertEquals("Lunch", arc.getName());
    }

    @Test
    public void spacesInsideNameAreKept() {
        Arc arc = new Arc("Lunch Break", LocalTime.of(12, 0), LocalTime.of(12, 45));

        assertEquals("Lunch Break", arc.getName());
    }

    @Test
    public void midnightStartIsAllowed() {
        Arc arc = new Arc("Sleep", LocalTime.MIDNIGHT, LocalTime.of(7, 0));

        assertEquals(LocalTime.MIDNIGHT, arc.getStart());
    }

    // ── Constructor: bad input is rejected ───────────────────

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

    // ── getDurationMinutes() ─────────────────────────────────

    @Test
    public void lunchLasts45Minutes() {
        assertEquals(45, lunch().getDurationMinutes());
    }

    @Test
    public void shortestArcLasts1Minute() {
        Arc arc = new Arc("Blink", LocalTime.of(9, 0), LocalTime.of(9, 1));

        assertEquals(1, arc.getDurationMinutes());
    }

    @Test
    public void longestArcLasts1439Minutes() {
        // 00:00 to 23:59 is a whole dial. Days never cross midnight.
        Arc arc = new Arc("Everything", LocalTime.MIDNIGHT, LocalTime.of(23, 59));

        assertEquals(1439, arc.getDurationMinutes());
    }

    @Test
    public void multiHourArcWithOddMinutes() {
        Arc arc = new Arc("Work", LocalTime.of(9, 10), LocalTime.of(11, 35));

        assertEquals(145, arc.getDurationMinutes());
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
    public void doesNotContainMinuteBeforeStart() {
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

    // ── isOverlapping: clashes ───────────────────────────────

    // Short helper so the overlap tests read like a schedule.
    private static Arc at(int startHour, int startMin, int endHour, int endMin) {
        return new Arc("Test", LocalTime.of(startHour, startMin), LocalTime.of(endHour, endMin));
    }

    @Test
    public void overlapsWhenOtherStartsInside() {
        // Work 9-12, Gym 11-13
        assertTrue(at(9, 0, 12, 0).isOverlapping(at(11, 0, 13, 0)));
    }

    @Test
    public void overlapsWhenOtherEndsInside() {
        // Work 9-12, Gym 8-10
        assertTrue(at(9, 0, 12, 0).isOverlapping(at(8, 0, 10, 0)));
    }

    @Test
    public void overlapsWhenOtherIsFullyInside() {
        // Work 9-12, Gym 10-11
        assertTrue(at(9, 0, 12, 0).isOverlapping(at(10, 0, 11, 0)));
    }

    @Test
    public void overlapsWhenThisIsFullyInsideOther() {
        assertTrue(at(10, 0, 11, 0).isOverlapping(at(9, 0, 12, 0)));
    }

    @Test
    public void overlapsWhenTimesAreIdentical() {
        assertTrue(lunch().isOverlapping(lunch()));
    }

    @Test
    public void overlapIsTheSameBothWays() {
        Arc work = at(9, 0, 12, 0);
        Arc gym = at(11, 0, 13, 0);
        Arc lunch = lunch();

        assertEquals(work.isOverlapping(gym), gym.isOverlapping(work));
        assertEquals(work.isOverlapping(lunch), lunch.isOverlapping(work));
    }

    // ── isOverlapping: no clash ──────────────────────────────

    @Test
    public void backToBackAfterDoesNotOverlap() {
        // Lunch 12:00-12:45, then Work 12:45-14:00
        assertFalse(lunch().isOverlapping(at(12, 45, 14, 0)));
    }

    @Test
    public void backToBackBeforeDoesNotOverlap() {
        // Morning 11:00-12:00, then Lunch 12:00-12:45
        assertFalse(lunch().isOverlapping(at(11, 0, 12, 0)));
    }

    @Test
    public void completelyLaterDoesNotOverlap() {
        assertFalse(lunch().isOverlapping(at(18, 0, 19, 0)));
    }

    @Test
    public void completelyEarlierDoesNotOverlap() {
        assertFalse(lunch().isOverlapping(at(8, 0, 8, 30)));
    }

    // ── isOverlapping: bad input ─────────────────────────────

    @Test
    public void overlappingNullIsRejected() {
        Arc arc = lunch();

        assertThrows(IllegalArgumentException.class, () -> arc.isOverlapping(null));
    }
}
