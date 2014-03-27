package org.openntf.domino.formula;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.openntf.domino.formula.ast.SimpleNode;
import org.openntf.domino.formula.impl.ExtendedFunction;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Terminatable;

public abstract class AtFormulaParser {
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
	public static AtFormulaParser getInstance() {
		return instance_.get();
	}

	public AtFunction getFunction(String funcName) {
		funcName = funcName.toLowerCase();
		AtFunction func = customFunc.get(funcName);
		if (func != null) {
			return func;
		}
		return functionFactory.getFunction(funcName);
	}

	public void declareFunction(final ExtendedFunction func) {
		String funcName = func.getName();
		//		AtFunction currentFunc = getFunction(funcName);
		//		if (currentFunc != null) {
		//				throw new IllegalArgumentException("Function '" + funcName + "' cannot be redeclared");
		//		}
		customFunc.put(funcName.toLowerCase(), func);
	}

	final public SimpleNode parse(final Reader sr) throws ParseException {
		ReInit(sr);
		// clear the customFunc-Map
		customFunc = new HashMap<String, AtFunction>();

		return parseFocFormula();
	}

	final public SimpleNode parse(final InputStream sr, final String encoding) throws ParseException {
		ReInit(sr, encoding);
		// clear the customFunc-Map
		customFunc = new HashMap<String, AtFunction>();

		return parseFocFormula();
	}

	final public SimpleNode parse(final InputStream sr) throws ParseException {
		return parse(sr, null);
	}

	final public SimpleNode parse(final String formula) throws ParseException {
		StringReader sr = new java.io.StringReader(formula);
		return parse(sr);
	}

	protected abstract void ReInit(Reader sr);

	protected abstract void ReInit(java.io.InputStream stream, String encoding);

	abstract public SimpleNode parseFormula() throws ParseException;

	abstract public SimpleNode parseFocFormula() throws ParseException;
}
