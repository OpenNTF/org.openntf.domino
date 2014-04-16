package de.foconis.test.junit;

import lotus.domino.NotesException;

import org.junit.Test;
import org.openntf.domino.formula.EvaluateException;
import org.openntf.domino.formula.FormulaParseException;

import de.foconis.test.runner.FormulaFile;
import de.foconis.test.runner.FormulaTestCaseLotus;
import de.foconis.test.runner.TestParameter;

public class FormulaTestRoland extends FormulaTestCaseLotus {
	public FormulaTestRoland(final TestParameter p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@FormulaFile
	public static String getFormulaFile() {
		return "unittest\\common\\function_a.txt";
	}

	@Override
	@Test
	public void testDoc() throws NotesException, FormulaParseException, EvaluateException {
		// TODO Auto-generated method stub
		super.testDoc();
	}

	@Override
	@Test(expected = org.openntf.domino.formula.EvaluateException.class)
	public void testDocFail() throws NotesException, FormulaParseException, EvaluateException {
		super.testDocFail();
	}

}
