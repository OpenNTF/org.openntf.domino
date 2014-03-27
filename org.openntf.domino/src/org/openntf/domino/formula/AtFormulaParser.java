/*
 * © Copyright FOCONIS AG, 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 * 
 */
package org.openntf.domino.formula;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.openntf.domino.formula.parse.AtFormulaParserImpl;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Terminatable;

/**
 * This class adds additonal functionality to the AtFormulaParserImpl (which is auto generated).
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public abstract class AtFormulaParser {

	protected Formatter formatter;
	protected AtFunctionFactory functionFactory;

	protected Map<String, AtFunction> customFunc;

	/**
	 * keep a local preconfigured instance in memory
	 */
	private static ThreadLocal<AtFormulaParserImpl> instance_ = new ThreadLocal<AtFormulaParserImpl>() {
		@Override
		protected AtFormulaParserImpl initialValue() {
			AtFormulaParserImpl parser = new AtFormulaParserImpl(new java.io.StringReader(""));

			return parser;
		}
	};

	/**
	 * Register a callback to release instance when Factory terminates
	 */
	static {
		Factory.onTerminate(new Terminatable() {
			public void terminate() {
				instance_.set(null);
			}
		});
	}

	/**
	 * Returns a the Formatter for this parser
	 * 
	 * @return the formatter
	 */
	public Formatter getFormatter() {
		return formatter;
	}

	/**
	 * This function returns a preconfigured default instance
	 */
	public static AtFormulaParser getInstance() {
		AtFormulaParser parser = instance_.get();
		parser.reset();
		return parser;
	}

	/**
	 * Resets the parser to a predefined state;
	 */
	public void reset() {
		resetFunctions();
		// TODO RPr we need a formatter that can parse date times and so on without Domino-objects 
		formatter = DominoFormatter.getInstance();
		functionFactory = AtFunctionFactory.getInstance();
	}

	public void resetFunctions() {
		customFunc = new HashMap<String, AtFunction>();
	}

	/**
	 * Querys the functionFactory for a function. You can declare custom functions. These functions overrides implemented functions (if it
	 * is not an AST-Function)
	 * 
	 * @param funcName
	 *            the functionName (lowercase)
	 * @return the function or null
	 */
	public AtFunction getFunctionLC(final String funcName) {

		AtFunction func = customFunc.get(funcName);
		if (func != null) {
			return func;
		}
		return functionFactory.getFunction(funcName);
	}

	/**
	 * Declares a new function. This mehtod is called by the <code>@Function(methodDecl ; [variables])</code> formula
	 * 
	 * @param func
	 *            the function to declare
	 */
	public void declareFunction(final AtFunction func) {
		String funcName = func.getImage();
		//		AtFunction currentFunc = getFunction(funcName);
		//		if (currentFunc != null) {
		//				throw new IllegalArgumentException("Function '" + funcName + "' cannot be redeclared");
		//		}
		customFunc.put(funcName.toLowerCase(), func);
	}

	/**
	 * Parses the given formoula
	 * 
	 * @param reader
	 *            the reader where the formula is read
	 * @param useFocFormula
	 *            parses the formula in a special mode needed by Foconis. (You can inline formulas in normal text)
	 * @return an AST-node that can be evaluated
	 * @throws ParseException
	 *             if the formula contains errors
	 */
	final public AtFormulaNode parse(final Reader reader, final boolean useFocFormula) throws AtFormulaParseException {
		ReInit(reader);
		resetFunctions();
		if (useFocFormula) {
			return parseFocFormula();
		} else {
			return parseFormula();
		}

	}

	/**
	 * Parses the given formlula from an inputStream
	 * 
	 * @param sr
	 *            the input stream where the formula is read
	 * @param encoding
	 *            encoding of the stream
	 * @param useFocFormula
	 *            see: {@link #parse(Reader, boolean)}
	 * @return see: {@link #parse(Reader, boolean)}
	 * @throws ParseException
	 *             see: {@link #parse(Reader, boolean)}
	 */
	final public AtFormulaNode parse(final InputStream sr, final String encoding, final boolean useFocFormula)
			throws AtFormulaParseException {
		ReInit(sr, encoding);
		resetFunctions();
		if (useFocFormula) {
			return parseFocFormula();
		} else {
			return parseFormula();
		}
	}

	/**
	 * Parses the given formlula from an inputStream with default encoding
	 * 
	 * @param sr
	 *            the input stream where the formula is read
	 * @param useFocFormula
	 *            see: {@link #parse(Reader, boolean)}
	 * @return see: {@link #parse(Reader, boolean)}
	 * @throws ParseException
	 *             see: {@link #parse(Reader, boolean)}
	 */
	final public AtFormulaNode parse(final InputStream sr, final boolean useFocFormula) throws AtFormulaParseException {
		return parse(sr, null, useFocFormula);
	}

	/**
	 * Parses the given formlula from an inputStream
	 * 
	 * @param formula
	 *            String with formul
	 * @param useFocFormula
	 *            see: {@link #parse(Reader, boolean)}
	 * @return see: {@link #parse(Reader, boolean)}
	 * @throws ParseException
	 *             see: {@link #parse(Reader, boolean)}
	 */
	final public AtFormulaNode parse(final String formula, final boolean useFocFormula) throws AtFormulaParseException {
		StringReader sr = new java.io.StringReader(formula);
		return parse(sr, useFocFormula);
	}

	/**
	 * Reinits the parser
	 * 
	 * @param sr
	 *            reader
	 */
	protected abstract void ReInit(Reader reader);

	/**
	 * Reinits the parser
	 * 
	 * @param stream
	 *            stream with formula
	 * @param encoding
	 *            encoding of the stream
	 */
	protected abstract void ReInit(java.io.InputStream stream, String encoding);

	/**
	 * Parses the formula
	 * 
	 * @return AST-Node
	 * @throws ParseException
	 *             if formula contains errors
	 */
	abstract public AtFormulaNode parseFormula() throws AtFormulaParseException;

	/**
	 * Parses the formula in Foconis-mode (inline formulas are supported)
	 * 
	 * @return AST-Node
	 * @throws ParseException
	 *             if formula contains errors
	 */
	abstract public AtFormulaNode parseFocFormula() throws AtFormulaParseException;
}
