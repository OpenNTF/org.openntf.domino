package org.openntf.domino.nsfdata;

import java.io.Serializable;

public class NSFDateRange implements Serializable, NSFDateTimeValue {
	private static final long serialVersionUID = 1L;

	private final NSFDateTime start_;
	private final NSFDateTime end_;

	public NSFDateRange(final NSFDateTime start, final NSFDateTime end) {
		start_ = start;
		end_ = end;
	}

	public NSFDateTime getStart() {
		return start_;
	}
	public NSFDateTime getEnd() {
		return end_;
	}
}
