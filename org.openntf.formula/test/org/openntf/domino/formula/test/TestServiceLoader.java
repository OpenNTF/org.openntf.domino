package org.openntf.domino.formula.test;

import java.util.List;

import org.openntf.domino.formula.ASTNode;
import org.openntf.domino.formula.EvaluateException;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.FormulaParseException;
import org.openntf.domino.formula.FormulaParser;
import org.openntf.domino.formula.Formulas;

public class TestServiceLoader {

	public static void main(final String[] args) throws FormulaParseException, EvaluateException {
		// TODO Auto-generated method stub
		FormulaParser p = Formulas.getParser();

		ASTNode x = p.parse("@Left(@text(3+4*1000);2)");
		FormulaContext ctx = Formulas.createContext(null, p);
		List<Object> ret = x.solve(ctx);
		System.out.println(ret);
	}
}
