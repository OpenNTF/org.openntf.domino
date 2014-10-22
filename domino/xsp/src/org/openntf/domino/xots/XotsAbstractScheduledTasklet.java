package org.openntf.domino.xots;

import java.util.concurrent.TimeUnit;

public abstract class XotsAbstractScheduledTasklet extends XotsBaseTasklet implements XotsIScheduledTasklet {
	private static final long serialVersionUID = 1L;
	private Schedule schedule_;

	public XotsAbstractScheduledTasklet() {

	}

	public Schedule getSchedule() {
		if (schedule_ == null) {
			// default schedule is DAILY
			schedule_ = new Schedule(24, TimeUnit.HOURS);
		}
		return schedule_;
	}

	public void setSchedule(final Schedule schedule) {
		schedule_ = schedule;
	}

}
