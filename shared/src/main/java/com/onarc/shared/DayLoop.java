package com.onarc.shared;

import java.util.ArrayList;
import java.util.List;

// Holds all the arcs for one day
public class DayLoop {

	// Establishes an empty list of arcs
	private final List<Arc> arcs = new ArrayList<>();

	// Adds an arc to the day, unless it's null or clashes with one already there
	public void addArc(Arc arc) {
		if (arc == null) {
			throw new IllegalArgumentException("Arc cannot be null");
		}

		// Check the new arc against every arc already in the day
		for (Arc existing : arcs) {
			if (arc.isOverlapping(existing)) {
				throw new IllegalArgumentException("This arc clashes with " + existing.getName());
			}
		}

		arcs.add(arc);
	}
}