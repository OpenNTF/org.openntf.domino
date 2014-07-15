package org.openntf.domino.xots;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Session;

import com.ibm.designer.runtime.domino.adapter.HttpService;
import com.ibm.designer.runtime.domino.adapter.LCDEnvironment;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NSFService;
import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.domino.xsp.module.nsf.RuntimeFileSystem.NSFResource;
import com.ibm.domino.xsp.module.nsf.RuntimeFileSystem.NSFXspClassResource;

public class XotsNsfScanner extends XotsBaseTasklet {
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

	private NSFService getNSFService() {
		for (HttpService service : LCDEnvironment.getInstance().getServices()) {
			if (service instanceof NSFService) {
				return (NSFService) service;
			}
		}
		return null;
	}

	public Set<Class<?>> scanDatabase(final Database db) throws ServletException, InterruptedException {
		if (TRACE) {
			System.out.println("TRACE: Scanning database " + db.getApiPath() + " for Xots Tasklets");
		}
		final NSFComponentModule module = AccessController.doPrivileged(new PrivilegedAction<NSFComponentModule>() {

			@Override
			public NSFComponentModule run() {
				try {
					return (NSFComponentModule) getNSFService().getComponentModule("/" + db.getFilePath()); // TODO RPr: Is it possible to open modules across servers?
				} catch (ServletException e) {
					e.printStackTrace();
					return null;
				}
			}
		});

		Thread t = new Thread(new Runnable() {
			// Encapsulate everything in an own thread as you can only have one moduleClassLoader at once per thread.
			@Override
			public void run() {
				NotesContext ctxContext = new NotesContext(module);
				try {
					NotesContext.initThread(ctxContext);

					ClassLoader mcl = module.getModuleClassLoader();
					Map<String, NSFResource> res = module.getRuntimeFileSystem().getAllResources();
					for (Entry<String, NSFResource> entry : res.entrySet()) {
						if (entry.getValue() instanceof NSFXspClassResource) {
							String clsName = entry.getKey();
							if (clsName.startsWith("WEB-INF/classes/") && clsName.endsWith(".class")) {
								clsName = clsName.substring(16, clsName.length() - 6).replace('/', '.');
								if (!clsName.startsWith("xsp.")) {

									try {
										Class candidate = mcl.loadClass(clsName);
										if (XotsBaseTasklet.class.isAssignableFrom(candidate)) {
											if (TRACE) {
												System.out.println("Tasklet found: " + clsName);
											}
										}
									} catch (Throwable t) {
										// TODO correct error handling
										// e.printStackTrace();
									}

								}
							}
						}
					}
				} finally {
					NotesContext.termThread();
				}

			}
		});

		t.start();
		t.join();

		//System.out.println(res);
		/*
				DatabaseDesign design = db.getDesign();

				DatabaseClassLoader classLoader = design.getDatabaseClassLoader(XotsNsfScanner.class.getClassLoader());
				Set<Class<?>> taskletClasses = classLoader.getClassesExtending(XotsBaseTasklet.class);
				if (TRACE && taskletClasses.size() > 0) {
					System.out.println("TRACE: Found " + taskletClasses.size() + " Tasklets!");
				}
				return taskletClasses;
				*/return new HashSet<Class<?>>();
	}
}
