package de.foconis.test.junit;

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
		return "unittest\\common\\function_t.txt";
	}

}
