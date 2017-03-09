package org.openntf.domino.tests.paul;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class TimeSensitive implements Runnable {

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new TimeSensitive(), TestRunnerUtil.NATIVE_SESSION);
	}

	@Override
	public void run() {
		Session s = Factory.getSession(SessionType.NATIVE);
		DbDirectory dir = s.getDbDirectory(s.getServerName());
		for (Database db : dir) {
			if (null == db) {
				System.out.println("Database " + db.getFilePath() + " is null");
			} else {
				try {
					for (View vw : db.getViews()) {
						if (vw.isTimeSensitive()) {
							System.out.println("TIME SENSITIVE: View " + vw.getName() + " in db " + db.getFilePath());
						}
					}
				} catch (Exception e) {
					System.out.println("Database " + db.getFilePath() + " cannot be opened");
				}
			}
		}
	}

}
