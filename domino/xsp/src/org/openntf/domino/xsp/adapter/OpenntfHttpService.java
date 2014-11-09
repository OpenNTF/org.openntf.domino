/**
 * 
 */
package org.openntf.domino.xsp.adapter;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import javolution.util.FastTable;

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
<<<<<<< HEAD
	private static OpenntfHttpService instance_;
=======
	private static OpenntfHttpService INSTANCE;
	public static final String PATH_EXTENSION = ".nsf";
	private List<HttpService> services;
	private HttpService priorService = null;
>>>>>>> openntf/nathan
	private boolean active = false;
	private final List<String> excludedPaths = new FastTable<String>();
	private final List<String> includedPaths = new FastTable<String>();

	private ThreadLocal<Boolean> doServiceEntered = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return Boolean.FALSE;
		}
	};

	public static OpenntfHttpService getCurrentInstance() {
		return INSTANCE;
	}

	public OpenntfHttpService(final LCDEnvironment lcdEnv) {
		super(lcdEnv);
<<<<<<< HEAD
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
=======
		synchronized (OpenntfHttpService.class) {
			INSTANCE = this;
		}
		this.services = lcdEnv.getServices();
		// here is the right place to initialize things on server start
		Factory.init();
		Factory.terminate();
>>>>>>> openntf/nathan
	}

	public static String parseNsf(final String path) {
		int pos = path.toLowerCase().indexOf(PATH_EXTENSION);
		if (pos > 1) {
			String basePath = path.substring(0, pos);
			return basePath;
		}
		return "";
	}

	public boolean isExcluded(final String path) {
		boolean result = false;
		String nsfPath = parseNsf(path);
		if (nsfPath.length() == 0)
			return true;
		result = excludedPaths.contains(nsfPath);
		return result;
	}

	public boolean isIncluded(final String path) {
		boolean result = false;
		String nsfPath = parseNsf(path);
		if (nsfPath.length() == 0)
			return false;
		result = includedPaths.contains(nsfPath);
		return result;
	}

	public void activate(final HttpServletRequest request) {
		String nsfPath = parseNsf(request.getContextPath());
		includedPaths.add(nsfPath);
		//		System.out.println("DEBUG: Activating application context " + nsfPath);
		active = true;
	}

	public void deactivate(final HttpServletRequest request) {
		String nsfPath = parseNsf(request.getContextPath());
		//		System.out.println("DEBUG: Deactivating application context " + nsfPath);
		excludedPaths.add(nsfPath);
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

	private boolean doParentService(final String contextPath, final String path, final HttpSessionAdapter httpSession,
			final HttpServletRequestAdapter httpRequest, final HttpServletResponseAdapter httpResponse) throws ServletException,
			IOException {
		if (priorService == null) {
			boolean behindUs = false;
			for (HttpService service : this.services) {
				if (behindUs) {
					if (service.doService(contextPath, path, httpSession, httpRequest, httpResponse)) {
						priorService = service;
						return true;
					}
				}
				if (service == this) {
					behindUs = true;
				}
			}
			return false;
		} else {
			return priorService.doService(contextPath, path, httpSession, httpRequest, httpResponse);
		}
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
<<<<<<< HEAD
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
=======
		if (!isExcluded(path)) {
			//			System.out.println("OpenntfHttp handling a request for " + path);
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
				return doParentService(contextPath, path, httpSession, httpRequest, httpResponse);
			} finally {
				doServiceEntered.set(Boolean.FALSE);
				Factory.terminate();
			}
		} else {
			return doParentService(contextPath, path, httpSession, httpRequest, httpResponse);
>>>>>>> openntf/nathan
		}
	}

	@Override
	public int getPriority() {
<<<<<<< HEAD
		return 98; // NSFService has 99, this must be lower
=======
		return 1000; // NSFService has 99, this must be lower
>>>>>>> openntf/nathan
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
