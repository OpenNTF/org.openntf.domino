package org.openntf.domino.xots;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public interface XotsIScheduledTasklet {

	public static class Schedule implements Serializable {
		private static final long serialVersionUID = 1L;
		private final long quantity_;
		private final TimeUnit unit_;

		public Schedule(final long quantity, final TimeUnit unit) {
			quantity_ = quantity;
			unit_ = unit;
		}

		public long getQuantity() {
			return quantity_;
		}

		public TimeUnit getUnit() {
			return unit_;
		}
	}

	public Schedule getSchedule();
}
