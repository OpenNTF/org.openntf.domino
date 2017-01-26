package org.openntf.domino.xsp.tests.paul;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class Connect17Documents implements Runnable {

	public Connect17Documents() {

	}

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new Connect17Documents(), TestRunnerUtil.NATIVE_SESSION);
	}

	@Override
	public void run() {
		Session sess = Factory.getSession(SessionType.NATIVE);
		Database extLib = sess.getDatabase("odademo/oda_1.nsf");
		View contacts = extLib.getView("AllContacts");
		View threads = extLib.getView("AllThreadsByAuthor");
		Document doc = contacts.getFirstDocument();
		resetDoc(doc);
		String prevDocAsJson = doc.toJson(true);
		doc.appendItemValue("State", "AZ");
		doc.replaceItemValue("DateTimeField", new Date());
		doc.replaceItemValue("DateOnlyField", new java.sql.Date(System.currentTimeMillis()));
		doc.replaceItemValue("EmptyDate", "");
		Date blankDate = doc.getItemValue("EmptyDate", Date.class);
		System.out.println(blankDate);
		ArrayList<String> list = new ArrayList<String>();
		list.add("Value 1");
		list.add("Value 2");
		doc.replaceItemValue("MVField", list);
		doc.replaceItemValue("DocAsJson", prevDocAsJson);

		HashMap<String, String> mapField = new HashMap<String, String>();
		DocumentCollection dc = threads.getAllDocumentsByKey(doc.getItemValueString("FullName"));
		for (Document tmp : dc) {
			mapField.put(tmp.getUniversalID(), tmp.getItemValueString("Title"));
		}
		doc.put("MapField", mapField);
		doc.save();
		HashMap tmp = doc.getItemValue("MapField", HashMap.class);
		System.out.println(tmp.size());
		System.out.println(doc.getMetaversalID());
	}

	public void resetDoc(final Document doc) {
		doc.replaceItemValue("State", doc.getItemValue("State").get(0));
		doc.replaceItemValue("DateTimeField", null);
		doc.removeItem("MVField");
		doc.removeItem("DocAsJson");
		doc.removeItem("MapField");
		doc.save();
	}

}
