package org.openntf.domino.xsp.tests.paul;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class DbCreate implements Runnable {

	public DbCreate() {

	}

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new DbCreate(), TestRunnerUtil.NATIVE_SESSION);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			Session sess = Factory.getSession(SessionType.NATIVE);
			Database db3 = sess.createBlankDatabase("temp\\demo2.nsf");
			Database db = sess.createBlankDatabase("temp", "demo1.nsf");
			System.out.println(db.getApiPath());
			Database db2 = sess.createBlankDatabaseAbsolutePath("C:/PaulTemp/ODATest", "demo1.nsf");
			System.out.println(db2.getApiPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
