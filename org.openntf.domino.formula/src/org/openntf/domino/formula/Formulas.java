package org.openntf.domino.formula;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;

import org.openntf.domino.formula.impl.FormatterImpl;
import org.openntf.domino.formula.parse.AtFormulaParserImpl;

public enum Formulas {
	;

	public static FunctionFactory getFunctionFactory() {

		FunctionFactory instance = new FunctionFactory();

		ServiceLoader<FunctionFactory> loader = ServiceLoader.load(FunctionFactory.class);

		for (FunctionFactory fact : loader) {
			System.out.println("ADD Factory " + fact.getClass().getName());
			instance.addFactory(fact);
		}

		instance.setImmutable();

		return instance;
	}

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
		return getParser(getFormatter(), getFunctionFactory());
	}

	public static FormulaContext createContext(final Map<String, Object> document, final FormulaParser parser) {
		return createContext(document, parser.getFormatter(), parser);
	}

	public static FormulaContext createContext(final Map<String, Object> document, final Formatter formatter, final FormulaParser parser) {
		//		if (NotesThread.isLoaded) {
		// TODO RPr find a better solution
		//			return new FormulaContextNotes(document, formatter);
		//		} else {
		ServiceLoader<FormulaContext> loader = ServiceLoader.load(FormulaContext.class);

		FormulaContext instance = loader.iterator().next();
		instance.init(document, formatter, parser);
		return instance;
		//		}
	}
}
