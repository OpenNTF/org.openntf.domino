package org.openntf.domino.xsp.tests;

import lotus.notes.NotesThread;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openntf.domino.utils.Factory;

public abstract class XspJUnit {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Factory.startup();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Factory.shutdown();
	}

	@Before
	public void setUp() throws Exception {
		NotesThread.sinitThread();
		Factory.init();
	}

	@After
	public void tearDown() throws Exception {
		Factory.terminate();
		NotesThread.stermThread();
	}

}
