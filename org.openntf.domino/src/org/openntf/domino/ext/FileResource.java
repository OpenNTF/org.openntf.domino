/**
 * 
 */
package org.openntf.domino.ext;

import java.io.InputStream;

import org.openntf.domino.Database;

/**
 * @author withersp
 * 
 */
public interface FileResource {

	/**
	 * @return File resource as InputStream
	 */
	public InputStream getInputStream();

	/**
	 * @return mime type
	 */
	public String getMimeType();

	/**
	 * @return file resource name
	 */
	public String getName();

	/**
	 * @return parent database
	 */
	public Database getParent();

	/**
	 * @return whether hidden from web
	 */
	public boolean isHideFromWeb();

	/**
	 * @return whether hidden from notes
	 */
	public boolean isHideFromNotes();

	/**
	 * @return whether refresh flag is set
	 */
	public boolean isNeedsRefresh();

	/**
	 * @return whether prohibit design refresh is set
	 */
	public boolean isPreventChanges();

	public boolean isPropagatePreventChanges();

	public boolean isReadOnly();

}
