/**
 * 
 */
package org.openntf.domino.tests.paul;

import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.email.DominoEmail;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

/**
 * @author withersp
 * 
 */
public class SimpleEmailTest extends org.openntf.domino.thread.AbstractDominoRunnable {
	private long marktime;

	/**
	 * 
	 */
	public SimpleEmailTest() {
	}

	public static void main(final String[] args) {
		try {
			System.out.println("Loading TestRunner...");
			TestRunnerUtil.runAsDominoThread(new SimpleEmailTest(), TestRunnerUtil.NATIVE_SESSION);
			System.out.println("Finished");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			System.out.println("Starting...");
			Session session = Factory.getSession(SessionType.NATIVE);
			DominoEmail myEmail = new DominoEmail(session);
			myEmail.addToAddress("IntecTestUser1@intec.co.uk");
			myEmail.addToAddress("IntecTestUser2@intec.co.uk");
			myEmail.setSubject("Multi email test");
			myEmail.addText("This is a test email");
			Document email = myEmail.send();
			System.out.println(email.getUniversalID());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void timelog(final String message) {
		long curtime = System.nanoTime();
		long elapsed = curtime - marktime;
		marktime = curtime;
		System.out.println(elapsed / 1000000 + " ms: " + message);
	}
}
