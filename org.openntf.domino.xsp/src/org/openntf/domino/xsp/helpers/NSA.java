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
import com.ibm.domino.xsp.module.nsf.runtime.NotesApplication;
import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.application.events.ApplicationListener;
import com.ibm.xsp.application.events.SessionListener;

/**
 * @author Nathan T. Freeman
 * 
 */
public enum NSA implements ApplicationListener, SessionListener {
	INSTANCE;

	private Map<String, Application> apps_ = new ConcurrentHashMap<String, Application>();
	private Map<String, NSFComponentModule> modules_ = new ConcurrentHashMap<String, NSFComponentModule>();
	private Map<String, HttpSession> sessions_ = new ConcurrentHashMap<String, HttpSession>();

	// private OpenntfServlet parent_;

	public Map<String, Application> getApps() {
		// System.out.println("Getting application map from " + System.identityHashCode(this));
		if (apps_ == null) {
			// System.out.println("creating new map for applications");
			apps_ = new ConcurrentHashMap<String, Application>();
		}
		return apps_;
	}

	public Map<String, NSFComponentModule> getModules() {
		// System.out.println("Getting modules map from " + System.identityHashCode(this));
		if (modules_ == null) {
			// System.out.println("creating new map for modules");
			modules_ = new ConcurrentHashMap<String, NSFComponentModule>();
		}
		return modules_;
	}

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

	@Override
	public void applicationCreated(final ApplicationEx arg0) {
		registerApplication(arg0);
	}

	@Override
	public void applicationDestroyed(final ApplicationEx arg0) {
		unregisterApplication(arg0);
	}

	@Override
	public void sessionCreated(final ApplicationEx paramApplicationEx, final HttpSessionEvent paramHttpSessionEvent) {

		registerSession(paramApplicationEx, paramHttpSessionEvent.getSession());
	}

	@Override
	public void sessionDestroyed(final ApplicationEx paramApplicationEx, final HttpSessionEvent paramHttpSessionEvent) {
		unregisterSession(paramApplicationEx, paramHttpSessionEvent.getSession());
	}

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

	public void unregisterApplication(final ApplicationEx app) {
		Map<String, Application> apps = getApps();
		String id = app.getApplicationId();
		// System.out.println("Unregistering Application " + id);
		synchronized (apps) {
			apps.remove(id);
			unregisterModule(id);
		}
	}

	public void registerModule(final String appId, final NSFComponentModule app) {
		Map<String, NSFComponentModule> modules = getModules();
		String id = appId;
		NotesApplication na = app.getNotesApplication();
		// System.out.println("Registering Module " + id + " for NotesApplication " + na.getAppName() + " (" + na.getAppId() + ") into "
		// + System.identityHashCode(this));
		synchronized (modules) {
			modules.put(id, app);
		}
	}

	public void unregisterModule(final String appId) {
		Map<String, NSFComponentModule> modules = getModules();
		// System.out.println("Unregistering Module " + appId);
		synchronized (modules) {
			modules.remove(appId);
		}
	}

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
				NSFComponentModule module = getModules().get(key);
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
				Map attribs = session.getAttributes();
				for (Object akey : attribs.keySet()) {
					sb.append(String.valueOf(akey) + ": " + String.valueOf(attribs.get(key)) + ", ");
				}

				ServletContext sc = session.getServletContext();
				if (sc instanceof LCDAdapterServletContext) {
					sb.append("<br/>**********" + sc.getClass().getName() + "*********<br/>");
					// sb.append(", ");
					LCDAdapterServletContext lasc = (LCDAdapterServletContext) sc;
					Enumeration lascAs = lasc.getAttributeNames();
					while (lascAs.hasMoreElements()) {
						Object o = lascAs.nextElement();
						sb.append((String) o + ": " + lasc.getAttribute((String) o) + "<br/>");
					}
					Enumeration servlets = lasc.getServlets();
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
