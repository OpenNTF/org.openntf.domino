package org.openntf.domino.xots;

public abstract class AbstractXotsRunnable implements Runnable {

	private XotsContext context_;

	public XotsContext getContext() {
		return context_;
	}

	public void setContext(final XotsContext context) {
		context_ = context;
	}

}
