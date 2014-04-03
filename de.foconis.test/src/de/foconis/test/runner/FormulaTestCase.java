package de.foconis.test.runner;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openntf.domino.formula.ASTNode;
import org.openntf.domino.formula.DominoFormatter;
import org.openntf.domino.formula.EvaluateException;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.FormulaParseException;
import org.openntf.domino.formula.FormulaParser;
import org.openntf.domino.utils.Strings;

@RunWith(FormulaTestSuite.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FormulaTestCase {

	protected String formula;
	protected TestParameter param;

	/**
	 * Constructor
	 * 
	 */
	public FormulaTestCase(final TestParameter p) {
		param = p;
		formula = p.formula;
	}

	@Test
	public void testMap() throws FormulaParseException, EvaluateException {
		String s = "" + map();
		System.out.println("MAP\t" + s);
		if (Strings.isBlankString(param.expect))
			return;
		assertEquals(param.expect, s);
	}

	//	@FormulaFile
	//	public static String getFormulaFile() {
	//		return "W:\\Daten\\Entwicklung\\UnitTest\\FormulaEngine\\";
	//	}

	protected List<Object> map() throws FormulaParseException, EvaluateException {
		Map<String, Object> ntfMap = new HashMap<String, Object>();
		fillDemoDoc(ntfMap);
		ASTNode ast = null;
		ast = FormulaParser.getDefaultInstance().parse(formula);
		FormulaContext ctx1 = FormulaContext.createContext(ntfMap, DominoFormatter.getDefaultInstance());
		return ast.solve(ctx1);
	}

	@Test(expected = org.openntf.domino.formula.EvaluateException.class)
	public void testMapFail() throws FormulaParseException, EvaluateException {
		Map<String, Object> ntfMap = new HashMap<String, Object>();
		ASTNode ast = null;
		ast = FormulaParser.getDefaultInstance().parse(formula);
		FormulaContext ctx1 = FormulaContext.createContext(ntfMap, DominoFormatter.getDefaultInstance());
		ast.solve(ctx1);
	}

	protected void fillDemoDoc(final Map<String, Object> doc) {

		doc.put("rnd", new double[] { param.rndVal });

		doc.put("text1", "This is a test string");
		doc.put("text2", new String[] { "1", "2", "3" });

		doc.put("int1", new int[] { 1 });
		doc.put("int2", new int[] { 1, 2, 3 });
		Map<String, String> map = new HashMap<String, String>();
		map.put("K1", "v1");
		map.put("K2", "v2");
		doc.put("mime1", map);

	}

}
