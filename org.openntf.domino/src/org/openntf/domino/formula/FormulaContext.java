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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import lotus.domino.NotesThread;

/**
 * This is the FormulaContext that is used for evaluation
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public class FormulaContext {
	private static final Logger log_ = Logger.getLogger(FormulaContext.class.getName());
	protected Map<String, Object> document;
	private Map<String, ValueHolder> vars = new HashMap<String, ValueHolder>();
	private Formatter formatter;

	public ValueHolder TRUE;
	public ValueHolder FALSE;
	public ValueHolder NEWLINE = ValueHolder.valueOf(System.getProperty("line.separator", "\n"));
	public boolean useBooleans = true;

	/**
	 * @param document
	 *            the context document
	 * @param formatter
	 *            the formatter to format date/times
	 * 
	 */
	protected FormulaContext(final Map<String, Object> document, final Formatter formatter) {
		super();
		this.document = document;
		this.formatter = formatter;
		useBooleans(true);
	}

	public static FormulaContext createContext(final Map<String, Object> document, final Formatter formatter) {
		if (NotesThread.isLoaded) {
			// TODO RPr find a better solution
			return new FormulaContextNotes(document, formatter);
		} else {
			return new FormulaContext(document, formatter);
		}
	}

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

	public Map<String, Object> getDocument() {
		return document;
	}

	/**
	 * Reading a value looks first in the internal vars and then in the document. Every value read from document is cached INTERNALLY. So
	 * pay attention if you program functions that modify the document otherwise!
	 * 
	 * @param varName
	 *            the var name to read. must be lowercase
	 * @return ValueHolder
	 */
	@SuppressWarnings("deprecation")
	public ValueHolder getVarLC(final String key) {
		ValueHolder var = vars.get(key);
		if (var != null) {
			return var;
		}
		if (document != null) {
			Object o = document.get(key);
			if (o != null) {
				var = ValueHolder.valueOf(o); // RPr here it is allowed to access the deprecate method
			} else {
				var = ValueHolder.valueDefault();
			}
		} else {
			var = ValueHolder.valueDefault();
		}

		vars.put(key, var);
		return var;

	}

	/**
	 * Set a value in the internal var cache. Nothing is written to a Document
	 * 
	 * @param key
	 *            the key. Must be lowercase
	 * @param elem
	 *            the ValueHolder to set
	 * @return the OLD value
	 */
	public ValueHolder setVarLC(final String key, final ValueHolder elem) {
		if (elem == null) {
			ValueHolder old = vars.get(key);
			vars.remove(key);
			return old;
		} else {
			return vars.put(key, elem);
		}
	}

	/**
	 * Set a field (and a value!)
	 * 
	 * @param key
	 *            the field to set
	 * @param elem
	 *            the element to set
	 */
	public void setField(final String key, final ValueHolder elem) {
		setVarLC(key.toLowerCase(), elem);
		if (document != null) {
			document.put(key, elem);
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
	public void setDefault(final String key, final ValueHolder elem) {
		if (vars.containsKey(key))
			return;
		if (document != null) {
			if (document.containsKey(key))
				return;
		}
		setVarLC(key.toLowerCase(), elem);
	}

	public Formatter getFormatter() {
		return formatter;
	}

	public void setEnvLC(final String varNameLC, final ValueHolder value) {
		// TODO Auto-generated method stub

	}

	public ValueHolder evaluateNative(final String formula, final ValueHolder... params) {
		throw new UnsupportedOperationException("EvaluateNative of '" + formula + "' is not supported in this context.");
	}
}
