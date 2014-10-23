package org.openntf.domino.thread.tasklet;

import org.openntf.domino.thread.AbstractDominoRunnable;
import org.openntf.domino.thread.DominoSessionType;
import org.openntf.domino.utils.DominoUtils;

@SuppressWarnings("serial")
public abstract class AbstractTasklet extends AbstractDominoRunnable {

	protected AbstractTasklet(final DominoSessionType sessType) {
		this(sessType, true);
	}

	protected AbstractTasklet(final DominoSessionType sessType, final boolean bubbleExc) {
		super(sessType);
		DominoUtils.setBubbleExceptions(bubbleExc);
	}

	@Override
	public boolean shouldStop() {
		return false;
	}

	@Override
	public abstract void run();
}
