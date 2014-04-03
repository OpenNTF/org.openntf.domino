package de.foconis.test.runner;

public class TestParameter {

	public TestMode lotus = TestMode.NONE;
	public TestMode doc = TestMode.NONE;
	public TestMode map = TestMode.NONE;
	public String formula;
	public String ifo;
	public String expect;
	public double rndVal = Math.random();

	public TestParameter(final String ifo, final String currentFormula) {
		// TODO Auto-generated constructor stub
		this.ifo = ifo;
		this.formula = currentFormula;

	}

}