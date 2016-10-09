import static org.apache.commons.lang.StringUtils.join;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.DbDirectory;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;

import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.github.javafaker.Faker;

public enum CreateTestDatabaseODA {
	;
	static final String FAKENAMES_NSF = "fakenames.nsf";

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(DbSetup.class, TestRunnerUtil.NATIVE_SESSION, 1);
		// 1 Thread: 840 docs/sec
		// 2 Threads: 1264 docs/sec
		// 3 Threads: 1400 docs/sec
		// 4 Threads: 1480 docs/sec
		TestRunnerUtil.runAsDominoThread(DocCreate.class, TestRunnerUtil.NATIVE_SESSION, 5);
	}

	public static class DbSetup implements Runnable {
		@Override
		public void run() {
			try {
				Session sess = Factory.getSession(SessionType.CURRENT);
				DbDirectory dir = sess.getDbDirectory("");

				Database db = sess.getDatabase("", FAKENAMES_NSF);
				if (db != null)
					db.remove();

				db = dir.createDatabase(FAKENAMES_NSF, true);
				View view = db.createView("Names", "SELECT Form = \"DocName\"");
				view.createColumn(2, "Title", "Title").recycle();

				view.createColumn(3, "Prefix", "NamePrefix").recycle();
				view.createColumn(4, "First Name", "FirstName").recycle();
				view.createColumn(5, "Last Name", "LastName").recycle();

				view.createColumn(6, "Zip Code", "ZipCode").recycle();
				view.createColumn(7, "Country", "Country").recycle();
				view.createColumn(8, "Address", "Address").recycle();

				view.createColumn(9, "Birthday", "Birthday").recycle();
				view.createColumn(10, "PhoneNumber", "PhoneNumber").recycle();
				view.recycle();
				db.recycle();
				dir.recycle();
				//	sess.recycle();
			} catch (NotesException e) {
				e.printStackTrace();
			}
		}
	}

	public static class DocCreate implements Runnable {
		static public int docCount = 0;
		static int dbCnt = 0;
		Faker faker = new Faker();
		static long startTime = System.currentTimeMillis();

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			try {
				Database db;
				Session sess = Factory.getSession(SessionType.CURRENT);

				db = sess.getDatabase("", FAKENAMES_NSF);
				DominoUtils.setBubbleExceptions(true);
				System.out.println("START");
				String[] group = { "LocalDomainAdmins", "User", "Developer", "Staff" };
				Document doc;
				for (int i = 0; i < 50000; i++) {
					try {
						doc = db.createDocument();
						doc.replaceItemValue("Form", "DocName").recycle();

						String firstName;
						String lastName;
						String namePrefix;
						doc.replaceItemValue("NamePrefix", namePrefix = faker.name().prefix()).recycle();
						doc.replaceItemValue("FirstName", firstName = faker.name().firstName()).recycle();
						doc.replaceItemValue("LastName", lastName = faker.name().lastName()).recycle();
						doc.replaceItemValue("Title", join(new String[] { namePrefix, firstName, lastName }, " ")).recycle();
						doc.replaceItemValue("NameSuffix", faker.name().suffix()).recycle();

						doc.replaceItemValue("CityPrefix", faker.address().cityPrefix()).recycle();
						doc.replaceItemValue("CitySuffix", faker.address().citySuffix()).recycle();

						doc.replaceItemValue("ZipCode", faker.address().zipCode()).recycle();
						doc.replaceItemValue("Country", faker.address().country()).recycle();
						doc.replaceItemValue("Address", faker.address().streetAddress(true)).recycle();

						Date d = new Date(faker.integer(1900, 2010), faker.integer(1, 11), faker.integer(1, 31));
						DateTime birthday = sess.createDateTime(d);
						doc.replaceItemValue("Birthday", birthday).recycle();
						birthday.recycle();
						doc.replaceItemValue("PhoneNumber", faker.phoneNumber().phoneNumber()).recycle();

						doc.replaceItemValue("CreditCardNumber", faker.business().creditCardNumber()).recycle();
						doc.replaceItemValue("CreditCartExpiry", faker.business().creditCardExpiry()).recycle();
						doc.replaceItemValue("CreditCartType", faker.business().creditCardType()).recycle();
						Set<String> members = new HashSet<String>();
						int j;
						int grpCnt = faker.integer(0, 4);
						for (j = 0; j < grpCnt; j++) {
							members.add(faker.options().option(group));
						}
						if (grpCnt > 0)
							doc.replaceItemValue("Member", new Vector<String>(members)).recycle();
						doc.save();
						doc.recycle();
					} catch (Exception e) {
						System.err.println("[i=" + i + "] Exception " + e.getClass().getName() + " at DocCount=" + docCount);
						e.printStackTrace();
						return;
					}
					docCount++;

					if (docCount % 1000 == 0) {
						long runTime = System.currentTimeMillis() - startTime;
						if (runTime > 0) {
							System.out.println("Created " + docCount + " documents. " + (docCount * 1000 / runTime) + " docs/sec.");
						}
					}
				}
				db.recycle();
				sess.recycle();
			} catch (NotesException e) {
				e.printStackTrace();
			}
		}
	}
}
