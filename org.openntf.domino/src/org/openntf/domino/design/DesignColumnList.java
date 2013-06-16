/**
 * 
 */
package org.openntf.domino.design;

import java.util.List;

/**
 * @author jgallagher
 * 
 */
public interface DesignColumnList extends List<DesignColumn> {
	public DesignColumn addColumn();

	public void swap(final int a, final int b);
}
