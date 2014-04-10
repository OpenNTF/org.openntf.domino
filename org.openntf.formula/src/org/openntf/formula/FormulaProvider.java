package org.openntf.formula;

public interface FormulaProvider<T> {
	public T get(String key);
}
