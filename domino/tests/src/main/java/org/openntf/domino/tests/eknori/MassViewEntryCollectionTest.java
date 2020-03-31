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

/*
 * -- START --
 1000000
 -- STOP --
 Thread MassViewEntryCollectionTest elapsed time: 293767ms
 Thread MassViewEntryCollectionTest auto-recycled 994806 lotus references during run. Then recycled 5197 lotus references on completion and had 0 recycle errors

 */
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@SuppressWarnings("unused")
@RunWith(DominoJUnitRunner.class)
public class MassViewEntryCollectionTest {

	private static final String TARGET = "OneMillion.nsf";
	private static final String VIEW = "Persons";

	@Test
	public void run() {

		Session s = Factory.getSession(SessionType.CURRENT);
		Database source = s.getDatabase("", TARGET, true);
		View view = source.getView(VIEW);
		System.out.println("-- START --");
		long start = System.nanoTime();
		int i = 0;
		if (null != view) {
			view.setAutoUpdate(false);

			System.out.println(view.getEntryCount());

			for (ViewEntry entry : view.getAllEntries()) {
				i++;
			}
			/* */
			view.setAutoUpdate(true);
		}
		System.out.println(i);
		long elapsed = System.nanoTime() - start;
		System.out.println("-- STOP --");
		System.out.println("Thread " + Thread.currentThread().getName() + " elapsed time: " + elapsed / 1000000 + "ms");

	}

}
