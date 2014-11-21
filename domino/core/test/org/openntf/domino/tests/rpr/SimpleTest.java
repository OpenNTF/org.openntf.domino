package org.openntf.domino.tests.rpr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class SimpleTest implements Runnable {
	public static void main(final String[] args) {
		System.out.println("START");
		TestRunnerUtil.runAsDominoThread(new SimpleTest());
		System.out.println("STOP");
	}

	public SimpleTest() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		Factory.enableCounters(true, false);

		Session s = Factory.getSession(SessionType.CURRENT);
		Database d = s.getDatabase("", "names.nsf");
		List<String> ids = new ArrayList<String>();

		Collection<Document> documents = d.getAllDocuments();

		for (Document doc : documents) {
			ids.add(doc.getNoteID());
		}
		//System.out.println("IDS.size " + ids.size());
		for (int i = 1; i < 10; i++) {
			System.out.println(i + ": " + Factory.dumpCounters(false));
			Document doc1 = null;
			Document doc2 = null;
			Document doc3 = null;
			for (String id : ids) {
				d = s.getDatabase("", "names.nsf");
				doc1 = d.getDocumentByID(id);
				doc1 = null;
				doc2 = d.getDocumentByID(id);
				doc2 = null;
				doc3 = d.getDocumentByID(id);
				doc3 = null;
				int rndIdx = (int) Math.floor(Math.random() * ids.size());

				//				if (i % 20 == 0 && false) {
				//					System.gc();
				//					try {
				//						Thread.sleep(10);
				//					} catch (InterruptedException e) {
				//						// TODO Auto-generated catch block
				//						e.printStackTrace();
				//					}
				//				}

				doc2 = d.getDocumentByID(ids.get(rndIdx));
				doc2 = null;
				d = null;
			}
			if (doc1 == null && doc2 == null && doc3 == null) {
				// NOP - just ensure access to doc1..3 
			}
			//System.out.println(doc1 + "" + doc2 + "" + doc3);
		}

	}

}
