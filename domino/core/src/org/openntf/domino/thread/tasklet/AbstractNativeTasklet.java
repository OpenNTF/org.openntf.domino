package org.openntf.domino.thread.tasklet;

import org.openntf.domino.thread.DominoSessionType;

@SuppressWarnings("serial")
public abstract class AbstractNativeTasklet extends AbstractTasklet {

	protected AbstractNativeTasklet() {
		this(true);
	}

	protected AbstractNativeTasklet(final boolean bubbleExc) {
		super(DominoSessionType.NATIVE, bubbleExc);
	}
}
