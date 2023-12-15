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
package org.openntf.domino.xsp.tests.paul;

import java.util.ArrayList;
import java.util.TreeSet;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.XspOpenLogUtil;

public class Connect17ODA implements Runnable {

	public Connect17ODA() {

	}

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new Connect17ODA(), TestRunnerUtil.NATIVE_SESSION);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			Session sess = Factory.getSession(SessionType.NATIVE);
			TreeSet<String> names = new TreeSet<String>();
			Database extLib = sess.getDatabase("odademo/oda_1.nsf");
			View states = extLib.getView("AllStates");
			ViewEntry entState = states.getAllEntries().getFirstEntry();
			View byState = extLib.getView("AllContactsByState");
			ArrayList<Object> stateVals = new ArrayList(entState.getColumnValuesEx());
			ViewEntryCollection ec = byState.getAllEntriesByKey(stateVals.get(0));
			for (ViewEntry ent : ec) {
				names.add((String) ent.getColumnValues().get(8));
			}
			System.out.println(names.toString());
		} catch (Exception e) {
			XspOpenLogUtil.logError(e);
		}
	}

}
