package org.openntf.domino.xsp.tests;

import lotus.domino.NotesException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openntf.domino.utils.Factory;

import com.ibm.domino.xsp.module.nsf.NotesContext;

public abstract class XspJUnitEx {

	protected static lotus.domino.Session masterSession;
	protected static NotesContext ctx;

	protected static String getCurrentId() {
		try {
			return masterSession.getUserName();
		} catch (NotesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Factory.startup();
		ctx = new NotesContext(null);
		NotesContext.initThread(ctx);
		masterSession = lotus.domino.local.Session.createSession();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		masterSession.recycle();
		NotesContext.termThread();
		Factory.shutdown();
	}

	@Before
	public void setUp() throws Exception {
		Factory.init();
	}

	@After
	public void tearDown() throws Exception {
		Factory.terminate();
	}

}
