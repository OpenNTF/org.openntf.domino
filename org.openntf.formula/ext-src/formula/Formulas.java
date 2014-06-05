package org.openntf.formula;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import lotus.notes.addins.changeman.FunctionFactory;

/**
 * This is the general factory
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public enum Formulas {
	;

	public static Formatter getFormatter(final Locale loc) {
		return null;
	}

	public static Formatter getFormatter() {
		return null;
	} /*----------------------------------------------------------------------------*/

	public static void initialize() {

	}

	public static void terminate() {
	}

	/**
	 * This function returns a preconfigured default instance
	 */
	public static FormulaParser getParser(final Formatter formatter, final FunctionFactory factory) {
		return null;
	}

	public static FormulaParser getParser() {
		return null;
	}

	public static FunctionFactory getFunctionFactory() {
		return null;
	}

	public static FormulaContext createContext(final Map<String, Object> document, final FormulaParser parser) {
		return createContext(document, parser == null ? null : parser.getFormatter(), parser);
	}

	@SuppressWarnings("unchecked")
	public static FormulaContext createContext(final Map<String, Object> document, final Formatter formatter, final FormulaParser parser) {
		return null;
	}

	public static List<Object> evaluate(final String formula, final Map<String, Object> map) throws FormulaParseException,
			EvaluateException {
		return null;
	}

	public static Map<String, Function> getFunctions(final Class<?> cls) {
		return null;
	}
}
