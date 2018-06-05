package org.openntf.domino.xsp.xots;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.openntf.domino.xots.Tasklet;
import org.openntf.domino.xots.XotsUtil;
import org.openntf.domino.xsp.IXspHttpServletResponseCallback;

@Tasklet(session = Tasklet.Session.CLONE, context = Tasklet.Context.XSPSCOPED)
public class BasicXotsXspCallbackRunnable extends AbstractXotsXspRunnable {

	private IXspHttpServletResponseCallback callback;
	private HttpServletRequest request;

	public BasicXotsXspCallbackRunnable(final IXspHttpServletResponseCallback callback, final HttpServletRequest request) {
		this.callback = callback;
		this.request = request;
	}

	@Override
	public void run() {
		try {
			callback.process(request, null);
		} catch (IOException e) {
			XotsUtil.handleException(e, getContext());
		}
	}

}
