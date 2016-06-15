/**
 * 
 */
package org.openntf.domino.graph2;

import java.util.Map;
import java.util.Set;

import org.openntf.domino.Document;

/**
 * @author nfreeman
 * 
 */
public interface DElement extends com.tinkerpop.blueprints.Element {
	public static final String TYPE_FIELD = "_ODA_GRAPHTYPE";
	public static final String FORMULA_FILTER = DElement.TYPE_FIELD + "=\"" + DVertex.GRAPH_TYPE_VALUE + "\" | " + DElement.TYPE_FIELD
			+ "=\"" + DEdge.GRAPH_TYPE_VALUE + "\"";

	public boolean hasProperty(String key);

	public <T> T getProperty(String key, Class<T> type);

	public <T> T getProperty(String key, Class<T> type, boolean allowNull);

	public int incrementProperty(String key);

	public int decrementProperty(String key);

	public Map<String, Object> getDelegate();

	public Class<?> getDelegateType();

	public void setDelegate(Map<String, Object> delegate);

	//	public Set<String> getPropertyKeys(boolean includeEdgeFields);

	public Map<String, Object> toMap(String[] props);

	public Map<String, Object> toMap(Set<String> props);

	public void fromMap(Map<String, Object> map);

	public void rollback();

	public void commit();

	public Document asDocument();
}
