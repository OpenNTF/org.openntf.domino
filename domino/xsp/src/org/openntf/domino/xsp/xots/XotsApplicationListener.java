package org.openntf.domino.xsp.xots;

import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.design.IconNote;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xots.XotsBaseTasklet;

import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.application.DesignerApplicationEx;
import com.ibm.xsp.application.events.ApplicationListener2;

public class XotsApplicationListener implements ApplicationListener2 {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(XotsApplicationListener.class.getName());

	@Override
	public void applicationCreated(final ApplicationEx app) {
		if (app instanceof DesignerApplicationEx) {
			//NTF The following code block destroys all XPages execution on the server
			//   At this time, I don't know why.

			//			Application designerApp = ((DesignerApplicationEx) app).getDesignerApplication();
			//			XotsDaemon.queue(new XotsRefresher(designerApp.getAppName()));
		}
	}

	@Override
	public void applicationDestroyed(final ApplicationEx app) {
	}

	@Override
	public void applicationRefreshed(final ApplicationEx app) {
		if (app instanceof DesignerApplicationEx) {
			//NTF The following code block destroys all XPages execution on the server
			//   At this time, I don't know why.

			//			Application designerApp = ((DesignerApplicationEx) app).getDesignerApplication();
			//			XotsDaemon.queue(new XotsRefresher(designerApp.getAppName()));
		}
	}

	// TODO de-duplicate this code copied from XotsNsfScanner
	public static class XotsRefresher extends XotsBaseTasklet {
		private static final long serialVersionUID = 1L;

		private final boolean TRACE = true;

		private final String appName_;

		public XotsRefresher(final String appName) {
			appName_ = appName;
		}

		//		@Override
		//		public DominoSessionType getSessionType() {
		//			return DominoSessionType.NATIVE;
		//		}

		@Override
		public void run() {
			Session session = Factory.getSession();
			Database db = session.getDatabase(appName_);
			DatabaseDesign design = db.getDesign();
			IconNote icon = design.getIconNote();
			if (icon != null) {
				String[] xotsClassNames = icon.getXotsClassNames();
				if (xotsClassNames != null && xotsClassNames.length > 0) {
					if (TRACE) {
						System.out.println("TRACE: Adding Xots Tasklets for database " + db.getApiPath());
					}
					//					try {
					//						//	XotsService xs = XotsService.getInstance();
					//						//	xs.getComponentModule("/" + db.getFilePath());
					//						//	xs.loadXotsTasklets("/" + db.getFilePath(), xotsClassNames);
					//					} catch (ServletException se) {
					//						DominoUtils.handleException(se);
					//					}
				}
			}
		}

	}
}
