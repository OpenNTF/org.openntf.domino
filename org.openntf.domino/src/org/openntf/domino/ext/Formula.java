/**
 * 
 */
package org.openntf.domino.ext;

import java.io.Externalizable;
import java.util.Vector;

/**
 * @author nfreeman
 * 
 */
public interface Formula extends Externalizable {
	public void setSession(org.openntf.domino.Session session);

	public void setExpression(String expression);

	@SuppressWarnings("rawtypes")
	public Vector getValue();

	public <T> T getValue(Class<?> T);

	@SuppressWarnings("rawtypes")
	public Vector getValue(org.openntf.domino.Session session);

	public <T> T getValue(org.openntf.domino.Session session, Class<?> T);

	@SuppressWarnings("rawtypes")
	public Vector getValue(org.openntf.domino.Document document);

	public <T> T getValue(org.openntf.domino.Document document, Class<?> T);

}
