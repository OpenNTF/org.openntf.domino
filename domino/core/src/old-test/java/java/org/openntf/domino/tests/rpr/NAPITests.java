package org.openntf.domino.tests.rpr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.design.DesignBase;
import org.openntf.domino.design.DesignCollection;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@RunWith(DominoJUnitRunner.class)
public class NAPITests {

	//@Test
	public void testNAPI() throws Exception {
		Session sess = Factory.getSession(SessionType.CURRENT);
		//Database db = sess.getDatabase("D:/Daten/notesdaten_9/foconis/misviewer.nsf");
		Database db = sess.getDatabase("names.nsf");
		System.out.println(Factory.isNapiPresent());
		for (Document doc : db.getAllDocuments()) {
			System.out.println(doc.getNoteClass() + ", " + doc.isDefault() + ", " + doc.isPrivate());
		}

		Document doc = db.getDocumentByID("FFFF0010");
		System.out.println(doc.getNoteClass() + ", " + doc.isDefault() + ", " + doc.isPrivate());

		//		NotesDatabase napiDb = Factory.getNapiSession().getDatabase(db);
		//		System.out.println(napiDb.findDBScriptNote());
		//
		//		for (Document doc : db.getAllDocuments()) {
		//			NotesNote napiDoc = Factory.getNapiSession().getNotesNote(doc);
		//			//NotesNote napiDoc = napiDb.openNoteByUnid(doc.getUniversalID(), 1);
		//			String oapiID = doc.getNoteID();
		//			//			doc.recycle();
		//			String napiID = Integer.toHexString(napiDoc.getNoteId());
		//			//			napiDoc.recycle();
		//
		//			System.out.println(oapiID + " - " + napiID);
		//		}

	}

	//@Test
	public void testKeySet() throws Exception {
		Session sess = Factory.getSession(SessionType.CURRENT);
		//Database db = sess.getDatabase("D:/Daten/notesdaten_9/foconis/misviewer.nsf");
		Database db = sess.getDatabase("names.nsf");
		System.out.println(Factory.isNapiPresent());
		int i = 0;
		for (Document doc : db.getAllDocuments()) {
			i += doc.keySet().size();
			//System.out.println(doc.keySet().size());
		}

		System.out.println("Total: " + i);
		Document doc = db.getDocumentByID("FFFF0010");
		System.out.println(doc.getNoteClass() + ", " + doc.isDefault() + ", " + doc.isPrivate());

		//		NotesDatabase napiDb = Factory.getNapiSession().getDatabase(db);
		//		System.out.println(napiDb.findDBScriptNote());
		//
		//		for (Document doc : db.getAllDocuments()) {
		//			NotesNote napiDoc = Factory.getNapiSession().getNotesNote(doc);
		//			//NotesNote napiDoc = napiDb.openNoteByUnid(doc.getUniversalID(), 1);
		//			String oapiID = doc.getNoteID();
		//			//			doc.recycle();
		//			String napiID = Integer.toHexString(napiDoc.getNoteId());
		//			//			napiDoc.recycle();
		//
		//			System.out.println(oapiID + " - " + napiID);
		//		}

	}

	@Test
	public void testDesign() throws Exception {
		Session sess = Factory.getSession(SessionType.CURRENT);
		//Database db = sess.getDatabase("D:/Daten/notesdaten_9/foconis/misviewer.nsf");
		//Database db = sess.getDatabase("D:/Daten/notesdaten_9/bookmark.nsf");
		//Database db = sess.getDatabase("fakenames2.nsf");
		Database db = sess.getDatabase("D:/Daten/notesdaten_9/localdb/proglib4work2.nsf");
		DatabaseDesign design = db.getDesign();
		DesignCollection<DesignBase> els = design.getDesignElements();

		for (DesignBase el : els) {
			Document doc = el.getDocument();
			//System.out.println(doc.getUniversalID() + "\t" + doc.getNoteClass() + ", " + doc.isDefault() + ", " + doc.isPrivate() + "\t"
			//		+ el);
		}

	}

}
