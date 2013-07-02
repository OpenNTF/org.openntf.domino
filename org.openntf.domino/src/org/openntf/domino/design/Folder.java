/**
 * 
 */
package org.openntf.domino.design;

import org.openntf.domino.design.impl.DesignColumn;

/**
 * @author jgallagher
 * 
 */
public interface Folder extends DesignBaseNamed {
	public DesignColumn addColumn();

	public DesignColumnList getColumns();
}
