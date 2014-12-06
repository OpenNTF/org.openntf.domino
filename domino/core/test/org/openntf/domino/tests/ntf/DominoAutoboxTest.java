package org.openntf.domino.tests.ntf;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import lotus.domino.NotesFactory;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.Session;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class DominoAutoboxTest implements Runnable {
	public static void main(final String[] args) {
		DominoThread thread = new DominoThread(new DominoAutoboxTest(), "Index Thread");
		thread.start();
	}

	public DominoAutoboxTest() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		Session session = this.getSession();
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

	protected Session getSession() {
		try {
			Session session = Factory.fromLotus(NotesFactory.createSession(), Session.SCHEMA, null);
			return session;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return null;
		}
	}
}
