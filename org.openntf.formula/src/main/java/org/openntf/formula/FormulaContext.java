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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.openntf.formula.ValueHolder.DataType;

/**
 * This is the FormulaContext that is used for evaluation
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public class FormulaContext {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(FormulaContext.class.getName());
	protected Map<String, Object> dataMap;
	private Map<String, ValueHolder> vars = new HashMap<String, ValueHolder>();
	private Map<String, Object> parameters = new HashMap<String, Object>();

	/** the formatter is needed to parse/convert number and dateTime values */
	private Formatter formatter;

	/** the parser is needed for evaluate */
	private FormulaParser parser;
	/** the parameterProvider for <code>{@literal @}FocParam</code> or <code>&lt;#...#&gt;</code> parameters */
	private FormulaProvider<?> paramProvider;

	public boolean useBooleans = true;

	public ValueHolder TRUE;
	public ValueHolder FALSE;

	public ValueHolder NEWLINE = ValueHolder.valueOf(System.getProperty("line.separator", "\n"));

	/**
	 * @param dataMap
	 *            the context document
	 * @param formatter
	 *            the formatter to format date/times
	 * @param parser
	 *            the parser
	 * 
	 */
	public void init(final Map<String, Object> dataMap, final Formatter formatter, final FormulaParser parser) {
		this.dataMap = dataMap;
		this.formatter = formatter;
		this.parser = parser;
		useBooleans(true);
	}

	/**
	 * the formula engine uses real booleans instead of 1 and 0. This is not 100% compatible to lotus, but it is more typesafe for
	 * misspellings. If you do not want this, you can disable booleans here
	 * 
	 * @param useit
	 *            Use booleans: <br>
	 *            <code>TRUE</code> if you want to use booleans (default) <br>
	 *            <code>FALSE</code> if you want to use 1 and 0 for boolean operatiosn.
	 */
	public void useBooleans(final boolean useit) {
		useBooleans = useit;
		if (useit) {
			TRUE = ValueHolder.valueOf(true);
			FALSE = ValueHolder.valueOf(false);
		} else {
			TRUE = ValueHolder.valueOf(1);
			FALSE = ValueHolder.valueOf(0);
		}
	}

	/**
	 * Reading a value looks first in the internal vars and then in the document. Every value read from document is cached INTERNALLY. So
	 * pay attention if you program functions that modify the document otherwise!
	 * 
	 * @param keyLowercase
	 *            the key in lowercase. This is done for performance reasons
	 * 
	 * @param key
	 *            the key in propercase
	 * @return ValueHolder
	 */
	public ValueHolder getVarLC(final String keyLowercase, final String key) {
		ValueHolder var = vars.get(keyLowercase);
		if (var != null) {
			return var;
		}
		var = getField(key);

		// read valid value from doc. So cache it
		if (var != ValueHolder.valueDefault()) {
			vars.put(keyLowercase, var);
		}
		return var;

	}

	/**
	 * Checks if a variable or a doc field is available
	 * 
	 */
	public boolean isAvailableVarLC(final String keyLowercase, final String key) {
		ValueHolder var = vars.get(keyLowercase);
		if (var != null) {
			if (var.dataType == DataType.UNAVAILABLE)
				return false;
			return true;
		}
		if (dataMap != null) {
			return dataMap.containsKey(key);
		}
		return false;
	}

	/**
	 * Set a value in the internal var cache. Nothing is written to a Document
	 * 
	 * @param keyLowercase
	 *            the key. Must be lowercase
	 * @param elem
	 *            the ValueHolder to set
	 * @return the OLD value
	 */
	public ValueHolder setVarLC(final String keyLowercase, final ValueHolder elem) {
		if (elem == null) {
			ValueHolder old = vars.get(keyLowercase);
			vars.remove(keyLowercase);
			return old;
		} else {
			return vars.put(keyLowercase, elem);
		}
	}

	/**
	 * reads out a field from the dataMap
	 * 
	 * @param key
	 *            the key in properCase
	 */
	public ValueHolder getField(final String key) {
		if (dataMap != null) {
			Object o = dataMap.get(key);
			if (o != null)
				return ValueHolder.valueOf(o); // RPr here it is allowed to access the deprecate method
		}
		return ValueHolder.valueDefault();
	}

	/**
	 * Set a field (and a value!)
	 * 
	 * @param key
	 *            the fieldname to set
	 * @param elem
	 *            the element to set in the field
	 * @throws EvaluateException
	 *             in the case of an evaluation exception
	 */
	public void setField(final String key, final ValueHolder elem) {
		setVarLC(key.toLowerCase(), elem);
		if (dataMap != null) {
			if (elem.dataType == DataType.UNAVAILABLE) {
				dataMap.remove(key);
			} else {
				try {
					dataMap.put(key, elem.toList());
				} catch (EvaluateException e) {
					// TODO Don't know what is the best here
					// dataMap.remove(key);
					dataMap.put(key, "@ERROR: " + e.getMessage());
				}
			}
		}
	}

	/**
	 * Set a default value
	 * 
	 * @param key
	 *            the field to set
	 * @param elem
	 *            the element to set
	 */
	public void setDefaultLC(final String keyLowercase, final String key, final ValueHolder elem) {
		if (vars.containsKey(keyLowercase))
			return;
		if (dataMap != null) {
			if (dataMap.containsKey(key))
				return;
		}
		setVarLC(keyLowercase, elem);
	}

	/**
	 * returns the formatter for this context
	 * 
	 */
	public Formatter getFormatter() {
		return formatter;
	}

	/**
	 * returns the parser (needed for evaluate/checkFormulaSyntax)
	 * 
	 */
	public FormulaParser getParser() {
		return parser;
	}

	/**
	 * Reads a system property
	 * 
	 */
	public String getEnv(final String key) {
		return System.getProperty(key);
	}

	/**
	 * writes a system property
	 * 
	 */
	public void setEnv(final String key, final String value) {
		System.setProperty(key, value);
	}

	//------------------------------------------------------
	//
	// Parameter support
	//
	// ------------------------------------------------------

	/**
	 * Setup for the parameter provider (needed by FOCONIS)
	 * 
	 */
	public void setParameterProvider(final FormulaProvider<?> prov) {
		paramProvider = prov;
	}

	public void setParam(final String paramName, final Object value) {
		parameters.put(paramName, value);
	}

	/**
	 * Read a formula parameter
	 * 
	 */
	public Object getParam(final String paramName) {
		if (parameters.containsKey(paramName))
			return parameters.get(paramName);
		if (paramProvider != null) {
			return paramProvider.get(paramName);
		}
		return null;
	}

}
