package de.foconis.test.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.openntf.domino.formula.impl.TextFunctions.atLeft;
import static org.openntf.domino.formula.impl.TextFunctions.atRight;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lotus.domino.EmbeddedObject;
import lotus.domino.NotesException;
import lotus.domino.RichTextItem;

import org.junit.FixMethodOrder;
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
public class FormulaTestCaseAbstract {

	protected String formula;
	protected TestParameter param;

	protected static Database db;

	/**
	 * Constructor
	 * 
	 */
	public FormulaTestCaseAbstract(final TestParameter p) {
		param = p;
		formula = p.formula;
	}

	protected void fillDemoDoc(final Map<String, Object> doc) {

		doc.put("rnd", new double[] { param.rndVal });

		doc.put("text1", "This is a test string");
		doc.put("text2", new String[] { "1", "2", "3" });

		doc.put("int1", new int[] { 1 });
		doc.put("int2", new int[] { 1, 2, 3 });
		Date d = new Date(79, 07, 17, 12, 0, 0);
		doc.put("birthday", d);
		Map<String, String> map = new HashMap<String, String>();
		map.put("K1", "v1");
		map.put("K2", "v2");
		doc.put("mime1", map);
		try {
			if (doc instanceof lotus.domino.Document) {
				lotus.domino.Document lotusDoc = (lotus.domino.Document) doc;
				RichTextItem rti = lotusDoc.createRichTextItem("body");
				rti.appendText("This is autoexec.bat:");
				rti.embedObject(EmbeddedObject.EMBED_ATTACHMENT, "", "c:\\autoexec.bat", null).recycle();
				rti.compact();
				rti.recycle();
			}
		} catch (NotesException ex) {

		}

	}

	// ========================== Lotus Tests

	//@Test
	public void testLotus() throws NotesException {
		String s = toString(lotus());
		System.out.println("LOTUS\t" + s);
		assertResult(s);

	}

	private String toString(final List<Object> inp) {
		if (inp == null)
			return "null";
		List<Object> ret = new ArrayList<Object>();
		for (Object o : inp) {
			if (o instanceof Number) {
				ret.add(((Number) o).doubleValue());
			} else if (o instanceof Boolean) {
				ret.add(((Boolean) o).booleanValue() ? 1.0 : 0.0);
			} else {
				ret.add(o);
			}

		}
		// TODO Auto-generated method stub
		return ret.toString();
	}

	@SuppressWarnings("unchecked")
	protected List<Object> lotus() throws NotesException {
		Document lotusDoc = db.createDocument();
		fillDemoDoc(lotusDoc);
		lotus.domino.Session rawSession = Factory.toLotus(Factory.getSession());
		lotus.domino.Document rawDocument = Factory.toLotus(lotusDoc);
		return rawSession.evaluate(formula, rawDocument);

	}

	@SuppressWarnings("unchecked")
	//@Test(expected = NotesException.class)
	public void testLotusFail() throws NotesException {

		Document lotusDoc = db.createDocument();
		lotus.domino.Session rawSession = Factory.toLotus(Factory.getSession());
		lotus.domino.Document rawDocument = Factory.toLotus(lotusDoc);
		try {
			rawSession.evaluate(formula, rawDocument);
			fail("Expected " + NotesException.class);
		} catch (NotesException e) {
			System.out.println("LOTUS\t" + e.toString());
			assertEquals("Could not evaluate formula", atLeft(atRight(e.toString(), ":"), ":").trim());
			throw e;
		}

	}

	protected void assertResult(final String s) {
		int l = s.length();
		if (param.expectMin <= l && l <= param.expectMax) {
			if (Strings.isBlankString(param.expect))
				return;
			assertEquals(param.expect, s);
		} else {
			fail("Got " + s + " (length:" + l + ") but expected length was " + param.expectMin
					+ (param.expectMax > param.expectMin ? (" to " + param.expectMax) : ""));
		}
	}

	// ========================== openntf-Doc Tests
	//@Test
	public void testDoc() throws NotesException, FormulaParseException, EvaluateException {
		String s = toString(doc());
		System.out.println("DOC\t" + s);

		assertResult(s);
	}

	protected List<Object> doc() throws NotesException, FormulaParseException, EvaluateException {
		Document ntfDoc = db.createDocument();
		fillDemoDoc(ntfDoc);
		ASTNode ast = null;

		ast = FormulaParser.getDefaultInstance().parse(formula);
		FormulaContext ctx1 = FormulaContext.createContext(ntfDoc, DominoFormatter.getDefaultInstance());
		return ast.solve(ctx1);
	}

	//@Test(expected = org.openntf.domino.formula.EvaluateException.class)
	public void testDocFail() throws NotesException, FormulaParseException, EvaluateException {
		Document ntfDoc = db.createDocument();
		ASTNode ast = null;

		ast = FormulaParser.getDefaultInstance().parse(formula);
		FormulaContext ctx1 = FormulaContext.createContext(ntfDoc, DominoFormatter.getDefaultInstance());
		ast.solve(ctx1);

	}

	// ========================== openntf-map Tests
	//@Test
	public void testMap() throws FormulaParseException, EvaluateException {
		String s = toString(map());
		System.out.println("MAP\t" + s);
		assertResult(s);

	}

	protected List<Object> map() throws FormulaParseException, EvaluateException {
		Map<String, Object> ntfMap = new HashMap<String, Object>();
		fillDemoDoc(ntfMap);
		ASTNode ast = null;
		ast = FormulaParser.getDefaultInstance().parse(formula);
		FormulaContext ctx1 = FormulaContext.createContext(ntfMap, DominoFormatter.getDefaultInstance());
		return ast.solve(ctx1);
	}

	//@Test(expected = org.openntf.domino.formula.EvaluateException.class)
	public void testMapFail() throws FormulaParseException, EvaluateException {
		Map<String, Object> ntfMap = new HashMap<String, Object>();
		ASTNode ast = null;
		ast = FormulaParser.getDefaultInstance().parse(formula);
		FormulaContext ctx1 = FormulaContext.createContext(ntfMap, DominoFormatter.getDefaultInstance());
		ast.solve(ctx1);
	}

	// ============ compare-tests
	//@Test
	public void compareLotusDoc() throws NotesException, FormulaParseException, EvaluateException {
		List<Object> a = lotus();
		List<Object> b = doc();
		assertTrue(compareList(a, b));
	}

	//@Test
	public void compareLotusMap() throws NotesException, FormulaParseException, EvaluateException {
		List<Object> a = lotus();
		List<Object> b = map();
		assertTrue(compareList(a, b));
	}

	//@Test
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
						assertEquals(dt1.toJavaDate(), dt2.toJavaDate());
					} catch (NotesException e) {
						// TODO Auto-generated catch block
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
