package org.openntf.domino.tests.paul;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.openntf.domino.*;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@SuppressWarnings("unused")
public class TestAttachments {

	public TestAttachments() {

	}

	static class DocJson implements Runnable {

		@Override
		public void run() {
				Session s = Factory.getSession(SessionType.CURRENT);
				Database db = s.getDatabase("OneMillionLotus.nsf");
				Document doc = db.getAllDocuments().getFirstDocument();

				System.out.println(doc.toJson(true));
				
				Database names = s.getDatabase("names.nsf");
				doc = names.getDocumentByUNID("85255E01001356A8852554C200753106/89413B807885E4F2802584990050144E");

				System.out.println(doc.toJson(true));
		}
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new DocJson(), TestRunnerUtil.NATIVE_SESSION);
	}
}
