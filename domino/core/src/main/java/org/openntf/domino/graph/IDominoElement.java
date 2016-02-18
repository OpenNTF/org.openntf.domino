/**
 * 
 */
package org.openntf.domino.graph;

import java.util.Map;
import java.util.Set;

import org.openntf.domino.Document;

import com.tinkerpop.blueprints.Element;

/**
 * @author nfreeman
 * 
 */
@Deprecated
public interface IDominoElement extends Element {

	public boolean hasProperty(String key);

	public <T> T getProperty(String key, Class<T> type);

	public <T> T getProperty(String key, Class<T> type, boolean allowNull);

	public int incrementProperty(String key);

	public int decrementProperty(String key);

	public Document getRawDocument();

	public void setRawDocument(Document doc);

	public int incrementProperty(IDominoProperties prop);

	public int decrementProperty(IDominoProperties prop);

	public Set<String> getPropertyKeys(boolean includeEdgeFields);

	public <T> T getProperty(IDominoProperties prop);

	public <T> T getProperty(IDominoProperties prop, boolean allowNull);

	public void setProperty(IDominoProperties prop, java.lang.Object value);

	public Map<String, Object> toMap(IDominoProperties[] props);

	public Map<String, Object> toMap(Set<IDominoProperties> props);

	public DominoGraph getParent();
}
