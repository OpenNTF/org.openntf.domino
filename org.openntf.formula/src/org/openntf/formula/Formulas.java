package org.openntf.formula;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;

import org.openntf.formula.impl.FormatterImpl;
import org.openntf.formula.parse.AtFormulaParserImpl;

/**
 * This is the general factory
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public enum Formulas {
	;

	private static final ThreadLocal<FormulaParser> parserCache = new ThreadLocal<FormulaParser>();
	private static final ThreadLocal<FunctionFactory> functionFactoryCache = new ThreadLocal<FunctionFactory>();

	/*----------------------------------------------------------------------------*/
	private static Map<Locale, Formatter> instances = new HashMap<Locale, Formatter>();

	public static synchronized Formatter getFormatter(Locale loc) {
		if (loc == null)
			loc = Locale.getDefault();
		Formatter ret = instances.get(loc);
		if (ret == null)
			instances.put(loc, ret = new FormatterImpl(loc));
		return ret;
	}

	public static Formatter getFormatter() {
		return getFormatter(null);
	} /*----------------------------------------------------------------------------*/

	public void reset() {
		parserCache.set(null);
		functionFactoryCache.set(null);
	}

	/**
	 * This function returns a preconfigured default instance
	 */
	public static FormulaParser getParser(final Formatter formatter, final FunctionFactory factory) {
		AtFormulaParserImpl parser = new AtFormulaParserImpl(new java.io.StringReader(""));
		parser.reset();
		parser.formatter = formatter;
		parser.functionFactory = factory;

		return parser;
	}

	public static FormulaParser getParser() {
		FormulaParser parser = parserCache.get();
		if (parser == null) {
			parser = getParser(getFormatter(), getFunctionFactory());
			parserCache.set(parser);
		}
		return parser;
	}

	public static FunctionFactory getFunctionFactory() {
		FunctionFactory functionFactory = functionFactoryCache.get();
		if (functionFactory == null) {
			functionFactory = FunctionFactory.createInstance();
			functionFactoryCache.set(functionFactory);
		}
		return functionFactory;
	}

	public static FormulaContext createContext(final Map<String, Object> document, final FormulaParser parser) {
		return createContext(document, parser == null ? null : parser.getFormatter(), parser);
	}

	public static FormulaContext createContext(final Map<String, Object> document, final Formatter formatter, final FormulaParser parser) {
		ServiceLoader<FormulaContext> loader = ServiceLoader.load(FormulaContext.class);

		FormulaContext instance = loader.iterator().next();
		instance.init(document, formatter, parser);
		return instance;
	}
}
