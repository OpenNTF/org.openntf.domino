/**
 * 
 */
package org.openntf.domino.design;

import java.util.List;

/**
 * @author jgallagher
 * 
 */
public interface Folder extends DesignBaseNamed {
	public List<DesignColumn> getColumns();
}
