// Made by Claude

package com.onarc.shared;

import static org.junit.Assert.assertThrows;

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
 *
 *  NOT COVERED YET (waiting on features that don't exist)
 *    [ ] the arc actually ends up stored (needs getArcs())
 *    [ ] getArcs() can't be used to sneak changes into the list
 *    [ ] rejects an arc that overlaps one already added
 *        (needs Arc.overlaps() + the check in addArc)
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
}
