/**
 * 
 */
package org.openntf.domino.types;

/**
 * Type used for generic, to ensure that the correct parameters are applied
 * 
 * @author Roland Praml, Foconis AG
 * 
 * @param <T>
 *            the org.openntf.domino type
 * @param <D>
 *            the lotus.domino type
 * @param <P>
 *            the parent type (Base when object can have different parents)
 */
@SuppressWarnings("rawtypes")
public abstract class FactorySchema<T extends org.openntf.domino.Base, D extends lotus.domino.Base, P extends org.openntf.domino.Base> {

	/** don't know it this is ever been used. */
	public abstract Class<T> typeClass();

	public abstract Class<D> delegateClass();

	public abstract Class<P> parentClass();
}
