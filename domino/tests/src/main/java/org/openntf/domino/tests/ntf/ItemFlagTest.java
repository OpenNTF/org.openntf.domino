/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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
package org.openntf.domino.tests.ntf;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.Session;
import org.openntf.domino.thread.AbstractDominoRunnable;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@SuppressWarnings("unused")
public class ItemFlagTest extends AbstractDominoRunnable {
	private static final long serialVersionUID = 1L;
	private static int THREAD_COUNT = 1;

	public static void main(final String[] args) {
		org.openntf.domino.thread.DominoExecutor de = new org.openntf.domino.thread.DominoExecutor(10);
		for (int i = 0; i < THREAD_COUNT; i++) {
			de.execute(new ItemFlagTest());
		}
		de.shutdown();
	}

	@Override
	public void run() {
		Session session = Factory.getSession(SessionType.CURRENT);
		long collectionBuildNS = 0l;
		long documentBuildNS = 0l;
		long documentPageNS = 0l;
		int collectionSize = 0;
		int dbDocs = 0;
		try {
			Database db = session.getDatabase("", "imdb/movies.bak");
			dbDocs = db.getAllDocuments().getCount();
			long testStartTime = System.nanoTime();
			NoteCollection nc = db.createNoteCollection(false);
			nc.selectAllDataNotes(true);
			nc.setSelectionFormula("@Begins(Title; \"B\")"); //NTF comment or uncomment this to toggle testing selection formula impact
			nc.buildCollection();
			int[] nids = nc.getNoteIDs();
			long collectionTime = System.nanoTime();
			collectionBuildNS = collectionTime - testStartTime;
			collectionSize = nids.length;
			Document[] documents = new Document[collectionSize];
			for (int i = 0; i < collectionSize; i++) {
				documents[i] = db.getDocumentByID(nids[i], true/*false would mean full load of the Document*/);
			}
			long documentTime = System.nanoTime();
			documentBuildNS = documentTime - collectionTime;
			int pageSize = 1000;
			int pageStart = 2014;
			for (int i = pageStart; i < (pageStart + pageSize); i++) {
				Item titleItem = documents[i].getFirstItem("Title");
				titleItem.isAuthors();
				titleItem.isEncrypted();
				titleItem.isNames();
				titleItem.isProtected();
				titleItem.isReaders();
				titleItem.isSigned();
				titleItem.isSummary();
				String report = documents[i].getNoteID() + " is a " + titleItem.getText();
				//				System.out.println(report);
				//				report = report + (" Summary: " + String.valueOf());
			}
			documentPageNS = System.nanoTime() - documentTime;

			System.out.println("Done with document details for " + pageSize + " out of " + collectionSize + " (" + dbDocs
					+ " total in db.) NC build took " + (collectionBuildNS / 1000000) + "ms, Document build took "
					+ (documentBuildNS / 1000000) + "ms, Document page took " + (documentPageNS / 1000000) + "ms");
		} catch (Throwable t) {
			t.printStackTrace();
		}
		long testEndTime = System.nanoTime();
		System.out.println("Completed " + getClass().getSimpleName());
	}

	@Override
	public boolean shouldStop() {
		return true;
	}

}
