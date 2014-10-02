/**
 * 
 */
package org.openntf.domino.xsp.adapter;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.openntf.domino.utils.Factory;

import com.ibm.designer.runtime.domino.adapter.ComponentModule;
import com.ibm.designer.runtime.domino.adapter.HttpService;
import com.ibm.designer.runtime.domino.adapter.LCDEnvironment;
import com.ibm.designer.runtime.domino.bootstrap.adapter.HttpServletRequestAdapter;
import com.ibm.designer.runtime.domino.bootstrap.adapter.HttpServletResponseAdapter;
import com.ibm.designer.runtime.domino.bootstrap.adapter.HttpSessionAdapter;

/**
 * This class wraps doService calls and terminates the factory after execution
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class OpenntfHttpService extends HttpService {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(OpenntfHttpService.class.getName());

	private List<HttpService> services;

	private ThreadLocal<Boolean> doServiceEntered = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return Boolean.FALSE;
		}

	};

	public OpenntfHttpService(final LCDEnvironment lcdEnv) {
		super(lcdEnv);
		this.services = lcdEnv.getServices();
		// here is the right place to initialize things on server start
		Factory.init();
		Factory.terminate();
	}

	@Override
	public void destroyService() {
		// here you can put code that runs when the Http-Task will shut down.

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.designer.runtime.domino.adapter.HttpService#doService(java.lang.String, java.lang.String,
	 * com.ibm.designer.runtime.domino.bootstrap.adapter.HttpSessionAdapter,
	 * com.ibm.designer.runtime.domino.bootstrap.adapter.HttpServletRequestAdapter,
	 * com.ibm.designer.runtime.domino.bootstrap.adapter.HttpServletResponseAdapter)
	 */
	@Override
	public boolean doService(final String contextPath, final String path, final HttpSessionAdapter httpSession,
			final HttpServletRequestAdapter httpRequest, final HttpServletResponseAdapter httpResponse) throws ServletException,
			IOException {
		// System.out.println("DEBUG ALERT!! OpenntfHttpService has been asked to service an HttpRequest!");

		if (doServiceEntered.get().booleanValue()) {
			// prevent recursion (if someone does the same trick)
			return false;
		}

		Factory.init();
		Factory.setUserLocale(httpRequest.getLocale());
		doServiceEntered.set(Boolean.TRUE);

		try {
			// TODO - NSA: This is a optimal place where you can put your code to sniff the whole unencrypted HTTP-traffic
			// FIXME - NSA: Go get a real job and mind your own business
			// System.out.println("ContexPath: " + contextPath);
			// System.out.println("Path: " + path);

			boolean behindUs = false;
			for (HttpService service : this.services) {
				if (behindUs) {
					if (service.doService(contextPath, path, httpSession, httpRequest, httpResponse)) {
						return true;
					}
				}
				if (service == this) {
					behindUs = true;
				}
			}
			return false;
		} finally {
			doServiceEntered.set(Boolean.FALSE);
			Factory.terminate();

			// System.out.println("DEBUG: terminating a Session with object id: " + System.identityHashCode(session)
			// + " after an http request");
		}
	}

	@Override
	public int getPriority() {
		return 0; // NSFService has 99, this must be lower
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.designer.runtime.domino.adapter.HttpService#getModules(java.util.List)
	 */
	@Override
	public void getModules(final List<ComponentModule> paramList) {

	}

}
