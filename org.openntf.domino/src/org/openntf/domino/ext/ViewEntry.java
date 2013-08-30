/**
 * 
 */
package org.openntf.domino.ext;

/**
 * @author withersp
 * 
 */
public interface ViewEntry {
	public org.openntf.domino.View getParentView();

	public Object getColumnValue(final String columnName);
}
