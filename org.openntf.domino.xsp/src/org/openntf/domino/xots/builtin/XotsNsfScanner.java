package org.openntf.domino.xots.builtin;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.design.IconNote;
import org.openntf.domino.xots.XotsBaseTasklet;
import org.openntf.domino.xots.XotsService;
import org.openntf.domino.xots.annotations.Persistent;
import org.openntf.domino.xots.annotations.Schedule;

import com.ibm.designer.runtime.domino.adapter.HttpService;
import com.ibm.designer.runtime.domino.adapter.LCDEnvironment;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;

@Schedule(frequency = 4, timeunit = TimeUnit.HOURS)
@Persistent
public class XotsNsfScanner extends XotsBaseTasklet implements Serializable {
	public static class LoaderRunnable implements Runnable {
		private NSFComponentModule module_;
		private String[] classNames_;
		private Set<Class<?>> classes_;

		public LoaderRunnable(final NSFComponentModule module, final String[] classNames) {
			module_ = module;
			classNames_ = classNames;
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
	}

	private final boolean TRACE = true;
	private final String serverName_;

	public XotsNsfScanner(final String serverName) {
		serverName_ = serverName;
	}

	public String getServerName() {
		return serverName_;
	}

	@Override
	public void run() {
		Set<Class<?>> taskletClasses = scan();
		setChanged();
		notifyObservers(taskletClasses);
	}

	public Set<Class<?>> scan() {
		Set<Class<?>> result = new LinkedHashSet<Class<?>>();
		Session session = getSession();
		DbDirectory dir = session.getDbDirectory(getServerName());
		dir.setDirectoryType(DbDirectory.Type.DATABASE);
		for (Database db : dir) {
			try {
				Set<Class<?>> taskletClasses = scanDatabase(db);
				if (taskletClasses.size() > 0) {
					result.addAll(taskletClasses);
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		if (TRACE) {
			System.out.println("Found a total of " + result.size() + " Xots Tasklets.");
		}
		return result;
	}

	private XotsService getXotsService() {
		for (HttpService service : LCDEnvironment.getInstance().getServices()) {
			if (service instanceof XotsService) {
				return (XotsService) service;
			}
		}
		return null;
	}

	public Set<Class<?>> scanDatabase(final Database db) throws ServletException, InterruptedException {
		if (TRACE) {
			System.out.println("TRACE: Scanning database " + db.getApiPath() + " for Xots Tasklets");
		}
		DatabaseDesign design = db.getDesign();
		IconNote icon = design.getIconNote();
		Document iconDoc = icon.getDocument();
		if (iconDoc.hasItem("$Xots")) {
			String[] xotsClassNames = iconDoc.getItemValue("$Xots", String[].class);
			NSFComponentModule module = (NSFComponentModule) getXotsService().getComponentModule("/" + db.getFilePath());
			final LoaderRunnable loaderRunnable = new LoaderRunnable(module, xotsClassNames);
			Thread t = new Thread(loaderRunnable);
			t.start();
			t.join();
		}

		return new HashSet<Class<?>>();
	}
}
