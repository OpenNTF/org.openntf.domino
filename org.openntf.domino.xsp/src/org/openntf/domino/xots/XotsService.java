package org.openntf.domino.xots;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;

import org.openntf.domino.xots.annotations.Persistent;
import org.openntf.domino.xots.annotations.Schedule;

import com.ibm.designer.runtime.domino.adapter.HttpService;
import com.ibm.designer.runtime.domino.adapter.LCDEnvironment;
import com.ibm.designer.runtime.domino.bootstrap.adapter.HttpServletRequestAdapter;
import com.ibm.designer.runtime.domino.bootstrap.adapter.HttpServletResponseAdapter;
import com.ibm.designer.runtime.domino.bootstrap.adapter.HttpSessionAdapter;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NSFService;
import com.ibm.domino.xsp.module.nsf.NotesContext;

public class XotsService extends NSFService {

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
			classes_ = new HashSet<Class<?>>();
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
					classes_.add(curClass);
					callback_.loaderCallback(this);
				}
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

	// private final Map<String, NSFComponentModule> modules_ = new ConcurrentHashMap<String, NSFComponentModule>();
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

	//	@Override
	//	public ComponentModule getComponentModule(String modulePath) throws ServletException {
	//		// RPr: on a windows machine, the databasePath is returned with "\\"
	//		// BTW: why don't we use "loadModule" here
	//		// BTW: Why do we override this method at all?
	//		// [15.07.2014 20:38:57] Nathan T. Freeman: okay, if we call createNSFModule instead of loadModule, then it won't cache the module
	//		// [15.07.2014 20:39:08] Nathan T. Freeman: but createNSFModule is protected
	//		// but we DO cache the module, so I see no advantage
	//		modulePath = StringUtil.replace(modulePath, '\\', '/');
	//		
	//		System.out.println("DEBUG: XotsService created NSF module for path " + modulePath);
	//		NSFComponentModule result = modules_.get(modulePath);
	//		if (result == null) {
	//			result = super.createNSFModule(modulePath);
	//			modules_.put(modulePath, result);
	//		}
	//		return result;
	//	}

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
		try {
			t.join();// RPr: should we really load all modules in an own thread async?s
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void loaderCallback(final LoaderRunnable runner) {
		NSFComponentModule referenceMod = runner.getModule();

		Set<Class<?>> classes = runner.getClasses();
		for (Class<?> clazz : classes) {
			Persistent persistent = clazz.getAnnotation(Persistent.class);
			Persistent.Context ctx = persistent.appContext();
			Persistent.Scope scope = persistent.scope();
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		System.out.println(TimeUnit.MINUTES);
		for (Entry<NSFComponentModule, Set<Class<?>>> e : classMap_.entrySet()) {
			if (sb.length() > 0)
				sb.append('\n');
			sb.append(e.getKey().getDatabasePath());
			sb.append('\n');
			for (Class<?> cls : e.getValue()) {
				sb.append('\t');
				sb.append(cls.getName());
				Persistent persistent = cls.getAnnotation(Persistent.class);
				if (persistent != null) {
					sb.append(", Scope:");
					sb.append(persistent.scope());
					sb.append(", AppContext:");
					sb.append(persistent.appContext());
				}
				Schedule schedule = cls.getAnnotation(Schedule.class);
				if (schedule != null) {
					sb.append(", Freq:");
					sb.append(schedule.frequency());

					// sb.append(", TimeUnit:");
					// sb.append(schedule.timeunit());
					//
					// WHEE: Why do I get this error here:
					//			java.lang.TypeNotPresentException: Type java.util.concurrent.TimeUnit not present
					//				       at com.ibm.oti.reflect.AnnotationHelper$AnnotationInvocationHandler.invoke(AnnotationHelper.java:134)
					//				       at com.sun.proxy.$Proxy1.timeunit(Unknown Source)
					//				       at org.openntf.domino.xots.XotsService.toString(XotsService.java:242)
					//				       at org.openntf.domino.xots.builtin.XotsNsfScanner.scan(XotsNsfScanner.java:56)
					//				       at org.openntf.domino.xots.builtin.XotsNsfScanner.run(XotsNsfScanner.java:39)
					//				       at lotus.domino.NotesThread.run(Unknown Source)
					//				Caused by:
					//				java.lang.ClassNotFoundException: java.util.concurrent.TimeUnit
					//				       at com.ibm.oti.reflect.AnnotationHelper.getReturnValueForEntry(Native Method)
					//				       at com.ibm.oti.reflect.AnnotationHelper.access$000(AnnotationHelper.java:19)
					//				       at com.ibm.oti.reflect.AnnotationHelper$AnnotationInvocationHandler.invoke(AnnotationHelper.java:132)
					//				       ... 5 more

					sb.append(", WeekDays:");
					sb.append(Arrays.toString(schedule.weekdays()));
					sb.append(", ");
					sb.append(schedule.starthour());
					sb.append("-");
					sb.append(schedule.endhour());
					sb.append("h");
				}
				sb.append('\n');
			}
		}
		return sb.toString();
	}
}
