/**
 * 
 */
package org.openntf.domino.xsp.adapter;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.openntf.domino.utils.Factory;
import org.openntf.domino.xots.XotsDaemon;

import com.ibm.designer.runtime.domino.adapter.ComponentModule;
import com.ibm.designer.runtime.domino.adapter.HttpService;
import com.ibm.designer.runtime.domino.adapter.LCDEnvironment;
import com.ibm.designer.runtime.domino.bootstrap.adapter.HttpServletRequestAdapter;
import com.ibm.designer.runtime.domino.bootstrap.adapter.HttpServletResponseAdapter;
import com.ibm.designer.runtime.domino.bootstrap.adapter.HttpSessionAdapter;
import com.ibm.domino.xsp.module.nsf.NSFService;

/**
 * This class wraps doService calls and terminates the factory after execution
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class OpenntfHttpService extends HttpService {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(OpenntfHttpService.class.getName());
	private static OpenntfHttpService instance_;
	private boolean active = false;

	private ThreadLocal<Boolean> doServiceEntered = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return Boolean.FALSE;
		}
	};

	public OpenntfHttpService(final LCDEnvironment lcdEnv) {
		super(lcdEnv);
		System.out.println("Openntf-Service loaded");
		try {
			//this.services = lcdEnv.getServices();
			if (instance_ != null) {
				log_.severe("There is more than one XotsService instance active. This may cause problems.");
			}
			instance_ = this;
			// here is the right place to initialize things on server start
			Factory.init();
			Factory.terminate();

			XotsDaemon.getInstance().start();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public void destroyService() {
		XotsDaemon.getInstance().stop();
		Factory.shutdown();
		super.destroyService();
		instance_ = null;
	}

	public void activate() {
		log_.info("OpenNTFHttpService activated");
		active = true;
	}

	public void deactivate() {
		log_.info("OpenNTFHttpService deactivated");
		active = false;
	}

	private NSFService nsfservice_;

	/**
	 * Method to find the active NSFService
	 * 
	 * @return the NSFService
	 */
	private NSFService getNsfService() {
		if (nsfservice_ == null) {
			for (HttpService service : getEnvironment().getServices()) {
				if (service instanceof NSFService) {
					nsfservice_ = (NSFService) service;
					break;
				}
			}
		}
		return nsfservice_;
	}

	public static NSFService sGetNsfService() {
		return instance_.getNsfService();
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

		//FIXME We really should have a registry of the paths that are using the API so we only 
		if (!active) {
			return false;
		}

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
			return getNsfService().doService(contextPath, path, httpSession, httpRequest, httpResponse);
		} finally {
			doServiceEntered.set(Boolean.FALSE);
			Factory.terminate();
		}
	}

	@Override
	public int getPriority() {
		return 98; // NSFService has 99, this must be lower
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
