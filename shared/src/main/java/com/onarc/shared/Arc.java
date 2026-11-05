package com.onarc.shared;

import java.time.LocalTime;
import java.time.Duration;

// Establishes the fields for the Arcs
public class Arc {

	// These declare the fields for the Arc
	private final String name;
	private final LocalTime start;
	private final LocalTime end;

	// This is the Constructor for the Arc
	public Arc(String name, LocalTime start, LocalTime end) {
		// Check for bad name input
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("The Arc's name cannot be blank");
		}

		// Checks for bad start time
		if (start == null) {
			throw new IllegalArgumentException("The Arc's start time cannot be empty");
		}

		// Checks for bad end time
		if (end == null) {
			throw new IllegalArgumentException("The Arc's end time cannot be empty");
		}

		// Checks that the end time is after the start time
		if (!end.isAfter(start)) {
			throw new IllegalArgumentException("The Arc's end time must be after the start time");
		}

		// Inputs the values for the fields
		this.name = name.trim();
		this.start = start;
		this.end = end;
	}

	// These are the Getters for the Arc
	public String getName() {
		return name;
	}

	public LocalTime getStart() {
		return start;
	}

	public LocalTime getEnd() {
		return end;
	}

	public long getDurationMinutes() {
		return Duration.between(start, end).toMinutes();
	}

	// These are the Behavior Methods for the Arc
	public boolean contains(LocalTime time) {
		// Fail with a clear message instead of a vague NullPointerException
		if (time == null) {
			throw new IllegalArgumentException("The time to check cannot be empty");
		}

		return !time.isBefore(start) && time.isBefore(end);
	}

	public boolean isOverlapping(Arc other) {
		if (other == null) {
			throw new IllegalArgumentException("Arcs cannot be null");
		}
		return start.isBefore(other.end) && other.start.isBefore(end);
	}
}