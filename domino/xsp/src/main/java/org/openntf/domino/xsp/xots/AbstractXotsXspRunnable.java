package org.openntf.domino.xsp.xots;

import org.openntf.domino.xots.AbstractXotsRunnable;

public abstract class AbstractXotsXspRunnable extends AbstractXotsRunnable {

	private XotsXspContext context_;

	@Override
	public XotsXspContext getContext() {
		return context_;
	}

	public void setContext(final XotsXspContext context) {
		context_ = context;
	}

}
