package org.openntf.domino.thread.tasklet;

import org.openntf.domino.thread.DominoSessionType;
import org.openntf.domino.utils.Factory;

@SuppressWarnings("serial")
public abstract class AbstractNamedTasklet extends AbstractTasklet {

	protected AbstractNamedTasklet(final String runAs, final boolean bubbleExc) {
		super(DominoSessionType.NAMED, bubbleExc);
		setRunAs(runAs);
	}

	protected AbstractNamedTasklet(final String runAs) {
		super(DominoSessionType.NAMED, true);
		setRunAs(runAs);
	}

	protected AbstractNamedTasklet() {
		this(Factory.getSession().getEffectiveUserName(), true);
	}

}
