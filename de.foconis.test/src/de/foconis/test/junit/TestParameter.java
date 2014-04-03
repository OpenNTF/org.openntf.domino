package de.foconis.test.junit;


class TestParameter {

	public TestMode lotus = TestMode.NONE;
	public TestMode doc = TestMode.NONE;
	public TestMode map = TestMode.NONE;
	public String formula;
	public String ifo;
	public String expect;

	public TestParameter(final String ifo, final String currentFormula) {
		// TODO Auto-generated constructor stub
		this.ifo = ifo;
		this.formula = currentFormula;

	}

}