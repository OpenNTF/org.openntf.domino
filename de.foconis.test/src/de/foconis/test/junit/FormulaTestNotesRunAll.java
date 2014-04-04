package de.foconis.test.junit;

import de.foconis.test.runner.FormulaFile;
import de.foconis.test.runner.FormulaTestCaseAll;
import de.foconis.test.runner.TestParameter;

public class FormulaTestNotesRunAll extends FormulaTestCaseAll {
	public FormulaTestNotesRunAll(final TestParameter p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@FormulaFile
	public static String getFormulaFile() {
		return "unittest\\";
	}
}
