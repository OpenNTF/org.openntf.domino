package org.openntf.domino.tests.eknori;

/*
 -- START --
 DocumentIterator set up idArray of 2780192
 Thread MassCopyDocumentsScratchTest elapsed time: 4104398ms
 -- STOP --
 Thread MassCopyDocumentsScratchTest auto-recycled 5523389 lotus references during run. 
 Then recycled 36999 lotus references on completion and had 0 recycle errors
 */
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@RunWith(DominoJUnitRunner.class)
public class MassCopyDocumentsScratchTest {
	private static final String SOURCE = "OneMillion.nsf";
	private static final String TARGET = "target.nsf";

	@Test
	public void run() {

		long start = System.nanoTime();
		Session s = Factory.getSession(SessionType.CURRENT);
		Database source = s.getDatabase("", SOURCE, true);
		Database target = s.getDatabase("", TARGET, true);
		if (target != null)
			target.remove();

		DbDirectory dir = s.getDbDirectory("");
		target = dir.createDatabase(TARGET, true);

		assertEquals(0, target.getAllDocuments().size());
		System.out.println("-- START --");

		for (Document doc : source.getAllDocuments()) {
			assertNotNull(doc.copyToDatabase(target));
		}

		System.out.println("-- STOP --");

		assertEquals(source.getAllDocuments().size(), target.getAllDocuments().size());
		long elapsed = System.nanoTime() - start;
		System.out.println("Thread " + Thread.currentThread().getName() + " elapsed time: " + elapsed / 1000000 + "ms");

	}

}
