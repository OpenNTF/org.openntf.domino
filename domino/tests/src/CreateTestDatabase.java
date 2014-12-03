import static org.apache.commons.lang.StringUtils.join;

import java.util.HashSet;
import java.util.Set;

import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;

import com.github.javafaker.Faker;

public enum CreateTestDatabase {
	;
	static final String FAKENAMES_NSF = "fakenames.nsf";

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new DbSetup(), TestRunnerUtil.NATIVE_SESSION);
		System.out.println("Start next thread");
		// 1 Thread: 840 docs/sec
		// 2 Threads: 1264 docs/sec
		// 3 Threads: 1400 docs/sec
		// 4 Threads: 1480 docs/sec
		TestRunnerUtil.runAsDominoThread(new DocCreate(), TestRunnerUtil.NATIVE_SESSION, 10);
	}

	public static class DbSetup implements Runnable {
		@Override
		public void run() {
			Session sess = Factory.getSession();
			DbDirectory dir = sess.getDbDirectory("");

			Database db = sess.getDatabase(FAKENAMES_NSF);
			if (db != null)
				db.remove();

			db = dir.createDatabase(FAKENAMES_NSF, true);
			View view = db.createView("Names", "SELECT Form = \"DocName\"");
			view.createColumn(2, "Full Name", "FullName");

			view.createColumn(3, "Prefix", "NamePrefix");
			view.createColumn(4, "First Name", "FirstName");
			view.createColumn(5, "Last Name", "LastName");

			view.createColumn(6, "Zip Code", "ZipCode");
			view.createColumn(7, "Country", "Country");
			view.createColumn(8, "Address", "Address");

			view.createColumn(9, "Birthday", "Birthday");
			view.createColumn(10, "PhoneNumber", "PhoneNumber");
		}
	}

	public static class DocCreate implements Runnable {
		public int docCount = 0;
		int dbCnt = 0;
		Faker faker = new Faker();
		long startTime = System.currentTimeMillis();

		@Override
		public void run() {

			Database db;
			Session sess;
			sess = Factory.getSession();
			db = sess.getDatabase("", FAKENAMES_NSF);

			String[] group = { "LocalDomainAdmins", "User", "Developer", "Staff" };
			Document doc;
			for (int i = 0; i < 10000; i++) {
				doc = db.createDocument();
				doc.replaceItemValue("Form", "DocName");

				String firstName;
				String lastName;
				String namePrefix;
				doc.replaceItemValue("NamePrefix", namePrefix = faker.name().prefix());
				doc.replaceItemValue("FirstName", firstName = faker.name().firstName());
				doc.replaceItemValue("LastName", lastName = faker.name().lastName());
				doc.replaceItemValue("FullName", join(new String[] { namePrefix, firstName, lastName }, " "));
				doc.replaceItemValue("NameSuffix", faker.name().suffix());

				doc.replaceItemValue("CityPrefix", faker.address().cityPrefix());
				doc.replaceItemValue("CitySuffix", faker.address().citySuffix());

				doc.replaceItemValue("ZipCode", faker.address().zipCode());
				doc.replaceItemValue("Country", faker.address().country());
				doc.replaceItemValue("Address", faker.address().streetAddress(true));

				DateTime birthday = sess.createDateTime(faker.integer(1900, 2010), faker.integer(1, 12), faker.integer(1, 31), 0, 0, 0);
				doc.replaceItemValue("Birthday", birthday);

				doc.replaceItemValue("PhoneNumber", faker.phoneNumber().phoneNumber());

				doc.replaceItemValue("CreditCardNumber", faker.business().creditCardNumber());
				doc.replaceItemValue("CreditCartExpiry", faker.business().creditCardExpiry());
				doc.replaceItemValue("CreditCartType", faker.business().creditCardType());
				Set<String> members = new HashSet<String>();
				int j;
				int grpCnt = faker.integer(0, 4);
				for (j = 0; j < grpCnt; j++) {
					members.add(faker.options().option(group));
				}
				doc.replaceItemValue("Member", members);
				doc.save();
				docCount++;

				if (docCount % 1000 == 0) {
					long runTime = System.currentTimeMillis() - startTime;
					if (runTime > 0) {
						System.out.println("Created " + docCount + " documents. " + (docCount * 1000 / runTime) + " docs/sec.");
					}
				}
			}
		}
	}
}
