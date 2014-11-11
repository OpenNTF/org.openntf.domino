package org.openntf.domino.tests.eknori;

/*
 * 2.780.192 documents in source application
 -- START --
 -- STOP --
 Thread DCRemoveAllScratchTest elapsed time: 316095ms
 Thread DCRemoveAllScratchTest auto-recycled 0 lotus references during run. Then recycled 2 lotus references on completion and had 0 recycle errors

 */
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.Database;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.utils.Factory;

@RunWith(DominoJUnitRunner.class)
public class MassDCRemoveAllScratchTest {

	private static final String SOURCE = "target.nsf";

	@Test
	public void run() {

		Session s = Factory.getSession();
		Database source = s.getDatabase("", SOURCE, true);

		System.out.println("-- START --");
		long start = System.nanoTime();
		DocumentCollection dc = source.getAllDocuments();
		dc.removeAll(true);
		long elapsed = System.nanoTime() - start;
		System.out.println("-- STOP --");

		System.out.println("Thread " + Thread.currentThread().getName() + " elapsed time: " + elapsed / 1000000 + "ms");

	}

}
