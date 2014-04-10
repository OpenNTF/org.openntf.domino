package org.openntf.formula;

/**
 * A Simplpe provider that can return objects on demand if requested by formula engine
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 * @param <T>
 */
public interface FormulaProvider<T> {
	public T get(String key);
}
