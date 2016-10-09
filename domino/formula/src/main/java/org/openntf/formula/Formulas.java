package org.openntf.formula;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
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
	private static final ThreadLocal<Class<FormulaContext>> contextClassCache = new ThreadLocal<Class<FormulaContext>>();
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

	public static void initialize() {
		parserCache.set(null);
		functionFactoryCache.set(null);
	}

	public static void terminate() {
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

	public static FormulaParser getMinimalParser() {
		return getParser(getFormatter(), FunctionFactory.getMinimalFF());
	}

	public static FormulaContext createContext(final Map<String, Object> document, final FormulaParser parser) {
		return createContext(document, parser == null ? null : parser.getFormatter(), parser);
	}

	@SuppressWarnings("unchecked")
	public static FormulaContext createContext(final Map<String, Object> document, final Formatter formatter, final FormulaParser parser) {
		Class<FormulaContext> ctxClass = contextClassCache.get();
		FormulaContext instance = null;

		if (ctxClass == null) {
			ServiceLoader<FormulaContext> loader = ServiceLoader.load(FormulaContext.class);
			Iterator<FormulaContext> it = loader.iterator();
			if (it.hasNext()) {
				instance = it.next();
			} else {
				instance = new FormulaContext();
			}
			ctxClass = (Class<FormulaContext>) instance.getClass();
			contextClassCache.set(ctxClass);
		} else {
			try {
				instance = ctxClass.newInstance();
			} catch (InstantiationException e) {
				throw new MissingResourceException("Can't instantiate context: " + e.getMessage(), ctxClass.getName(),
						"InstantiationException");
			} catch (IllegalAccessException e) {
				throw new MissingResourceException("Can't instantiate context: " + e.getMessage(), ctxClass.getName(),
						"IllegalAccessException");

			}
		}
		instance.init(document, formatter, parser);
		return instance;
	}

	public static List<Object> evaluate(final String formula, final Map<String, Object> map) throws FormulaParseException,
			EvaluateException {
		FormulaParser parser = Formulas.getParser();
		ASTNode node = parser.parse(formula);
		FormulaContext ctx = Formulas.createContext(map, parser);
		return node.solve(ctx);
	}
}
