package org.openntf.domino.xots.builtin;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Session;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.design.IconNote;
import org.openntf.domino.exceptions.UserAccessException;
import org.openntf.domino.thread.AbstractDominoRunnable;
import org.openntf.domino.thread.model.Context;
import org.openntf.domino.thread.model.Schedule;
import org.openntf.domino.thread.model.Scope;
import org.openntf.domino.thread.model.SessionType;
import org.openntf.domino.thread.model.XotsTasklet;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xots.IRunsInModule;
import org.openntf.domino.xots.TaskletDefinition;
import org.openntf.domino.xots.XotsDaemon;
import org.openntf.domino.xots.XotsScheduler;
import org.openntf.domino.xsp.adapter.OpenntfHttpService;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NSFService;

@Schedule(frequency = 4, timeunit = TimeUnit.HOURS)
@XotsTasklet(session = SessionType.NATIVE, scope = Scope.NONE, context = Context.XOTS)
/**
 * A Runnable that scans for tasklet classes on a specified server
 * 
 * @author Nathan T. Freeman
 * @author Roland Praml, FOCONIS AG
 */
public class XotsNsfScanner extends AbstractDominoRunnable implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger log_ = Logger.getLogger(XotsNsfScanner.class.getName());

	private final boolean TRACE = false;
	private final String serverName_;

	public XotsNsfScanner(final String serverName) {
		serverName_ = serverName;
	}

	public String getServerName() {
		return serverName_;
	}

	@Override
	public void run() {
		scan();
		System.out.println("Current XOTS Classes:");
		System.out.println(XotsScheduler.INSTANCE);
	}

	/**
	 * Scans all databases on the specified server
	 */
	public void scan() {
		Session session = Factory.getSession();
		DbDirectory dir = session.getDbDirectory(getServerName());
		dir.setDirectoryType(DbDirectory.Type.DATABASE);
		for (Database db : dir) {
			try {
				if (!scanDatabase(db)) {
					XotsScheduler.INSTANCE.unregisterTasklets(db.getApiPath());
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
				XotsScheduler.INSTANCE.unregisterTasklets(db.getApiPath());
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}

		setChanged();
		notifyObservers(null);
	}

	/**
	 * 
	 * @param session
	 * @param db
	 * @return true, if the database has XOTS-Classes
	 * @throws ServletException
	 */
	protected boolean scanDatabase(final Database db) throws ServletException {
		//NTF Keeping JG's implementation since he made an enhancement to the IconNote class for it! :)
		log_.finest("Scanning database " + db.getApiPath() + " for Xots Tasklets");

		try {
			DatabaseDesign design = db.getDesign();
			IconNote icon = design.getIconNote();

			if (icon != null) {
				String[] xotsClassNames = icon.getXotsClassNames();
				if (xotsClassNames != null && xotsClassNames.length > 0) {
					if (TRACE) {
						System.out.println("TRACE: Adding Xots Tasklets for database " + db.getApiPath());
					}

					String dbPath = db.getFilePath().replace('\\', '/');
					dbPath = NSFService.FILE_CASE_INSENSITIVE ? dbPath.toLowerCase() : dbPath;
					if (!StringUtil.isEmpty(db.getServer())) {
						dbPath = db.getServer() + "!!" + dbPath;
					}

					NSFComponentModule module = OpenntfHttpService.sGetNsfService().loadModule(dbPath);

					Runnable loader = new LoaderRunnable(db.getApiPath(), xotsClassNames, module);
					XotsDaemon.addToQueue(loader);
					return true;
				}
			}
		} catch (UserAccessException uae) {
			log_.warning("XotsNsfScanner: Database " + db.getFilePath() + " cannot be opened by "
					+ db.getAncestorSession().getEffectiveUserName());
		}
		return false;
	}

	/**
	 * 
	 * @author Roland Praml, FOCONIS AG
	 * 
	 */
	@XotsTasklet(session = SessionType.NATIVE, scope = Scope.NONE, context = Context.XOTS)
	public class LoaderRunnable implements Runnable, IRunsInModule {
		private String apiPath_;
		private String[] classNames_;
		private NSFComponentModule module_;

		public LoaderRunnable(final String apiPath, final String[] classNames, final NSFComponentModule module) {
			apiPath_ = apiPath;
			classNames_ = classNames;
			module_ = module;
		}

		@Override
		public NSFComponentModule getRunModule() {
			return module_;
		}

		@Override
		public void run() {
			try {
				//NotesContext ctxContext = new NotesContext(module_);
				//NotesContext.initThread(ctxContext);
				// TODO RPR use NotesSession.getLastNonDataModificationDateByName(arg0, arg1)

				Set<TaskletDefinition> taskletDefinitions = new HashSet<TaskletDefinition>();
				ClassLoader mcl = Thread.currentThread().getContextClassLoader();
				for (String className : classNames_) {
					String[] classDefBits = className.split(";");	// There may be ";boolean" at the end
					boolean enabled = classDefBits.length < 2 || "true".equals(classDefBits[1]);
					if (enabled) {
						try {
							Class cls = mcl.loadClass(classDefBits[0]);
							if (Runnable.class.isAssignableFrom(cls)) {
								TaskletDefinition def = new TaskletDefinition(cls);
								taskletDefinitions.add(def);
							}
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
				XotsScheduler.INSTANCE.registerTasklets(apiPath_, taskletDefinitions);
			} catch (Throwable t) {
				t.printStackTrace();
			} finally {
				//NotesContext.termThread();
			}
		}

	}
}
