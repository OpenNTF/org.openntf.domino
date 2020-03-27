/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
package org.openntf.domino.tests.wrapper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.design.DatabaseDesign.DbProperties;
import org.openntf.domino.exceptions.OpenNTFNotesException;
import org.openntf.domino.tests.AllTests;
import org.openntf.domino.utils.Factory;

import lotus.domino.NotesFactory;

public class TestDatabasePropertyWrapper {

	public TestDatabasePropertyWrapper() {
	}

	@BeforeClass
	public static void init() throws Exception {

	}

	@Test
	public void testDatabaseProperties() {
		testProperty(DbProperties.USE_JS);
		testProperty(DbProperties.REQUIRE_SSL);
		testProperty(DbProperties.NO_URL_OPEN);
		testProperty(DbProperties.DISABLE_BACKGROUND_AGENTS);
		testProperty(DbProperties.ALLOW_STORED_FORMS);
		testProperty(DbProperties.DEFER_IMAGE_LOADING);
		testProperty(DbProperties.ALLOW_DOC_LOCKING);
		testProperty(DbProperties.INHERIT_OS_THEME);
		testProperty(DbProperties.ALLOW_DESIGN_LOCKING);
		testProperty(DbProperties.SHOW_IN_OPEN_DIALOG);
		testProperty(DbProperties.MULTI_DB_INDEXING);
		testProperty(DbProperties.MODIFIED_NOT_UNREAD);
		testProperty(DbProperties.MARK_PARENT_REPLY_FORWARD);
		testProperty(DbProperties.MULTILINGUAL);
		testProperty(DbProperties.DONT_MAINTAIN_UNREAD);
		testProperty(DbProperties.OPTIMIZE_DOC_MAP);
		testProperty(DbProperties.MAINTAIN_LAST_ACCESSED);
		testProperty(DbProperties.MULTI_DB_INDEXING);
		testProperty(DbProperties.DISABLE_TRANSACTION_LOGGING);
		testProperty(DbProperties.NO_SPECIAL_RESPONSE_HIERARCHY);
		testProperty(DbProperties.USE_LZ1);
		testProperty(DbProperties.NO_HEADLINE_MONITORING);
		testProperty(DbProperties.ALLOW_MORE_FIELDS);
		testProperty(DbProperties.SUPPORT_RESPONSE_THREADS);
		testProperty(DbProperties.NO_SIMPLE_SEARCH);
		testProperty(DbProperties.COMPRESS_DESIGN);
		testProperty(DbProperties.COMPRESS_DATA);
		testProperty(DbProperties.NO_AUTO_VIEW_UPDATE);
		testProperty(DbProperties.ALLOW_SOFT_DELETE);
	}

	public DatabaseDesign getNewSessionDesign() {
		try {
			lotus.domino.Session newSess = NotesFactory.createSession();
			Session sess = Factory.getWrapperFactory().fromLotus(AllTests.lotusSession, Session.SCHEMA, null);
			Database db = sess.getDatabase("", AllTests.EMPTY_DB);
			return db.getDesign();
		} catch (Exception e) {
			return null;
		}
	}

	public void testProperty(DbProperties prop) {
		HashMap<DbProperties, Boolean> props = new HashMap<DbProperties, Boolean>();
		props.put(prop, true);
		DatabaseDesign design = getNewSessionDesign();
		design.setDatabaseProperties(props);
		design.save();

		design = getNewSessionDesign();
		ArrayList<DbProperties> retProps = (ArrayList<DbProperties>) design.getDatabaseProperties();
		assertTrue(retProps.contains(prop));

		design = getNewSessionDesign();
		props.put(prop, false);
		design.setDatabaseProperties(props);
		design.save();

		design = getNewSessionDesign();
		retProps = (ArrayList<DbProperties>) design.getDatabaseProperties();
		assertFalse(retProps.contains(prop));
	}

	@Test(expected = OpenNTFNotesException.class)
	public void testDaosError() {
		HashMap<DbProperties, Boolean> invalidProps = new HashMap<DbProperties, Boolean>();
		invalidProps.put(DbProperties.DAOS_ENABLED, true);
		getNewSessionDesign().setDatabaseProperties(invalidProps);
	}

	@Test(expected = OpenNTFNotesException.class)
	public void testDocSummaryError() {
		HashMap<DbProperties, Boolean> invalidProps = new HashMap<DbProperties, Boolean>();
		invalidProps.put(DbProperties.DOCUMENT_SUMMARY_16MB, true);
		getNewSessionDesign().setDatabaseProperties(invalidProps);
	}

	@Test(expected = OpenNTFNotesException.class)
	public void testAllowDasError() {
		HashMap<DbProperties, Boolean> invalidProps = new HashMap<DbProperties, Boolean>();
		invalidProps.put(DbProperties.ALLOW_DAS, true);
		getNewSessionDesign().setDatabaseProperties(invalidProps);
	}

	@Test(expected = OpenNTFNotesException.class)
	public void testDbTemplateError() {
		HashMap<DbProperties, Boolean> invalidProps = new HashMap<DbProperties, Boolean>();
		invalidProps.put(DbProperties.DB_IS_TEMPLATE, true);
		getNewSessionDesign().setDatabaseProperties(invalidProps);
	}

	@Test(expected = OpenNTFNotesException.class)
	public void testInheritError() {
		HashMap<DbProperties, Boolean> invalidProps = new HashMap<DbProperties, Boolean>();
		invalidProps.put(DbProperties.INHERIT_FROM_TEMPLATE, true);
		getNewSessionDesign().setDatabaseProperties(invalidProps);
	}

	@Test(expected = OpenNTFNotesException.class)
	public void testReplicateError() {
		HashMap<DbProperties, Boolean> invalidProps = new HashMap<DbProperties, Boolean>();
		invalidProps.put(DbProperties.REPLICATE_UNREAD, true);
		getNewSessionDesign().setDatabaseProperties(invalidProps);
	}

	@Test(expected = OpenNTFNotesException.class)
	public void testMaxRevError() {
		HashMap<DbProperties, Boolean> invalidProps = new HashMap<DbProperties, Boolean>();
		invalidProps.put(DbProperties.MAX_REVISIONS, true);
		getNewSessionDesign().setDatabaseProperties(invalidProps);
	}

	@Test(expected = OpenNTFNotesException.class)
	public void testMaxUpdatedError() {
		HashMap<DbProperties, Boolean> invalidProps = new HashMap<DbProperties, Boolean>();
		invalidProps.put(DbProperties.MAX_UPDATED_BY, true);
		getNewSessionDesign().setDatabaseProperties(invalidProps);
	}

	@Test(expected = OpenNTFNotesException.class)
	public void testSoftDeleteExpipryError() {
		HashMap<DbProperties, Boolean> invalidProps = new HashMap<DbProperties, Boolean>();
		invalidProps.put(DbProperties.SOFT_DELETE_EXPIRY, true);
		getNewSessionDesign().setDatabaseProperties(invalidProps);
	}

}
