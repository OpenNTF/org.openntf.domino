package org.openntf.formula.test;

import java.util.List;

import org.openntf.formula.ASTNode;
import org.openntf.formula.EvaluateException;
import org.openntf.formula.FormulaContext;
import org.openntf.formula.FormulaParseException;
import org.openntf.formula.FormulaParser;
import org.openntf.formula.FormulaProvider;
import org.openntf.formula.Formulas;

public class TestServiceLoader {

	public static void main(final String[] args) throws FormulaParseException, EvaluateException {

		final FormulaParser p = Formulas.getParser();

		p.setIncludeProvider(new FormulaProvider<ASTNode>() {

			public ASTNode get(final String key) {
				try {
					if ("demofunc".equals(key)) {
						return p.parse("@listSupportedFunctions");
					}

					if ("ackermann".equals(key)) {
						return p.parse("@Function(@Ack(n;m)) := @if( n = 0; m+1; m=0; @Ack(n-1;1); @Ack(n-1;@Ack(n;m-1))); @Ack(1;1)");
					}
				} catch (FormulaParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// TODO Auto-generated method stub
				return null;
			}

		});
		ASTNode x = p.parse(System.in, true);
		FormulaContext ctx = Formulas.createContext(null, p);

		ctx.setParameterProvider(new FormulaProvider<String>() {

			public String get(final String key) {
				return args[Integer.valueOf(key)];
			}
		});

		List<Object> ret = x.solve(ctx);
		System.out.println(ret);
	}
}
