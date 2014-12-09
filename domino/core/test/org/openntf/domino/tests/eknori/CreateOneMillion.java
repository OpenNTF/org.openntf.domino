package org.openntf.domino.tests.eknori;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@RunWith(DominoJUnitRunner.class)
public class CreateOneMillion {
	private static final Logger log_ = Logger.getLogger(CreateOneMillion.class.getName());

	//@Test
	public void runODA() {
		long startTime = System.currentTimeMillis();
		Document doc = null;
		System.out.println("START Creation of Documents");
		Session s = Factory.getSession(SessionType.CURRENT);
		Set<Document> docset = new HashSet<Document>();
		Database db = s.getDatabase("", "OneMillion.nsf", true);
		if (db != null)
			db.remove();

		Database template = s.getDatabase("", "billing.ntf", true);
		db = template.createCopy("", "OneMillion.nsf");
		assertTrue(db.isOpen());
		assertEquals(0, db.getAllDocuments().size());
		for (int i = 1; i <= _Suite.DOC_COUNT; i++) {

			doc = db.createDocument();
			doc.replaceItemValue("form", "Agent");
			doc.replaceItemValue("UserName", s.createName("TestAgent/MyCompany"));
			doc.replaceItemValue("Subject", String.valueOf(System.nanoTime()));
			assertTrue(doc.save());
			if (i % 5000 == 0) {
				// System.gc();
				docset.add(doc);
				System.out.println("ODA normal. Created " + i + " documents so far. Still going... (" + i * 1000
						/ (System.currentTimeMillis() - startTime) + " docs/sec)");
			}
		}
		assertEquals(_Suite.DOC_COUNT, db.getAllDocuments().size());
		System.out.println("ENDING Creation of Documents");
		System.out.println("START Extra-processing of retained docs");
		for (Document d : docset) {
			DateTime dt = d.getCreated();
			d.replaceItemValue("$Created", dt);
			assertTrue(d.save());
		}
		System.out.println("ENDED Extra-processing of retained docs");
	}

	@Test
	public void runODARecycle() throws NotesException {
		run("ODARecycle", Factory.getSession(SessionType.CURRENT));
	}

	@Test
	public void runNotes() throws NotesException {
		Session s = Factory.getSession(SessionType.CURRENT);
		run("ODARecycle", s.getFactory().toLotus(s));
	}

	/*
	 * Performance comparison:
	 * 
	 * 								Lotus			ODA			Factor slower
	 * getItemValue("null") 		182				71			2,56
	 * getItemValue("form")			82				49			1,67
	 * getItemValueString("null")	219				84			2,60
	 * getItemValueString("form")	100				96			1,04 
	 * sess.isConvertMime()			424				390
	 */
	protected long run(final String prefix, final lotus.domino.Session s) throws NotesException {
		long startTime = System.currentTimeMillis();
		lotus.domino.Document doc = null;
		System.out.println("START Creation of Documents");
		Set<lotus.domino.Document> docset = new HashSet<lotus.domino.Document>();
		lotus.domino.Database db = s.getDatabase("", "OneMillion.nsf", true);
		if (db != null) {
			db.remove();
			db.recycle();
		}
		lotus.domino.Database template = s.getDatabase("", "billing.ntf", true);
		db = template.createCopy("", "OneMillion.nsf");
		assertTrue(db.isOpen());
		assertEquals(0, db.getAllDocuments().getCount());
		for (int i = 1; i <= _Suite.DOC_COUNT; i++) {

			doc = db.createDocument();
			//doc.replaceItemValue("form", "Agent").recycle();
			for (int j = 1; j < 1000; j++)
				//org.openntf.domino.impl.Base.isDead(s);
				//doc.getItemValueString("null");
				//			Name name = s.createName("TestAgent/MyCompany");
				//			doc.replaceItemValue("UserName", name.getCanonical()).recycle();
				//			name.recycle();
				//			doc.replaceItemValue("Subject", String.valueOf(System.nanoTime())).recycle();
				//			assertTrue(doc.save());
				if (i % 1000 == 0) {
					// System.gc();
					docset.add(doc);
					System.out.println(prefix + " Created " + i + " documents so far. Still going... (" + i * 1000
							/ (System.currentTimeMillis() - startTime) + " docs/sec)");
				} else {
					doc.recycle();
				}
		}
		assertEquals(_Suite.DOC_COUNT, db.getAllDocuments().getCount());
		System.out.println("ENDING Creation of Documents");
		System.out.println("START Extra-processing of retained docs");
		for (lotus.domino.Document d : docset) {
			lotus.domino.DateTime dt = d.getCreated();
			d.replaceItemValue("$Created", dt).recycle();
			dt.recycle();
			assertTrue(d.save());
			d.recycle();
		}
		System.out.println("ENDED Extra-processing of retained docs");
		return System.currentTimeMillis() - startTime;
	}

}
