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
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.events.EnumEvent;
import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.events.IDominoEventFactory;
import org.openntf.domino.utils.DominoFormatter;
import org.openntf.domino.utils.Factory.SessionType;

import com.ibm.icu.util.Calendar;

/**
 * @author withersp
 *
 *         OpenNTF extensions to Session class
 */
public interface Session {

	/**
	 * Enum for Khan-mode "fixes" to make lotus.domino methods "better"
	 *
	 * @since org.openntf.domino 1.0.0
	 */
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
		DOC_UNID_NULLS,

		/** ViewEntries should return correct column values. (Constant values are ommited) */
		VIEWENTRY_RETURN_CONSTANT_VALUES,

		/** Alternative implementation of Names */
		ODA_NAMES,

		/**
		 * Instruct the automatic garbage collector to maintain an internal tracker for C++ backend-object IDs and only recycle when no
		 * further references remain (causes a performance hit).
		 *
		 * <p>
		 * This fix is not included by default in "KHAN" mode.
		 * </p>
		 *
		 * @since ODA 3.0.0
		 */
		PEDANTIC_GC_TRACKING(false),

		/**
		 *
		 * Hex values such as replicaid, unid and noteid are forced to lower case before being returned
		 *
		 */
		FORCE_HEX_LOWER_CASE,

		/**
		 *
		 * Prevent use of getNthDocument. False by default because repeat controls etc need to use getNthDocument to get starting point
		 *
		 */
		BLOCK_NTH_DOCUMENT(false);

		private final boolean khan_;

		private Fixes() {
			khan_ = true;
		}

		private Fixes(final boolean khan) {
			khan_ = khan;
		}

