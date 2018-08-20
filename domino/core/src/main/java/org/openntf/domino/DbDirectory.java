/*
 * Copyright 2013
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.openntf.domino;

import java.util.Collection;
import java.util.Date;

import org.openntf.domino.annotations.Legacy;
import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.Resurrectable;
import org.openntf.domino.types.SessionDescendant;

/**
 * Represents the Domino databases on a server or the local computer.
 * <h3>Notable enhancements and changes</h3>
 * <ul>
 * <li>Use {@link org.openntf.domino.ext.DbDirectory#getTree()} to easily navigate through several directories.</li>
 * <li>Use {@link org.openntf.domino.ext.DbDirectory#setSortByLastModified(boolean)} to navigate through the directory based on when the
 * databases were last modified.</li>
 * <li>Use the foreach construct to iterate through database (see Usage section below)</li>
 * <li>Use the {@link org.openntf.domino.impl.DbDirectory#size()} method to get the number of databases</li>
 * </ul>
 * <h3>Creation</h3>
 * <p>
 * You create a new <code>NotesDbDirectory</code> object with {@link Session#getDbDirectory(String)}.
 * </p>
 * <p>
 * If you create a database directory for a server that does not exist or cannot be accessed, the object is created and properties are set.
 * However, an exception occurs if you try to use the object in a useful way, for example, to get a database in the directory.
 * </p>
 * <h3>Usage</h3>
 * <p>
 * To iterate through all databases on the server use:
 * </p>
 *
 * <pre>
 * Session ses = Factory.getSession();
 * DbDirectory dir = ses.getDbDirectory(null);
 * dir.setDirectoryType(Type.DATABASE);	//optionally set the required database types, Type.TEMPLATE_CANDIDATE is the default
 * for (Database db : dir) {
 * 	//process the database
 * }
 * </pre>
 */
