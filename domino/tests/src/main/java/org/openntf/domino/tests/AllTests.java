package org.openntf.domino.tests;

import static com.ibm.commons.util.StringUtil.format;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openntf.domino.Session;
import org.openntf.domino.tests.wrapper.TestDatabasePropertyWrapper;
import org.openntf.domino.tests.wrapper.TestWrapperFactory;
import org.openntf.domino.utils.Factory;

import com.ibm.commons.util.io.StreamUtil;

import lotus.domino.NotesFactory;
import lotus.domino.NotesThread;


@RunWith(Suite.class)
@Suite.SuiteClasses({
		TestWrapperFactory.class, TestDatabasePropertyWrapper.class
})
public class AllTests {
	public static lotus.domino.Session lotusSession;
	public static Session session; // Set by TestWrapperFactory
	
	public static String EMPTY_DB;

	@BeforeClass
	public static void init() throws Exception {
		System.out.println("Init " + AllTests.class.getName());

		NotesThread.sinitThread();
		lotusSession = NotesFactory.createSession();
		
		Factory.startup();
		Factory.initThread(null);
		
		EMPTY_DB = instantiateDb("empty");
	}

	@AfterClass
	public static void term() throws Exception {
		Factory.termThread();
		Factory.shutdown();
		lotusSession.recycle();
		NotesThread.stermThread();
		
		deleteDb(EMPTY_DB);

		System.out.println("Term " + AllTests.class.getName());
	}

	/* ******************************************************************************
	 * Internal utility methods
	 ********************************************************************************/

	public static String instantiateDb(final String basename) throws IOException {
		File dbFile = File.createTempFile(basename, ".nsf"); //$NON-NLS-1$
		System.out.println(format("{0} location is {1}", basename, dbFile.getAbsolutePath())); //$NON-NLS-1$
		FileOutputStream fos = new FileOutputStream(dbFile);
		InputStream is = AllTests.class.getResourceAsStream("/" + basename + ".nsf"); //$NON-NLS-1$ //$NON-NLS-2$
		StreamUtil.copyStream(is, fos);
		fos.flush();
		StreamUtil.close(fos);
		StreamUtil.close(is);
		return dbFile.getAbsolutePath();
	}

	public static void deleteDb(final String dbName) {
		try {
			File testDB = new File(dbName);
			if(testDB.exists()) {
				testDB.delete();
				System.out.println(format("{0} deleted", dbName)); //$NON-NLS-1$
			}
		} catch(Exception e) { }
	}
}
