/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.tests;

import static com.ibm.commons.util.StringUtil.format;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openntf.domino.Session;
import org.openntf.domino.tests.general.TestDatabase;
import org.openntf.domino.tests.general.TestDocuments;
import org.openntf.domino.tests.general.TestFactory;
import org.openntf.domino.tests.general.TestName;
import org.openntf.domino.tests.wrapper.TestDatabasePropertyWrapper;
import org.openntf.domino.tests.wrapper.TestWrapperFactory;
import org.openntf.domino.utils.Factory;

import lotus.domino.NotesFactory;
import lotus.domino.NotesThread;


@RunWith(Suite.class)
@Suite.SuiteClasses({
		TestWrapperFactory.class,
		TestDatabase.class,
		TestDatabasePropertyWrapper.class,
		TestName.class,
		TestFactory.class,
		TestDocuments.class
})
public class AllTests {
	public static lotus.domino.Session lotusSession;
	public static Session session; // Set by TestWrapperFactory
	
	public static String EMPTY_DB;
	public static String EMPTY_DB_COPY; // Set by TestDatabase;

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
		deleteDb(EMPTY_DB_COPY);

		System.out.println("Term " + AllTests.class.getName());
	}

	/* ******************************************************************************
	 * Internal utility methods
	 ********************************************************************************/

	public static String instantiateDb(final String basename) throws IOException {
		Path dbFile = Files.createTempFile(basename, ".nsf"); //$NON-NLS-1$
		System.out.println(format("{0} location is {1}", basename, dbFile.toString())); //$NON-NLS-1$
		try(InputStream is = AllTests.class.getResourceAsStream("/" + basename + ".nsf")) { //$NON-NLS-1$ //$NON-NLS-2$
			Files.copy(is, dbFile, StandardCopyOption.REPLACE_EXISTING);
		}
		return dbFile.toString();
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
