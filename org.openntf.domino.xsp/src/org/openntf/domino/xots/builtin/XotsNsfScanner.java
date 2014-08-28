package org.openntf.domino.xots.builtin;

import java.io.Serializable;
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

@Schedule(frequency = 4, timeunit = TimeUnit.HOURS)
@Persistent
public class XotsNsfScanner extends XotsBaseTasklet implements Serializable {
	private static final long serialVersionUID = 1L;

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
				t.printStackTrace();
			}
		}
	}

	private XotsService service_;

	private XotsService getXotsService() {
		if (service_ == null) {
			for (HttpService service : LCDEnvironment.getInstance().getServices()) {
				if (service instanceof XotsService) {
					service_ = (XotsService) service;
					break;
				}
			}
		}
		return service_;
	}

	public void scanDatabase(final Database db) throws ServletException, InterruptedException {
		if (TRACE) {
			System.out.println("TRACE: Scanning database " + db.getApiPath() + " for Xots Tasklets");
		}
		DatabaseDesign design = db.getDesign();
		IconNote icon = design.getIconNote();
		Document iconDoc = icon.getDocument();
		if (iconDoc.hasItem("$Xots")) {
			String[] xotsClassNames = iconDoc.getItemValue("$Xots", String[].class);
			getXotsService().getComponentModule("/" + db.getFilePath());
			getXotsService().loadXotsTasklets("/" + db.getFilePath(), xotsClassNames);
		}

	}
}
