package org.openntf.domino.formula;

import java.util.HashMap;
import java.util.Map;

public class FormulaContext {
	private Map<String, Object> document;
	private Map<String, ValueHolder> vars = new HashMap<String, ValueHolder>();
	private Formatter formatter;

	/**
	 * @param document
	 * @param formatter
	 */
	public FormulaContext(final Map<String, Object> document, final Formatter formatter) {
		super();
		this.document = document;
		this.formatter = formatter;
	}

	/**
	 * Reading a value looks first in the internal vars and then in the document. Every value read from document is cached INTERNALLY. So
	 * pay attention if you program functions that modify the document otherwise!
	 * 
	 * @param varName
	 * @return
	 */
	public ValueHolder getVar(final String varName) {
		String key = varName.toLowerCase();

		ValueHolder var = vars.get(key);
		if (var != null) {
			return var;
		}
		if (document != null) {
			Object o = document.get(key);
			if (o != null) {
				var = new ValueHolder(o);
			} else {
				var = new ValueHolder("");
			}
		} else {
			var = new ValueHolder("");
		}

		vars.put(key, var);
		return var;

	}

	/**
	 * Set a value in the internal var cache. Nothing is written to a Document
	 * 
	 * @param key
	 * @param elem
	 * @return the OLD value
	 */
	public ValueHolder setVar(final String key, final ValueHolder elem) {
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
	 * @param elem
	 */
	public void setField(final String key, final ValueHolder elem) {
		setVar(key, elem);
		if (document != null) {
			document.put(key, elem);
		}
	}

	/**
	 * Set a default value
	 * 
	 * @param key
	 * @param elem
	 */
	public void setDefault(final String key, final ValueHolder elem) {
		if (vars.containsKey(key))
			return;
		if (document != null) {
			if (document.containsKey(key))
				return;
		}
		setVar(key, elem);
	}

	public Formatter getFormatter() {
		return formatter;
	}
}
