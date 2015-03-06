package org.openntf.domino.tests.rpr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xots.Xots;

@RunWith(DominoJUnitRunner.class)
public class ViewRecycleTest {

	public static class TestRunner implements Runnable {
		@Override
		public void run() {
			try {
				Session sess = Factory.getSession(SessionType.NATIVE);
				Database db = sess.getDatabase("names.nsf");
				View vw = db.getView("($Servers)"); // take a view that contains DateTimes
				for (int i = 1; i < 100; i++) {
					ViewEntryCollection tmp = vw.getAllEntries();
					for (ViewEntry ve : tmp) {
						System.out.println(db.getView("$Servers").getAliases());
						System.out.println(ve.getColumnValue("ServerTitle") + "\t" + ve.getColumnValue("Schedule"));
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@Test
	public void test() throws Exception {
		Xots.getService().submit(new TestRunner());
		Xots.getService().submit(new TestRunner());
		Xots.getService().submit(new TestRunner());
	}

}
