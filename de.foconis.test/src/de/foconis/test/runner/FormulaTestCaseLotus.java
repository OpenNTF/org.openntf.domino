package de.foconis.test.runner;

import lotus.domino.NotesException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(FormulaTestSuite.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FormulaTestCaseLotus extends FormulaTestCaseAbstractLotus {

	/**
	 * Constructor
	 * 
	 */
	public FormulaTestCaseLotus(final TestParameter p) {
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

}
