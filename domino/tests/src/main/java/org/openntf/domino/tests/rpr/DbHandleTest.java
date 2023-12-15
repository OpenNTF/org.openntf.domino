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
package org.openntf.domino.tests.rpr;

import lotus.domino.NotesException;

import org.junit.runner.RunWith;
import org.openntf.domino.Session;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@RunWith(DominoJUnitRunner.class)
public class DbHandleTest implements Runnable {
	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new DbHandleTest(), TestRunnerUtil.NATIVE_SESSION);
	}

	public DbHandleTest() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		Factory.enableCounters(true, false);
		try {
			Session sess = Factory.getSession(SessionType.CURRENT);
			lotus.domino.Session s = sess.getFactory().toLotus(sess);
			lotus.domino.Database d1 = s.getDatabase("", "");
			lotus.domino.Database d2 = s.getDatabase("", "");
			//System.out.println("d1.cppId: " + Base.getLotusId(d1));
			//System.out.println("d2.cppId: " + Base.getLotusId(d2));

			d1.openWithFailover("", "names.nsf");
			d2.openWithFailover("", "names.nsf");
			//System.out.println("d1.cppId: " + Base.getLotusId(d1));
			//System.out.println("d2.cppId: " + Base.getLotusId(d2));

			System.out.println("D1:" + d1.getFilePath());
			d1.recycle();
			System.out.println("D2:" + d2.getFilePath());
			d2.recycle();
		} catch (NotesException e) {
			e.printStackTrace();
		}
	}

}
