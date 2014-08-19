/**
 * 
 */
package org.openntf.domino.ext;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory.Type;
import org.openntf.domino.annotations.Legacy;
import org.openntf.domino.helpers.DbDirectoryTree;

/**
 * @author withersp
 * 
 *         OpenNTF extensions to DbDirectory class
 * 
 */
public interface DbDirectory {

	/**
	 * Deprecated, use {@link org.openntf.domino.DbDirectory#iterator()} instead to loop through databases in the Directory
	 * 
	 * @param type
	 *            Type database type, e.g. Database, Template, Template_Candidate etc
	 * @return Database of type passed in
	 * @since org.openntf.domino 1.0.0
	 */
	@Deprecated
	@Legacy(Legacy.ITERATION_WARNING)
	public Database getFirstDatabase(final Type type);

	/**
	 * Whether or not the DbDirectory is sorted by Last Modified. Use {@link org.openntf.domino.DbDirectory#setSortByLastModified(boolean)}
	 * to sort the DbDirectory by last modified date
	 * 
	 * @return boolean, if sorted on last modified
	 * @since org.openntf.domino 4.5.0
	 */
	public boolean isSortByLastModified();

	/**
	 * Re-sorts the DbDirectory by Last Modified date or clears that sorting
	 * 
	 * @param value
	 *            boolean whether to sort on Last Modified date or not
	 * @since org.openntf.domino 4.5.0
	 */
	public void setSortByLastModified(final boolean value);

	/**
	 * Type of database the DbDirectory is, using {@link org.openntf.domino.DbDirectory.Type} enum. The default is TEMPLATE_CANDIDATE
	 * 
	 * @return org.openntf.domino.DbDirectory.Type of the database
	 * @since org.openntf.domino 4.5.0
	 */
	public Type getDirectoryType();

	/**
	 * Sets the type of database the DbDirectory is, using {@link org.openntf.domino.DbDirectory.Type} enum. The default is
	 * TEMPLATE_CANDIDATE
	 * 
	 * @param type
	 *            org.openntf.domino.DbDirectory.Type enum instance
	 * @since org.openntf.domino 4.5.0
	 */
	public void setDirectoryType(final Type type);

	/**
	 * Returns a tree datastructure, so that you easily can navigate through several directories
	 * 
	 * @return a DbDirectoryTree
	 */
	public DbDirectoryTree getTree();

	/**
	 * Returns a tree datastructure for a certain type, so that you easily can navigate through several directories
	 * 
	 * @param type
	 *            the type
	 * @return a DbDirectoryTree
	 */
	public DbDirectoryTree getTree(final Type type);
}
