package org.openntf.domino.xots;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;

import org.openntf.domino.xots.annotations.Persistent;

import com.ibm.designer.runtime.domino.adapter.ComponentModule;
import com.ibm.designer.runtime.domino.adapter.HttpService;
import com.ibm.designer.runtime.domino.adapter.LCDEnvironment;
import com.ibm.designer.runtime.domino.bootstrap.adapter.HttpServletRequestAdapter;
import com.ibm.designer.runtime.domino.bootstrap.adapter.HttpServletResponseAdapter;
import com.ibm.designer.runtime.domino.bootstrap.adapter.HttpSessionAdapter;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NSFService;
import com.ibm.domino.xsp.module.nsf.NotesContext;

public class XotsService extends NSFService {

	@Override
	public void destroyService() {
		System.out.println("DEBUG: Destroying XotsService...");
		XotsDaemon.stop();
		super.destroyService();
	}

	public static XotsService getInstance() {
		for (HttpService service : LCDEnvironment.getInstance().getServices()) {
			if (service instanceof XotsService) {
				return (XotsService) service;
			}
		}
		return null;
	}

	public synchronized static void addToQueue(final Runnable runnable) {
		try {
			AccessController.doPrivileged(new PrivilegedAction<Object>() {
				@Override
				public Object run() {
					// getInstance().queue(runnable, runnable.getClass().getClassLoader());
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class LoaderRunnable implements Runnable {
		private NSFComponentModule module_;
		private String[] classNames_;
		private Set<Class<?>> classes_;
		private XotsService callback_;

		public LoaderRunnable(final NSFComponentModule module, final String[] classNames, final XotsService callback) {
			module_ = module;
			classNames_ = classNames;
			callback_ = callback;
		}

		@Override
		public void run() {
			Set<Class<?>> result = new HashSet<Class<?>>();
			try {
				NotesContext ctxContext = new NotesContext(module_);
				NotesContext.initThread(ctxContext);
				ClassLoader mcl = module_.getModuleClassLoader();
				for (String className : classNames_) {
					Class<?> curClass = null;
					try {
						curClass = mcl.loadClass(className);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					result.add(curClass);
				}

				classes_ = result;

				callback_.loaderCallback(this);
			} catch (Throwable t) {
				t.printStackTrace();
			} finally {
				NotesContext.termThread();
			}
		}

		public Set<Class<?>> getClasses() {
			return classes_;
		}

		public NSFComponentModule getModule() {
			return module_;
		}
	}

	private final Map<String, NSFComponentModule> modules_ = new ConcurrentHashMap<String, NSFComponentModule>();
	private final Map<NSFComponentModule, Set<Class<?>>> classMap_ = new ConcurrentHashMap<NSFComponentModule, Set<Class<?>>>();

	public XotsService(final LCDEnvironment arg0) {
		super(arg0);
	}

	@Override
	public int getPriority() {
		return 100;
	}

	private NSFService nsfservice_;

	private NSFService getNsfService() {
		if (nsfservice_ == null) {
			for (HttpService service : LCDEnvironment.getInstance().getServices()) {
				if (service instanceof NSFService) {
					nsfservice_ = (NSFService) service;
					break;
				}
			}
		}
		return nsfservice_;
	}

	@Override
	protected NSFComponentModule createNSFModule(final String arg0) throws ServletException {
		System.out.println("DEBUG: XotsService created NSF module for path " + arg0);
		return super.createNSFModule(arg0);
	}

	@Override
	public ComponentModule getComponentModule(final String arg0) throws ServletException {
		System.out.println("DEBUG: XotsService created NSF module for path " + arg0);
		NSFComponentModule result = modules_.get(arg0);
		if (result == null) {
			// Loading seems to not return it immediately
			getNsfService().loadModule(arg0);
			result = (NSFComponentModule) getNsfService().getComponentModule(arg0);
			//			result = super.createNSFModule(arg0);
			modules_.put(arg0, result);
		}
		return result;
	}

	@Override
	public boolean doService(final String arg0, final String arg1, final HttpSessionAdapter arg2, final HttpServletRequestAdapter arg3,
			final HttpServletResponseAdapter arg4) throws ServletException, IOException {
		System.out.println("DEBUG ALERT!! XotsService has been asked to service an HttpRequest!");
		return super.doService(arg0, arg1, arg2, arg3, arg4);
	}

	public void loadXotsTasklets(final String arg0, final String[] classNames) throws ServletException {
		NSFComponentModule module = (NSFComponentModule) getComponentModule(arg0);
		final LoaderRunnable loaderRunnable = new LoaderRunnable(module, classNames, this);
		Thread t = new lotus.domino.NotesThread(loaderRunnable);
		t.start();
	}

	public void loaderCallback(final LoaderRunnable runner) {
		NSFComponentModule referenceMod = runner.getModule();
		Set<Class<?>> classes = runner.getClasses();
		for (Class<?> clazz : classes) {
			Persistent persistent = clazz.getAnnotation(Persistent.class);
			Persistent.Context ctx = persistent.appContext();
			Persistent.Scope scope = persistent.scope();
			// TODO de-dupe based on replica ID to handle faux text ".nsf" redirection files
			if (scope == Persistent.Scope.APPLICATION) {
				if (ctx == Persistent.Context.XSPFORCED) {
					try {
						NSFComponentModule forcedMod = getNsfService().loadModule(referenceMod.getDatabasePath());
						synchronized (classMap_) {
							classMap_.put(forcedMod, classes);
						}
					} catch (Throwable t) {
						t.printStackTrace();
					}
				} else if (ctx == Persistent.Context.XSPSCOPED) {

				}

			} else if (scope == Persistent.Scope.SERVER) {
				NSFComponentModule mod = runner.getModule();
				synchronized (classMap_) {
					classMap_.put(mod, classes);	// TODO need to put actual objects rather than classes into the map
				}
			} else {
				NSFComponentModule mod = runner.getModule();
				synchronized (classMap_) {
					classMap_.put(mod, classes);
				}
			}
		}

	}

	@Override
	public void initThreads() {
		System.out.println("DEBUG: XotsService is being initialized.");
	}

	@Override
	public void termThreads() {
		System.out.println("DEBUG: XotsService is being terminated.");
	}

	protected Set<Class<?>> getLoadedClasses() {
		Set<Class<?>> result = new HashSet<Class<?>>();
		for (Set<Class<?>> classes : classMap_.values()) {
			result.addAll(classes);
		}
		return result;
	}

}
