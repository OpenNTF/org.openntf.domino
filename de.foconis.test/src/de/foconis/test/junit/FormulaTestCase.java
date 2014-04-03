package de.foconis.test.junit;

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
		if (Strings.isBlankString(param.expect))
			return;
		assertEquals(param.expect, s);
	}

	protected List<Object> map() throws FormulaParseException, EvaluateException {
		Map<String, Object> ntfMap = new HashMap<String, Object>();
		ASTNode ast = null;
		ast = FormulaParser.getDefaultInstance().parse(formula);
		FormulaContext ctx1 = new FormulaContext(ntfMap, DominoFormatter.getDefaultInstance());
		return ast.solve(ctx1);
	}

	@Test(expected = org.openntf.domino.formula.EvaluateException.class)
	public void testMapFail() throws FormulaParseException, EvaluateException {
		Map<String, Object> ntfMap = new HashMap<String, Object>();
		ASTNode ast = null;
		ast = FormulaParser.getDefaultInstance().parse(formula);
		FormulaContext ctx1 = new FormulaContext(ntfMap, DominoFormatter.getDefaultInstance());
		ast.solve(ctx1);
	}

}
