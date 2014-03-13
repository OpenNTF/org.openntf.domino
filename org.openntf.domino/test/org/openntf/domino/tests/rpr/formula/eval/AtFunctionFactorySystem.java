package org.openntf.domino.tests.rpr.formula.eval;

import java.util.ArrayList;
import java.util.List;

public class AtFunctionFactorySystem implements AtFunctionFactory {
	private List<AtFunctionFactory> factories = new ArrayList<AtFunctionFactory>();

	public AtFunction getFunction(final String funcName) {
		for (AtFunctionFactory factory : factories) {
			AtFunction atFunction = factory.getFunction(funcName);
			if (atFunction != null) {
				return atFunction;
			}
		}
		return null;
	}

	public void add(final AtFunctionFactory fact) {
		factories.add(fact);
	}
}
