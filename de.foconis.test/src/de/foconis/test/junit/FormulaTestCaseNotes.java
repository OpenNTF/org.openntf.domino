package de.foconis.test.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import lotus.domino.NotesException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.formula.ASTNode;
import org.openntf.domino.formula.DominoFormatter;
import org.openntf.domino.formula.EvaluateException;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.FormulaParseException;
import org.openntf.domino.formula.FormulaParser;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Strings;

@RunWith(FormulaTestSuite.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FormulaTestCaseNotes extends FormulaTestCase {

	private Database db;

	/**
	 * Constructor
	 * 
	 */
	public FormulaTestCaseNotes(final TestParameter p) {
		super(p);
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
		lotus.domino.NotesThread.sinitThread();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		lotus.domino.NotesThread.stermThread();
	}

	@Before
	public void setUp() throws Exception {
		Factory.setClassLoader(Thread.currentThread().getContextClassLoader());

		if (param.lotus.enabled || param.doc.enabled) {
			db = Factory.getSession().getDatabase("", "log.nsf");
			if (db == null) {
				throw new IllegalStateException("Cannot open log.nsf. Check if server is running");
			}
		}
	}

	@After
	public void tearDown() throws Exception {
		Factory.terminate();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testLotus() throws NotesException {
		String s = "" + lotus();
		if (Strings.isBlankString(param.expect))
			return;
		assertEquals(param.expect, s);

	}

	protected List<Object> lotus() throws NotesException {
		Document lotusDoc = db.createDocument();
		lotus.domino.Session rawSession = Factory.toLotus(Factory.getSession());
		lotus.domino.Document rawDocument = Factory.toLotus(lotusDoc);
		return rawSession.evaluate(formula, rawDocument);

	}

	@Test
	public void testDoc() throws NotesException, FormulaParseException, EvaluateException {
		String s = "" + doc();
		if (Strings.isBlankString(param.expect))
			return;
		assertEquals(param.expect, s);
	}

	protected List<Object> doc() throws NotesException, FormulaParseException, EvaluateException {
		Document ntfDoc = db.createDocument();
		ASTNode ast = null;

		ast = FormulaParser.getDefaultInstance().parse(formula);
		FormulaContext ctx1 = new FormulaContext(ntfDoc, DominoFormatter.getDefaultInstance());
		return ast.solve(ctx1);

	}

	@SuppressWarnings("unchecked")
	@Test(expected = NotesException.class)
	public void testLotusFail() throws NotesException {

		Document lotusDoc = db.createDocument();
		lotus.domino.Session rawSession = Factory.toLotus(Factory.getSession());
		lotus.domino.Document rawDocument = Factory.toLotus(lotusDoc);
		rawSession.evaluate(formula, rawDocument);

	}

	@Test(expected = org.openntf.domino.formula.EvaluateException.class)
	public void testDocFail() throws NotesException, FormulaParseException, EvaluateException {
		Document ntfDoc = db.createDocument();
		ASTNode ast = null;

		ast = FormulaParser.getDefaultInstance().parse(formula);
		FormulaContext ctx1 = new FormulaContext(ntfDoc, DominoFormatter.getDefaultInstance());
		ast.solve(ctx1);

	}

	@Test
	public void compareLotusDoc() throws NotesException, FormulaParseException, EvaluateException {
		List<Object> a = lotus();
		List<Object> b = doc();
		assertTrue(compareList(a, b));
	}

	@Test
	public void compareLotusMap() throws NotesException, FormulaParseException, EvaluateException {
		List<Object> a = lotus();
		List<Object> b = map();
		assertTrue(compareList(a, b));
	}

	@Test
	public void compareDocMap() throws NotesException, FormulaParseException, EvaluateException {
		List<Object> a = doc();
		List<Object> b = map();
		assertTrue(compareList(a, b));
	}

	private boolean compareList(final List<Object> list1, final List<Object> list2) {
		if (list1 == null && list2 == null)
			return true;
		if (list1 == null || list2 == null) {
			assertEquals(list1, list2);
			return false;
		}

		if (list1.size() == 0 && list2.size() == 1) {
			if ("".equals(list2.get(0)))
				return true;
		}
		if (list2.size() == 0 && list1.size() == 1) {
			if ("".equals(list1.get(0)))
				return true;
		}

		if (list1.size() == list2.size()) {
			for (int i = 0; i < list1.size(); i++) {
				Object a = list1.get(i);
				Object b = list2.get(i);
				if (a == null && b == null) {

				} else if (a == null || b == null) {
					assertEquals(a, b);
					return false;
				} else if (a instanceof Boolean && b instanceof Number) {
					if ((Boolean) a) {
						//if (Double.compare(1.0, ((Number) b).doubleValue()) != 0) {
						assertEquals(1.0, ((Number) b).doubleValue(), 0.0000000001);
						//	return false;
						//}
					} else {
						//if (Double.compare(0.0, ((Number) b).doubleValue()) != 0) {
						assertEquals(0.0, ((Number) b).doubleValue(), 0.0000000001);
						//	return false;
						//}
					}
				} else if (a instanceof Number && b instanceof Number) {
					//if (Double.compare(((Number) a).doubleValue(), ((Number) b).doubleValue()) != 0) {
					assertEquals(((Number) a).doubleValue(), ((Number) b).doubleValue(), 0.0000000001);
					//	return false;
					//}

				} else if (a instanceof lotus.domino.DateTime && b instanceof lotus.domino.DateTime) {
					lotus.domino.DateTime dt1 = (lotus.domino.DateTime) a;
					lotus.domino.DateTime dt2 = (lotus.domino.DateTime) b;
					try {
						if (dt1.timeDifference(dt2) != 0) {
							// TODO: Do we need a delta here?
							assertEquals(dt1.toJavaDate(), dt2.toJavaDate());
							return false;
						}
					} catch (NotesException e) {
						e.printStackTrace();
						return false;
					}

				} else if (!a.equals(b)) {
					assertEquals(a, b);
					return false;
				}
			}
		} else {
			assertEquals(list1, list2);
			return false;
		}
		return true;
	}
}
