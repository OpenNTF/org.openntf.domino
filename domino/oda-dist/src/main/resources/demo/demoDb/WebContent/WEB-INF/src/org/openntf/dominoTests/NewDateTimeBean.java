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
import java.util.Date;
import java.util.Random;

import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;
import org.openntf.domino.utils.Factory;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class NewDateTimeBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public NewDateTimeBean() {

	}

	public void runDateTimes() {
		Session s = Factory.getSession();
		Date d = new Date();
		DateTime dt = s.createDateTime(d);
		DateTime dt2 = s.createDateTime(d);
		StringBuilder sb = new StringBuilder();
		sb.append(Utils.doChecks(dt, dt2));
		sb.append("<br/><br/>");
		dt.adjustHour(1);
		sb.append(Utils.doChecks(dt, dt2));
		sb.append("<br/><br/>");
		dt.adjustDay(-1);
		sb.append(Utils.doChecks(dt, dt2));
		sb.append("<br/><br/>");
		dt.adjustHour(-1);
		sb.append(Utils.doChecks(dt, dt2));
		ExtLibUtil.getViewScope().put("javaTest", sb.toString());
	}

	public void dateTimeIsBeforeTest() {
		StringBuilder sb = new StringBuilder();
		Session s = Factory.getSession();
		Database currDb = s.getCurrentDatabase();
		View threadsByDate = currDb.getView("AllThreadsByDate");
		ViewNavigator vNav = threadsByDate.createViewNav();
		vNav.setEntryOptions(org.openntf.domino.ViewNavigator.VN_ENTRYOPT_NOCOLUMNVALUES);
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(20);
		ViewEntry firstEnt = vNav.getNth(randomInt);
		while (!firstEnt.isDocument()) {
			firstEnt = vNav.getNext();
		}
		randomInt = randomGenerator.nextInt(20);
		ViewEntry secondEnt = vNav.getNth(randomInt);
		while (!secondEnt.isDocument()) {
			secondEnt = vNav.getNext();
		}
		Document firstDoc = firstEnt.getDocument();
		Document secondDoc = secondEnt.getDocument();
		String firstDt = firstDoc.getFirstItem("Date").getText();
		String secondDt = secondDoc.getFirstItem("Date").getText();
		DateTime firstDate = s.createDateTime(firstDt);
		DateTime secondDate = s.createDateTime(secondDt);
		sb.append("Comparing " + firstDt + " (" + firstDoc.getUniversalID() + ") with " + secondDt + " ("
				+ secondDoc.getUniversalID() + ")...");
		if (firstDate.isBefore(secondDate)) {
			sb.append("first before second");
		} else {
			sb.append("first NOT before second");
		}
		sb.append("..........................................................................................");
		sb.append("Comparing " + secondDt + " (" + secondDoc.getUniversalID() + ") with " + firstDt + " ("
				+ firstDoc.getUniversalID() + ")...");
		if (secondDate.isBefore(firstDate)) {
			sb.append("second before first");
		} else {
			sb.append("second NOT before first");
		}
		ExtLibUtil.getViewScope().put("javaTest", sb.toString());
	}

	public void dateTimeIsAfterTest() {
		StringBuilder sb = new StringBuilder();
		Session s = Factory.getSession();
		Database currDb = s.getCurrentDatabase();
		View threadsByDate = currDb.getView("AllThreadsByDate");
		ViewNavigator vNav = threadsByDate.createViewNav();
		vNav.setEntryOptions(org.openntf.domino.ViewNavigator.VN_ENTRYOPT_NOCOLUMNVALUES);
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(20);
		ViewEntry firstEnt = vNav.getNth(randomInt);
		while (!firstEnt.isDocument()) {
			firstEnt = vNav.getNext();
		}
		randomInt = randomGenerator.nextInt(20);
		ViewEntry secondEnt = vNav.getNth(randomInt);
		while (!secondEnt.isDocument()) {
			secondEnt = vNav.getNext();
		}
		Document firstDoc = firstEnt.getDocument();
		Document secondDoc = secondEnt.getDocument();
		String firstDt = firstDoc.getFirstItem("Date").getText();
		String secondDt = secondDoc.getFirstItem("Date").getText();
		DateTime firstDate = s.createDateTime(firstDt);
		DateTime secondDate = s.createDateTime(secondDt);
		sb.append("Comparing " + firstDt + " (" + firstDoc.getUniversalID() + ") with " + secondDt + " ("
				+ secondDoc.getUniversalID() + ")...");
		if (firstDate.isAfter(secondDate)) {
			sb.append("first after second");
		} else {
			sb.append("first NOT after second");
		}
		sb.append("..........................................................................................");
		sb.append("Comparing " + secondDt + " (" + secondDoc.getUniversalID() + ") with " + firstDt + " ("
				+ firstDoc.getUniversalID() + ")...");
		if (secondDate.isAfter(firstDate)) {
			sb.append("second after first");
		} else {
			sb.append("second NOT after first");
		}
		ExtLibUtil.getViewScope().put("javaTest", sb.toString());
	}

	public void dateTimeEqualsTest() {
		StringBuilder sb = new StringBuilder();
		Session s = Factory.getSession();
		Database currDb = s.getCurrentDatabase();
		View threads = currDb.getView("AllThreads");
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(100);
		Document firstDoc = threads.getNthDocument(randomInt);
		randomInt = randomGenerator.nextInt(100);
		Document secondDoc = threads.getNthDocument(randomInt);
		String firstDt = firstDoc.getFirstItem("Date").getText();
		String secondDt = secondDoc.getFirstItem("Date").getText();
		DateTime firstDate = s.createDateTime(firstDt);
		DateTime secondDate = s.createDateTime(secondDt);
		sb.append("Comparing " + firstDt + " (" + firstDoc.getUniversalID() + ") with " + secondDt + " ("
				+ secondDoc.getUniversalID() + ")...");
		if (firstDate.equals(secondDate)) {
			sb.append("first is the same date/time as second");
		} else {
			sb.append("first is NOT the same date/time as second");
		}
		ExtLibUtil.getViewScope().put("javaTest", sb.toString());
	}

	public void dateTimeEqualsIgnoreDateTest() {
		StringBuilder sb = new StringBuilder();
		Session s = Factory.getSession();
		Database currDb = s.getCurrentDatabase();
		View threads = currDb.getView("AllThreads");
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(100);
		Document firstDoc = threads.getNthDocument(randomInt);
		randomInt = randomGenerator.nextInt(100);
		Document secondDoc = threads.getNthDocument(randomInt);
		String firstDt = firstDoc.getFirstItem("Date").getText();
		String secondDt = secondDoc.getFirstItem("Date").getText();
		DateTime firstDate = s.createDateTime(firstDt);
		DateTime secondDate = s.createDateTime(secondDt);
		sb.append("Comparing " + firstDt + " (" + firstDoc.getUniversalID() + ") with " + secondDt + " ("
				+ secondDoc.getUniversalID() + ")...");
		if (firstDate.equalsIgnoreDate(secondDate)) {
			sb.append("first is the same time as second");
		} else {
			sb.append("first is NOT the same time as second");
		}
		ExtLibUtil.getViewScope().put("javaTest", sb.toString());
	}

	public void dateTimeEqualsIgnoreTimeTest() {
		StringBuilder sb = new StringBuilder();
		Session s = Factory.getSession();
		Database currDb = s.getCurrentDatabase();
		View threads = currDb.getView("AllThreads");
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(100);
		Document firstDoc = threads.getNthDocument(randomInt);
		randomInt = randomGenerator.nextInt(100);
		Document secondDoc = threads.getNthDocument(randomInt);
		String firstDt = firstDoc.getFirstItem("Date").getText();
		String secondDt = secondDoc.getFirstItem("Date").getText();
		DateTime firstDate = s.createDateTime(firstDt);
		DateTime secondDate = s.createDateTime(secondDt);
		sb.append("Comparing " + firstDt + " (" + firstDoc.getUniversalID() + ") with " + secondDt + " ("
				+ secondDoc.getUniversalID() + ")...");
		if (firstDate.equalsIgnoreTime(secondDate)) {
			sb.append("first is the same date as second");
		} else {
			sb.append("first is NOT the same date as second");
		}
		ExtLibUtil.getViewScope().put("javaTest", sb.toString());
	}

	public void getProcessedDate() {
		Session s = Factory.getSession();
		Database currDb = s.getCurrentDatabase();
		Utils.addAllListeners(currDb);
		View threads = currDb.getView("AllContacts");
		Document doc = threads.getFirstDocument();
		DateTime dt = s.createDateTime(new Date());
		doc.put("testDate", dt);
		doc.save(true, false);
		ExtLibUtil.getViewScope().put("javaTest", doc.get("testDate").toString());
	}
}
