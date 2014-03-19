/**
 * 
 */
package org.openntf.domino.ext;

import java.util.Collection;
import java.util.Date;

import org.openntf.domino.AutoMime;
import org.openntf.domino.ColorObject;
import org.openntf.domino.Database;
import org.openntf.domino.DateRange;
import org.openntf.domino.DateTime;
import org.openntf.domino.events.EnumEvent;
import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.events.IDominoEventFactory;

import com.ibm.icu.util.Calendar;

/**
 * @author withersp
 * 
 */
public interface Session {

	public static enum Fixes {
		/** This Fix is not used */
		MIME_CONVERT,

		/** Writing <code>null</code> will remove the item */
		REPLACE_ITEM_NULL,

		/** DbDirectory.createDatabase will not fail when DB exists */
		CREATE_DB, REMOVE_ITEM,

		/** use replaceItemValue instead of appendItemValue. This works also for MIME items */
		APPEND_ITEM_VALUE,

		/** set view.autoUpdate to off by default. */
		VIEW_UPDATE_OFF,

		/** use java-dates in ViewEntries by default */
		FORCE_JAVA_DATES,

		/** block the MIME interface in the document while accessing MIME items */
		MIME_BLOCK_ITEM_INTERFACE,
		/** Document.getDocumentByUNID() returns null instead of an exception */
		DOC_UNID_NULLS
	}

	public IDominoEventFactory getEventFactory();

	public void setEventFactory(IDominoEventFactory factory);

	public IDominoEvent generateEvent(EnumEvent event, org.openntf.domino.Base source, org.openntf.domino.Base target, Object payload);

	// public RunContext getRunContext();

	/**
	 * @param color
	 *            Java color
	 * @return Color Object
	 */
	public ColorObject createColorObject(final java.awt.Color color);

	/**
	 * Free time search.
	 * 
	 * @param window
	 *            the window
	 * @param duration
	 *            the duration
	 * @param names
	 *            the names
	 * @param firstFit
	 *            the first fit
	 * @return the collection
	 */
	public Collection<DateRange> freeTimeSearch(final org.openntf.domino.DateRange window, final int duration,
			final Collection<String> names, final boolean firstFit);

	/**
	 * Free time search.
	 * 
	 * @param window
	 *            the window
	 * @param duration
	 *            the duration
	 * @param names
	 *            the names
	 * @param firstFit
	 *            the first fit
	 * @return the collection
	 */
	public Collection<DateRange> freeTimeSearch(final org.openntf.domino.DateRange window, final int duration, final String names,
			final boolean firstFit);

	/**
	 * A collection of Domino Directories and Personal Address Books, including directory catalogs, known to the current session.
	 * 
	 * <p>
	 * To distinguish between a Domino Directory and a Personal Address Book, use isPublicAddressBook and isPrivateAddressBook of Database.
	 * </p>
	 * 
	 * <p>
	 * A database retrieved through getAddressBooks is closed. To access all its properties and methods, you must open the database with the
	 * open method in NotesDatabase.
	 * </p>
	 * 
	 * @return A collection of Databases.
	 * @since openntf.domino 1.0.0
	 */
	public Collection<Database> getAddressBookCollection();

	/**
	 * A collection of groups to which the current user belongs.
	 * 
	 * <p>
	 * The "groups" include the hierarchical parents of the current effective user name and the alternate user name, if available. For Mary
	 * Smith/Department One/Acme, for example, the groups include /Department One/Acme and /Acme.
	 * </p>
	 * 
	 * <p>
	 * The groups include those to which the user name belongs in the Domino� Directory or Personal Address Book where the program is
	 * running.
	 * </p>
	 * 
	 * @return The user group name collection
	 * @since openntf.domino 1.0.0
	 */
	public Collection<String> getUserGroupNameCollection();

	/**
	 * Gets a collection names of the user or server that created the session, and the alternate name if it exists.
	 * 
	 * @return The user name collection
	 * @since openntf.domino 1.0.0
	 */
	public Collection<String> getUserNameCollection();

	/**
	 * Creates a DateTime object that represents a specified date and time.
	 * 
	 * @param date
	 *            The date, time, and time zone you want the object to represent using a {@link com.ibm.icu.util.Calendar} object.
	 * 
	 * @return The newly created {@link DateTime} object.
	 * @since lotus.domino 4.5.0
	 */
	public DateTime createDateTime(Calendar date);

	public boolean isFixEnabled(Fixes fix);

	public void setFixEnable(Fixes fix, boolean value);

	@Deprecated
	public String toCommonName(String name);

	public void boogie();

	public String getUnique();

	public org.openntf.domino.Database getDatabaseByReplicaID(String server, String replicaid);

	public org.openntf.domino.Database getDatabaseWithFailover(String server, String dbfile);

	public org.openntf.domino.Database getDatabaseIfModified(String server, String dbfile, lotus.domino.DateTime modifiedsince);

	public org.openntf.domino.Database getDatabaseIfModified(String server, String dbfile, Date modifiedsince);

	public org.openntf.domino.Database getMailDatabase();

	public org.openntf.domino.Document getDocumentByMetaversalID(String metaversalID);

	public org.openntf.domino.Document getDocumentByMetaversalID(String metaversalID, String serverName);

	public AutoMime getAutoMime();

	public void setAutoMime(AutoMime autoMime);
}
