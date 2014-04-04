package de.foconis.test.runner;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openntf.domino.formula.EvaluateException;
import org.openntf.domino.formula.FormulaParseException;

@RunWith(FormulaTestSuite.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FormulaTestCaseMap extends FormulaTestCaseAbstract {

	/**
	 * Constructor
	 * 
	 */
	public FormulaTestCaseMap(final TestParameter p) {
		super(p);
	}

	@Override
	@Test
	public void testMap() throws FormulaParseException, EvaluateException {
		super.testMap();
	}

	@Override
	@Test(expected = org.openntf.domino.formula.EvaluateException.class)
	public void testMapFail() throws FormulaParseException, EvaluateException {
		super.testMapFail();
	}

}