public interface DbDirectory extends Base<lotus.domino.DbDirectory>, lotus.domino.DbDirectory, org.openntf.domino.ext.DbDirectory,
		Collection<org.openntf.domino.Database>, SessionDescendant, Resurrectable {

	public static class Schema extends FactorySchema<DbDirectory, lotus.domino.DbDirectory, Session> {
		@Override
		public Class<DbDirectory> typeClass() {
			return DbDirectory.class;
		}

		@Override
		public Class<lotus.domino.DbDirectory> delegateClass() {
			return lotus.domino.DbDirectory.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Enum to allow easy access to database types, e.g. Database, Template Candidate
	 *
	 * @since org.openntf.domino 1.0.0
	 */
	public static enum Type {

		/** The database. */
		DATABASE(DbDirectory.DATABASE),
		/** The template. */
		TEMPLATE(DbDirectory.TEMPLATE),
		/** The replica candidate. */
		REPLICA_CANDIDATE(DbDirectory.REPLICA_CANDIDATE),
		/** The template candidate. */
		TEMPLATE_CANDIDATE(DbDirectory.TEMPLATE_CANDIDATE);

		/**
		 * @Deprecated better use valueOf
		 */
		@Deprecated
		public static Type getType(final int value) {
			return valueOf(value);
		}

		/**
		 * Return the {@link DbDirectory.Type} of a numeric value
		 *
		 * @param value
		 *            the numeric value
		 * @return a {@link DbDirectory.Type} Object
		 */
		public static Type valueOf(final int value) {
			for (Type type : Type.values()) {
				if (type.getValue() == value) {
					return type;
				}
			}
			return null;
		}

		/** The value_. */
		private final int value_;

		/**
		 * Instantiates a new type.
		 *
		 * @param value
		 *            the value
		 */
		private Type(final int value) {
			value_ = value;
		}

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public int getValue() {
			return value_;
		}
	}

	/**
	 * Creates a new database, using the server and file name that you specify. Because the new database is not based on a template, it is
	 * blank and does not contain any forms or views.
	 * <p>
	 * If you do not open the database, only a subset of its methods are available. See {@link Database#isOpen()}.
	 * </p>
	 * <p>
	 * Because the new database is not based on a template, it's blank and does not contain any forms or views.
	 * </p>
	 *
	 * @param dbFile
	 *            The file name of the new database.
	 * @return The newly created database.
	 */
	@Override
	public Database createDatabase(final String dbFile);

	/**
	 * Creates a new database, using the server and file name that you specify. Because the new database is not based on a template, it is
	 * blank and does not contain any forms or views.
	 * <p>
	 * If you do not open the database, only a subset of its methods are available. See {@link Database#isOpen()}.
	 * </p>
	 * <p>
	 * Because the new database is not based on a template, it's blank and does not contain any forms or views.
	 * </p>
	 *
	 * @param dbFile
	 *            The file name of the new database.
	 * @param open
	 *            true if you want to open the database and false if you do not.
	 * @return The newly created database.
	 */
	@Override
	public Database createDatabase(final String dbFile, final boolean open);

	/**
	 * Returns the name of the cluster containing this database directory (for current server).
	 */
	@Override
	public String getClusterName();

	/**
	 * Returns the name of the cluster containing this database directory.
	 *
	 * @param server
	 *            The full hierarchical name (can be abbreviated) of the server.
	 */
	@Override
	public String getClusterName(final String server);

	/**
	 * Returns the first database from a server or the local directory, using the file type you specify.
	 * <p>
	 * The returned database is closed. If you do not open the database, only a subset of its methods are available. See
	 * {@link Database#isOpen()}.
	 * </p>
	 * <p>
	 * Each time you call this method, the database directory is reset and a new search is conducted. If you are searching for template
	 * files, for example, a new call to getFirstDatabase with the parameter DATABASE starts searching the directory from the beginning,
	 * this time for database files
	 * </p>
	 *
	 * @param type
	 *            Indicates the kind of database file you want to retrieve:
	 *            <ul>
	 *            <li>DbDirectory.DATABASE means any database (NSF, NSG, or NSH file).</li>
	 *            <li>DbDirectory.TEMPLATE means any template (NTF file).</li>
	 *            <li>DbDirectory.REPLICA_CANDIDATE means any database or template not disabled for replication.</li>
	 *            <li>DbDirectory.TEMPLATE_CANDIDATE means any database or template.</li>
	 *            </ul>
	 * @return The first database of the specified file type located in the directory, or null if the directory contains no databases of the
	 *         specified type.
	 */
	@Override
	@Deprecated
	@Legacy(Legacy.ITERATION_WARNING)
	public Database getFirstDatabase(final int type);

	/**
	 * Returns the first database from a server or the local directory, using the file type you specify.
	 * <p>
	 * The returned database is closed. If you do not open the database, only a subset of its methods are available. See
	 * {@link Database#isOpen()}.
	 * </p>
	 * <p>
	 * Each time you call this method, the database directory is reset and a new search is conducted. If you are searching for template
	 * files, for example, a new call to getFirstDatabase with the parameter DATABASE starts searching the directory from the beginning,
	 * this time for database files
	 * </p>
	 *
	 * @param type
	 *            the type
	 * @return the first database
	 */
	@Override
	@Deprecated
	@Legacy(Legacy.ITERATION_WARNING)
	public Database getFirstDatabase(final Type type);

	/**
	 * The name of the server whose database directory you are searching.
	 * <p>
	 * This property returns an empty string for DbDirectory objects that represent the local directory.
	 * </p>
	 *
	 * @return The name of the server whose database directory you are searching.
	 */
	@Override
	public String getName();

	/**
	 * Returns the next database from a server or the local directory, using the file type specified in the preceding
	 * {@link #getFirstDatabase(Type)} method.
	 * <p>
	 * The returned database is closed. If you do not open the database, only a subset of its methods are available. See
	 * {@link Database#isOpen()}.
	 * </p>
	 * <p>
	 * This method must be preceded in the code by {@link #getFirstDatabase(Type)}.
	 * </p>
	 *
	 * @return The next database located in the directory, or null if there are no more.
	 */
	@Override
	@Deprecated
	@Legacy(Legacy.ITERATION_WARNING)
	public Database getNextDatabase();

	/**
	 * The Domino session that contains a DbDirectory object.
	 */
	@Override
	public Session getParent();

	/**
	 * Indicates whether the application design property Show in 'Open Application' dialog is honored for a database directory.
	 * <p>
	 * When this property is true, a database with disabled design property Show in 'Open Application' dialog will not be among the
	 * databases returned by <code>getFirstDatabase</code> and <code>getNextDatabase</code> methods.
	 * </p>
	 *
	 * @return true, when Show in 'Open Application' dialog is honored
	 */
	@Override
	public boolean isHonorShowInOpenDatabaseDialog();

	/**
	 * Opens a database.
	 *
	 * @param dbFile
	 *            The file name of the database to open.
	 * @return The opened database, or null if the database is not opened.
	 */
	@Override
	public Database openDatabase(final String dbFile);

	/**
	 * Opens a database.
	 *
	 * @param dbFile
	 *            The file name of the database to open.
	 * @param failover
	 *            If true and the database cannot be opened on the current server, an attempt is made to open it on another server in the
	 *            cluster (if there is a cluster). The object Server and FilePath properties reflect the server on which the database is
	 *            opened. For remote (IIOP) access, failover is always false.
	 * @return The opened database, or null if the database is not opened.
	 */
	@Override
	public Database openDatabase(final String dbFile, final boolean failover);

	/**
	 * Opens the database with a specified replica ID.null
	 *
	 * @param replicaId
	 *            The replica ID of the database that you want to open.
	 * @return The opened database, or null if the database is not opened.
	 */
	@Override
	public Database openDatabaseByReplicaID(final String replicaId);

	/**
	 * Opens a database if it has been modified since a specified date.
	 *
	 * @param dbfile
	 *            The file name of the database.
	 * @param date
	 *            A cutoff date. If one or more documents in the database were modified since this date, the database is opened; if not, it
	 *            is not opened. Cannot be null.
	 *
	 * @return The opened database, or null if the database is not opened.
	 */
	@Override
	public Database openDatabaseIfModified(final String dbFile, final lotus.domino.DateTime date);

	/**
	 * Opens the current user's mail database.
	 * <p>
	 * If the program runs on a workstation, <code>openMailDatabase</code> finds the current user's mail server and database in the
	 * notes.ini file.
	 * </p>
	 * <p>
	 * If the program runs on a server, the current user is considered to be the last person who modified the agent (the agent's owner), and
	 * openMailDatabase finds the user's mail server and database in the Domino Directory on the server.
	 * </p>
	 * <p>
	 * If the program is making remote (IIOP) calls to a server, the current user is the user who created the session, and
	 * <code>openMailDatabase</code> finds the user's mail server and database in the Domino Directory on the server.
	 * </p>
	 *
	 * @return The opened database, or null if the database is not opened.
	 */
	@Override
	public Database openMailDatabase();

	/**
	 * Sets whether the application design property Show in 'Open Application' dialog is honored for a database directory.
	 * <p>
	 * When this property is true, a database with disabled design property Show in 'Open Application' dialog will not be among the
	 * databases returned by <code>getFirstDatabase</code> and <code>getNextDatabase</code> methods.
	 * </p>
	 *
	 *
	 * @param flag
	 *            true, when Show in 'Open Application' dialog is honored
	 */
	@Override
	public void setHonorShowInOpenDatabaseDialog(final boolean flag);

	/**
	 * Opens a database if it has been modified since a specified date.
	 *
	 * @param dbfile
	 *            The file name of the database.
	 * @param date
	 *            A cutoff date. If one or more documents in the database were modified since this date, the database is opened; if not, it
	 *            is not opened. Cannot be null.
	 *
	 * @return The opened database, or null if the database is not opened.
	 *
	 */
	Database openDatabaseIfModified(String dbFile, Date date);

}
