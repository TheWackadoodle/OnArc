// Made by Claude

package com.onarc.shared;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalTime;

import org.junit.Test;

/*
 * =====================================================================
 *  DayLoopTest: what's covered in DayLoop.java
 * =====================================================================
 *
 *  Update this list whenever DayLoop.java changes or tests are added.
 *
 *  addArc(arc)
 *    [x] accepts a normal arc
 *    [x] accepts several arcs with gaps between them
 *    [x] accepts back-to-back arcs (12:00-12:45 then 12:45-14:00)
 *    [x] rejects a null arc
 *    [x] rejects an arc that clashes with the only arc in the day
 *    [x] rejects a clash with an arc that isn't the first one added
 *    [x] rejects an arc that swallows a whole existing arc
 *    [x] error message names the arc it clashed with
 *    [x] a rejected arc doesn't block a later arc that fits
 *
 *  NOT COVERED YET (waiting on features that don't exist)
 *    [ ] the arc actually ends up stored (needs getArcs())
 *    [ ] a rejected arc is NOT stored (needs getArcs())
 *    [ ] getArcs() can't be used to sneak changes into the list
 *    [ ] current arc at a given time
 *    [ ] next arc after a given time
 * =====================================================================
 */
public class DayLoopTest {

    private static Arc arc(String name, int startHour, int startMin, int endHour, int endMin) {
        return new Arc(name, LocalTime.of(startHour, startMin), LocalTime.of(endHour, endMin));
    }

    // ── addArc: good input ───────────────────────────────────

    // No assert needed: if addArc throws, the test fails.
    @Test
    public void acceptsANormalArc() {
        DayLoop day = new DayLoop();

        day.addArc(arc("Lunch", 12, 0, 12, 45));
    }

    @Test
    public void acceptsSeveralArcsWithGaps() {
        DayLoop day = new DayLoop();

        day.addArc(arc("Breakfast", 8, 0, 8, 30));
        day.addArc(arc("Lunch", 12, 0, 12, 45));
        day.addArc(arc("Dinner", 18, 0, 19, 0));
    }

    // Back-to-back arcs touch but don't overlap. This must keep passing
    // once the overlap check goes into addArc.
    @Test
    public void acceptsBackToBackArcs() {
        DayLoop day = new DayLoop();

        day.addArc(arc("Lunch", 12, 0, 12, 45));
        day.addArc(arc("Work", 12, 45, 14, 0));
    }

    // ── addArc: bad input is rejected ────────────────────────

    @Test
    public void nullArcIsRejected() {
        DayLoop day = new DayLoop();

        assertThrows(IllegalArgumentException.class, () -> day.addArc(null));
    }

    @Test
    public void clashWithOnlyArcIsRejected() {
        DayLoop day = new DayLoop();
        day.addArc(arc("Work", 9, 0, 12, 0));

        assertThrows(IllegalArgumentException.class,
                () -> day.addArc(arc("Gym", 10, 0, 11, 0)));
    }

    // Makes sure the loop checks every arc, not just the first one.
    @Test
    public void clashWithLaterArcIsRejected() {
        DayLoop day = new DayLoop();
        day.addArc(arc("Breakfast", 8, 0, 8, 30));
        day.addArc(arc("Lunch", 12, 0, 12, 45));
        day.addArc(arc("Dinner", 18, 0, 19, 0));

        assertThrows(IllegalArgumentException.class,
                () -> day.addArc(arc("Snack", 18, 30, 18, 45)));
    }

    @Test
    public void arcSwallowingAnExistingArcIsRejected() {
        DayLoop day = new DayLoop();
        day.addArc(arc("Lunch", 12, 0, 12, 45));

        assertThrows(IllegalArgumentException.class,
                () -> day.addArc(arc("Work", 9, 0, 17, 0)));
    }

    @Test
    public void clashMessageNamesTheExistingArc() {
        DayLoop day = new DayLoop();
        day.addArc(arc("Work", 9, 0, 12, 0));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> day.addArc(arc("Gym", 10, 0, 11, 0)));

        assertTrue(e.getMessage().contains("Work"));
    }

    @Test
    public void rejectedArcDoesNotBlockALaterArcThatFits() {
        DayLoop day = new DayLoop();
        day.addArc(arc("Work", 9, 0, 12, 0));

        assertThrows(IllegalArgumentException.class,
                () -> day.addArc(arc("Gym", 10, 0, 11, 0)));

        // If Gym had sneaked into the list anyway, this would clash with it.
        day.addArc(arc("Lunch", 12, 0, 12, 45));
        day.addArc(arc("Walk", 12, 45, 13, 0));
    }
}
