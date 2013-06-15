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
	public List<Object> getColumns();

	public void addColumn();

	public void removeColumn(int index);

	public void swapColumns(int a, int b);
}
