package org.openntf.domino.xots.builtin;

import org.openntf.domino.thread.DominoSessionType;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.xots.XotsBaseTasklet;

@SuppressWarnings("serial")
public abstract class XotsAbstractNativeTasklet extends XotsBaseTasklet {

	public XotsAbstractNativeTasklet() {
		super();
		setSessionType(DominoSessionType.NATIVE);
		DominoUtils.setBubbleExceptions(true);
	}

	@Override
	public abstract void run();
}
