/**
 * 
 */
package org.openntf.domino.design;


/**
 * @author jgallagher
 * 
 */
public interface DesignCollection<E extends DesignBase> extends Iterable<E> {
	public int getCount();
}
