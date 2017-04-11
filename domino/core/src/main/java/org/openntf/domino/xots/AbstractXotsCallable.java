package org.openntf.domino.xots;

import java.util.concurrent.Callable;

public abstract class AbstractXotsCallable<Object> implements Callable<Object> {

	private XotsContext context_;

	public XotsContext getContext() {
		return context_;
	}

	public void setContext(final XotsContext context) {
		context_ = context;
	}

}
