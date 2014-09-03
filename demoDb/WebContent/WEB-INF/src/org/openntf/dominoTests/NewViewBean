package org.openntf.dominoTests;

/*
 	Copyright 2013 Paul Withers Licensed under the Apache License, Version 2.0
	(the "License"); you may not use this file except in compliance with the
	License. You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
	or agreed to in writing, software distributed under the License is distributed
	on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
	express or implied. See the License for the specific language governing
	permissions and limitations under the License
	
*/

import java.io.Serializable;
import java.util.ArrayList;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class NewViewBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void processView() {
		StringBuilder sb = new StringBuilder();
		Database db = Factory.getSession().getCurrentDatabase();
		View view = db.getView("allStates");
		for (ViewEntry currentEntry : view.getAllEntries()) {
			sb.append(currentEntry.getNoteID() + "..."); // Do whatever it is you actually want to get done
		}
		ExtLibUtil.getViewScope().put("javaTest", sb.toString());
	}

	public void getAllEntriesByKey() {
		StringBuilder sb = new StringBuilder();
		Database db = Factory.getSession().getCurrentDatabase();
		View view = db.getView("allContactsByState");
		ArrayList<String> key = new ArrayList<String>();
		key.add("CA");
		ViewEntryCollection ec = view.getAllEntriesByKey(key, true);
		for (ViewEntry entry : ec) {
			sb.append(entry.getColumnValues().get(7) + "...");
		}
		ExtLibUtil.getViewScope().put("javaTest", sb.toString());
	}

	public void getAllDocumentsByKey() {
		StringBuilder sb = new StringBuilder();
		Database db = Factory.getSession().getCurrentDatabase();
		View view = db.getView("allContactsByState");
		ArrayList<String> key = new ArrayList<String>();
		key.add("CA");
		DocumentCollection dc = view.getAllDocumentsByKey(key, true);
		for (Document doc : dc) {
			sb.append(doc.get("FirstName") + " " + doc.get("LastName") + "...");
		}
		ExtLibUtil.getViewScope().put("javaTest", sb.toString());
	}

	public void getAllDocumentsByKeyNoMatch() {
		StringBuilder sb = new StringBuilder();
		Database db = Factory.getSession().getCurrentDatabase();
		View view = db.getView("allContactsByState");
		ArrayList<String> key = new ArrayList<String>();
		key.add("CX");
		DocumentCollection dc = view.getAllDocumentsByKey(key, true);
		sb.append("Getting values...");
		for (Document doc : dc) {
			sb.append(doc.get("FirstName") + " " + doc.get("LastName") + "...");
		}
		sb.append("Done");
		ExtLibUtil.getViewScope().put("javaTest", sb.toString());
	}

	public void getAllEntriesByKeyNoMatch() {
		StringBuilder sb = new StringBuilder();
		Database db = Factory.getSession().getCurrentDatabase();
		View view = db.getView("allContactsByState");
		ArrayList<String> key = new ArrayList<String>();
		key.add("CX");
		ViewEntryCollection ec = view.getAllEntriesByKey(key, true);
		sb.append("Getting values...");
		for (ViewEntry entry : ec) {
			sb.append(entry.getColumnValues().get(7) + "...");
		}
		sb.append("Done");
		ExtLibUtil.getViewScope().put("javaTest", sb.toString());
	}

	public void checkIsUnique() {
		try {
			StringBuilder sb = new StringBuilder();
			Database db = Factory.getSession().getCurrentDatabase();
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
			ExtLibUtil.getViewScope().put("javaTest", sb.toString());
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	public void checkViewsForReeducation() {
		try {
			StringBuilder sb = new StringBuilder();
			for (View vw : Utils.getCurrDb().getViews()) {
				sb.append("View " + vw.getName() + ": ");
				if (vw.isTimeSensitive()) {
					sb.append("WARNING: DEVELOPER RE-EDUCATION REQUIRED!! TIME-SENSITIVE FORMULA INCLUDED!");
				} else {
					sb.append("fine");
				}
				sb.append("<br/>");
			}
			ExtLibUtil.getViewScope().put("javaTest", sb.toString());
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	public void checkViewsIndexFlags() {
		try {
			StringBuilder sb = new StringBuilder();
			for (View vw : Utils.getCurrDb().getViews()) {
				sb.append("View " + vw.getName() + ": ");
				sb.append("Index type - " + vw.getIndexType() + ", ");
				sb.append("isDisableAutoUpdate - " + vw.isDisableAutoUpdate() + ", ");
				sb.append("isHideEmptyCategories - " + vw.isHideEmptyCategories() + ", ");
				sb.append("isDiscardIndex - " + vw.isDiscardIndex() + ", ");
				sb.append("is auto refresh after first use - " + vw.isAutoRefreshAfterFirstUse() + ", ");
				sb.append("isManualRefresh - " + vw.isManualRefresh() + ", ");
				sb.append("isAutomaticRefresh - " + vw.isAutomaticRefresh() + ", ");
				if (vw.getAutoRefreshSeconds() > 0) {
					sb.append("auto refresh interval - auto refresh, at most every "
							+ Integer.toString(vw.getAutoRefreshSeconds()) + " seconds, ");
				} else {
					sb.append("auto refresh interval - 0 seconds, ");
				}
				sb.append("index discarded in - " + Integer.toString(vw.getDiscardHours()) + " hours, ");
				sb.append("<br/>");
			}
			ExtLibUtil.getViewScope().put("javaTest", sb.toString());
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

}
