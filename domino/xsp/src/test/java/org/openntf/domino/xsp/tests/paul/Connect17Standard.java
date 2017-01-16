package org.openntf.domino.xsp.tests.paul;

import java.util.TreeSet;
import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;

import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.utils.TypeUtils;

public class Connect17Standard implements Runnable {

	public Connect17Standard() {

	}

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new Connect17Standard(), TestRunnerUtil.NATIVE_SESSION);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		org.openntf.domino.Session sess = Factory.getSession(SessionType.NATIVE);
		try {
			TreeSet<String> names = new TreeSet<String>();
			Session session = TypeUtils.toLotus(sess);
			// Point to ExtLib demo, create a view called AllContactsByState
			// Copy AllContacts but adding a categorised column for State
			Database extLib = session.getDatabase(session.getServerName(), "odademo/oda_1.nsf");
			View states = extLib.getView("AllStates");
			states.setAutoUpdate(false);
			ViewEntry entState = states.getAllEntries().getFirstEntry();
			View byState = extLib.getView("AllContactsByState");
			byState.setAutoUpdate(false);
			Vector<String> key = new Vector<String>();
			Vector<String> stateVals = entState.getColumnValues();
			key.add(stateVals.get(0));
			ViewEntryCollection ec = byState.getAllEntriesByKey(key, true);
			ViewEntry ent = ec.getFirstEntry();
			while (null != ent) {
				Vector<Object> vals = ent.getColumnValues();
				names.add((String) vals.get(7));

				ViewEntry tmpEnt = ec.getNextEntry();
				ent.recycle(vals);
				ent.recycle();
				ent = tmpEnt;
			}
			System.out.println(names.toString());
		} catch (NotesException e) {
			e.printStackTrace();
		}
	}

}
