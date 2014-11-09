package org.openntf.domino.xots;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

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
	protected boolean scanDatabase(final Database db) {
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

					//					String dbPath = db.getFilePath().replace('\\', '/');
					//					dbPath = NSFService.FILE_CASE_INSENSITIVE ? dbPath.toLowerCase() : dbPath;
					//					if (!StringUtil.isEmpty(db.getServer())) {
					//						dbPath = db.getServer() + "!!" + dbPath;
					//					}
					//
					//					NSFComponentModule module = OpenntfHttpService.sGetNsfService().loadModule(dbPath);
					//
					//					Runnable loader = new LoaderRunnable(db.getApiPath(), xotsClassNames, module);
					//					XotsDaemon.addToQueue(loader);
					return true;
				}
			}
		} catch (UserAccessException uae) {
			log_.warning("XotsNsfScanner: Database " + db.getFilePath() + " cannot be opened by "
					+ db.getAncestorSession().getEffectiveUserName());
		}
		return false;
	}

}
