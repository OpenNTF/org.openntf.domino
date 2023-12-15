/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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
package org.openntf.domino.tests.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Vector;

import org.junit.Test;
import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
import org.openntf.domino.tests.AllTests;
import org.openntf.domino.utils.Factory;

public class TestDocuments {
	/**
	 * Tests for https://github.com/OpenNTF/org.openntf.domino/issues/62
	 */
	@Test
	public void testReplaceItemValueMulti() {
		Session session = Factory.getSession();
		Database database = session.getDatabase(AllTests.EMPTY_DB);
		Document doc = database.createDocument();
		Vector<Object> md = new Vector<>();
		md.add(session.createDateTime(2010, 1, 1, 12, 0, 0));
		md.add(session.createDateTime(2010, 2, 1, 12, 0, 0));
		md.add(session.createDateTime(2010, 3, 1, 12, 0, 0));
        doc.replaceItemValue("fldDate2", md);
        assertEquals(md, doc.getItemValue("fldDate2"));
	}
	
	/**
	 * Tests for https://github.com/OpenNTF/org.openntf.domino/issues/55
	 */
	@Test
	public void testDocumentCollectionParent() {
		Session session = Factory.getSession();
		Database database = session.getDatabase(AllTests.EMPTY_DB);
		
		{
			DocumentCollection docs = database.getAllDocuments();
			assertTrue("docs parent should be a database", docs.getParent() instanceof Database);
		}
		{
			Document doc = database.createDocument();
			doc.save();
			Document child = database.createDocument();
			child.makeResponse(doc);
			child.save();
			
			DocumentCollection children = doc.getResponses();
			assertTrue("children parent should be a database", children.getParent() instanceof Database);
		}
	}
	
	/**
	 * Tests for https://github.com/OpenNTF/org.openntf.domino/issues/152
	 */
	@Test
	public void testCopyToDatabase() {
		Session session = Factory.getSession();
		Database source = session.getDatabase(AllTests.EMPTY_DB);
		Document sourceDoc = source.createDocument();
		sourceDoc.put("Foo", "Bar");
		sourceDoc.save();
		
		Database dest = session.getDatabase(AllTests.EMPTY_DB_COPY);
		Document destDoc = sourceDoc.copyToDatabase(dest);
		assertEquals("Foo should match", "Bar", destDoc.getItemValueString("Foo"));
		assertNotEquals("Dest Doc parent replica ID should not match source DB", source.getReplicaID(), destDoc.getParentDatabase().getReplicaID());
		assertEquals("Dest Doc parent replica ID should match expected", dest.getReplicaID(), destDoc.getParentDatabase().getReplicaID());
	}
	
	@Test
	public void testLocalDate() {
		Session session = Factory.getSession();
		Database source = session.getDatabase(AllTests.EMPTY_DB);
		Document sourceDoc = source.createDocument();
		LocalDate expected = LocalDate.now();
		sourceDoc.put("Foo", expected);
		assertEquals(expected, sourceDoc.getItemValue("Foo", LocalDate.class));
		
		DateTime dt = sourceDoc.getItemValue("Foo", DateTime.class);
		long expectedTenSec = expected.toEpochSecond(LocalTime.now(), ZoneId.systemDefault().getRules().getOffset(Instant.now())) / 100;
		long dtTenSec = dt.toJavaDate().getTime() / 1000 / 100;
		assertEquals(expectedTenSec, dtTenSec);
	}
	
	@Test
	public void testLocalTime() {
		Session session = Factory.getSession();
		session.setTrackMillisecInJavaDates(true);
		Database source = session.getDatabase(AllTests.EMPTY_DB);
		Document sourceDoc = source.createDocument();
		// Domino only supports hundredths of a second, so only test for that
		LocalTime expected = LocalTime.now().withNano(33 * 10 * 1000 * 1000);
		sourceDoc.put("Foo", expected);
		assertEquals(expected, sourceDoc.getItemValue("Foo", LocalTime.class));
		
		DateTime dt = sourceDoc.getItemValue("Foo", DateTime.class);
		long expectedTenSec = expected.toEpochSecond(LocalDate.now(), ZoneId.systemDefault().getRules().getOffset(Instant.now())) / 100;
		long dtTenSec = dt.toJavaDate().getTime() / 1000 / 100;
		assertEquals(expectedTenSec, dtTenSec);
	}
}
