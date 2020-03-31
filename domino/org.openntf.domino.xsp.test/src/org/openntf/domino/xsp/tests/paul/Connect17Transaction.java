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
package org.openntf.domino.xsp.tests.paul;

import java.util.Date;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.transactions.DatabaseTransaction;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class Connect17Transaction implements Runnable {

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new Connect17Transaction(), TestRunnerUtil.NATIVE_SESSION);
	}

	@Override
	public void run() {
		Session sess = Factory.getSession(SessionType.NATIVE);
		Database extLib = sess.getDatabase("odademo/oda_1.nsf");
		Database extLib2 = sess.getDatabase("odademo/oda_2.nsf");
		View states = extLib.getView("AllStates");
		Document stateDoc = states.getFirstDocument();
		DatabaseTransaction txn = extLib.startTransaction();
		extLib2.setTransaction(txn);
		boolean successOrFail = false;
		String state = stateDoc.getItemValueString("Key");
		System.out.println("Processing state " + state);
		queueTransaction(extLib, state);	// Update some documents
		queueTransaction(extLib2, state);	// Update some documents
		if (successOrFail) {
			txn.commit();
			System.out.println("...Committed");
		} else {
			txn.rollback();
			System.out.println("rolled back");
		}
	}

	private void queueTransaction(final Database db, final String state) {
		boolean toggle = true;
		int count = 0;
		View contacts = db.getView("AllContactsByState");
		DocumentCollection dc = contacts.getAllDocumentsByKey(state, true);
		for (Document doc : dc) {
			if (toggle) {
				doc.replaceItemValue("txnTest", new Date());
				count += 1;
			}
			toggle = !toggle;
		}
		System.out.println("...Updated " + Integer.toString(count) + " Contacts pending committal.");
	}

}
