package org.openntf.domino.xsp.tests.paul;

import org.openntf.domino.Database;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.helpers.DocumentSyncHelper;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class Connect17SyncHelper implements Runnable {

	public Connect17SyncHelper() {

	}

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new Connect17SyncHelper(), TestRunnerUtil.NATIVE_SESSION);
	}

	@Override
	public void run() {
		Session sess = Factory.getSession(SessionType.NATIVE);
		Database extLib = sess.getDatabase("odademo/oda_1.nsf");
		java.util.Map<Object, String> syncMap = new java.util.HashMap<Object, String>();
		syncMap.put("Key", "State");
		syncMap.put("Name", "StateName");
		syncMap.put("@Now", "LastSync");
		DocumentSyncHelper helper = new DocumentSyncHelper(DocumentSyncHelper.Strategy.CREATE_AND_REPLACE, syncMap, extLib.getServer(),
				extLib.getFilePath(), "AllContactsByState", "Key");
		View states = extLib.getView("AllStates");
		DocumentCollection sourceCollection = states.getAllDocuments();
		helper.process(sourceCollection);
		System.out.println("Updated " + sourceCollection.getCount() + " documents");
	}

}
