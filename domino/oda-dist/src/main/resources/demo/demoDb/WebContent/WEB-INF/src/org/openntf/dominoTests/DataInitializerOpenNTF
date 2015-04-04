package extlib;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Vector;

import org.openntf.domino.Database;
import org.openntf.domino.DateRange;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.utils.Factory;

import com.ibm.commons.util.StringUtil;

public class DataInitializerOpenNTF {

	// Delete?
	boolean deleteAllDoc;

	// Users
	boolean createUsers;
	int users_maxUsers;

	// US States
	boolean createStates;

	// Discussion threads
	boolean createDiscussionDocuments;
	int disc_rootDocs;
	int disc_maxResponse;
	int disc_maxDepth;

	// All types
	boolean createAllTypes;

	public DataInitializerOpenNTF() {
	}

	// ===================================================================
	// Import data
	// ===================================================================

	public void run() throws IOException {
		try {
			Database db = Factory.getSession().getCurrentDatabase();
			if (deleteAllDoc) {
				deleteAllDocuments(db);
			}
			if (createUsers) {
				createUsers(db);
			}
			if (createStates) {
				createStates(db);
			}
			if (createDiscussionDocuments) {
				createDiscussionDocuments(db);
			}
			if (createAllTypes) {
				createAllTypes(db);
			}
		} catch (Exception ee) {
			System.out.println(ee.getMessage());
		}
	}

	public void initDeleteDocuments() {
		this.deleteAllDoc = true;
	}

	public void initUsers(int maxUsers) {
		this.createUsers = true;
		this.users_maxUsers = maxUsers;
	}

	public void initStates() {
		this.createStates = true;
	}

	public void initDiscussionDocuments(int rootDocs, int maxResponse, int maxDepth) {
		this.createDiscussionDocuments = true;
		this.disc_rootDocs = rootDocs;
		this.disc_maxResponse = maxResponse;
		this.disc_maxDepth = maxDepth;
	}

	public void initAllTypes() {
		this.createAllTypes = true;
	}

	// ===================================================================
	// Delete all documents
	// ===================================================================

	void deleteAllDocuments(Database currDb) {
		currDb.getAllDocuments().removeAll(true);
	}

	// ===================================================================
	// Users
	// ===================================================================

	private static final boolean UNIQUE_USERS = true;

	void createUsers(Database db) throws IOException {
		View w = db.getView("AllContacts");
		w.getAllEntries().removeAll(true);

		String[] firstNames = SampleDataUtil.readFirstNames();
		String[] lastNames = SampleDataUtil.readLastNames();
		String[] cities = SampleDataUtil.readCities();

		HashSet<String> users = UNIQUE_USERS ? new HashSet<String>() : null;
		for (int i = 0; i < users_maxUsers; i++) {
			while (true) {
				String firstName = firstNames[(int) (Math.random() * firstNames.length)];
				String lastName = lastNames[(int) (Math.random() * lastNames.length)];
				String fullcity = cities[(int) (Math.random() * cities.length)];
				String city = SampleDataUtil.cityName(fullcity);
				String state = SampleDataUtil.cityState(fullcity);
				String email = createEmail(firstName, lastName, city);
				String id = "CN=" + firstName + " " + lastName + "/O=renovations";

				// If user already there, then reject and continue
				// Else, create it...
				String nn = lastName + " " + firstName;
				if (users == null || !users.contains(nn)) {
					if (users != null) {
						users.add(nn);
					}
					createUser(db, id, firstName, lastName, city, state, email);
					break;
				}
			}
		}

	}

	void createUser(Database db, String id, String firstName, String lastName, String city, String state, String email) {
		Document doc = db.createDocument("Form", "Contact", "Id", id, "FirstName", firstName, "LastName", lastName,
				"City", city, "State", state, "email", email);
		doc.save();
	}

