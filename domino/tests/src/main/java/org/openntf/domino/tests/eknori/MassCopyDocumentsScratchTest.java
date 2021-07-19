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
		int i = 0;
		for (Document doc : source.getAllDocuments()) {
			assertNotNull(doc.copyToDatabase(target));
			i++;
			if (i % 5000 == 0)
				System.out.println("Copied " + i + " documents of " + source.getAllDocuments().size());
		}

		System.out.println("-- STOP --");

		assertEquals(source.getAllDocuments().size(), target.getAllDocuments().size());
		long elapsed = System.nanoTime() - start;
		System.out.println("Thread " + Thread.currentThread().getName() + " elapsed time: " + elapsed / 1000000 + "ms");

	}

}
