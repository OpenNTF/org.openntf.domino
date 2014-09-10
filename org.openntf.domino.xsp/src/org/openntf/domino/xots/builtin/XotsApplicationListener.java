package org.openntf.domino.xots.builtin;

import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.design.IconNote;
import org.openntf.domino.thread.DominoSessionType;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.xots.XotsBaseTasklet;
import org.openntf.domino.xots.XotsDaemon;
import org.openntf.domino.xots.XotsService;

import com.ibm.designer.runtime.Application;
import com.ibm.designer.runtime.domino.adapter.HttpService;
import com.ibm.designer.runtime.domino.adapter.LCDEnvironment;
import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.application.DesignerApplicationEx;
import com.ibm.xsp.application.events.ApplicationListener2;

public class XotsApplicationListener implements ApplicationListener2 {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(XotsApplicationListener.class.getName());

	@Override
	public void applicationCreated(final ApplicationEx app) {
		if (app instanceof DesignerApplicationEx) {
			Application designerApp = ((DesignerApplicationEx) app).getDesignerApplication();
			XotsDaemon.addToQueue(new XotsRefresher(designerApp.getAppName()));
		}
	}

	@Override
	public void applicationDestroyed(final ApplicationEx app) {
	}

	@Override
	public void applicationRefreshed(final ApplicationEx app) {
		if (app instanceof DesignerApplicationEx) {
			Application designerApp = ((DesignerApplicationEx) app).getDesignerApplication();
			XotsDaemon.addToQueue(new XotsRefresher(designerApp.getAppName()));
		}
	}

	// TODO de-duplicate this code copied from XotsNsfScanner
	public static class XotsRefresher extends XotsBaseTasklet {
		private static final long serialVersionUID = 1L;

		private final boolean TRACE = true;

		private boolean finished_ = false;
		private final String appName_;

		public XotsRefresher(final String appName) {
			appName_ = appName;
		}

		@Override
		public DominoSessionType getSessionType() {
			return DominoSessionType.NATIVE;
		}

		@Override
		public void run() {
			Session session = getSession();
			Database db = session.getDatabase(appName_);
			DatabaseDesign design = db.getDesign();
			IconNote icon = design.getIconNote();
			if (icon != null) {
				String[] xotsClassNames = icon.getXotsClassNames();
				if (xotsClassNames != null && xotsClassNames.length > 0) {
					if (TRACE) {
						System.out.println("TRACE: Adding Xots Tasklets for database " + db.getApiPath());
					}
					try {
						getXotsService().getComponentModule("/" + db.getFilePath());
						getXotsService().loadXotsTasklets("/" + db.getFilePath(), xotsClassNames);
					} catch (ServletException se) {
						DominoUtils.handleException(se);
					}
				}
			}

			finished_ = true;
		}

		@Override
		public boolean shouldStop() {
			return finished_;
		}

		private XotsService getXotsService() {
			for (HttpService service : LCDEnvironment.getInstance().getServices()) {
				if (service instanceof XotsService) {
					return (XotsService) service;
				}
			}
			return null;
		}
	}
}
