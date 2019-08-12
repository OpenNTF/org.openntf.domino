package org.openntf.domino.tests.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.Test;
import org.openntf.domino.Database;
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
}
