/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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
 * 2.780.192 documents in source application
 Thread MassDeleteDocumentsScratchTest elapsed time: 528132ms
 Thread MassDeleteDocumentsScratchTest auto-recycled 2774889 lotus references during run. Then recycled 5306 lotus references on completion and had 0 recycle errors
 with Base line 165: There's already a reference to Document (...). The current call stack is ...

 after commenting out the print statement

 -- START --
 DocumentIterator set up idArray of 2780192
 -- STOP --
 Thread MassDeleteDocumentsScratchTest elapsed time: 451921ms
 Thread MassDeleteDocumentsScratchTest auto-recycled 2779616 lotus references during run. Then recycled 579 lotus references on completion and had 0 recycle errors

 */
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@RunWith(DominoJUnitRunner.class)
public class MassDeleteDocumentsScratchTest {

	private static final String TARGET = "target.nsf";

	@Test
	public void run() {

		Session s = Factory.getSession(SessionType.CURRENT);
		Database source = s.getDatabase("", TARGET, true);

		System.out.println("-- START --");
		long start = System.nanoTime();
		for (Document doc : source.getAllDocuments()) {
			doc.removePermanently(true);
		}
		long elapsed = System.nanoTime() - start;
		System.out.println("-- STOP --");

		System.out.println("Thread " + Thread.currentThread().getName() + " elapsed time: " + elapsed / 1000000 + "ms");

	}

}
