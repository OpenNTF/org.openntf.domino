package org.openntf.domino.tests;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class SessionTests implements Runnable {

	@Override
	public void run() {
		Session sess = Factory.getSession(SessionType.NATIVE);
		Database db = sess.getDatabase("names.nsf");
		View vw = db.getView("$Users");
		Document doc = vw.getFirstDocument();
		String metaversalId = doc.getMetaversalID();
		System.out.println(metaversalId);
		Document doc2 = sess.getDocumentByMetaversalID(db.getServer(), metaversalId);
		if (null == doc2) {
			System.out.println("Failed");
		} else {
			System.out.println("Worked");
		}
		Document doc3 = sess.getDocumentByMetaversalID(metaversalId);
		if (null == doc2) {
			System.out.println("Failed");
		} else {
			System.out.println("Worked");
		}
	}

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new SessionTests(), TestRunnerUtil.NATIVE_SESSION);
	}

}
