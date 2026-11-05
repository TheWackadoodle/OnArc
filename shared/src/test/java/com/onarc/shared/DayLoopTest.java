// Made by Claude

package com.onarc.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalTime;
import java.util.List;

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
 *    [x] a rejected arc is NOT stored
 *
 *  getArcs()
 *    [x] a new day is empty
 *    [x] added arcs show up, in the order they were added
 *    [x] can't add() through it (no sneaking past the clash check)
 *    [x] can't remove() or clear() through it
 *    [x] arcs added later still show up in a list you grabbed earlier
 *
 *  NOT COVERED YET (waiting on features that don't exist)
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

    @Test
    public void rejectedArcIsNotStored() {
        DayLoop day = new DayLoop();
        day.addArc(arc("Work", 9, 0, 12, 0));

        assertThrows(IllegalArgumentException.class,
                () -> day.addArc(arc("Gym", 10, 0, 11, 0)));

        assertEquals(1, day.getArcs().size());
        assertEquals("Work", day.getArcs().get(0).getName());
    }

    // ── getArcs ──────────────────────────────────────────────

    @Test
    public void newDayIsEmpty() {
        assertTrue(new DayLoop().getArcs().isEmpty());
    }

    @Test
    public void addedArcsShowUpInOrder() {
        DayLoop day = new DayLoop();
        day.addArc(arc("Breakfast", 8, 0, 8, 30));
        day.addArc(arc("Lunch", 12, 0, 12, 45));

        List<Arc> arcs = day.getArcs();

        assertEquals(2, arcs.size());
        assertEquals("Breakfast", arcs.get(0).getName());
        assertEquals("Lunch", arcs.get(1).getName());
    }

    @Test
    public void cannotAddThroughGetArcs() {
        DayLoop day = new DayLoop();
        day.addArc(arc("Work", 9, 0, 12, 0));

        // A clashing Gym must not be able to sneak in the back door.
        assertThrows(UnsupportedOperationException.class,
                () -> day.getArcs().add(arc("Gym", 10, 0, 11, 0)));
        assertEquals(1, day.getArcs().size());
    }

    @Test
    public void cannotRemoveOrClearThroughGetArcs() {
        DayLoop day = new DayLoop();
        day.addArc(arc("Work", 9, 0, 12, 0));

        assertThrows(UnsupportedOperationException.class, () -> day.getArcs().remove(0));
        assertThrows(UnsupportedOperationException.class, () -> day.getArcs().clear());
        assertEquals(1, day.getArcs().size());
    }

    // unmodifiableList is a live read-only window, not a frozen copy.
    @Test
    public void earlierGrabbedListSeesLaterArcs() {
        DayLoop day = new DayLoop();
        List<Arc> view = day.getArcs();

        day.addArc(arc("Lunch", 12, 0, 12, 45));

        assertEquals(1, view.size());
    }
}
