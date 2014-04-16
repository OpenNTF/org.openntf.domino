package de.foconis.test.runner;

import lotus.domino.NotesException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openntf.domino.formula.EvaluateException;
import org.openntf.domino.formula.FormulaParseException;

@RunWith(FormulaTestSuite.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FormulaTestCaseAll extends FormulaTestCaseAbstractLotus {

	;

	/**
	 * Constructor
	 * 
	 */
	public FormulaTestCaseAll(final TestParameter p) {
		super(p);
	}

	@Override
	@Test
	public void testLotus() throws NotesException {
		super.testLotus();
	}

	@Override
	@Test(expected = NotesException.class)
	public void testLotusFail() throws NotesException {
		super.testLotusFail();
	}

	@Override
	@Test
	public void testDoc() throws NotesException, FormulaParseException, EvaluateException {
		super.testDoc();
	}

	@Override
	@Test(expected = org.openntf.domino.formula.EvaluateException.class)
	public void testDocFail() throws NotesException, FormulaParseException, EvaluateException {
		super.testDocFail();
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

	@Override
	@Test
	public void compareLotusDoc() throws NotesException, FormulaParseException, EvaluateException {
		super.compareLotusDoc();
	}

	@Override
	@Test
	public void compareLotusMap() throws NotesException, FormulaParseException, EvaluateException {
		super.compareLotusMap();
	}

	@Override
	@Test
	public void compareDocMap() throws NotesException, FormulaParseException, EvaluateException {
		super.compareDocMap();
	}

}
