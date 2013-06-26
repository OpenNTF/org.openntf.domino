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
	public void setSession(final org.openntf.domino.Session session);

	public void setExpression(final String expression);

	public Vector<Object> getValue();

	public <T> T getValue(final Class<?> T);

	public Vector<Object> getValue(final org.openntf.domino.Session session);

	public <T> T getValue(final org.openntf.domino.Session session, final Class<?> T);

	public Vector<Object> getValue(final org.openntf.domino.Document document);

	public <T> T getValue(final org.openntf.domino.Document document, final Class<?> T);

}
