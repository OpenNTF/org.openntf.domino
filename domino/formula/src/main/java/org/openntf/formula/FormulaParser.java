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
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.openntf.formula.parse.AtFormulaParserImpl;
import org.openntf.formula.parse.ParseException;
import org.openntf.formula.parse.TokenMgrError;

/**
 * This class adds additonal functionality to the AtFormulaParserImpl (which is auto generated).
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public abstract class FormulaParser {

	/** the formatter to format/parse date time values while parsing the formula */
	protected Formatter formatter;

	/** the functionFactory */
	protected FunctionFactory functionFactory;

	/** the includeProvider for {@literal @}include function */
	protected FormulaProvider<ASTNode> includeProvider;

	protected Map<String, Function> customFunc;
	protected boolean parsing = false;

	public static final int MAX_FORMULA_CACHESIZE = 512;
	public static final int REDUCE_FORMULA_CACHESIZE_TO = 256;

	protected class FormulaCache {
		class FormulaCacheEntry {
			ASTNode node;
			int usageCount;

			FormulaCacheEntry(final ASTNode n) {
				node = n;
				usageCount = 1;
			}
		}

		class FCMapEntryComparator implements Comparator<Object> {
			@Override
			public int compare(final Object ent1, final Object ent2) {
				if (!(ent1 instanceof Map.Entry) || !(ent2 instanceof Map.Entry)) {
					throw new IllegalArgumentException("FCMapEntryComparator");
				}
				@SuppressWarnings("unchecked")
				int us1 = ((Map.Entry<String, FormulaCacheEntry>) ent1).getValue().usageCount;
				@SuppressWarnings("unchecked")
				int us2 = ((Map.Entry<String, FormulaCacheEntry>) ent2).getValue().usageCount;
				return (us1 > us2) ? 1 : (us1 < us2) ? -1 : 0;
			}
		}

		private Map<String, FormulaCacheEntry> cacheMap = new HashMap<String, FormulaCacheEntry>();

		ASTNode get(final String key) {
			FormulaCacheEntry fce = cacheMap.get(key);
			if (fce == null) {
				return null;
			}
			fce.usageCount++;
			return fce.node;
		}

		void reset() {
			cacheMap.clear();
		}

		@SuppressWarnings("unchecked")
		void put(final String key, final ASTNode node) {
			if (cacheMap.size() > MAX_FORMULA_CACHESIZE) {
				Object[] arr = cacheMap.entrySet().toArray();
				Arrays.sort(arr, new FCMapEntryComparator());
				int numToThrow = cacheMap.size() - REDUCE_FORMULA_CACHESIZE_TO;
				String[] toThrow = new String[numToThrow];
				for (int i = 0; i < numToThrow; i++) {
					toThrow[i] = ((Map.Entry<String, FormulaCacheEntry>) arr[i]).getKey();
				}
				for (int i = 0; i < numToThrow; i++) {
					cacheMap.remove(toThrow[i]);
				}
				Set<Map.Entry<String, FormulaCacheEntry>> cacheSet = cacheMap.entrySet();
				for (Map.Entry<String, FormulaCacheEntry> ent : cacheSet) {
					ent.getValue().usageCount = 1;
				}
			}
			cacheMap.put(key, new FormulaCacheEntry(node));
		}
	}

	protected FormulaCache ntfFormulaCache = new FormulaCache();
	protected FormulaCache focFormulaCache = new FormulaCache();

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
		ntfFormulaCache.reset();
		focFormulaCache.reset();
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
		Function func = getCustomFunc().get(funcName);
		if (func != null) {
			return func;
		}
		return getFunctionFactory().getFunction(funcName);
	}

	/**
	 * Declares a new function. This mehtod is called by the <code>@Function(methodDecl ; [variables])</code> formula
	 * 
	 * @param func
	 *            the function to declare
	 */
	public void declareFunction(final Function func) {
		String funcName = func.getImage();
		getCustomFunc().put(funcName.toLowerCase(), func);
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
		} catch (TokenMgrError e) {
			throw new ParseException(e.getMessage(), e);
		} finally {
			parsing = false;
		}
	}

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
		} catch (TokenMgrError e) {
			throw new ParseException(e.getMessage());

		} finally {
			parsing = false;
		}
	}

	/**
	 * return a copy of the current parser. This is needed for include, because we cannot start a new parse task unless the old one is
	 * terminated
	 * 
	 * @return
	 */
	private FormulaParser getCopy() {
		AtFormulaParserImpl parser = new AtFormulaParserImpl(new java.io.StringReader(""));
		parser.reset();
		parser.formatter = formatter;
		parser.functionFactory = getFunctionFactory();
		parser.includeProvider = includeProvider;
		parser.customFunc = getCustomFunc();
		parser.focFormulaCache = focFormulaCache;
		parser.ntfFormulaCache = ntfFormulaCache;
		return parser;
	}

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
	final public ASTNode parse(final InputStream sr, final boolean useFocFormula) throws FormulaParseException {
		return parse(sr, null, useFocFormula);
	}

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
	final public ASTNode parse(final String formula, final boolean useFocFormula) throws FormulaParseException {
		FormulaCache formulaCache = useFocFormula ? focFormulaCache : ntfFormulaCache;
		ASTNode node = formulaCache.get(formula);
		if (node == null) {
			StringReader sr = new java.io.StringReader(formula);
			node = parse(sr, useFocFormula);
			node.setFormula(formula);
			formulaCache.put(formula, node);
		}
		return node;
	}

	/**
	 * Parses the given formula from an inputStream
	 * 
	 * @param formula
	 *            String with formul
	 * @return see: {@link #parse(Reader, boolean)}
	 * @throws ParseException
	 *             see: {@link #parse(Reader, boolean)}
	 */
	final public ASTNode parse(final String formula) throws FormulaParseException {
		ASTNode node = parse(formula, false);
		node.setFormula(formula);
		return node;
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

	/**
	 * get a node to include
	 * 
	 */
	public ASTNode getInclude(final String key) {
		if (includeProvider != null) {
			return includeProvider.get(key);
		}
		return null;
	}

	private Map<String, Function> getCustomFunc() {
		if (this.customFunc == null) {
			this.customFunc = new HashMap<String, Function>();
		}
		return this.customFunc;
	}

	private FunctionFactory getFunctionFactory() {
		if (this.functionFactory == null) {
			this.functionFactory = FunctionFactory.createInstance();
		}
		return this.functionFactory;
	}
}
