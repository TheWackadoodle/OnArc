package com.onarc.shared;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class DayLoop {
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

	public List<Arc> getArcs() {
		return Collections.unmodifiableList(arcs);
	}
}