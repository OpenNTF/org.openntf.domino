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
public interface DocumentCollection {
	public void stampAll(final Map<String, Object> map);

	public org.openntf.domino.DocumentCollection filter(final Object value);

	public org.openntf.domino.DocumentCollection filter(final Object value, final String[] itemnames);

	public org.openntf.domino.DocumentCollection filter(final Object value, final Collection<String> itemnames);

	public org.openntf.domino.DocumentCollection filter(final Map<String, Object> filterMap);

	public org.openntf.domino.View getParentView();
}
