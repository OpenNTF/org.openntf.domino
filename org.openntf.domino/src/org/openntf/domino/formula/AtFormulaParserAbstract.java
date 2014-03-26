package org.openntf.domino.formula;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.openntf.domino.formula.ast.SimpleNode;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Terminatable;

public abstract class AtFormulaParserAbstract {
	protected Formatter formatter;
	protected AtFunctionFactory functionFactory;

	protected Map<String, AtFunction> customFunc;

	private static ThreadLocal<AtFormulaParserImpl> instance_ = new ThreadLocal<AtFormulaParserImpl>() {
		@Override
		protected AtFormulaParserImpl initialValue() {
			AtFormulaParserImpl parser = new AtFormulaParserImpl(new java.io.StringReader(""));
			parser.formatter = DominoFormatter.getInstance();
			parser.functionFactory = AtFunctionFactory.getInstance();
			return parser;
		}
	};

	static {
		Factory.onTerminate(new Terminatable() {
			public void terminate() {
				instance_.set(null);
			}
		});
	}

	//	public AtFormulaParser(final Formatter fmt, final AtFunctionFactory fact) {
	//		this(new java.io.StringReader("")); // needed for proper init :(
	//		formatter = fmt;
	//		functionFactory = fact;
	//	}

	public Formatter getFormatter() {
		return formatter;
	}

	/**
	 * This function returns a preconfigured default instance
	 */
	public static AtFormulaParserAbstract getInstance() {
		return instance_.get();
	}

	public AtFunction getFunction(final String string) {
		AtFunction func = functionFactory.getFunction(string);
		if (func != null) {
			return func;
		}
		return customFunc.get(string.toLowerCase());
	}

	public void declareFunction(final String funcName, final AtFunction func) {
		if (getFunction(funcName) != null) {
			throw new IllegalArgumentException("Function '" + funcName + "' cannot be redeclared");
		}
		customFunc.put(funcName.toLowerCase(), func);
	}

	final public SimpleNode Parse(final String formula) throws ParseException {
		java.io.StringReader sr = new java.io.StringReader(formula);
		ReInit(sr);
		// clear the customFunc-Map
		customFunc = new HashMap<String, AtFunction>();
		return Parse();
	}

	public abstract void ReInit(Reader sr);

	abstract public SimpleNode Parse() throws ParseException;
}
