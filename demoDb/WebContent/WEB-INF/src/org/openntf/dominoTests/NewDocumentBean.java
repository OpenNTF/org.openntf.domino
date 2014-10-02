package org.openntf.dominoTests;

/*
 	Copyright 2014 OpenNTF Domino API Team Licensed under the Apache License, Version 2.0
	(the "License"); you may not use this file except in compliance with the
	License. You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
	or agreed to in writing, software distributed under the License is distributed
	on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
	express or implied. See the License for the specific language governing
	permissions and limitations under the License
	
*/

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Item;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.helpers.Formula;
import org.openntf.domino.utils.Factory;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class NewDocumentBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NewDocumentBean() {

	}

	public void doGetPut() {
		Session s = Factory.getSession();
		Database currDb = s.getCurrentDatabase();
		Utils.addAllListeners(currDb);
		View threads = currDb.getView("AllContacts");
		Document doc = threads.getFirstDocument();
		if (!doc.hasItem("documentToggle")) {
			doc.put("documentToggle", true);
		} else {
			doc.put("documentToggle", null);
		}
		doc.save(true, false);
		ExtLibUtil.getViewScope().put("javaTest", doc.get("documentToggle"));
	}

	public void containsCheckItems() {
		StringBuilder sb = new StringBuilder();
		Session s = Factory.getSession();
		Database currDb = s.getCurrentDatabase();
		View contacts = currDb.getView("AllContacts");
		ArrayList<String> items = new ArrayList<String>();
		items.add("FirstName");
		items.add("LastName");
		items.add("City");
		int count = 0;
		for (ViewEntry ent : contacts.getAllEntries()) {
			if (ent.getDocument().containsValue("Aurora", items)) {
				count += 1;
			}
		}
		sb.append(Integer.toString(count) + " documents contained a value 'Aurora'");
		sb.append("..............");
		Document testDoc = contacts.getFirstDocument();
		if (testDoc.containsKey("Form")) {
			sb.append("First doc contains key Form");
		} else {
			sb.append("First doc does not contain key Form");
		}
		ExtLibUtil.getViewScope().put("javaTest", sb.toString());
	}

	public void getCreated() {
		Session s = Factory.getSession();
		Database currDb = s.getCurrentDatabase();
		View contacts = currDb.getView("AllContacts");
		Document doc = contacts.getFirstDocument();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		String formatted = format.format(doc.getCreatedDate());
		ExtLibUtil.getViewScope().put("javaTest", formatted);
	}

	public void getCreatedOld() {
		Session s = Factory.getSession();
		Database currDb = s.getCurrentDatabase();
		View contacts = currDb.getView("AllContacts");
		Document doc = contacts.getFirstDocument();
		DateTime created = doc.getCreated();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		String formatted = format.format(created.toJavaDate());
		ExtLibUtil.getViewScope().put("javaTest", formatted);
	}

	public void getOtherDates() {
		Session s = Factory.getSession();
		Database currDb = s.getCurrentDatabase();
		View contacts = currDb.getView("AllContacts");
		Document doc = contacts.getFirstDocument();
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		sb.append("Created: " + format.format(doc.getCreatedDate()));
		sb.append("....");
		sb.append("First modified: " + format.format(doc.getInitiallyModifiedDate()));
		sb.append("....");
		sb.append("Last modified: " + format.format(doc.getLastModifiedDate()));
		sb.append("....");
		sb.append("Last accessed: " + format.format(doc.getLastAccessedDate()));
		ExtLibUtil.getViewScope().put("javaTest", sb.toString());
	}

	public void getFormName() {
		Session s = Factory.getSession();
		Database currDb = s.getCurrentDatabase();
		View contacts = currDb.getView("AllContacts");
		Document doc = contacts.getFirstDocument();
		ExtLibUtil.getViewScope().put("javaTest", doc.getFormName());
	}

	public void getForm() {
		Session s = Factory.getSession();
		Database currDb = s.getCurrentDatabase();
		View contacts = currDb.getView("AllContacts");
		Document doc = contacts.getFirstDocument();
		ExtLibUtil.getViewScope().put("javaTest", doc.getForm().getNoteID());
	}

	public void replaceItemValueSummary() {
		Session s = Factory.getSession();
		Database currDb = s.getCurrentDatabase();
		View contacts = currDb.getView("AllContacts");
		Utils.addAllListeners(currDb);
		Document doc = contacts.getFirstDocument();
		StringBuilder sb = new StringBuilder();
		sb.append("Here is a value");
		Item itm = doc.replaceItemValue("summaryField", sb, true);
		doc.save(true, false);
		ExtLibUtil.getViewScope().put("javaTest", doc.get("summaryField") + " " + Boolean.toString(itm.isSummary()));
	}

	public void createNathan() {
		Session s = Factory.getSession();
		Database currDb = s.getCurrentDatabase();
		Utils.addAllListeners(currDb);
		Document contact = currDb.createDocument("Form", "Contact", "FirstName", "Nathan", "LastName", "Freeman",
				"Email", "godOfAwesome@worldOfAweso.me", "City", "Washington", "State", "WA");
		contact.save();
		ExtLibUtil.getViewScope().put("javaTest", contact.getNoteID());
	}

	public void createPaul() {
		Session s = Factory.getSession();
		Database currDb = s.getCurrentDatabase();
		Utils.addAllListeners(currDb);
		HashMap<String, Object> fieldsMap = new HashMap<String, Object>();
		fieldsMap.put("Form", "Contact");
		fieldsMap.put("FirstName", "Paul");
		fieldsMap.put("LastName", "Withers");
		fieldsMap.put("Email", "lordOfPrettyGood@worldOfAweso.me");
		fieldsMap.put("City", "Washington");
		fieldsMap.put("State", "WA");
		Document contact = currDb.createDocument(fieldsMap);
		contact.save();
		ExtLibUtil.getViewScope().put("javaTest", contact.getNoteID());
	}

	public void setDateField() {
		Session s = Factory.getSession();
		Database currDb = s.getCurrentDatabase();
		View contacts = currDb.getView("AllContacts");
		Utils.addAllListeners(currDb);
		Document doc = contacts.getFirstDocument();
		doc.put("javaDateField", new Date());
		doc.save(true, false);
		ExtLibUtil.getViewScope().put("javaTest", doc.get("javaDateField"));
	}

	public void setFormulaField() {
		Session s = Factory.getSession();
		Database currDb = s.getCurrentDatabase();
		View contacts = currDb.getView("AllContacts");
		Utils.addAllListeners(currDb);
		Document doc = contacts.getFirstDocument();
		Formula fm = new Formula("@DocumentUniqueID");
		doc.put("javaFormulaField", "Document UNID is " + fm.getValue(doc));
		doc.save(true, false);
		ExtLibUtil.getViewScope().put("javaTest", doc.get("javaFormulaField"));
	}

	public void setDocumentCollectionField() {
		Session s = Factory.getSession();
		Database currDb = s.getCurrentDatabase();
		View contacts = currDb.getView("AllContacts");
		View contactsByState = currDb.getView("AllContactsByState");
		Utils.addAllListeners(currDb);
		Document doc = contacts.getFirstDocument();
		DocumentCollection dc = contactsByState.getAllDocumentsByKey("CA", true);
		doc.put("javaDCField", dc);
		doc.save(true, false);
		DocumentCollection srcDc = doc.getItemValue("javaDCField", DocumentCollection.class);
		StringBuilder sb = new StringBuilder();
		for (Document savedDoc : srcDc) {
			sb.append(savedDoc.getNoteID());
			sb.append(", ");
		}
		ExtLibUtil.getViewScope().put("javaTest", sb.toString());
	}

	public void setMapField() {
		Session s = Factory.getSession();
		Database currDb = s.getCurrentDatabase();
		View contacts = currDb.getView("AllContacts");
		Utils.addAllListeners(currDb);
		Document doc = contacts.getFirstDocument();
		TreeMap<String, String> testMap = new TreeMap<String, String>();
		testMap.put("Per", "Denmark");
		testMap.put("John", "South Africa");
		testMap.put("Mark", "Netherlands");
		testMap.put("Paul", "UK");
		doc.put("javaMapField", testMap);
		doc.save(true, false);
	}

	public void breakNames() {
		try {
			Session s = Factory.getSession();
			Database currDb = s.getCurrentDatabase();
			View contacts = currDb.getView("AllContacts");
			Document doc = contacts.getFirstDocument();
			Item testItem = doc.replaceItemValue("muppetField", 1);
			testItem.setNames(true);
			doc.save(true, false);
		} catch (Throwable t) {
			ExtLibUtil.getViewScope().put("javaTest", t.getClass().getName() + ": " + t.getLocalizedMessage());
		}
	}

	public void testNamesAuthorsReaders() {
		try {
			StringBuilder sb = new StringBuilder();
			Session s = Factory.getSession();
			Database currDb = s.getCurrentDatabase();
			View contacts = currDb.getView("AllContacts");
			Document doc = contacts.getFirstDocument();
			Item testItem = doc.replaceItemValue("readersAuthorsNamesField", s.getEffectiveUserName());
			sb.append("Checking if Item is Readers, Names or Authors...");
			sb.append(testItem.isReadersNamesAuthors());
			testItem.setNames(true);
			testItem.setAuthors(true);
			testItem.setReaders(true);
			sb.append("<br/>Checking if Item is Readers, Names or Authors...");
			sb.append(testItem.isReadersNamesAuthors());
			doc.save(true, false);
			sb.append("<br/>Success!!");
			ExtLibUtil.getViewScope().put("javaTest", sb.toString());
		} catch (Throwable t) {

		}
	}

	public void getDocAsJson() {
		try {
			Session s = Factory.getSession();
			Database currDb = s.getCurrentDatabase();
			View contacts = currDb.getView("AllContacts");
			Document doc = contacts.getFirstDocument();
			ExtLibUtil.getViewScope().put("javaTest", doc.toJson(true));
		} catch (Throwable t) {

		}
	}
}
