// Baser test of Arc.java.

package com.onarc.shared;

import static org.junit.Assert.*;
import java.time.LocalTime;
import org.junit.Test;

public class ArcTest {
	@Test
	public void blankNameIsRejected() {
		assertThrows(???.class, () -> new Arc(???, LocalTime.of(12, 0), LocalTime.of(12, 45)));
	}
}