		/**
		 * @return whether the fix should be included in the overarching "KHAN" all-fix mode
		 * @since ODA 3.0.0
		 */
		public boolean isKhan() {
			return khan_;
		}
	}

	/**
	 * Gets the factory that manages processing of the IDominoEvents
	 *
	 * @return IDominoEventFactory containing the IDominoEvents
	 * @since org.openntf.domino 3.0.0
	 */
	public IDominoEventFactory getEventFactory();

	/**
	 * Sets the factory for managing processing of the IDominoEvents
	 *
	 * @param factory
	 *            IDominoEventFactory containing the IDominoEvents
	 * @since org.openntf.domino 3.0.0
	 */
	public void setEventFactory(IDominoEventFactory factory);

	/**
	 * Generates an IDominoEvent into the IDominoEventFactory. The IDominoEvent will be for a specific EnumEvent, e.g.
	 * BEFORE_CREATE_DATABASE. This method basically triggers the EnumEvent, passing the relevant Objects that are currently being acted
	 * upon.
	 *
	 * <p>
	 * No EnumEvent types and contents are currently implemented but should be loaded in org.openntf.domino.ext.Session.Events
	 * </p>
	 *
	 * <p>
	 * The target should not be passed into this method, but the implementation should pass {@code this} to as the target to
	 * {@link org.openntf.domino.events.IDominoEventFactory.generate}
	 * </p>
	 *
	 * @param event
	 *            EnumEvent being triggered, e.g. BEFORE_CREATE_DOCUMENT.
	 * @param source
	 *            The source object for the event to be run on. The relevant
	 * @param payload
	 *            Object a payload that can be passed along with the Event
	 * @return An IDominoEvent which will be passed to {@link org.openntf.domino.ext.Base.fireListener}
	 * @since org.openntf.domino 3.0.0
	 */
	public IDominoEvent generateEvent(EnumEvent event, org.openntf.domino.Base<?> source, org.openntf.domino.Base<?> target,
			Object payload);

	// public RunContext getRunContext();

	/**
	 * Creates a ColorObject using a Java Color
	 *
	 * @param color
	 *            Color to load into the ColorObject
	 * @return ColorObject created
	 * @since org.openntf.domino 1.0.0
	 */
	public ColorObject createColorObject(final java.awt.Color color);

	/**
	 * Performs a free time search using a Collection of names to check for.
	 *
	 * @param window
	 *            DateRange window of start and end times to search through
	 * @param duration
	 *            int duration of the meeting, in minutes
	 * @param names
	 *            Collection<String> names to search for
	 * @param firstFit
	 *            boolean if only the first fit should be returned
	 * @return Collection<DateRange> of start/end instances for which all participants are free. Null if there are no matches
	 * @since org.openntf.domino 1.0.0
	 */
	public Collection<DateRange> freeTimeSearch(final org.openntf.domino.DateRange window, final int duration,
			final Collection<String> names, final boolean firstFit);

	/**
	 * Performs a free time search using a Collection of names to check for.
	 *
	 * @param window
	 *            DateRange window of start and end times to search through
	 * @param duration
	 *            int duration of the meeting, in minutes
	 * @param names
	 *            String a single name to search for
	 * @param firstFit
	 *            boolean if only the first fit should be returned
	 * @return Collection<DateRange> of start/end instances for which the required person is free. Null if there are no matches
	 * @since org.openntf.domino 1.0.0
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
	 * The groups include those to which the user name belongs in the Domino Directory or Personal Address Book where the program is
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
	 * Gets a <code>Name</code> object for the user returned by {@link org.openntf.domino.Session#getEffectiveUserName()}.
	 *
	 * @return a <code>Name</code> object for the effective user
	 * @since openntf.domino 2.0.1
	 */
	public org.openntf.domino.Name getEffectiveUserNameObject();

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

	/**
	 * Creates a Name object using a standard lotus.domino.Session, in case the fix is not enabled
	 *
	 * @param name
	 *            String for which to convert into a Notes Name
	 * @return The newly create {@link Name} object.
	 * @since 5.0.0
	 */
	public Name createNameNonODA(String name);

	/**
	 * Tells whether the current session object represents an anonymous user.
	 *
	 * @return boolean, whether the session is an anonymous user
	 */
	public boolean isAnonymous();

	/**
	 * Tells whether a specific Khan-mode fix is enabled, using {@link Fixes}
	 *
	 * @param fix
	 *            Fixes enum entry to test
	 * @return boolean, whether the fix is enabled or not
	 * @since org.openntf.domino 3.0.0
	 */
	public boolean isFixEnabled(Fixes fix);

	/**
	 * Returns all enabled fixes on that session
	 *
	 * @return a set with all enabled fixes
	 */
	public Fixes[] getEnabledFixes();

	/**
	 * Enables / disables a specific Khan-mode fix for the current Session object.
	 *
	 * <p>
	 * NOTE: The fix will only be enabled / disabled until the Session object is recycled. In the case of XPages, this is at the end of the
	 * current request lifecycle.
	 * </p>
	 *
	 * @param fix
	 *            Fixes enum entry to enable / disable
	 * @param value
	 *            boolean to enable or diable
	 */
	public void setFixEnable(Fixes fix, boolean value);

	/**
	 * Converts a String name to common name format. Deprecated in favour of
	 * {@link org.openntf.domino.utils.DominoUtils#toCommonName(String)}. That method is more performant and avoids creating a Name object.
	 *
	 * @param name
	 *            String hierarchical name to convert
	 * @return String name converted to common name format
	 * @since org.openntf.domino 4.5.0
	 */
	@Deprecated
	public String toCommonName(String name);

	/**
	 * Easter egg method to print a boogie image onto the server console :-)
	 *
	 * @since org.openntf.domino 4.5.0
	 */
	public void boogie();

	/**
	 * Whether the session is feature-restricted. Not currently implemented, but designed to allow us, in the future, to set up sandbox
	 * Sessions. Now go salivate!
	 *
	 * @return boolean
	 * @since org.openntf.domino 5.0.0
	 */
	public boolean isFeatureRestricted();

	/**
	 * Evaluates @Unique and returns the result as a String
	 *
	 * @return String unique reference
	 * @since org.openntf.domino 4.5.0
	 */
	public String getUnique();

	public void switchSessionType(SessionType type);

	/**
	 * Gets a Database object by its replica ID (e.g. 85255FA900747B84). Deprecated in favour of {@link Session#getDatabase(String)}
	 *
	 * @param server
	 *            String server name
	 * @param replicaid
	 *            String replica ID
	 * @return Database or null
	 * @since org.openntf.domino 4.5.0
	 * @deprecated in favour of {@link Session#getDatabase(String)}
	 */
	@Deprecated
	public org.openntf.domino.Database getDatabaseByReplicaID(String server, String replicaid);

	/**
	 * Gets a database with failover to another server, if it cannot be opened
	 *
	 * @param server
	 *            String server name to try first
	 * @param dbfile
	 *            String database file path
	 * @return Database
	 * @since org.openntf.domino 4.5.0
	 */
	public org.openntf.domino.Database getDatabaseWithFailover(String server, String dbfile);

	/**
	 * Gets a database, if it has been modified since a specific DateTime
	 *
	 * @param server
	 *            String server name
	 * @param dbfile
	 *            String database file path
	 * @param modifiedsince
	 *            DateTime to check against
	 * @return Database or null
	 * @since org.openntf.domino 4.5.0
	 */
	public org.openntf.domino.Database getDatabaseIfModified(String server, String dbfile, lotus.domino.DateTime modifiedsince);

	/**
	 * Gets a database, if it has been modified since a specific Java Date
	 *
	 * @param server
	 *            String server name
	 * @param dbfile
	 *            String database file path
	 * @param modifiedsince
	 *            DateTime to check against
	 * @return Database or null
	 * @since org.openntf.domino 4.5.0
	 */
	public org.openntf.domino.Database getDatabaseIfModified(String server, String dbfile, Date modifiedsince);

	/**
	 * Gets the current user's mail database, if the user uses Notes Mail
	 *
	 * @return Database or null
	 * @since org.openntf.domino 4.5.0
	 */
	public org.openntf.domino.Database getMailDatabase();

	/**
	 * Gets a database by:
	 * <ul>
	 * <li>Its {@link org.openntf.domino.Database#getFilePath()} property</li>
	 * <li>Its {@link org.openntf.domino.Database#getApiPath()} property (serverName!!filePath)</li>
	 * <li>Its {@link org.openntf.domino.Database#getReplicaID()} property</li>
	 * <li>Its {@link org.openntf.domino.Database#getMetaReplicaId()} property</li>
	 * </ul>
	 *
	 * @param key
	 *            String Api Path, ReplicaID or MetaReplicaID
	 * @return Database or null
	 * @since org.openntf.domino 5.0.0
	 */
	public org.openntf.domino.Database getDatabase(String key);

	/**
	 * Gets a document by its {@link org.openntf.domino.Document#getMetaversalID()} property
	 *
	 * @param metaversalID
	 *            String comprising replicaid + UNID
	 * @return Document
	 * @since org.openntf.domino 5.0.0
	 */
	public org.openntf.domino.Document getDocumentByMetaversalID(String metaversalID);

	/**
	 * Gets a document by its {@link org.openntf.domino.Document#getMetaversalID(String)} property
	 *
	 * @param serverName
	 *            String server name
	 * @param metaversalID
	 *            String comprising replicaid + UNID
	 * @return Document
	 * @since org.openntf.domino 5.0.0
	 */
	public org.openntf.domino.Document getDocumentByMetaversalID(String serverName, String metaversalID);

	/**
	 * Checks the Session's mechanism for converting mime, using {@link org.openntf.domino.AutoMime}
	 *
	 * @return AutoMime option
	 * @since org.openntf.domino 5.0.0
	 */
	public AutoMime getAutoMime();

	/**
	 * Sets the Session's mechanism for converting mime, using {@link org.openntf.domino.AutoMime}
	 *
	 * @param autoMime
	 *            AutoMime option
	 * @since org.openntf.domino 5.0.0
	 */
	public void setAutoMime(AutoMime autoMime);

	/**
	 * This method is needed for testing purposes or for XOTS
	 *
	 * ATTENTION: use that with care! You cannot change currentDatabase in delegate (AFAIK)
	 *
	 * @param db
	 *            the database that is the current one.
	 * @since org.openntf.domino 5.0.0
	 */
	public void setCurrentDatabase(final Database db);

	/**
	 * Returns a Domino Formatter
	 *
	 * @return the formatter
	 */
	DominoFormatter getFormatter();

	/**
	 * Sets the session type on construction, so that it can be recreated if used across threads
	 *
	 * @param sessionType
	 *            the sessionType
	 */
	public void setSessionType(final SessionType sessionType);

	/**
	 * Returns the wrapperFactory for this session
	 *
	 * @return the {@link WrapperFactory}
	 */
	public WrapperFactory getFactory();

	/**
	 * Sets this session to "no recycle". This means, a recycle call will do nothing.
	 *
	 * @param noRecycle
	 *            true = do not recycle that session
	 */
	public void setNoRecycle(boolean noRecycle);

	/**
	 * Recycling a session is allowed (needed to be compatible with @deprecated = error)
	 */
	public void recycle();

	public void clearIdentity();
}
