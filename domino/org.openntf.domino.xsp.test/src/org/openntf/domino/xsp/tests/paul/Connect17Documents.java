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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Item;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.types.AuthorsList;
import org.openntf.domino.types.NamesList;
import org.openntf.domino.types.ReadersList;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class Connect17Documents implements Runnable {

	public Connect17Documents() {

	}

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new Connect17Documents(), TestRunnerUtil.NATIVE_SESSION);
	}

	@Override
	public void run() {
		Session sess = Factory.getSession(SessionType.NATIVE);
		Database extLib = sess.getDatabase("odademo/oda_1.nsf");
		View contacts = extLib.getView("AllContacts");
		View threads = extLib.getView("AllThreadsByAuthor");
		Document doc = contacts.getFirstDocument();
		resetDoc(doc);	// Clears changes this function already made

		Document newDoc = extLib.createDocument();
		doc.copyAllItems(newDoc, true);

		String prevDocAsJson = doc.toJson(true);
		doc.appendItemValue("State", "AZ");
		doc.replaceItemValue("DateTimeField", new Date());
		doc.replaceItemValue("DateOnlyField", LocalDate.now());
		System.out.println(doc.getFirstItem("DateOnlyField").getValues());
		doc.replaceItemValue("TimeOnlyField", LocalTime.now());
		System.out.println(doc.getFirstItem("TimeOnlyField").getValues());
		doc.replaceItemValue("EmptyDate", "");
		Date blankDate = doc.getItemValue("EmptyDate", Date.class);
		System.out.println(blankDate);
		ArrayList<String> list = new ArrayList<String>();
		list.add("Value 1");
		list.add("Value 2");
		doc.replaceItemValue("MVField", list);
		doc.replaceItemValue("DocAsJson", prevDocAsJson);

		HashMap<String, String> mapField = new HashMap<String, String>();
		DocumentCollection dc = threads.getAllDocumentsByKey(doc.getItemValueString("FullName"));
		for (Document tmp : dc) {
			mapField.put(tmp.getUniversalID(), tmp.getItemValueString("Title"));
		}
		doc.put("MapField", mapField);
		BigDecimal decimal = new BigDecimal("2.5");
		doc.replaceItemValue("BigDecimalField", decimal);
		doc.replaceItemValue("EnumField", Fixes.FORCE_HEX_LOWER_CASE);
		doc.save();
		HashMap tmp = doc.getItemValue("MapField", HashMap.class);
		System.out.println(tmp.size());
		System.out.println(doc.getMetaversalID());
		System.out.println(doc.getItemValueString("EnumField"));
		LocalDate sqlDt = doc.getItemValue("DateTimeField", LocalDate.class);
		System.out.println(sqlDt);
		LocalTime sqlTime = doc.getItemValue("DateTimeField", LocalTime.class);
		System.out.println(sqlTime);
		System.out.println(doc.getItemValues("BigDecimalField", BigDecimal.class));
		System.out.println(doc.getFirstItem("MVField").getTypeEx());
		ArrayList<String> blank = new ArrayList<String>();
		doc.replaceItemValue("MVField", blank);
		System.out.println(doc.hasItem("MVField"));

		NamesList names = new NamesList();
		names.add("CN=Paul Withers/O=Intec");
		names.add("CN=Admin/O=Intec=PW");
		newDoc.replaceItemValue("Names", names);
		AuthorsList authors = new AuthorsList();
		authors.addAll(names);
		newDoc.replaceItemValue("Authors", authors);
		ReadersList readers = new ReadersList();
		readers.addAll(names);
		newDoc.replaceItemValue("Readers", readers);
		Item dt = newDoc.replaceItemValue("TestDate", "");
		Vector<DateTime> dates = new Vector();
		DateTime dt1 = sess.createDateTime("01/01/2017");
		DateTime dt2 = sess.createDateTime("02/01/2017");
		dates.add(dt1);
		dates.add(dt2);
		dt.setValues(dates);
		newDoc.save();
	}

	public void resetDoc(final Document doc) {
		doc.replaceItemValue("State", doc.getItemValue("State").get(0));
		doc.replaceItemValue("DateTimeField", null);
		doc.removeItem("MVField");
		doc.removeItem("DocAsJson");
		doc.removeItem("MapField");
		doc.removeItem("DateTimeField");
		doc.removeItem("DateOnlyField");
		doc.removeItem("TimeOnlyField");
		doc.removeItem("BigDecimalField");
		doc.removeItem("EnumField");
		doc.save();
	}

}
