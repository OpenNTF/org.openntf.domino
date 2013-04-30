/**
 * 
 */
package org.openntf.domino.ext;

/**
 * @author nfreeman
 * 
 */
public interface Formula {
	public void setExpression(String expression);

	public <T> T getValue();

	public <T> T getValue(Class<?> T);

	public <T> T getValue(org.openntf.domino.Document document);

	public <T> T getValue(org.openntf.domino.Document document, Class<?> T);

}
