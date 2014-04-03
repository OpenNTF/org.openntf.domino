package de.foconis.test.junit;

import de.foconis.test.runner.FormulaFile;
import de.foconis.test.runner.FormulaTestCaseNotes;
import de.foconis.test.runner.TestParameter;

public class FormulaTestRoland extends FormulaTestCaseNotes {
	public FormulaTestRoland(final TestParameter p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@FormulaFile
	public static String getFormulaFile() {
		return "unittest\\common\\function_a.txt";
	}
}
