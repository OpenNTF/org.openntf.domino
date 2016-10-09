/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.faces.application.Application;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import javax.servlet.http.HttpSessionEvent;

import com.ibm.designer.runtime.domino.adapter.ComponentModule;
import com.ibm.designer.runtime.domino.adapter.HttpService;
import com.ibm.designer.runtime.domino.adapter.LCDEnvironment;
import com.ibm.designer.runtime.domino.adapter.servlet.LCDAdapterHttpSession;
import com.ibm.designer.runtime.domino.adapter.servlet.LCDAdapterServletContext;
import com.ibm.designer.runtime.domino.bootstrap.BootstrapEnvironment;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NSFService;
import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.application.events.ApplicationListener;
import com.ibm.xsp.application.events.SessionListener;

/**
 * @author Nathan T. Freeman
 * 
 */
@SuppressWarnings("deprecation")
public enum NSA implements ApplicationListener, SessionListener {
	INSTANCE;

	private Map<String, Application> apps_ = new ConcurrentHashMap<String, Application>();
	private Map<String, NSFComponentModule> modules_ = new ConcurrentHashMap<String, NSFComponentModule>();
	private Map<String, HttpSession> sessions_ = new ConcurrentHashMap<String, HttpSession>();

	// private OpenntfServlet parent_;

