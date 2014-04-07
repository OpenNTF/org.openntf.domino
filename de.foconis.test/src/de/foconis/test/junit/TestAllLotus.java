package de.foconis.test.junit;

import de.foconis.test.runner.FormulaFile;
import de.foconis.test.runner.FormulaTestCaseLotus;
import de.foconis.test.runner.TestParameter;

public class TestAllLotus extends FormulaTestCaseLotus {
	public TestAllLotus(final TestParameter p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@FormulaFile
	public static String getFormulaFile() {
		return "unittest\\common\\";
	}

}
