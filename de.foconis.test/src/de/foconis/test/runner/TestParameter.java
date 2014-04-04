package de.foconis.test.runner;

public class TestParameter {

	public TestMode lotus = TestMode.NONE;
	public TestMode doc = TestMode.NONE;
	public TestMode map = TestMode.NONE;
	public String formula;
	public String ifo;
	public String expect;
	public double rndVal = Math.random();
	public int expectMin = 0;
	public int expectMax = Integer.MAX_VALUE;

	public TestParameter(final String ifo, final String currentFormula) {
		// TODO Auto-generated constructor stub
		this.ifo = ifo;
		this.formula = currentFormula;

	}

}