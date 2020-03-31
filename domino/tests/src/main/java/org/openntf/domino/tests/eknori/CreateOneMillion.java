/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.tests.eknori;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.tests._Suite;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@SuppressWarnings("unused")
@RunWith(DominoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateOneMillion {
	private static final Logger log_ = Logger.getLogger(CreateOneMillion.class.getName());
	static long odaTime = 0;
	static long odaRecycleTime = 0;
	static long notesTime = 0;

	@Test
	public void runODA() {

		Document doc = null;
		System.out.println("ODA normal\tSetup...");
		Session s = Factory.getSession(SessionType.CURRENT);
		Set<Document> docset = new HashSet<Document>();
		Database db = s.getDatabase("", "OneMillion.nsf", true);
		if (db != null)
			db.remove();

		Database template = s.getDatabase("", "billing.ntf", true);
		db = template.createCopy("", "OneMillion.nsf");
		assertTrue(db.isOpen());
		assertEquals(0, db.getAllDocuments().size());
		long startTime = System.currentTimeMillis();
		System.out.println("ODA normal\tSTART Test");
		for (int i = 1; i <= _Suite.MASS_DOC_COUNT; i++) {

			doc = db.createDocument();
			doc.replaceItemValue("form", "Agent");
			doc.replaceItemValue("UserName", s.createName("TestAgent/MyCompany"));
			doc.replaceItemValue("Subject", String.valueOf(System.nanoTime()));
			assertTrue(doc.save());
			if (i % 5000 == 0) {
				// System.gc();
				docset.add(doc);
				System.out.println("ODA normal\tCreated " + i + " documents so far. Still going... (" + i * 1000
						/ (System.currentTimeMillis() - startTime) + " docs/sec)");
			}
		}
		assertEquals(_Suite.MASS_DOC_COUNT, db.getAllDocuments().size());

		for (Document d : docset) {
			DateTime dt = d.getCreated();
			d.replaceItemValue("$Created", dt);
			assertTrue(d.save());
		}
		odaTime = (System.currentTimeMillis() - startTime);
		System.out.println("ODA normal\tTest time: " + odaTime + " ms");
	}

	@Test
	public void runODARecycle() throws NotesException {
		odaRecycleTime = run("ODA recycle", Factory.getSession(SessionType.CURRENT));
	}

	@Test
	public void runNotes() throws NotesException {
		Session s = Factory.getSession(SessionType.CURRENT);
		notesTime = run("Notes recycle", s.getFactory().toLotus(s));
	}

	protected long run(final String prefix, final lotus.domino.Session s) throws NotesException {
		lotus.domino.Document doc = null;

		System.out.println(prefix + "\tSetup...");
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
		lotus.domino.Item itm, itm2, itm3;

		long startTime = System.currentTimeMillis();
		System.out.println(prefix + "\tSTART Test");
		for (int i = 1; i <= _Suite.MASS_DOC_COUNT; i++) {

			doc = db.createDocument();
			itm = doc.replaceItemValue("form", "Agent");
			if (itm != null)
				itm.recycle();
			lotus.domino.Name name = s.createName("TestAgent/MyCompany");
			doc.replaceItemValue("UserName", name.getCanonical()).recycle();
			name.recycle();
			itm = doc.replaceItemValue("Subject", String.valueOf(System.nanoTime()));
			if (itm != null)
				itm.recycle();
			itm2 = doc.getFirstItem("Subject");
			assertTrue(itm != itm2);
			//assertTrue(!itm.equals(itm2));
			itm = doc.getFirstItem("Subject");
			assertTrue(itm == itm2);
			//assertTrue(itm.equals(itm2));
			if (itm != null)
				itm.recycle();
			if (itm2 != null)
				itm2.recycle();

			assertTrue(doc.save());
			if (i % 5000 == 0) {
				// System.gc();
				docset.add(doc);
				System.out.println(prefix + "\tCreated " + i + " documents so far. Still going... (" + i * 1000
						/ (System.currentTimeMillis() - startTime) + " docs/sec)");
			}
		}

		assertEquals(_Suite.MASS_DOC_COUNT, db.getAllDocuments().getCount());

		for (lotus.domino.Document d : docset) {
			lotus.domino.DateTime dt = d.getCreated();
			d.replaceItemValue("$Created", dt).recycle();
			dt.recycle();
			assertTrue(d.save());
			d.recycle();
		}
		long time = (System.currentTimeMillis() - startTime);
		System.out.println(prefix + "\tTest time: " + time + " ms");
		return time;

	}

	@Test
	public void z_summarize() {
		System.out.println("--------------------------- Summary ---------------------------------");
		System.out.println("Notes reference time:\t" + notesTime + " ms");
		System.out.println("OpenNTF API time:\t" + odaTime + " ms. This is " + ((double) odaTime / (double) notesTime)
				+ " x slower than notes");
		System.out.println("ODA with recycle time:\t" + odaRecycleTime + " ms. This is " + ((double) odaRecycleTime / (double) notesTime)
				+ " x slower than notes (but " + ((double) odaTime / (double) odaRecycleTime) + " faster as without recycling.)");
		assertTrue("The API is more than 1.5 times slower. This is not acceptable", ((double) odaTime / (double) notesTime) < 1.5);
	}
}
