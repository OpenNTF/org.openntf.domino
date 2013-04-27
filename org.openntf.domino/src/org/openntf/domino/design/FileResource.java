package org.openntf.domino.design;

import java.io.InputStream;

public interface FileResource extends DesignBase, org.openntf.domino.types.DatabaseDescendant {

	/**
	 * @return File resource as InputStream
	 */
	public InputStream getInputStream();

	/**
	 * @return mime type
	 */
	public String getMimeType();

	/**
	 * @return whether the file resource is marked as read-only
	 */
	public boolean isReadOnly();
}
