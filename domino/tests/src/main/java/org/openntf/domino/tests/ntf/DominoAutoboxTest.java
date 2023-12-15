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
package org.openntf.domino.tests.ntf;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.Session;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@SuppressWarnings("unused")
public class DominoAutoboxTest implements Runnable {
	public static void main(final String[] args) {
		DominoThread thread = new DominoThread(new DominoAutoboxTest(), "Index Thread");
		thread.start();
	}

	public DominoAutoboxTest() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		Session session = Factory.getSession(SessionType.CURRENT);
		session.setConvertMIME(false);
		session.setFixEnable(Fixes.APPEND_ITEM_VALUE, true);
		session.setFixEnable(Fixes.FORCE_JAVA_DATES, true);
		session.setFixEnable(Fixes.CREATE_DB, true);
		Database db = session.getDatabase("", "log.nsf");
		Document doc = db.createDocument();
		doc.replaceItemValue("form", "Events");
		doc.replaceItemValue("Server", "Test");
		Map<String, String> map = new HashMap<String, String>();
		map.put("me", "us");
		map.put("myself", "ourselves");
		map.put("I", "we");
		doc.replaceItemValue("map", map);
		doc.save();
		String unid = doc.getUniversalID();
		doc = null;
		Document docJunk = db.createDocument();
		doc = db.getDocumentByUNID(unid);
		System.out.println(doc.getNoteID());
		Object o = doc.getItemValue("map", Map.class);
		System.out.println(o.getClass().getName());
		Map<String, String> remap = (Map<String, String>) o;

		for (String key : remap.keySet()) {
			System.out.println(key + ":" + remap.get(key));
		}
		session.setConvertMIME(true);
		doc = null;
		docJunk = db.createDocument();
		doc = db.getDocumentByUNID(unid);
		Vector<Item> items = doc.getItems();
		for (Item item : items) {
			if (item.getName().equalsIgnoreCase("map")) {
				System.out.println("map: " + item.getTypeEx());
				System.out.println("map value: " + item.getText());
			}
		}
		doc.replaceItemValue("foo", "bar");
		doc.save();
		session.setConvertMIME(false);
		o = null;
		doc = null;
		docJunk = db.createDocument();
		doc = db.getDocumentByUNID(unid);
		o = doc.getItemValue("map", Map.class);
		System.out.println(o.getClass().getName());
		remap = (Map<String, String>) o;

		for (String key : remap.keySet()) {
			System.out.println(key + ":" + remap.get(key));
		}

		System.out.println("Complete");
	}

}
