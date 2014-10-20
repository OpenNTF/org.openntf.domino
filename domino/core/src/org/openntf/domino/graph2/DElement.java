/**
 * 
 */
package org.openntf.domino.graph2;

import java.util.Map;
import java.util.Set;

/**
 * @author nfreeman
 * 
 */
public interface DElement extends com.tinkerpop.blueprints.Element {
	public static final String TYPE_FIELD = "_ODA_GRAPHTYPE";

	public boolean hasProperty(String key);

	public <T> T getProperty(String key, Class<?> T);

	public <T> T getProperty(String key, Class<?> T, boolean allowNull);

	public int incrementProperty(String key);

	public int decrementProperty(String key);

	public Map<String, Object> getDelegate();

	public void setDelegate(Map<String, Object> delegate);

	//	public Set<String> getPropertyKeys(boolean includeEdgeFields);

	public Map<String, Object> toMap(String[] props);

	public Map<String, Object> toMap(Set<String> props);

	public void fromMap(Map<String, Object> map);

	public void rollback();

	public void commit();
}
