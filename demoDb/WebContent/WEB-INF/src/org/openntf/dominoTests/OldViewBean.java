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
import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class OldViewBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OldViewBean() {

	}

	public void processView() {
		Database db = null;
		View view = null;
		ViewEntryCollection collection = null;
		ViewEntry currentEntry = null;
		ViewEntry nextEntry = null;
		StringBuilder sb = new StringBuilder();
		try {
			db = ExtLibUtil.getCurrentDatabase();
			view = db.getView("allStates");
			view.setAutoUpdate(false);
			collection = view.getAllEntries();
			currentEntry = collection.getFirstEntry();
			while (currentEntry != null) {
				nextEntry = collection.getNextEntry(currentEntry);
				try {
					sb.append(currentEntry.getNoteID() + "..."); // Do whatever it is you actually want to get done
				} catch (lotus.domino.NotesException ne1) {
					ne1.printStackTrace();
				} finally {
					currentEntry.recycle();
				}
				currentEntry = nextEntry;
			}
		} catch (lotus.domino.NotesException ne) {
			ne.printStackTrace();
		} finally {
			try {
				collection.recycle();
			} catch (lotus.domino.NotesException ne) {

			}
		}
		ExtLibUtil.getViewScope().put("oldJavaTest", sb.toString());
	}

	public void getAllEntriesByKey() {
		Database db = null;
		View view = null;
		ViewEntryCollection ec = null;
		ViewEntry entry = null;
		ViewEntry nextEntry = null;
		StringBuilder sb = new StringBuilder();
		try {
			db = ExtLibUtil.getCurrentDatabase();
			view = db.getView("allContactsByState");
			view.setAutoUpdate(false);
			Vector<String> key = new Vector<String>();
			key.add("CA");
			ec = view.getAllEntriesByKey(key, true);
			entry = ec.getFirstEntry();
			while (entry != null) {
				nextEntry = ec.getNextEntry();
				try {
					sb.append(entry.getColumnValues().get(7) + "...");
				} catch (lotus.domino.NotesException ne1) {
					ne1.printStackTrace();
				} finally {
					entry.recycle();
				}
				entry = nextEntry;
			}
		} catch (lotus.domino.NotesException ne) {
			ne.printStackTrace();
		} finally {
			try {
				ec.recycle();
			} catch (lotus.domino.NotesException ne) {

			}
		}
		ExtLibUtil.getViewScope().put("oldJavaTest", sb.toString());
	}

	public void getAllDocumentsByKey() {
		Database db = null;
		View view = null;
		DocumentCollection dc = null;
		Document doc = null;
		Document nextDoc = null;
		StringBuilder sb = new StringBuilder();
		try {
			db = ExtLibUtil.getCurrentDatabase();
			view = db.getView("allContactsByState");
			view.setAutoUpdate(false);
			Vector<String> key = new Vector<String>();
			key.add("CA");
			dc = view.getAllDocumentsByKey(key, true);
			doc = dc.getFirstDocument();
			while (doc != null) {
				nextDoc = dc.getNextDocument();
				try {
					sb.append(doc.getItemValueString("FirstName") + " " + doc.getItemValueString("LastName") + "...");
				} catch (lotus.domino.NotesException ne1) {
					ne1.printStackTrace();
				} finally {
					doc.recycle();
				}
				doc = nextDoc;
			}
		} catch (lotus.domino.NotesException ne) {
			ne.printStackTrace();
		} finally {
			try {
				dc.recycle();
			} catch (lotus.domino.NotesException ne) {

			}
		}
		ExtLibUtil.getViewScope().put("oldJavaTest", sb.toString());
	}

	public void getAllDocumentsByKeyNoMatch() {
		Database db = null;
		View view = null;
		DocumentCollection dc = null;
		Document doc = null;
		Document nextDoc = null;
		StringBuilder sb = new StringBuilder();
		try {
			db = ExtLibUtil.getCurrentDatabase();
			view = db.getView("allContactsByState");
			view.setAutoUpdate(false);
			Vector<String> key = new Vector<String>();
			key.add("CX");
			dc = view.getAllDocumentsByKey(key, true);
			sb.append("Getting values...");
			doc = dc.getFirstDocument();
			while (doc != null) {
				nextDoc = dc.getNextDocument();
				try {
					sb.append(doc.getItemValueString("FirstName") + " " + doc.getItemValueString("LastName") + "...");
				} catch (lotus.domino.NotesException ne1) {
					ne1.printStackTrace();
				} finally {
					doc.recycle();
				}
				doc = nextDoc;
			}
			sb.append("Done");
		} catch (lotus.domino.NotesException ne) {
			ne.printStackTrace();
		} finally {
			try {
				dc.recycle();
			} catch (lotus.domino.NotesException ne) {

			}
		}
		ExtLibUtil.getViewScope().put("oldJavaTest", sb.toString());
	}

	public void getAllEntriesByKeyNoMatch() {
		Database db = null;
		View view = null;
		ViewEntryCollection ec = null;
		ViewEntry entry = null;
		ViewEntry nextEntry = null;
		StringBuilder sb = new StringBuilder();
		try {
			db = ExtLibUtil.getCurrentDatabase();
			view = db.getView("allContactsByState");
			view.setAutoUpdate(false);
			Vector<String> key = new Vector<String>();
			key.add("CX");
			ec = view.getAllEntriesByKey(key, true);
			sb.append("Getting values...");
			entry = ec.getFirstEntry();
			while (entry != null) {
				nextEntry = ec.getNextEntry();
				try {
					sb.append(entry.getColumnValues().get(7) + "...");
				} catch (lotus.domino.NotesException ne1) {
					ne1.printStackTrace();
				} finally {
					entry.recycle();
				}
				entry = nextEntry;
			}
			sb.append("Done");
		} catch (lotus.domino.NotesException ne) {
			ne.printStackTrace();
		} finally {
			try {
				ec.recycle();
			} catch (lotus.domino.NotesException ne) {

			}
		}
		ExtLibUtil.getViewScope().put("oldJavaTest", sb.toString());
	}

}
