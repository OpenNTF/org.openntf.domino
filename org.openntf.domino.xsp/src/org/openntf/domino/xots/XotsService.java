package org.openntf.domino.xots;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;

import com.ibm.designer.runtime.domino.adapter.ComponentModule;
import com.ibm.designer.runtime.domino.adapter.LCDEnvironment;
import com.ibm.designer.runtime.domino.bootstrap.adapter.HttpServletRequestAdapter;
import com.ibm.designer.runtime.domino.bootstrap.adapter.HttpServletResponseAdapter;
import com.ibm.designer.runtime.domino.bootstrap.adapter.HttpSessionAdapter;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NSFService;
import com.ibm.domino.xsp.module.nsf.NotesContext;

public class XotsService extends NSFService {
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
			} catch (Throwable t) {
				t.printStackTrace();
			} finally {
				NotesContext.termThread();
			}
			classes_ = result;
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
			result = super.createNSFModule(arg0);
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
		NSFComponentModule mod = runner.getModule();
		Set<Class<?>> classes = runner.getClasses();
		synchronized (classMap_) {
			classMap_.put(mod, classes);
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

}
