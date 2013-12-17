/**
 * 
 */
package org.openntf.domino.ext;

import java.util.Collection;
import java.util.Map;

/**
 * @author withersp
 * 
 */
public interface ViewEntry {
	public org.openntf.domino.View getParentView();

	public Object getColumnValue(final String columnName);

	public Map<String, Object> getColumnValuesMap();

	public Collection<Object> getColumnValuesEx();
}
