package org.openntf.domino.xots;

import java.util.LinkedHashSet;
import java.util.Set;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Session;
import org.openntf.domino.design.DatabaseClassLoader;
import org.openntf.domino.design.DatabaseDesign;

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
				Set<Class<? extends XotsBaseTasklet>> taskletClasses = scanDatabase(db);
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

	public Set<Class<? extends XotsBaseTasklet>> scanDatabase(final Database db) {
		if (TRACE) {
			System.out.println("TRACE: Scanning database " + db.getApiPath() + " for Xots Tasklets");
		}
		DatabaseDesign design = db.getDesign();
		DatabaseClassLoader classLoader = design.getDatabaseClassLoader(XotsNsfScanner.class.getClassLoader());
		Set<Class<? extends XotsBaseTasklet>> taskletClasses = classLoader.getClassesExtending(XotsBaseTasklet.class);
		if (TRACE && taskletClasses.size() > 0) {
			System.out.println("TRACE: Found " + taskletClasses.size() + " Tasklets!");
		}
		return taskletClasses;
	}

}
