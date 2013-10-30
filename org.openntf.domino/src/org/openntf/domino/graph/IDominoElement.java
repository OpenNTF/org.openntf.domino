/**
 * 
 */
package org.openntf.domino.graph;

import java.util.Set;

import org.openntf.domino.Document;

import com.tinkerpop.blueprints.Element;

/**
 * @author nfreeman
 * 
 */
public interface IDominoElement extends Element {
	public <T> T getProperty(String key, Class<?> T);

	public <T> T getProperty(String key, Class<?> T, boolean allowNull);

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
}
