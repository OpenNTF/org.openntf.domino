package org.openntf.domino.xsp.tests.paul;

import java.util.ArrayList;
import java.util.TreeSet;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.XspOpenLogUtil;

public class Connect17ODA implements Runnable {

	public Connect17ODA() {

	}

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new Connect17ODA(), TestRunnerUtil.NATIVE_SESSION);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			Session sess = Factory.getSession(SessionType.NATIVE);
			TreeSet<String> names = new TreeSet<String>();
			Database extLib = sess.getDatabase("odademo/oda_1.nsf");
			View states = extLib.getView("AllStates");
			ViewEntry entState = states.getAllEntries().getFirstEntry();
			View byState = extLib.getView("AllContactsByState");
			ArrayList<Object> stateVals = new ArrayList(entState.getColumnValuesEx());
			ViewEntryCollection ec = byState.getAllEntriesByKey(stateVals.get(0));
			for (ViewEntry ent : ec) {
				names.add((String) ent.getColumnValues().get(8));
			}
			System.out.println(names.toString());
		} catch (Exception e) {
			XspOpenLogUtil.logError(e);
		}
	}

}
