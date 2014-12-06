/**
 * 
 */
package org.openntf.domino.tests.paul;

import lotus.domino.NotesThread;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.email.DominoEmail;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

/**
 * @author withersp
 * 
 */
public class SimpleEmailTest {
	// member vars for session and database
	private Session session = null;
	private Database database = null;

	/**
	 * 
	 */
	public SimpleEmailTest() {
		// TODO Auto-generated constructor stub
	}

	public SimpleEmailTest(final Session s, final Database d) {
		this.session = s;
		this.database = d;
	}

	public static void main(final String[] args) {
		Session s = null;
		Database d = null;
		NotesThread.sinitThread();
		try {
			s = Factory.getSession(SessionType.CURRENT);
			d = s.getDatabase("heracles/intec-pw", "OpenNTF\\OpenNTFDomino.nsf");
			SimpleEmailTest a = new SimpleEmailTest(s, d);
			a.NotesMain();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			NotesThread.stermThread();
		}
	}

	@SuppressWarnings("unused")
	public void NotesMain() {
		Session session;
		Database database;

		// Fails on checking current database!!

		try {
			Session s;
			Database db;
			if (this.session != null) {
				s = this.session;
				db = this.database;
				DominoEmail myEmail = new DominoEmail(s);
				myEmail.createSimpleEmail("pwithers@intec.co.uk", "", "", "OpenNTF Domino Email",
						"Please find attached an email from OpenNTF Domino API", "");
			}
		} catch (Throwable t) {

		}
	}
}
