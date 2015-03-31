package org.openntf.domino.tests.rpr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DxlExporter;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@RunWith(DominoJUnitRunner.class)
public class DesignCollectionTest {

	@Test
	public void testDesignCollection() {
		Database db = Factory.getSession(SessionType.CURRENT).getDatabase("fakenames2.nsf");
		long start = System.currentTimeMillis();

		NoteCollection nnc = db.createNoteCollection(false);

		Document doc = db.getDocumentByID("FFFF0010"); // icon
		if (doc != null)
			nnc.add(doc);

		doc = db.getDocumentByID("FFFF0040"); // ACL
		if (doc != null)
			nnc.add(doc);

		//code above is faster than performing a slow buildCollection()
		//nnc.setSelectAcl(true);
		//nnc.setSelectIcon(true);
		//nnc.buildCollection();

		DxlExporter exp = db.getAncestorSession().createDxlExporter();
		System.out.println(exp.exportDxl(nnc));

		start = System.currentTimeMillis() - start;
		System.out.println(start);
	}
}