	/**
	 * Gets a map of Applications currently loaded, where the key is the applicationId and the value is the Application itself
	 * 
	 * @return Map<String, Application>
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public Map<String, Application> getApps() {
		// System.out.println("Getting application map from " + System.identityHashCode(this));
		if (apps_ == null) {
			// System.out.println("creating new map for applications");
			apps_ = new ConcurrentHashMap<String, Application>();
		}
		return apps_;
	}

	/**
	 * Gets a Map of NSFComponentModules currently loaded, where the key is the applicationId for the application containing the module and
	 * the value is the NSFComponentModule
	 * 
	 * @return Map<String, NSFComponentModule>
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public Map<String, NSFComponentModule> getModules() {
		// System.out.println("Getting modules map from " + System.identityHashCode(this));
		if (modules_ == null) {
			// System.out.println("creating new map for modules");
			modules_ = new ConcurrentHashMap<String, NSFComponentModule>();
		}
		return modules_;
	}

	/**
	 * Gets a Map of HttpSessions currently loaded
	 * 
	 * @return Map<String, HttpSession>
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public Map<String, HttpSession> getSessions() {
		// System.out.println("Getting session map from " + System.identityHashCode(this));
		if (sessions_ == null) {
			// System.out.println("creating new map for sessions");
			sessions_ = new ConcurrentHashMap<String, HttpSession>();
		}
		return sessions_;
	}

	// public void setParent(final OpenntfServlet parent) {
	// System.out.println("Setting parent on " + System.identityHashCode(this));
	// parent_ = parent;
	// }
	//
	// public OpenntfServlet getParent() {
	// return parent_;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.application.events.ApplicationListener#applicationCreated(com.ibm.xsp.application.ApplicationEx)
	 */
	@Override
	public void applicationCreated(final ApplicationEx arg0) {
		registerApplication(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.application.events.ApplicationListener#applicationDestroyed(com.ibm.xsp.application.ApplicationEx)
	 */
	@Override
	public void applicationDestroyed(final ApplicationEx arg0) {
		unregisterApplication(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.application.events.SessionListener#sessionCreated(com.ibm.xsp.application.ApplicationEx,
	 * javax.servlet.http.HttpSessionEvent)
	 */
	@Override
	public void sessionCreated(final ApplicationEx paramApplicationEx, final HttpSessionEvent paramHttpSessionEvent) {

		registerSession(paramApplicationEx, paramHttpSessionEvent.getSession());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.application.events.SessionListener#sessionDestroyed(com.ibm.xsp.application.ApplicationEx,
	 * javax.servlet.http.HttpSessionEvent)
	 */
	@Override
	public void sessionDestroyed(final ApplicationEx paramApplicationEx, final HttpSessionEvent paramHttpSessionEvent) {
		unregisterSession(paramApplicationEx, paramHttpSessionEvent.getSession());
	}

	/**
	 * Registers an application to the map
	 * 
	 * @param app
	 *            ApplicationEx
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public void registerApplication(final ApplicationEx app) {
		Map<String, Application> apps = getApps();
		String id = app.getApplicationId();
		// System.out.println("Registering Application " + id + " type: " + app.getClass().getName() + " into "
		// + System.identityHashCode(this));
		synchronized (apps) {
			apps.put(id, app);
		}
		registerModule(id, NotesContext.getCurrent().getModule());
		app.addApplicationListener(this);
		app.addSessionListener(this);
	}

	/**
	 * De-registers an application from the map
	 * 
	 * @param app
	 *            ApplicationEx
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public void unregisterApplication(final ApplicationEx app) {
		Map<String, Application> apps = getApps();
		String id = app.getApplicationId();
		// System.out.println("Unregistering Application " + id);
		synchronized (apps) {
			apps.remove(id);
			unregisterModule(id);
		}
	}

	/**
	 * Registers a module to the map
	 * 
	 * @param appId
	 *            String application id against which to register the module
	 * @param app
	 *            NSFComponentModule
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public void registerModule(final String appId, final NSFComponentModule app) {
		Map<String, NSFComponentModule> modules = getModules();
		String id = appId;
		app.getNotesApplication();
		// System.out.println("Registering Module " + id + " for NotesApplication " + na.getAppName() + " (" + na.getAppId() + ") into "
		// + System.identityHashCode(this));
		synchronized (modules) {
			modules.put(id, app);
		}
	}

	/**
	 * De-registers a module from the map
	 * 
	 * @param appId
	 *            String application id to remove from the map
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public void unregisterModule(final String appId) {
		Map<String, NSFComponentModule> modules = getModules();
		// System.out.println("Unregistering Module " + appId);
		synchronized (modules) {
			modules.remove(appId);
		}
	}

	/**
	 * Registers an HttpSession for an application
	 * 
	 * @param app
	 *            ApplicationEx the session is for
	 * @param session
	 *            HttpSession to register
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public void registerSession(final ApplicationEx app, final HttpSession session) {
		Map<String, HttpSession> sessions = getSessions();
		String id = app.getApplicationId();
		String sid = session.getId();
		// System.out.println("Registering HttpSession " + session.getId() + " type: " + session.getClass().getName() + " into "
		// + System.identityHashCode(this));
		synchronized (sessions) {
			sessions.put(id + "_" + sid, session);
		}
		// NotesContext.getCurrent().getCurrentSession()
	}

	/**
	 * De-registers an application from the map
	 * 
	 * @param app
	 *            ApplicationEx the session is for
	 * @param session
	 *            HttpSession to de-register
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	private void unregisterSession(final ApplicationEx app, final HttpSession session) {
		Map<String, HttpSession> sessions = getSessions();
		String id = app.getApplicationId();
		String sid = session.getId();
		// System.out.println("Unregistering HttpSession " + session.getId() + " type: " + session.getClass().getName() + " into "
		// + System.identityHashCode(this));
		synchronized (sessions) {
			sessions.remove(id + "_" + sid);
		}
	}

	/**
	 * Writes out a report of the contents of the various maps
	 * 
	 * @return String output of the maps
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public String getReport() {
		StringBuilder sb = new StringBuilder();
		sb.append("<ul>");
		Map<String, Application> apps = NSA.INSTANCE.getApps();
		synchronized (apps) {
			for (String key : apps.keySet()) {
				sb.append("<li>");
				sb.append(key);
				sb.append(" : ");
				Object o = apps.get(key);
				sb.append(o.getClass().getName());
				getModules().get(key);
				// if (module != null) {
				// NotesContext nc = new NotesContext(module);
				// NotesContext.initThread(nc);
				// try {
				// Class<?> clazz = module.getNotesApplication().loadClass("xsp.Application");
				// if (clazz != null) {
				// Object foo = clazz.newInstance();
				// sb.append("  " + foo.getClass().getName() + " successful!");
				// if (foo instanceof AbstractCompiledPageDispatcher) {
				// DispatcherParameter param = ((AbstractCompiledPageDispatcher) foo).getDispatcherParameter();
				// if (param != null) {
				// String pageName = param.getPageName();
				// sb.append("  page name: " + pageName);
				// } else {
				// sb.append(" DispatcherParameter not available");
				// }
				//
				// }
				// } else {
				// System.out.println("xsp.Application class not loaded, but no error occured");
				// }
				// } catch (Throwable t) {
				// System.out.println("Unable to find class because " + t.getClass().getName());
				// } finally {
				// NotesContext.termThread();
				// }
				// }
				sb.append("</li>");
			}
		}
		sb.append("</ul>");
		sb.append("<p/>");
		sb.append("<ul>");
		Map<String, NSFComponentModule> modules = NSA.INSTANCE.getModules();
		synchronized (modules) {
			for (String key : modules.keySet()) {
				sb.append("<li>");
				sb.append(key);
				sb.append(" : ");
				NSFComponentModule module = modules.get(key);
				sb.append(module.getModuleName());
				sb.append("");
				HttpService service = module.getHttpService();
				sb.append("<br/>***HttpService: " + service.getClass().getName() + "***<br/>");
				if (service instanceof NSFService) {
					NSFService nsfService = (NSFService) service;
					nsfService.tellCommand("ss com.redpill");
				}
				LCDEnvironment env = module.getEnvironment();
				sb.append("<br/>***Environment: " + env.getClass().getName() + "***<br/>");
				BootstrapEnvironment be = env.getBootstrapEnvironment();
				sb.append("Bootstrap: " + be.getClass().getName());

				sb.append("</li>");
			}
		}
		sb.append("</ul>");
		sb.append("<p/>");
		sb.append("<ul>");
		Map<String, HttpSession> sessions = NSA.INSTANCE.getSessions();
		synchronized (sessions) {
			for (String key : sessions.keySet()) {
				sb.append("<li>");

				LCDAdapterHttpSession session = (LCDAdapterHttpSession) sessions.get(key);

				sb.append(session.getId());
				sb.append(" : ");
				sb.append(session.getClass().getName());
				ComponentModule mod = session.getModule();
				sb.append(", " + mod.getClass().getName());
				sb.append("<br/>VALUES<br/>");
				for (String vname : session.getValueNames()) {
					sb.append(String.valueOf(vname) + ": " + String.valueOf(session.getValue(vname)) + ", ");
				}
				sb.append("<br/>ATTRIBUTES<br/>");
				Map<?, ?> attribs = session.getAttributes();
				for (Object akey : attribs.keySet()) {
					sb.append(String.valueOf(akey) + ": " + String.valueOf(attribs.get(key)) + ", ");
				}

				ServletContext sc = session.getServletContext();
				if (sc instanceof LCDAdapterServletContext) {
					sb.append("<br/>**********" + sc.getClass().getName() + "*********<br/>");
					// sb.append(", ");
					LCDAdapterServletContext lasc = (LCDAdapterServletContext) sc;
					Enumeration<?> lascAs = lasc.getAttributeNames();
					while (lascAs.hasMoreElements()) {
						Object o = lascAs.nextElement();
						sb.append((String) o + ": " + lasc.getAttribute((String) o) + "<br/>");
					}
					Enumeration<?> servlets = lasc.getServlets();
					while (servlets.hasMoreElements()) {
						Object o = servlets.nextElement();
						sb.append("Servlet: " + o.getClass().getName() + "<br/>");
					}
					sb.append("<br/>**********" + sc.getClass().getName() + "*********<br/>");
				}
				HttpSessionContext sctx = session.getSessionContext();
				sb.append(sctx.getClass().getName());

				sb.append("</li>");
			}
		}
		sb.append("</ul>");
		return sb.toString();
	}
}
