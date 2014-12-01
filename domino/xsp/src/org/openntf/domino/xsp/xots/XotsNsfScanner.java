package org.openntf.domino.xsp.xots;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Session;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.exceptions.UserAccessException;
import org.openntf.domino.thread.AbstractDominoRunnable;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xots.Tasklet;
import org.openntf.domino.xots.Xots;
import org.openntf.domino.xsp.thread.ModuleLoader;

import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.domino.xsp.module.nsf.RuntimeFileSystem;
import com.ibm.domino.xsp.module.nsf.RuntimeFileSystem.NSFResource;
import com.ibm.domino.xsp.module.nsf.RuntimeFileSystem.NSFXspClassResource;

// tell http osgi xots run bundle:org.openntf.domino.xsp org.openntf.domino.xsp.xots.XotsNsfScanner
@Tasklet(session = Tasklet.Session.NATIVE, scope = Tasklet.Scope.NONE, context = Tasklet.Context.PLUGIN, schedule = "periodic:90m")
/**
 * A Runnable that scans for tasklet classes on a specified server
 * 
 * 
 * @author Nathan T. Freeman
 * @author Roland Praml, FOCONIS AG
 */
public class XotsNsfScanner extends AbstractDominoRunnable implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger log_ = Logger.getLogger(XotsNsfScanner.class.getName());

	private final String serverName_;

	public XotsNsfScanner() {
		serverName_ = "";
	}

	public XotsNsfScanner(final String serverName) {
		serverName_ = serverName;
	}

	public String getServerName() {
		return serverName_;
	}

	@Override
	public void run() {
		Factory.println(this, "Scan started");
		scan();
		Factory.println(this, "Scan stopped");
	}

	/**
	 * Scans all databases on the specified server
	 */
	public void scan() {
		Session session = Factory.getSession(SessionType.CURRENT); // Returns a XotsSessionType.NATIVE
		DbDirectory dir = session.getDbDirectory(getServerName());
		dir.setDirectoryType(DbDirectory.Type.DATABASE);
		for (Database db : dir) {
			try {
				if (!scanDatabase(db)) {
					//XotsScheduler.INSTANCE.unregisterTasklets(db.getApiPath());
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		setChanged();
		notifyObservers(null);
	}

	/**
	 * Scan the specified database
	 * 
	 * @param db
	 */
	public void scan(final Database db) {
		try {
			if (!scanDatabase(db)) {
				//XotsScheduler.INSTANCE.unregisterTasklets(db.getApiPath());
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}

		setChanged();
		notifyObservers(null);
	}

	/**
	 * Scans the given database for xots enabled classes
	 * 
	 * @author Roland Praml, FOCONIS AG
	 * 
	 */
	private static class XotsClassScanner implements Callable<Object> {
		private String apiPath;

		public XotsClassScanner(final String apiPath) {
			super();
			this.apiPath = apiPath;
		}

		@Override
		public Object call() throws Exception {

			NSFComponentModule module = ModuleLoader.loadModule(apiPath, true);
			NotesContext ctx = new NotesContext(module);
			NotesContext.initThread(ctx);
			try {
				RuntimeFileSystem vfs = module.getRuntimeFileSystem();
				Map<String, NSFResource> resources = vfs.getAllResources();
				ClassLoader mcl = module.getModuleClassLoader();

				for (NSFResource resource : resources.values()) {
					if (resource instanceof NSFXspClassResource) {
						String path = resource.getNSFPath();
						// Check all classes, but not xsp.*
						if (path.startsWith("WEB-INF/classes/") && path.endsWith(".class") && !path.startsWith("WEB-INF/classes/xsp/")) {
							String className = path.substring(16, path.length() - 6).replace('/', '.');

							try {
								Class<?> clazz = mcl.loadClass(className);
								Tasklet annot = clazz.getAnnotation(Tasklet.class);
								if (annot != null) {
									Factory.println(this, apiPath + ": " + className + " - " + annot);
								}
							} catch (Exception e) {
								System.out.println("Cannot load: " + className + ". " + e.getMessage());
							}

						}
					}
					//if (resource instanceof NSFXspClassResource) {
					//	NSFXspClassResource clResource = (NSFXspClassResource) resource;
					//	clResource.
					//	byte[] arrayOfByte = ((RuntimeFileSystem)localObject3).getFileContentAsByteArray(((NotesContext)localObject2).getNotesDatabase(), str);
					//}

				}
			} finally {
				NotesContext.termThread();
			}
			return null;
		}
	}

	/**
	 * 
	 * @param session
	 * @param db
	 * @return true, if the database has XOTS-Classes
	 * @throws
	 * @throws ServletException
	 */
	protected boolean scanDatabase(final Database db) {
		//NTF Keeping JG's implementation since he made an enhancement to the IconNote class for it! :)
		log_.finest("Scanning database " + db.getApiPath() + " for Xots Tasklets");

		try {
			Database template = db.getXPageSharedDesignTemplate();
			DatabaseDesign design = template == null ? db.getDesign() : template.getDesign();

			if (design.isAPIEnabled()) {
				log_.info("ODA enabled database: " + db.getApiPath());
				Future<Object> future = Xots.getService().submit(new XotsClassScanner(db.getApiPath()));

			}

			//			
			//			
			//			
			//			
			//			
			//			IconNote icon = design.getIconNote();
			//
			//			if (icon != null) {
			//				String[] xotsClassNames = icon.getXotsClassNames();
			//				if (xotsClassNames != null && xotsClassNames.length > 0) {
			//					if (log_.isLoggable(Level.FINE)) {
			//						log_.fine("Adding Xots Tasklets for database " + db.getApiPath());
			//					}
			//
			//					//					String dbPath = db.getFilePath().replace('\\', '/');
			//					//					dbPath = NSFService.FILE_CASE_INSENSITIVE ? dbPath.toLowerCase() : dbPath;
			//					//					if (!StringUtil.isEmpty(db.getServer())) {
			//					//						dbPath = db.getServer() + "!!" + dbPath;
			//					//					}
			//					//
			//					//					NSFComponentModule module = OpenntfHttpService.sGetNsfService().loadModule(dbPath);
			//					//
			//					//					Runnable loader = new LoaderRunnable(db.getApiPath(), xotsClassNames, module);
			//					//					XotsDaemon.addToQueue(loader);
			//					return true;
			//				}
			//			}
		} catch (UserAccessException uae) {
			Factory.println(this, "WARNING: " + uae.getMessage());
			// we don't log the warning in the log, as this may confuse admins.
			log_.log(Level.INFO, uae.getMessage(), uae);
		} catch (FileNotFoundException e) {
			Factory.println(this, "WARNING: " + e.getMessage());
			log_.log(Level.INFO, e.getMessage(), e);
		}
		return false;
	}
}
