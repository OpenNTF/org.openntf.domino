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

/**
 * This class adds additonal functionality to the AtFormulaParserImpl (which is auto generated).
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public interface FormulaParser {

	/**
	 * Returns a the Formatter for this parser
	 * 
	 * @return the formatter
	 */
	public Formatter getFormatter();

	/**
	 * If you parse multiple formulas, you should call "reset" to clear predefined formulas!
	 */
	public void reset();

	/**
	 * Querys the functionFactory for a function. You can declare custom functions. These functions overrides implemented functions (if it
	 * is not an AST-Function)
	 * 
	 * @param funcName
	 *            the functionName (lowercase)
	 * @return the function or null
	 */
	public Function getFunctionLC(final String funcName);

	/**
	 * Declares a new function. This mehtod is called by the <code>@Function(methodDecl ; [variables])</code> formula
	 * 
	 * @param func
	 *            the function to declare
	 */
	public void declareFunction(final Function func);

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
	public ASTNode parse(final Reader reader, final boolean useFocFormula) throws FormulaParseException;

	/**
	 * Parses the given formula from an inputStream
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
	public ASTNode parse(final InputStream sr, final String encoding, final boolean useFocFormula) throws FormulaParseException;

	/**
	 * Parses the given formula from an inputStream with default encoding
	 * 
	 * @param sr
	 *            the input stream where the formula is read
	 * @param useFocFormula
	 *            see: {@link #parse(Reader, boolean)}
	 * @return see: {@link #parse(Reader, boolean)}
	 * @throws ParseException
	 *             see: {@link #parse(Reader, boolean)}
	 */
	public ASTNode parse(final InputStream sr, final boolean useFocFormula) throws FormulaParseException;

	/**
	 * Parses the given formula from an inputStream
	 * 
	 * @param formula
	 *            String with formula
	 * @param useFocFormula
	 *            see: {@link #parse(Reader, boolean)}
	 * @return see: {@link #parse(Reader, boolean)}
	 * @throws ParseException
	 *             see: {@link #parse(Reader, boolean)}
	 */
	public ASTNode parse(final String formula, final boolean useFocFormula) throws FormulaParseException;

	/**
	 * Parses the given formula from an inputStream
	 * 
	 * @param formula
	 *            String with formul
	 * @return see: {@link #parse(Reader, boolean)}
	 * @throws ParseException
	 *             see: {@link #parse(Reader, boolean)}
	 */
	public ASTNode parse(final String formula) throws FormulaParseException;

	public void setIncludeProvider(final FormulaProvider<ASTNode> prov);

	/**
	 * get a node to include
	 * 
	 * @param key
	 * @return
	 */
	public ASTNode getInclude(final String key);
}
