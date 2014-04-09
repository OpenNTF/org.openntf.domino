package org.openntf.domino.formula.test;

import java.util.List;

import org.openntf.domino.formula.ASTNode;
import org.openntf.domino.formula.EvaluateException;
import org.openntf.domino.formula.Factory;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.FormulaParseException;
import org.openntf.domino.formula.FormulaParser;

public class TestServiceLoader {

	public static void main(final String[] args) throws FormulaParseException, EvaluateException {
		// TODO Auto-generated method stub
		FormulaParser p = Factory.getParser(Thread.currentThread().getContextClassLoader());

		ASTNode x = p.parse("3+4");
		FormulaContext ctx = Factory.createContext(null, p);
		List<Object> ret = x.solve(ctx);
		System.out.println(ret);
	}
}
