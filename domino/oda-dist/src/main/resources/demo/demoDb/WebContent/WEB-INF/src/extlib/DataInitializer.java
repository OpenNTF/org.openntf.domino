package extlib;

/*
 * Copied from Extension Library Demo database
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.DateRange;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.util.ExtLibUtil;

public class DataInitializer {

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

	public DataInitializer() {
	}

	// ===================================================================
	// Import data
	// ===================================================================

	public void run() throws NotesException, IOException {
		Database db = ExtLibUtil.getCurrentDatabase();
		if (deleteAllDoc) {
			deleteAllDocuments();
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
	}

	public void initDeleteDocuments() throws NotesException {
		this.deleteAllDoc = true;
	}

	public void initUsers(int maxUsers) throws NotesException {
		this.createUsers = true;
		this.users_maxUsers = maxUsers;
	}

	public void initStates() throws NotesException {
		this.createStates = true;
	}

	public void initDiscussionDocuments(int rootDocs, int maxResponse, int maxDepth) {
		this.createDiscussionDocuments = true;
		this.disc_rootDocs = rootDocs;
		this.disc_maxResponse = maxResponse;
		this.disc_maxDepth = maxDepth;
	}

	public void initAllTypes() throws NotesException {
		this.createAllTypes = true;
	}

	// ===================================================================
	// Delete all documents
	// ===================================================================

	void deleteAllDocuments() throws NotesException {
		Database db = ExtLibUtil.getCurrentDatabase();
		if (db.getAllDocuments().getCount() > 0) {
			db.getAllDocuments().removeAll(true);
		}
	}

	// ===================================================================
	// Users
	// ===================================================================

	private static final boolean UNIQUE_USERS = true;

	void createUsers(Database db) throws NotesException, IOException {
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

	void createUser(Database db, String id, String firstName, String lastName, String city, String state, String email)
			throws NotesException {
		Document doc = db.createDocument();
		try {
			doc.replaceItemValue("Form", "Contact");
			doc.replaceItemValue("Id", id);
			doc.replaceItemValue("FirstName", firstName);
			doc.replaceItemValue("LastName", lastName);
			doc.replaceItemValue("City", city);
			doc.replaceItemValue("State", state);
			doc.replaceItemValue("email", email);
			doc.save();
		} finally {
			doc.recycle();
		}
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
		// for(int i=0; i<city.length(); i++) {
		// char c = Character.toLowerCase(city.charAt(i));
		// if(c>='a' && c<='z') {
		// b.append(c);
		// } else if(c==',') { // State...
		// break;
		// }
		// }
		// b.append(".");
		b.append("renovations.com");
		return b.toString();
	}

	// ===================================================================
	// US States
	// ===================================================================

	void createStates(Database db) throws NotesException, IOException {
		View w = db.getView("AllStates");
		w.getAllEntries().removeAll(true);

		String[] states = SampleDataUtil.readStates();

		for (int i = 0; i < states.length; i++) {
			String[] s = StringUtil.splitString(states[i], ',');
			createState(db, s[1], s[0]);
		}

	}

	void createState(Database db, String key, String name) throws NotesException {
		Document doc = db.createDocument();
		try {
			doc.replaceItemValue("Form", "State");
			doc.replaceItemValue("Key", key);
			doc.replaceItemValue("Name", name);
			doc.save();
		} finally {
			doc.recycle();
		}
	}

	// ===================================================================
	// Discussion
	// ===================================================================

	void createDiscussionDocuments(Database db) throws NotesException, IOException {
		// Construct a list of authors
		// As we want the tag cloud to render differences between the authors, we give
		// as different weight to each author by adding it a random # of times in the list
		// We read the author names from the database
		ArrayList<String> users = new ArrayList<String>();
		View authorView = db.getView("AllContacts");
		authorView.refresh();
		try {
			int maxAuthors = 15;
			int nAuthor = 0;
			ViewEntryCollection authorCol = authorView.getAllEntries();
			for (ViewEntry e = authorCol.getFirstEntry(); e != null && nAuthor < maxAuthors; e = authorCol
					.getNextEntry()) {
				Vector<?> values = e.getColumnValues();
				String name = (String) values.get(7);
				// Add it a random number of times to the list
				int n = ((int) (Math.random() * maxAuthors)) + 1;
				for (int jj = 0; jj < n; jj++) {
					users.add(name);
				}
				nAuthor++;
			}
		} finally {
			authorView.recycle();
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
			throws NotesException, IOException {
		DateTime date = db.getParent().createDateTime(new Date());
		String[] loremIpsum = SampleDataUtil.readLoremIpsum();
		for (int j = 0; j < nDoc; j++) {
			pos[pos.length - 1] = j + 1;

			Document doc = db.createDocument();
			try {
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
			} finally {
				doc.recycle();
			}
		}
	}

	// ===================================================================
	// All Types
	// ===================================================================

	void createAllTypes(Database db) throws NotesException, IOException {
		View w = db.getView("AllTypes");
		w.getAllEntries().removeAll(true);

		for (int i = 1; i < 25; i++) {
			createAllType(db, i);
		}
	}

	void createAllType(Database db, int index) throws NotesException {
		Session session = db.getParent();
		String sIndex = Integer.toString(index);
		Document doc = db.createDocument();
		try {
			doc.replaceItemValue("Form", "AllTypes");

			doc.replaceItemValue("fldIcon", index);
			doc.replaceItemValue("fldText", "text_" + sIndex);
			doc.replaceItemValue("fldNumber", index * 100);
			doc.replaceItemValue("fldDate", createDate(session, 2010, 1, index));
			doc.replaceItemValue("fldTime", createTime(session, 5, 1, index));
			doc.replaceItemValue("fldDateTime", createDateTime(session, 2011, 2, index, 8, 9, index));
			doc.replaceItemValue("fldDateTimeRange", createDateTimeRange(session, 2012, 3, index, 8, 9, index));
			doc.replaceItemValue("fldDialogList", "dlg_" + sIndex);

			Vector<Object> mx = new Vector<Object>();
			mx.add("text_" + sIndex + "_1");
			mx.add("text_" + sIndex + "_2");
			mx.add("text_" + sIndex + "_3");
			doc.replaceItemValue("fldText2", mx);

			Vector<Object> mn = new Vector<Object>();
			mn.add(index * 100 + 1);
			mn.add(index * 100 + 2);
			mn.add(index * 100 + 3);
			doc.replaceItemValue("fldNumber2", mn);

			Vector<Object> md = new Vector<Object>();
			md.add(createDate(session, 2010, 1, index));
			md.add(createDate(session, 2010, 2, index));
			md.add(createDate(session, 2010, 3, index));
			doc.replaceItemValue("fldDate2", md);

			Vector<Object> mt = new Vector<Object>();
			mt.add(createTime(session, 6, 1, index));
			mt.add(createTime(session, 6, 2, index));
			mt.add(createTime(session, 6, 3, index));
			doc.replaceItemValue("fldTime2", mt);

			Vector<Object> mdt = new Vector<Object>();
			mdt.add(createDateTime(session, 2011, 1, index, 6, 1, index));
			mdt.add(createDateTime(session, 2011, 2, index, 6, 2, index));
			mdt.add(createDateTime(session, 2011, 3, index, 6, 3, index));
			doc.replaceItemValue("fldDateTime2", mdt);

			if (false) { // DateTime range do not work with multiple values?
				Vector<Object> mrg = new Vector<Object>();
				mrg.add(createDateTimeRange(session, 2012, 2, index, 4, 1, index));
				mrg.add(createDateTimeRange(session, 2012, 3, index, 5, 1, index));
				mrg.add(createDateTimeRange(session, 2012, 4, index, 6, 1, index));
				doc.replaceItemValue("fldDateTimeRange2", mrg);
			}

			Vector<Object> mdg = new Vector<Object>();
			mdg.add("dlgx_" + sIndex + "_1");
			mdg.add("dlgx_" + sIndex + "_1");
			mdg.add("dlgx_" + sIndex + "_1");
			doc.replaceItemValue("fldDialogList2", mdg);

			doc.save();
		} finally {
			doc.recycle();
		}
	}

	protected DateTime createDate(Session session, int year, int month, int day) throws NotesException {
		DateTime d = session.createDateTime(new Date());
		d.setLocalDate(year, month, day);
		return d;
	}

	protected DateTime createTime(Session session, int hour, int minute, int second) throws NotesException {
		DateTime d = session.createDateTime(new Date());
		d.setLocalTime(hour, minute, second, 0);
		return d;
	}

	protected DateTime createDateTime(Session session, int year, int month, int day, int hour, int minute, int second)
			throws NotesException {
		DateTime d = session.createDateTime(new Date());
		d.setLocalDate(year, month, day);
		d.setLocalTime(hour, minute, second, 0);
		return d;
	}

	protected DateRange createDateTimeRange(Session session, int year, int month, int day, int hour, int minute,
			int second) throws NotesException {
		DateRange r = session.createDateRange(new Date(), new Date());
		r.setStartDateTime(createDateTime(session, year, month, day, hour, minute, second));
		r.setEndDateTime(createDateTime(session, year + 1, month, day, hour + 1, minute, second));
		return r;
	}
}
