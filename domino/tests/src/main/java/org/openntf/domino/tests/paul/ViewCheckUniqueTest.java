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
/**
 * 
 */
package org.openntf.domino.tests.paul;

import java.util.ArrayList;

import lotus.domino.NotesThread;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

/**
 * @author withersp
 * 
 */
public class ViewCheckUniqueTest {
	// member vars for session and database
	private Session session = null;
	private Database database = null;

	/**
	 * 
	 */
	public ViewCheckUniqueTest() {
	}

	public ViewCheckUniqueTest(final Session s, final Database d) {
		this.session = s;
		this.database = d;
	}

	public static void main(final String[] args) {
		Session s = null;
		Database d = null;
		NotesThread.sinitThread();
		try {
			s = Factory.getSession(SessionType.CURRENT);
			d = s.getDatabase("heracles/intec-pw", "OpenNTF\\OpenNTFDomino.nsf");
			ViewCheckUniqueTest a = new ViewCheckUniqueTest(s, d);
			a.NotesMain();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			NotesThread.stermThread();
		}
	}

	@SuppressWarnings("unused")
	public void NotesMain() {
		Session session;
		Database database;

		// Fails on checking current database!!

		try {
			Session s;
			Database db;
			if (this.session != null) {
				s = this.session;
				db = this.database;
				try {
					StringBuilder sb = new StringBuilder();
					Document doc = db.createDocument();
					doc.put("FirstName", "Aaron");
					doc.put("LastName", "Monroe");
					View view = db.getView("AllContacts");
					ArrayList<String> key = new ArrayList<String>();
					key.add(doc.getItemValueString("FirstName"));
					key.add(doc.getItemValueString("LastName"));
					if (view.checkUnique(key, doc)) {
						sb.append("No document yet exists with name Aaron Monroe");
					} else {
						sb.append("Document already exists with name Aaron Monroe");
					}
					System.out.println(sb.toString());
				} catch (Throwable t) {
					System.err.println(t);
				}
			}
		} catch (Throwable t) {

		}
	}
}
