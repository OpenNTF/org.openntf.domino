/**
 * 
 */
package org.openntf.domino.graph;

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
}
