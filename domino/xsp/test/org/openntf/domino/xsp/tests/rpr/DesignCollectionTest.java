package org.openntf.domino.xsp.tests.rpr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.Database;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.junit4xpages.OsgiTest;

import com.ibm.designer.domino.napi.NotesAPIException;
import com.ibm.designer.domino.napi.NotesCollection;
import com.ibm.designer.domino.napi.NotesCollectionEntry;
import com.ibm.designer.domino.napi.NotesDatabase;
import com.ibm.designer.domino.napi.NotesSession;
import com.ibm.designer.domino.napi.util.NotesIterator;

@RunWith(DominoJUnitRunner.class)
@OsgiTest
public class DesignCollectionTest {

	@Test
	public void testDesignCollection() throws NotesAPIException {
		Database db = Factory.getSession(SessionType.TRUSTED).getDatabase("Entwicklung\\jfof4\\proglibjfof.nsf");
		long start = System.currentTimeMillis();
		db.getDesign().isAPIEnabled();
		start = System.currentTimeMillis() - start;
		System.out.println(start);
	}

	//@Test
	public void testDesignCollection2() throws NotesAPIException {
		Database db = Factory.getSession(SessionType.TRUSTED).getDatabase("Entwicklung\\jfof4\\proglibjfof.nsf");
		//		long start = System.currentTimeMillis();
		//		db.getDesign().isAPIEnabled();
		//		start = System.currentTimeMillis() - start;
		//		System.out.println(start);

		NoteCollection nnc = db.createNoteCollection(false);

		System.out.println("1..");
		NotesSession nsess = new NotesSession();
		System.out.println("2..");
		NotesDatabase ndb = null;
		if (db.getServer().equals(Factory.getLocalServerName())) {
			ndb = nsess.getDatabase("", db.getFilePath());
		} else {
			ndb = nsess.getDatabase(db.getServer(), db.getFilePath());
		}
		//NotesDatabase ndb = nsess.getDatabase("srv-01-ndev/FOCONIS", "entwicklung\\intern\\proglib4work.nsf");
		ndb.open();
		System.out.println("3..");

		// $FormulaClass of designCollection = 3724 of 0xFFFF0020
		// 2048 = 0x0800 = NOTE_CLASS_REPLFORMULA
		// 1024 = 0x0400 = NOTE_CLASS_FIELD
		// 512  = 0x0200 = NOTE_CLASS_FILTER
		// 128  = 0x0080 = NOTE_CLASS_HELP_INDEX
		// 8    = 0x0008 = NOTE_CLASS_VIEW
		// 4    = 0x0004 = NOTE_CLASS_FORM

		// --- Summary ---
		// NOTE_CLASS_DOCUMENT 		0x0001	not required	/* document note */
		// NOTE_CLASS_INFO 			0x0002	= FFFF0002		/* notefile info (help-about) note */
		// NOTE_CLASS_FORM 			0x0004	available		/* form note */
		// NOTE_CLASS_VIEW 			0x0008 	available		/* view note */
		// NOTE_CLASS_ICON 			0x0010 	= FFFF0010		/* icon note */
		// NOTE_CLASS_DESIGN 		0x0020	= FFFF0020		/* design note collection */
		// NOTE_CLASS_ACL 			0x0040 	= FFFF0040		/* acl note */
		// NOTE_CLASS_HELP_INDEX 	0x0080 	available		/* Notes product help index note */
		// NOTE_CLASS_HELP 			0x0100 	= FFFF0100		/* designer's help note */
		// NOTE_CLASS_FILTER 		0x0200 	available		/* filter note */
		// NOTE_CLASS_FIELD 		0x0400  available		/* field note */
		// NOTE_CLASS_REPLFORMULA 	0x0800 	available		/* replication formula */
		// NOTE_CLASS_PRIVATE 		0x1000  not required	/* Private design note, use $PrivateDesign view to locate/classify */

		NotesCollection design = ndb.openCollection(0xFFFF0020, 0);
		System.out.println("4..");
		int returnMask = 0x8007; // 0x8000 = read summary table, 4 = read class, 2 = read unid, 1 = read note id 
		System.out.println("5..");
		NotesIterator iterator = design.readEntries(returnMask, 0, 32);
		System.out.println("6..");

		while (iterator.hasNext()) {
			NotesCollectionEntry entry = (NotesCollectionEntry) iterator.next();
			//			NotesSummaryTable stable = entry.getSummaryTable();
			//			for (int i = 1; i < 5; i++) {
			//				NotesSummaryItem item = stable.getItem(i);
			//				System.out.println(item.getName());
			//				item.recycle();
			//			}
			//			stable.recycle();
			//@formatter:off
			System.out.println(Integer.toHexString(entry.getNoteID()) + "\t" + 
					entry.getItemValueAsString("$TITLE") + "\t" +
					entry.getItemValueAsString("$FormPrivs") + "\t" +
					entry.getItemValueAsString("$FormUsers") + "\t" +
					entry.getItemValueAsString("$Body") + "\t" +
					entry.getItemValueAsString("$Flags") + "\t" +
					entry.getItemValueAsString("$Class") + "\t" +
					entry.getItemValueAsString("$Modified") + "\t" +
					entry.getItemValueAsString("$Comment") + "\t" +
					entry.getItemValueAsString("$AssistTrigger") 
					);
			nnc.add(entry.getNoteID());
			//@formatter:on
			entry.recycle();
		}
		iterator.recycle();
		design.recycle();
		ndb.recycle();
		nsess.recycle();

		for (String n : nnc) {
			System.out.println("NNC:" + n);
		}
	}
}
