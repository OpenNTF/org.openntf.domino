package de.foconis.test.runner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openntf.domino.utils.Factory;

@RunWith(FormulaTestSuite.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FormulaTestCaseAbstractLotus extends FormulaTestCaseAbstract {

	public FormulaTestCaseAbstractLotus(final TestParameter p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
		lotus.domino.NotesThread.sinitThread();
		setUp();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		tearDown();
		lotus.domino.NotesThread.stermThread();
	}

	//@Before
	public static void setUp() throws Exception {
		Factory.setClassLoader(Thread.currentThread().getContextClassLoader());

		//if (param.lotus.enabled || param.doc.enabled) {
		db = Factory.getSession().getDatabase("", "log.nsf");
		if (db == null) {
			throw new IllegalStateException("Cannot open log.nsf. Check if server is running");
		}
		//}
	}

	//@After
	public static void tearDown() throws Exception {
		Factory.terminate();
	}

}
