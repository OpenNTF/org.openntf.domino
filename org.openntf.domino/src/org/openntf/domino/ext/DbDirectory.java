/**
 * 
 */
package org.openntf.domino.ext;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory.Type;

/**
 * @author withersp
 * 
 */
public interface DbDirectory {

	/**
	 * @param type
	 *            file type
	 * @return Database
	 */
	public Database getFirstDatabase(final Type type);

	public boolean isSortByLastModified();

	public void setSortByLastModified(final boolean value);

	public Type getDirectoryType();

	public void setDirectoryType(final Type type);
}
