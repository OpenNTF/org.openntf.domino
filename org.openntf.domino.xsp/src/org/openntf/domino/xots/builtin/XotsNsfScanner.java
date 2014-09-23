package org.openntf.domino.xots.builtin;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Session;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.design.IconNote;
import org.openntf.domino.xots.XotsBaseTasklet;
import org.openntf.domino.xots.XotsService;
import org.openntf.domino.xots.annotations.Persistent;
import org.openntf.domino.xots.annotations.Schedule;

@Schedule(frequency = 4, timeunit = TimeUnit.HOURS)
@Persistent
public class XotsNsfScanner extends XotsBaseTasklet implements Serializable {
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
		setChanged();
		notifyObservers(null);
	}

	public void scan() {
		Session session = getSession();
		DbDirectory dir = session.getDbDirectory(getServerName());
		dir.setDirectoryType(DbDirectory.Type.DATABASE);
		for (Database db : dir) {
			try {
				scanDatabase(db);
			} catch (Throwable t) {
				//				t.printStackTrace();
				System.err.println("*** Exception: " + t.getMessage());
			}
		}
		System.out.println("Current XOTS Classes:");
		System.out.println(getXotsService().toString());
	}

	private XotsService service_;

	private XotsService getXotsService() {
		if (service_ == null)
			service_ = XotsService.getInstance();
		return service_;
	}

	public void scanDatabase(final Database db) throws ServletException, InterruptedException {
		//NTF Keeping JG's implementation since he made an enhancement to the IconNote class for it! :)
		log_.finest("Scanning database " + db.getApiPath() + " for Xots Tasklets");

		DatabaseDesign design = db.getDesign();
		IconNote icon = design.getIconNote();
		if (icon != null) {
			String[] xotsClassNames = icon.getXotsClassNames();
			if (xotsClassNames != null && xotsClassNames.length > 0) {
				if (TRACE) {
					System.out.println("TRACE: Adding Xots Tasklets for database " + db.getApiPath());
				}
				getXotsService().getComponentModule("/" + db.getFilePath());
				getXotsService().loadXotsTasklets("/" + db.getFilePath(), xotsClassNames);

			}
		}
	}
}
