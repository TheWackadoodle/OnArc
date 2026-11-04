package com.onarc.shared;

import java.util.ArrayList;
import java.util.List;


public class DayLoop {																											// Constructor for creating a new Arc???
	private final List<Arc> arcs = new ArrayList<>();															// Establishes an empty list of arcs
	public void addArc(Arc arc) {
		if (arc == null) throw new IllegalArgumentException("Arc cannot be null");
		arcs.add(arc);
	}
}