package org.openntf.domino.xsp.xots;

import org.openntf.domino.xots.AbstractXotsCallable;

public abstract class AbstractXotsXspCallable<Object> extends AbstractXotsCallable {

	private XotsXspContext context_;

	@Override
	public XotsXspContext getContext() {
		return context_;
	}

	public void setContext(final XotsXspContext context) {
		context_ = context;
	}

}
