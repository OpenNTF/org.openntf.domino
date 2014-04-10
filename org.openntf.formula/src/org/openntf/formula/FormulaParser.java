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
package org.openntf.formula;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.openntf.formula.parse.AtFormulaParserImpl;

/**
 * This class adds additonal functionality to the AtFormulaParserImpl (which is auto generated).
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public abstract class FormulaParser {

	protected Formatter formatter;
	protected FunctionFactory functionFactory;
	protected FormulaProvider<ASTNode> includeProvider;

	protected Map<String, Function> customFunc;
	protected boolean parsing = false;

	/**
	 * Returns a the Formatter for this parser
	 * 
	 * @return the formatter
	 */
	public Formatter getFormatter() {
		return formatter;
	}

	/**
	 * If you parse multiple formulas, you should call "reset" to clear predefined formulas!
	 */
	public void reset() {
		customFunc = new HashMap<String, Function>();
	}

	/**
	 * Querys the functionFactory for a function. You can declare custom functions. These functions overrides implemented functions (if it
	 * is not an AST-Function)
	 * 
	 * @param funcName
	 *            the functionName (lowercase)
	 * @return the function or null
	 */
	public Function getFunctionLC(final String funcName) {

		Function func = customFunc.get(funcName);
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
	public void declareFunction(final Function func) {
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
	final public ASTNode parse(final Reader reader, final boolean useFocFormula) throws FormulaParseException {
		if (parsing) {
			return getCopy().parse(reader, useFocFormula);
		}

		parsing = true;
		try {
			ReInit(reader);
			if (useFocFormula) {
				return parseFocFormula();
			} else {
				return parseFormula();
			}
		} finally {
			parsing = false;
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
	final public ASTNode parse(final InputStream sr, final String encoding, final boolean useFocFormula) throws FormulaParseException {
		if (parsing) {
			return getCopy().parse(sr, useFocFormula);
		}
		parsing = true;
		try {
			ReInit(sr, encoding);
			if (useFocFormula) {
				return parseFocFormula();
			} else {
				return parseFormula();
			}
		} finally {
			parsing = false;
		}
	}

	private FormulaParser getCopy() {
		AtFormulaParserImpl parser = new AtFormulaParserImpl(new java.io.StringReader(""));
		parser.reset();
		parser.formatter = formatter;
		parser.functionFactory = functionFactory;
		parser.includeProvider = includeProvider;
		parser.customFunc = customFunc;
		return parser;
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
	final public ASTNode parse(final InputStream sr, final boolean useFocFormula) throws FormulaParseException {
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
	final public ASTNode parse(final String formula, final boolean useFocFormula) throws FormulaParseException {
		StringReader sr = new java.io.StringReader(formula);
		return parse(sr, useFocFormula);
	}

	/**
	 * Parses the given formlula from an inputStream
	 * 
	 * @param formula
	 *            String with formul
	 * @return see: {@link #parse(Reader, boolean)}
	 * @throws ParseException
	 *             see: {@link #parse(Reader, boolean)}
	 */
	final public ASTNode parse(final String formula) throws FormulaParseException {
		StringReader sr = new java.io.StringReader(formula);
		return parse(sr, false);
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
	abstract public ASTNode parseFormula() throws FormulaParseException;

	/**
	 * Parses the formula in Foconis-mode (inline formulas are supported)
	 * 
	 * @return AST-Node
	 * @throws ParseException
	 *             if formula contains errors
	 */
	abstract public ASTNode parseFocFormula() throws FormulaParseException;

	public void setIncludeProvider(final FormulaProvider<ASTNode> prov) {
		includeProvider = prov;
	}

	public ASTNode getInclude(final String key) {
		if (includeProvider != null) {
			return includeProvider.get(key);
		}
		return null;
	}
}