	String createEmail(String firstName, String lastName, String city) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < firstName.length(); i++) {
			char c = Character.toLowerCase(firstName.charAt(i));
			if (c >= 'a' && c <= 'z') {
				b.append(c);
			}
		}
		b.append('_');
		for (int i = 0; i < lastName.length(); i++) {
			char c = Character.toLowerCase(lastName.charAt(i));
			if (c >= 'a' && c <= 'z') {
				b.append(c);
			}
		}
		b.append("@");
		b.append("renovations.com");
		return b.toString();
	}

	// ===================================================================
	// US States
	// ===================================================================

	void createStates(Database db) throws IOException {
		View w = db.getView("AllStates");
		w.getAllEntries().removeAll(true);

		String[] states = SampleDataUtil.readStates();

		for (int i = 0; i < states.length; i++) {
			String[] s = StringUtil.splitString(states[i], ',');
			createState(db, s[1], s[0]);
		}

	}

	void createState(Database db, String key, String name) {
		Document doc = db.createDocument("Form", "State", "Key", key, "Name", name);
		doc.save();
	}

	// ===================================================================
	// Discussion
	// ===================================================================

	void createDiscussionDocuments(Database db) throws IOException {
		// Construct a list of authors
		// As we want the tag cloud to render differences between the authors, we give
		// as different weight to each author by adding it a random # of times in the list
		// We read the author names from the database
		ArrayList<String> users = new ArrayList<String>();
		View authorView = db.getView("AllContacts");
		authorView.refresh();
		int maxAuthors = 15;
		int nAuthor = 0;
		ViewEntryCollection ec = authorView.getAllEntries();
		for (ViewEntry e : ec) {
			Vector<?> values = e.getColumnValues();
			String name = (String) values.get(7);
			// Add it a random number of times to the list
			int n = ((int) (Math.random() * maxAuthors)) + 1;
			for (int jj = 0; jj < n; jj++) {
				users.add(name);
			}
			nAuthor++;
			if (nAuthor > maxAuthors) {
				break;
			}
		}
		if (users.size() == 0) {
			// Just in case they were not created...
			users.add("John Doe");
		}

		View w = db.getView("AllThreads");
		w.getAllEntries().removeAll(true);
		createDiscussionDocument(db, null, users, new int[] { 0 }, disc_rootDocs);
	}

	void createDiscussionDocument(Database db, Document parent, ArrayList<String> users, int[] pos, int nDoc)
			throws IOException {
		DateTime date = db.getParent().createDateTime(new Date());
		String[] loremIpsum = SampleDataUtil.readLoremIpsum();
		for (int j = 0; j < nDoc; j++) {
			pos[pos.length - 1] = j + 1;

			Document doc = db.createDocument();
			doc.replaceItemValue("Form", "Discussion");
			StringBuilder b = new StringBuilder();
			for (int i = 0; i < pos.length; i++) {
				if (i > 0) {
					b.append("/");
				}
				b.append(pos[i]);
			}
			int idx = (int) (Math.random() * (loremIpsum.length - 1));
			String body = loremIpsum[idx];
			int dot = body.indexOf('.');
			if (dot < 0) {
				dot = body.length() - 1;
			}
			int coma = body.indexOf(',');
			if (coma < 0) {
				coma = body.length() - 1;
			}
			String title = body.substring(0, Math.min(dot, coma));

			// Get a random author
			int x = Math.min((int) (Math.random() * (users.size())), users.size());
			String author = users.get(x);

			doc.replaceItemValue("Title", title);
			doc.replaceItemValue("Body", body);
			doc.replaceItemValue("Author", author);
			doc.replaceItemValue("Date", date);
			if (parent != null) {
				doc.makeResponse(parent);
			}
			doc.computeWithForm(false, false);
			doc.save();

			if (pos.length < disc_maxDepth) {
				double r = Math.random();
				if (r <= (1.0 / pos.length)) {
					int[] newPos = new int[pos.length + 1];
					System.arraycopy(pos, 0, newPos, 0, pos.length);
					int n = (int) (Math.random() * 5);
					createDiscussionDocument(db, doc, users, newPos, n);
				}
			}
			// Move the date to the day before if requested
			boolean mvd = Math.random() <= 0.40;
			if (mvd) {
				// Previous day...
				date.adjustDay(-1);
			}
		}
	}

	// ===================================================================
	// All Types
	// ===================================================================

	void createAllTypes(Database db) throws IOException {
		View w = db.getView("AllTypes");
		w.getAllEntries().removeAll(true);

		for (int i = 1; i < 25; i++) {
			createAllType(db, i);
		}
	}

	void createAllType(Database db, int index) {
		Session session = db.getParent();
		String sIndex = Integer.toString(index);
		Document doc = db.createDocument();
		doc.put("Form", "AllTypes");

		doc.put("fldIcon", index);
		doc.put("fldText", "text_" + sIndex);
		doc.put("fldNumber", index * 100);
		doc.put("fldDate", createDate(2012, Calendar.JANUARY, index));
		doc.put("fldTime", createTime(session, 5, 1, index));
		doc.put("fldDateTime", createDateTime(2011, 2, index, 8, 9, index));
		doc.put("fldDateTimeRange", createDateTimeRange(session, 2012, 3, index, 8, 9, index));
		doc.put("fldDialogList", "dlg_" + sIndex);

		ArrayList<String> mx = new ArrayList<String>();
		mx.add("text_" + sIndex + "_1");
		mx.add("text_" + sIndex + "_2");
		mx.add("text_" + sIndex + "_3");
		doc.put("fldText2", mx);

		ArrayList<Object> mn = new ArrayList<Object>();
		mn.add(index * 100 + 1);
		mn.add(index * 100 + 2);
		mn.add(index * 100 + 3);
		doc.put("fldNumber2", mn);

		ArrayList<Date> md = new ArrayList<Date>();
		md.add(createDate(2010, Calendar.JANUARY, index));
		md.add(createDate(2010, Calendar.FEBRUARY, index));
		md.add(createDate(2010, Calendar.MARCH, index));
		doc.put("fldDate2", md);

		Vector<Date> mt = new Vector<Date>();
		mt.add(createTime(session, 6, 1, index));
		mt.add(createTime(session, 6, 2, index));
		mt.add(createTime(session, 6, 3, index));
		doc.put("fldTime2", mt);

		Vector<Date> mdt = new Vector<Date>();
		mdt.add(createDateTime(2011, 1, index, 6, 1, index));
		mdt.add(createDateTime(2011, 2, index, 6, 2, index));
		mdt.add(createDateTime(2011, 3, index, 6, 3, index));
		doc.put("fldDateTime2", mdt);

		if (false) { // DateTime range do not work with multiple values?
			Vector<DateRange> mrg = new Vector<DateRange>();
			mrg.add(createDateTimeRange(session, 2012, 2, index, 4, 1, index));
			mrg.add(createDateTimeRange(session, 2012, 3, index, 5, 1, index));
			mrg.add(createDateTimeRange(session, 2012, 4, index, 6, 1, index));
			doc.put("fldDateTimeRange2", mrg);
		}

		ArrayList<Object> mdg = new ArrayList<Object>();
		mdg.add("dlgx_" + sIndex + "_1");
		mdg.add("dlgx_" + sIndex + "_1");
		mdg.add("dlgx_" + sIndex + "_1");
		doc.put("fldDialogList2", mdg);

		doc.save();
	}

	protected Date createDate(int year, int month, int day) {
		Calendar c1 = GregorianCalendar.getInstance();
		c1.set(year, month, day);
		Date d = c1.getTime();
		return d;
	}

	protected Date createTime(Session session, int hour, int minute, int second) {
		Calendar c1 = GregorianCalendar.getInstance();
		c1.set(Calendar.HOUR_OF_DAY, hour);
		c1.set(Calendar.MINUTE, minute);
		c1.set(Calendar.SECOND, second);
		Date d = c1.getTime();
		return d;
	}

	protected Date createDateTime(int year, int month, int day, int hour, int minute, int second) {
		Calendar c1 = GregorianCalendar.getInstance();
		c1.set(year, month, day, hour, minute, second);
		Date d = c1.getTime();
		return d;
	}

	protected DateRange createDateTimeRange(Session session, int year, int month, int day, int hour, int minute,
			int second) {
		DateRange r = session.createDateRange(new Date(), new Date());
		r.setStartDateTime(session.createDateTime(createDateTime(year, month, day, hour, minute, second)));
		r.setEndDateTime(session.createDateTime(createDateTime(year + 1, month, day, hour + 1, minute, second)));
		return r;
	}
}
