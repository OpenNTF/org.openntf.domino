/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino;

import java.io.Externalizable;
import java.util.Comparator;
import java.util.Vector;

import org.openntf.domino.annotations.Incomplete;
import org.openntf.domino.annotations.Legacy;
import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.Resurrectable;
import org.openntf.domino.types.SessionDescendant;
import org.openntf.domino.utils.enums.DominoEnumUtil;
import org.openntf.domino.utils.enums.INumberEnum;

/**
 * Represents a Notes database.
 * <h3>Notable enhancements and changes</h3>
 * <ul>
 * <li>Create a document and set its values in one call using {@link org.openntf.domino.ext.Database#createDocument(Object...)} method</li>
 * <li>Use {@link org.openntf.domino.ext.Database#getDocumentWithKey(java.io.Serializable, boolean)} to create/access a document with your
 * own UNID</li>
 * <li>Contains support for transactions - changes to Notes documents in a single transaction are either written to all documents or none at
 * all</li>
 * <li>Database generates events in various situations - before and after a document is created, updated or deleted and so on.</li>
 * </ul>
 * <h3>Transactions</h3>
 * <p>
 * Use transactions to cache changes to multiple documents. When an error occurs within your block of code, changes to those documents can
 * be rolled back. You control when a transaction starts and when the changes are written to all modified documents or when the changes
 * should be rolled back. The ODA Platform keeps track of documents modified within a single transaction.
 * </p>
 * <h5>Usage</h5>
 * <p>
 * <ul>
 * <li>{@link org.openntf.domino.Database#startTransaction()} - returns {@link org.openntf.domino.transactions.DatabaseTransaction}.
 * Indicates to the database that Document updates should be cached until the transaction is explicitly committed or rollbacked. This
 * includes field changes as well as document deletions.</li>
 *
 * <li>{@link org.openntf.domino.transactions.DatabaseTransaction#commit()} - calls .save() and .remove() on all transaction-cached
 * Documents in the current database. The transaction is closed in this process so you have to start a new one after a call to
 * commit().</li>
 *
 * <li>{@link org.openntf.domino.transactions.DatabaseTransaction#rollback()} - reverts all changes to Documents within the
 * transaction.</li>
 * <li>{@link org.openntf.domino.Database#closeTransaction()} - erases the cache with Document updates. The Documents will still have the
 * changes but you have to save them yourself.</li>
 * </ul>
 * </p>
 * <h5>Note</h5>
 * <p>
 * The mechanism can't roll back changes to documents already saved within the commit() operation. If an error occurs while saving a
 * document, other documents will still be saved.
 * </p>
 * <p>
 * <h5>Example</h5>
 *
 * <pre>
 * private void processOrder(String line) {
 *  Order order = parseInput(line);
 *  if (order != null) {
 *    DatabaseTransaction transaction = database.startTransaction();
 *
 *    try {
 *      Document docCustomer = getCustomer(order.getCustomerID())
 *      Document docOrder = database.createDocument("Form", "Order", "CustomerID", order.getCustomerID());
 *
 *      //update both the docCustomer and docOrder
 *      .....
 *      //if no exception or error condition occurs, save all changed documents
 *      transaction.commit();
 *    } catch (Throwable t) {
 *       //error occurred, roll the changes back
 *       transaction.rollback();
 *
 *       //and log the error
 *    }
 *  }
 * }
 * </pre>
 * </p>
 * <h3>Events</h3>
 * <p>
 * The Database class publishes events in certain situations. You can subscribe to these events and react to them. Events are published
 * before and after a certain situation happens. An event listener can prevent the situation to happen if it returns false when reacting to
 * the "before" event.
 * </p>
 * <h5>Note</h5>
 * <p>
 * An event listener must return event types it wants to subscribe to.<br/>
 * Database class fires events when one of its method is called. For example when a replicate() method is called, then a BEFORE_REPLICATION
 * and AFTER_REPLICATION events are fired. Database class can't fire events when a scheduled replication occurs on the server.
 * </p>
 * <h5>Available events</h5>
 * <p>
 * Events are described in {@link org.openntf.domino.ext.Database.Events}.
 * </p>
 * <h5>Usage</h5>
 * <p>
 * <ul>
 * <li>Implement the {@link org.openntf.domino.events.IDominoListener} interface.
 * <li>Attach the listener using {@link #addListener(org.openntf.domino.events.IDominoListener)} method.
 * </ul>
 * </p>
 * <h5>Example</h5>
 * <p>
 * See the examples in the {@link org.openntf.domino.events.IDominoListener} interface.
 * </p>
 * <h3>Creation and access</h3>
 * <p>
 * There are several ways you can use the Database class to access existing databases and to create new ones.
 * </p>
 * <ul>
 * <li>To access the current database if you are running as an agent, use {@link AgentContext#getCurrentDatabase()}</li>
 * <li>To access an existing database when you know its server and file name, use {@link Session#getDatabase(String, String)}.</li>
 * <li>To access an existing database when you know its server and replica ID, use {@link DbDirectory#openDatabaseByReplicaID(String)}.</li>
 * <li>To locate an existing database when you know its server but not its file name, use the {@link DbDirectory} class.</li>
 * <li>To access the current user's mail database, use {@link DbDirectory#openMailDatabase()}.</li>
 * <li>To open the default Web Navigator database, use {@link Session#getURLDatabase()}.</li>
 * <li>To access the available Domino Directories and Personal Address Books, use {@link Session#getAddressBooks()}.</li>
 * <li>To test for the existence of a database with a specific server and file name before accessing it, use
 * {@link DbDirectory#openDatabase(String)} or {@link DbDirectory#openDatabaseIfModified(String, java.util.Date)}.</li>
 * <li>To create a new database from an existing database, use {@link #createCopy}, {@link #createFromTemplate}, or
 * {@link #createReplica}.</li>
 * <li>To create a new database from scratch, use {@link DbDirectory#createDatabase(String, boolean)}.</li>
 * <li>To access a database when you have a contained object such as {@link View}, {@link Document}, {@link DocumentCollection},
 * {@link ACL}, or {@link Agent}, use the appropriate <code>Parent</code> (or <code>ParentDatabase</code>) property.</li>
 * </ul>
 * <h3>Usage</h3>
 * <p>
 * A database must be open before you can use all the properties and methods in the corresponding Database object. In most cases, the class
 * library automatically opens a database for you. But see {@link #isOpen()} for the exceptions.
 * </p>
 * <h3>Access levels</h3>
 * <p>
 * Notes throws an exception when you attempt to perform an operation for which the user does not have appropriate access. The properties
 * and methods that you can successfully use on a <code>Database</code> object are determined by these factors:
 * </p>
 * <ul>
 * <li>The user's access level to the database, as determined by the database access control list. The ACL determines if the user can open a
 * database, add documents to it, remove documents from it, modify the ACL, and so on.</li>
 * <li>The user's access level to the server on which the database resides, as determined by the Server document in the Domino
 * Directory.</li>
 * </ul>
 *
 * @see org.openntf.domino.events.IDominoListener
 * @see org.openntf.domino.ext.Database.Events
 */
public interface Database extends lotus.domino.Database, org.openntf.domino.Base<lotus.domino.Database>, org.openntf.domino.ext.Database,
Resurrectable, SessionDescendant, ExceptionDetails, Externalizable {

	/**
	 * Enum to allow easy access to Schema
	 *
	 * @since 5.0.0
	 */
	public static class Schema extends FactorySchema<Database, lotus.domino.Database, Session> {
		@Override
		public Class<Database> typeClass() {
			return Database.class;
		}

		@Override
		public Class<lotus.domino.Database> delegateClass() {
			return lotus.domino.Database.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Generic database utilities
	 *
	 * @deprecated RPr: As far as I know, this was only used in the DbDirectory
	 */
	@Deprecated
	public enum Utils {
		;
		/**
		 * Checks whether the database is a template with .ntf (Notes Template Facility) file type
		 *
		 * @param db
		 *            Database to check
		 * @return boolean whether the database has a .ntf suffix or not
		 * @since org.openntf.domino 4.5.0
		 */
		public static boolean isTemplate(final Database db) {
			return db.getFilePath().toLowerCase().endsWith(".ntf"); //$NON-NLS-1$
		}

		/**
		 * Checks whether the database is a database with .nsf-style (Notes Storage Facility) file type
		 *
		 * @param db
		 *            Database to check
		 * @return boolean whether the database has a .nsf, .nsh or .nsg suffix or not
		 * @since org.openntf.domino 4.5.0
		 */
		public static boolean isDatabase(final Database db) {
			String path = db.getFilePath().toLowerCase();
			return (path.endsWith(".nsf") || path.endsWith(".nsh") || path.endsWith(".nsg")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		/**
		 * Checks whether replication is disabled for the specified Database
		 *
		 * @param db
		 *            Database to check
		 * @return boolean whether {@link org.openntf.domino.Database#isReplicationDisabled()} is true
		 * @since org.openntf.domino 4.5.0
		 */
		public static boolean isReplicaCandidate(final Database db) {
			boolean result = true;
			result = db.isReplicationDisabled();
			return result;
		}

		/**
		 * Not currently implemented, just returns true
		 *
		 * @param db
		 *            Database to check
		 * @return boolean, currently always returns true
		 * @since org.openntf.domino 4.5.0
		 */
		@Incomplete
		public static boolean isTemplateCandidate(final Database db) {
			boolean result = true;
			//TODO do we actually want to add any future checks for this?
			return result;
		}
	}

	/**
	 * Comparator to allow easy checking whether two databases have the same filepath (e.g. on different servers)
	 *
	 * @Deprecated better use the DatabaseHolder for sorting
	 * @since org.openntf.domino 4.5.0
	 */
	@Deprecated
	public final static Comparator<Database> FILEPATH_COMPARATOR = new Comparator<Database>() {
		@Override
		public int compare(final Database o1, final Database o2) {
			return o1.getFilePath().compareToIgnoreCase(o2.getFilePath());
		}
	};

	/**
	 * Comparator to alow easy checking whether database A was modified before/after database B
	 *
	 * @Deprecated better use the DatabaseHolder for sorting
	 * @since org.openntf.domino 4.5.0
	 */
	@Deprecated
	public final static Comparator<Database> LASTMOD_COMPARATOR = new Comparator<Database>() {
		@Override
		public int compare(final Database o1, final Database o2) {
			return o1.getLastModifiedDate().compareTo(o2.getLastModifiedDate());
		}
	};

	/**
	 * Comparator to allow easy checking whether two databases have the same title
	 *
	 * @Deprecated better use the DatabaseHolder for sorting
	 * @since org.openntf.domino 4.5.0
	 */
	@Deprecated
	public final static Comparator<Database> TITLE_COMPARATOR = new Comparator<Database>() {
		@Override
		public int compare(final Database o1, final Database o2) {
			return o1.getTitle().compareToIgnoreCase(o2.getTitle());
		}
	};

	/**
	 * Comparator to allow easy checking whether two databases have the same API path (server!!filepath)
	 *
	 * @Deprecated better use the DatabaseHolder for sorting
	 * @since org.openntf.domino 4.5.0
	 */
	@Deprecated
	public final static Comparator<Database> APIPATH_COMPARATOR = new Comparator<Database>() {
		@Override
		public int compare(final Database o1, final Database o2) {
			return o1.getApiPath().compareToIgnoreCase(o2.getApiPath());
		}
	};

	/**
	 * Enum to allow easy access to database options for use with {@link org.openntf.domino.ext.Database#getOption(DBOption)} method
	 *
	 * @since org.openntf.domino 1.0.0
	 */
	// TODO: Remove OPTIMIZAION for 3.0
	public static enum DBOption implements INumberEnum<Integer> {
		LZ1(Database.DBOPT_LZ1), LZCOMPRESSION(Database.DBOPT_LZCOMPRESSION), MAINTAINLASTACCESSED(Database.DBOPT_MAINTAINLASTACCESSED),
		MOREFIELDS(Database.DBOPT_MOREFIELDS), NOHEADLINEMONITORS(Database.DBOPT_NOHEADLINEMONITORS),
		NOOVERWRITE(Database.DBOPT_NOOVERWRITE), NORESPONSEINFO(Database.DBOPT_NORESPONSEINFO),
		NOTRANSACTIONLOGGING(Database.DBOPT_NOTRANSACTIONLOGGING), NOUNREAD(Database.DBOPT_NOUNREAD),
		OPTIMIZAION(Database.DBOPT_OPTIMIZATION), OPTIMIZATION(Database.DBOPT_OPTIMIZATION),
		REPLICATEUNREADMARKSTOANY(Database.DBOPT_REPLICATEUNREADMARKSTOANY),
		REPLICATEUNREADMARKSTOCLUSTER(Database.DBOPT_REPLICATEUNREADMARKSTOCLUSTER),
		REPLICATEUNREADMARKSNEVER(Database.DBOPT_REPLICATEUNREADMARKSNEVER), SOFTDELETE(Database.DBOPT_SOFTDELETE),
		COMPRESSDESIGN(Database.DBOPT_COMPRESSDESIGN), COMPRESSDOCUMENTS(Database.DBOPT_COMPRESSDOCUMENTS),
		OUTOFOFFICEENABLED(Database.DBOPT_OUTOFOFFICEENABLED), NOSIMPLESEARCH(Database.DBOPT_NOSIMPLESEARCH),
		USEDAOS(Database.DBOPT_USEDAOS);

		/** The value_. */
		private final int value_;

		/**
		 * Instantiates a new dB option.
		 *
		 * @param value
		 *            the value
		 */
		private DBOption(final int value) {
			value_ = value;
		}

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		@Override
		public Integer getValue() {
			return value_;
		}

		public static DBOption valueOf(final int value) {
			return DominoEnumUtil.valueOf(DBOption.class, value);
		}
	}

	/**
	 * Enum to allow easy access to database signing options for use with {@link org.openntf.domino.Database} sign methods
	 *
	 * @since org.openntf.domino 1.0.0
	 */
	public static enum SignDocType implements INumberEnum<Integer> {

		/** The acl. */
		ACL(Database.DBSIGN_DOC_ACL),
		/** The agent. */
		AGENT(Database.DBSIGN_DOC_AGENT),
		/** The all. */
		ALL(Database.DBSIGN_DOC_ALL),
		/** The data. */
		DATA(Database.DBSIGN_DOC_DATA),
		/** The form. */
		FORM(Database.DBSIGN_DOC_FORM),
		/** The help. */
		HELP(Database.DBSIGN_DOC_HELP),
		/** The icon. */
		ICON(Database.DBSIGN_DOC_ICON),
		/** The replformula. */
		REPLFORMULA(Database.DBSIGN_DOC_REPLFORMULA),
		/** The sharedfield. */
		SHAREDFIELD(Database.DBSIGN_DOC_SHAREDFIELD),
		/** The view. */
		VIEW(Database.DBSIGN_DOC_VIEW);

		/** The value_. */
		private final int value_;

		/**
		 * Instantiates a new sign doc type.
		 *
		 * @param value
		 *            the value
		 */
		private SignDocType(final int value) {
			value_ = value;
		}

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		@Override
		public Integer getValue() {
			return value_;
		}

		public static SignDocType valueOf(final int value) {
			return DominoEnumUtil.valueOf(SignDocType.class, value);
		}
	}

	/**
	 * Enum to allow easy access to options for {@link org.openntf.domino.Database} compact methods
	 *
	 * @since org.openntf.domino 1.0.0
	 */
	public static enum CompactOption implements INumberEnum<Integer> {
		ARCHIVE_DELETE_COMPACT(Database.CMPC_ARCHIVE_DELETE_COMPACT), ARCHIVE_DELETE_ONLY(Database.CMPC_ARCHIVE_DELETE_ONLY),
		CHK_OVERLAP(Database.CMPC_CHK_OVERLAP), COPYSTYLE(Database.CMPC_COPYSTYLE),
		DISABLE_DOCTBLBIT_OPTMZN(Database.CMPC_DISABLE_DOCTBLBIT_OPTMZN), DISABLE_LARGE_UNKTBL(Database.CMPC_DISABLE_LARGE_UNKTBL),
		DISABLE_RESPONSE_INFO(Database.CMPC_DISABLE_RESPONSE_INFO), DISABLE_TRANSACTIONLOGGING(Database.CMPC_DISABLE_TRANSACTIONLOGGING),
		DISABLE_UNREAD_MARKS(Database.CMPC_DISABLE_UNREAD_MARKS), DISCARD_VIEW_INDICIES(Database.CMPC_DISCARD_VIEW_INDICES),
		ENABLE_DOCTBLBIT_OPTMZN(Database.CMPC_ENABLE_DOCTBLBIT_OPTMZN), ENABLE_LARGE_UNKTBL(Database.CMPC_ENABLE_LARGE_UNKTBL),
		ENABLE_RESPONSE_INFO(Database.CMPC_ENABLE_RESPONSE_INFO), ENABLE_TRANSACTIONLOGGING(Database.CMPC_ENABLE_TRANSACTIONLOGGING),
		ENABLE_UNREAD_MARKS(Database.CMPC_ENABLE_UNREAD_MARKS), IGNORE_COPYSTYLE_ERRORS(Database.CMPC_IGNORE_COPYSTYLE_ERRORS),
		MAX_4GB(Database.CMPC_MAX_4GB), NO_LOCKOUT(Database.CMPC_NO_LOCKOUT), RECOVER_INPLACE(Database.CMPC_RECOVER_INPLACE),
		RECOVER_REDUCE_INPLACE(Database.CMPC_RECOVER_REDUCE_INPLACE), REVERT_FILEFORMAT(Database.CMPC_REVERT_FILEFORMAT);

		private final int value_;

		private CompactOption(final int value) {
			value_ = value;
		}

		@Override
		public Integer getValue() {
			return value_;
		}

		public static CompactOption valueOf(final int value) {
			return DominoEnumUtil.valueOf(CompactOption.class, value);
		}
	}

	/**
	 * Enum to allow easy access to FTIndex options for {@link org.openntf.domino.Database#createFTIndex(java.util.Set, boolean)} method
	 *
	 * @since org.openntf.domino 2.5.0
	 */
	public static enum FTIndexOption implements INumberEnum<Integer> {
		ALL_BREAKS(Database.FTINDEX_ALL_BREAKS), ATTACHED_BIN_FILES(Database.FTINDEX_ATTACHED_BIN_FILES),
		ATTACHED_FILES(Database.FTINDEX_ATTACHED_FILES), CASE_SENSITIVE(Database.FTINDEX_CASE_SENSITIVE),
		ENCRYPTED_FIELDS(Database.FTINDEX_ENCRYPTED_FIELDS);

		private final int value_;

		private FTIndexOption(final int value) {
			value_ = value;
		}

		@Override
		public Integer getValue() {
			return value_;
		}

		public static FTIndexOption valueOf(final int value) {
			return DominoEnumUtil.valueOf(FTIndexOption.class, value);
		}
	}

	/**
	 * Enum to allow easy access to fixup options for {@link org.openntf.domino.Database#fixup(java.util.Set)} method
	 *
	 * @since org.openntf.domino 2.5.0
	 */
	public static enum FixupOption implements INumberEnum<Integer> {
		INCREMENTAL(Database.FIXUP_INCREMENTAL), NODELETE(Database.FIXUP_NODELETE), NOVIEWS(Database.FIXUP_NOVIEWS),
		QUICK(Database.FIXUP_QUICK), REVERT(Database.FIXUP_REVERT), TXLOGGED(Database.FIXUP_TXLOGGED), VERIFY(Database.FIXUP_VERIFY);

		private final int value_;

		private FixupOption(final int value) {
			value_ = value;
		}

		@Override
		public Integer getValue() {
			return value_;
		}

		public static FixupOption valueOf(final int value) {
			return DominoEnumUtil.valueOf(FixupOption.class, value);
		}
	}

	/**
	 * Enum to allow easy access to FTIndex frequency options for {@link org.openntf.domino.Database#setFTIndexFrequency(FTIndexFrequency)}
	 * method
	 *
	 * @since org.openntf.domino 2.5.0
	 */
	public static enum FTIndexFrequency implements INumberEnum<Integer> {
		DAILY(Database.FTINDEX_DAILY), HOURLY(Database.FTINDEX_HOURLY), IMMEDIATE(Database.FTINDEX_IMMEDIATE),
		SCHEDULED(Database.FTINDEX_SCHEDULED);

		private final int value_;

		private FTIndexFrequency(final int value) {
			value_ = value;
		}

		@Override
		public Integer getValue() {
			return value_;
		}

		public static FTIndexFrequency valueOf(final int value) {
			return DominoEnumUtil.valueOf(FTIndexFrequency.class, value);
		}
	}

	/**
	 * Enum to provide easy access to FTDomain sort options, used by {@link org.openntf.domino.Database} FTDomainSearch methods
	 *
	 * @since org.openntf.domino 2.5.0
	 */
	public static enum FTDomainSortOption implements INumberEnum<Integer> {
		SCORES(Database.FT_SCORES), DATE_DES(Database.FT_DATE_DES), DATE_ASC(Database.FT_DATE_ASC);

		private final int value_;

		private FTDomainSortOption(final int value) {
			value_ = value;
		}

		@Override
		public Integer getValue() {
			return value_;
		}

		public static FTDomainSortOption valueOf(final int value) {
			return DominoEnumUtil.valueOf(FTDomainSortOption.class, value);
		}
	}

	/**
	 * Enum to allow easy access to FTDomain search option, used by
	 * {@link org.openntf.domino.Database#FTDomainSearch(String, int, FTDomainSortOption, java.util.Set, int, int, String)}
	 *
	 * @since org.openntf.domino 2.5.0
	 */
	public static enum FTDomainSearchOption implements INumberEnum<Integer> {
		DATABASE(Database.FT_DATABASE), FILESYSTEM(Database.FT_FILESYSTEM), FUZZY(Database.FT_FUZZY), STEMS(Database.FT_STEMS);

		private final int value_;

		private FTDomainSearchOption(final int value) {
			value_ = value;
		}

		@Override
		public Integer getValue() {
			return value_;
		}

		public static FTDomainSearchOption valueOf(final int value) {
			return DominoEnumUtil.valueOf(FTDomainSearchOption.class, value);
		}
	}

	/**
	 * Enum to allow easy access to FTSearch sort options for use by {@link org.openntf.domino.Database} FTSearch methods
	 *
	 * @since org.openntf.domino 2.5.0
	 */
	public static enum FTSortOption implements INumberEnum<Integer> {
		SCORES(Database.FT_SCORES), DATE_DES(Database.FT_DATE_DES), DATE_ASC(Database.FT_DATE_ASC),
		DATECREATED_DES(Database.FT_DATECREATED_DES), DATECREATED_ASC(Database.FT_DATECREATED_ASC);

		private final int value_;

		private FTSortOption(final int value) {
			value_ = value;
		}

		@Override
		public Integer getValue() {
			return value_;
		}

		public static FTSortOption valueOf(final int value) {
			return DominoEnumUtil.valueOf(FTSortOption.class, value);
		}
	}

	/**
	 * Enum to allow easy access to FTSearch options for use by {@link org.openntf.domino.Database} FTSearch methods
	 *
	 * @since org.openntf.domino 2.5.0
	 */
	public static enum FTSearchOption implements INumberEnum<Integer> {
		FUZZY(Database.FT_FUZZY), STEMS(Database.FT_STEMS);

		private final int value_;

		private FTSearchOption(final int value) {
			value_ = value;
		}

		@Override
		public Integer getValue() {
			return value_;
		}

		public static FTSearchOption valueOf(final int value) {
			return DominoEnumUtil.valueOf(FTSearchOption.class, value);
		}
	}

	/**
	 * Enum to provide easy access to modified documents class, for use with {@link org.openntf.domino.Database} getModifiedDocuments
	 * methods
	 *
	 * @since org.openntf.domino 2.5.0
	 */
	public static enum ModifiedDocClass implements INumberEnum<Integer> {
		ACL(Database.DBMOD_DOC_ACL), AGENT(Database.DBMOD_DOC_AGENT), ALL(Database.DBMOD_DOC_ALL), DATA(Database.DBMOD_DOC_DATA),
		FORM(Database.DBMOD_DOC_FORM), HELP(Database.DBMOD_DOC_HELP), ICON(Database.DBMOD_DOC_ICON),
		REPLFORMULA(Database.DBMOD_DOC_REPLFORMULA), SHAREDFIELD(Database.DBMOD_DOC_SHAREDFIELD), VIEW(Database.DBMOD_DOC_VIEW);

		private final int value_;

		private ModifiedDocClass(final int value) {
			value_ = value;
		}

		@Override
		public Integer getValue() {
			return value_;
		}

		public static ModifiedDocClass valueOf(final int value) {
			return DominoEnumUtil.valueOf(ModifiedDocClass.class, value);
		}
	}

	/**
	 * Enum to allow easy access to database types for use with {@link org.openntf.domino.Database#getType()} method
	 *
	 * @since org.openntf.domino 2.5.0
	 */
	public static enum Type implements INumberEnum<Integer> {
		ADDR_BOOK(Database.DBTYPE_ADDR_BOOK), IMAP_SVR_PROXY(Database.DBTYPE_IMAP_SVR_PROXY), LIBRARY(Database.DBTYPE_LIBRARY),
		LIGHT_ADDR_BOOK(Database.DBTYPE_LIGHT_ADDR_BOOK), MAILBOX(Database.DBTYPE_MAILBOX), MAILFILE(Database.DBTYPE_MAILFILE),
		MULTIDB_SRCH(Database.DBTYPE_MULTIDB_SRCH), NEWS_SVR_PROXY(Database.DBTYPE_NEWS_SVR_PROXY),
		PERS_JOURNAL(Database.DBTYPE_PERS_JOURNAL), PORTFOLIO(Database.DBTYPE_PORTFOLIO), STANDARD(Database.DBTYPE_STANDARD),
		SUBSCRIPTIONS(Database.DBTYPE_SUBSCRIPTIONS), WEB_APP(Database.DBTYPE_WEB_APP);

		private final int value_;

		private Type(final int value) {
			value_ = value;
		}

		@Override
		public Integer getValue() {
			return value_;
		}

		/**
		 * Return the {@link Database.Type} of a numeric value
		 *
		 * @param value
		 *            the numeric value
		 * @return a {@link Database.Type} Object
		 */
		public static Type valueOf(final int value) {
			return DominoEnumUtil.valueOf(Type.class, value);
		}
	}

	public static enum DBPrivilege implements INumberEnum<Integer> {
		CREATE_DOCS(DBACL_CREATE_DOCS), DELETE_DOCS(DBACL_DELETE_DOCS), CREATE_PRIV_AGENTS(DBACL_CREATE_PRIV_AGENTS),
		CREATE_PRIV_FOLDERS_VIEWS(DBACL_CREATE_PRIV_FOLDERS_VIEWS), CREATE_SHARED_FOLDERS_VIEWS(DBACL_CREATE_SHARED_FOLDERS_VIEWS),
		CREATE_SCRIPT_AGENTS(DBACL_CREATE_SCRIPT_AGENTS), READ_PUBLIC_DOCS(DBACL_READ_PUBLIC_DOCS),
		WRITE_PUBLIC_DOCS(DBACL_WRITE_PUBLIC_DOCS), REPLICATE_COPY_DOCS(DBACL_REPLICATE_COPY_DOCS);

		private final int value_;

		private DBPrivilege(final int value) {
			value_ = value;
		}

		@Override
		public Integer getValue() {
			return value_;
		}

		public static DBPrivilege valueOf(final int value) {
			return DominoEnumUtil.valueOf(DBPrivilege.class, value);
		}
	}
	
	/**
	 * Strength values for database encryption.
	 * 
	 * @since 12.0.2
	 */
	enum EncryptionStrength implements INumberEnum<Integer> {
		NONE(DBENCRYPT_STRENGTH_NONE), SIMPLE(DBENCRYPT_STRENGTH_SIMPLE),
		MEDIUM(DBENCRYPT_STRENGTH_MEDIUM), STRONG(DBENCRYPT_STRENGTH_STRONG),
		AES128(DBENCRYPT_STRENGTH_AES128), AES256(DBENCRYPT_STRENGTH_AES256);

		private final int value_;

		private EncryptionStrength(final int value) {
			value_ = value;
		}

		@Override
		public Integer getValue() {
			return value_;
		}

		public static DBPrivilege valueOf(final int value) {
			return DominoEnumUtil.valueOf(DBPrivilege.class, value);
		}
	}

	/**
	 * The log from the access control list for a database. The database must be open to use this property.
	 *
	 * @return Vector with entries from the log
	 */
	@Override
	@Deprecated
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<String> getACLActivityLog();

	/**
	 * Compacts a local database.
	 *
	 * @return The difference in bytes between the size of the database before and after compacting.
	 */
	@Override
	public int compact();

	/**
	 * Compacts a local database with given options.
	 *
	 * @param options
	 *            One or more of the following constants. Combine constants by adding.
	 *            <ul>
	 *            <li>Database.CMPC_ARCHIVE_DELETE_COMPACT (1) a (archive and delete, then compact)</li>
	 *            <li>Database.CMPC_ARCHIVE_DELETE_ONLY (2) A (archive and delete with no compact; supersedes a)</li>
	 *            <li>Database.CMPC_CHK_OVERLAP (32768) o and O (check overlap)</li>
	 *            <li>Database.CMPC_COPYSTYLE (16) c and C (copy style; supersedes b and B)</li>
	 *            <li>Database.CMPC_DISABLE_DOCTBLBIT_OPTMZN (128) f (disable document table bit map optimization)</li>
	 *            <li>Database.CMPC_DISABLE_LARGE_UNKTBL (4096) k (disable large unknown table)</li>
	 *            <li>Database.CMPC_DISABLE_RESPONSE_INFO (512) H (disable "Don't support specialized response hierarchy")</li>
	 *            <li>Database.CMPC_DISABLE_TRANSACTIONLOGGING (262144) t (disable transaction logging)</li>
	 *            <li>Database.CMPC_DISABLE_UNREAD_MARKS (1048576) U (disable "Don't maintain unread marks")</li>
	 *            <li>Database.CMPC_DISCARD_VIEW_INDICES (32) d and D (discard view indexes)</li>
	 *            <li>Database.CMPC_ENABLE_DOCTBLBIT_OPTMZN (64) F (enable document table bit map optimization; supersedes f)</li>
	 *            <li>Database.CMPC_ENABLE_LARGE_UNKTBL (2048) K (enable large unknown table; supersedes k)</li>
	 *            <li>Database.CMPC_ENABLE_RESPONSE_INFO (256) h (enable "Don't support specialized response hierarchy"; supersedes H)</li>
	 *            <li>Database.CMPC_ENABLE_TRANSACTIONLOGGING (131072) T (enable transaction logging; supersedes t)</li>
	 *            <li>Database.CMPC_ENABLE_UNREAD_MARKS (524288) u (enable "Don't maintain unread marks"; supersedes U)</li>
	 *            <li>Database.CMPC_IGNORE_COPYSTYLE_ERRORS (1024) i (ignore copy-style errors)</li>
	 *            <li>Database.CMPC_MAX_4GB (16384) m and M (set maximum database size at 4 gigabytes)</li>
	 *            <li>Database.CMPC_NO_LOCKOUT (8192) l and L (do not lock out users)</li>
	 *            <li>Database.CMPC_RECOVER_INPLACE (8) B (recover unused space in-place and reduce file size; supersedes b)</li>
	 *            <li>Database.CMPC_RECOVER_REDUCE_INPLACE (4) b (recover unused space in-place without reducing file size)</li>
	 *            <li>Database.CMPC_REVERT_FILEFORMAT (65536) r and R (do not convert old file format)</li>
	 *            </ul>
	 * @return The difference in bytes between the size of the database before and after compacting.
	 * @deprecated replaced by {@link org.openntf.domino.ext.Database#compactWithOptions(java.util.Set)}
	 */
	@Override
	@Deprecated
	public int compactWithOptions(final int options);

	/**
	 * Compacts a local database with given options if specified amount of percent or unused space exceeds given spaceThreshold.
	 *
	 * @param options
	 *            For a list of options see {@link org.openntf.domino.ext.Database#compactWithOptions(options)}
	 * @param spaceThreshold
	 *            The value of the S option (compact if specified percent or amount of unused space) without the S, for example, "10" for 10
	 *            percent, "10K" for 10 kilobytes, or "10M" for 10 megabytes.
	 * @return The difference in bytes between the size of the database before and after compacting.
	 * @deprecated replaced by {@link org.openntf.domino.ext.Database#compactWithOptions(java.util.Set, String)}
	 */
	@Override
	@Deprecated
	public int compactWithOptions(final int options, final String spaceThreshold);

	/**
	 * Compacts a local database with given options.
	 *
	 * @param options
	 *            One or more command-line options supported by the Compact server task without the minus signs. Spaces are insignificant
	 *            except that a space cannot be placed in the S option between the number and the final K, k, M, or m. Options are processed
	 *            in their order of specification.
	 * @return The difference in bytes between the size of the database before and after compacting.
	 */
	@Override
	public int compactWithOptions(final String options);

	/**
	 * Creates an empty copy of the current database.
	 *
	 * @param server
	 *            The name of the server where the new database resides. Specify null or an empty string ("") to create a local copy.
	 * @param dbFile
	 *            The file name of the new copy.
	 */
	@Override
	public Database createCopy(final String server, final String dbFile);

	/**
	 * Creates an empty copy of the current database.
	 *
	 * @param server
	 *            The name of the server where the new database resides. Specify null or an empty string ("") to create a local copy.
	 * @param dbFile
	 *            The file name of the new copy.
	 * @param maxSize
	 *            The maximum size (in gigabytes) that you would like to assign to the new database. This parameter applies only to Release
	 *            4 databases or those created on a server that has not been upgraded to Release 5. Entering an integer greater than 4
	 *            generates a run-time error.
	 *
	 * @deprecated Applies to a Release 4 server only, use {@link #createCopy(String, String)}
	 */
	@Override
	@Deprecated
	public Database createCopy(final String server, final String dbFile, final int maxSize);

	/**
	 * Creates a document in this database
	 *
	 * @return The new document.
	 */
	@Override
	public Document createDocument();

	@Override
	public Document createDocument(final Object... keyValuePairs);

	/**
	 * Creates an empty collection for documents
	 *
	 * @return The new collection.
	 */
	@Override
	public DocumentCollection createDocumentCollection();

	/**
	 * Creates a new database from an existing database.
	 *
	 * @param server
	 *            The name of the server where the new database resides. Specify null or an empty string ("") to create a database on the
	 *            current computer.
	 * @param dbFile
	 *            The file name of the new database.
	 * @param inherit
	 *            Specify true if you want the new database to inherit future design changes from the template; otherwise, specify false.
	 * @return The new database, which contains the forms, subforms, fields, views, folders, navigators, agents, and documents of the
	 *         template.
	 */
	@Override
	public Database createFromTemplate(final String server, final String dbFile, final boolean inherit);

	/**
	 * Creates a new database from an existing database.
	 *
	 * @param server
	 *            The name of the server where the new database resides. Specify null or an empty string ("") to create a database on the
	 *            current computer.
	 * @param dbFile
	 *            The file name of the new database.
	 * @param inherit
	 *            Specify true if you want the new database to inherit future design changes from the template; otherwise, specify false.
	 * @param maxSize
	 *            The maximum size (in gigabytes) that you would like to assign to the new database. This parameter applies only to Release
	 *            4 databases or those created on a server that has not been upgraded to Release 5. Entering an integer greater than 4
	 *            generates a run-time error.
	 * @return The new database, which contains the forms, subforms, fields, views, folders, navigators, agents, and documents of the
	 *         template.
	 */
	@Override
	public Database createFromTemplate(final String server, final String dbFile, final boolean inherit, final int maxSize);

	/**
	 * Creates a new database from an existing database.
	 *
	 * @param server
	 *            The name of the server where the new database resides. Specify null or an empty string ("") to create a database on the
	 *            current computer.
	 * @param dbFile
	 *            The file name of the new database.
	 * @param inherit
	 *            Specify true if you want the new database to inherit future design changes from the template; otherwise, specify false.
	 * @param maxSize
	 *            The maximum size (in gigabytes) that you would like to assign to the new database. This parameter applies only to Release
	 *            4 databases or those created on a server that has not been upgraded to Release 5. Entering an integer greater than 4
	 *            generates a run-time error.
	 * @param doNotForce
	 *            TODO: Not sure what this is, documentation not complete in beta 2
	 * @return The new database, which contains the forms, subforms, fields, views, folders, navigators, agents, and documents of the
	 *         template.
	 * @since Domino V10
	 */
	@Override
	public Database createFromTemplate(final String server, final String dbFile, final boolean inherit, final int maxSize,
			final boolean doNotForce);

	/**
	 * Creates a full-text index for a database.
	 *
	 * @param options
	 *            Combine options with addition.
	 *            <ul>
	 *            <li>Database.FTINDEX_ALL_BREAKS (4) to index sentence and paragraph breaks</li>
	 *            <li>Database.FTINDEX_ATTACHED_BIN_FILES (16) to index attached files (binary)</li>
	 *            <li>Database.FTINDEX_ATTACHED_FILES (1) to index attached files (raw text)</li>
	 *            <li>Database.FTINDEX_CASE_SENSITIVE (8) to enable case-sensitive searches</li>
	 *            <li>Database.FTINDEX_ENCRYPTED_FIELDS (2) to index encrypted fields</li>
	 *            </ul>
	 * @param recreate
	 *            true removes any existing full-text index before creating one. If this parameter is false and an index exists, no action
	 *            is taken.
	 * @deprecated replaced by {@link org.openntf.domino.ext.Database#createFTIndex(java.util.Set, boolean)} method.
	 */
	@Override
	@Deprecated
	public void createFTIndex(final int options, final boolean recreate);

	/**
	 * Creates a note collection based on the current database.
	 *
	 * @param selectAllFlag
	 *            Sets all the "Select" note collection properties true or false.
	 * @return The new note collection.
	 * @see NoteCollection
	 */
	@Override
	public NoteCollection createNoteCollection(final boolean selectAllFlag);

	/**
	 * Creates an outline in the current database.
	 *
	 * @param name
	 *            A name for the outline.
	 * @return The new outline.
	 */
	@Override
	public Outline createOutline(final String name);

	/**
	 * Creates an outline in the current database.
	 *
	 * @param name
	 *            A name for the outline.
	 * @param defaultOutline
	 *            specify false to create an empty outline
	 * @return The new outline.
	 */
	@Override
	public Outline createOutline(final String name, final boolean defaultOutline);

	/**
	 * Creates a DB2 query view. To use query views, the Domino server must be using DB2 as the back end, have the DB2 Access server
	 * installed and have the database DB2 enabled.
	 *
	 * @param viewName
	 *            Name of the view
	 * @param query
	 *            query for the view
	 * @return The new view.
	 */
	@Override
	public View createQueryView(final String viewName, final String query);

	/**
	 * Creates a DB2 query view. To use query views, the Domino server must be using DB2 as the back end, have the DB2 Access server
	 * installed and have the database DB2 enabled.
	 *
	 * @param viewName
	 *            Name of the view
	 * @param query
	 *            query for the view
	 * @param templateView
	 *            a View to use as a template
	 * @return The new view.
	 */
	@Override
	public View createQueryView(final String viewName, final String query, final lotus.domino.View templateView);

	/**
	 * Creates a DB2 query view. To use query views, the Domino server must be using DB2 as the back end, have the DB2 Access server
	 * installed and have the database DB2 enabled.
	 *
	 * @param viewName
	 *            Name of the view
	 * @param query
	 *            query for the view
	 * @param templateView
	 *            a View to use as a template
	 * @param prohibitDesignRefresh
	 *            true if the Prohibit Design Refresh flag should be set on the new view
	 * @return The new view.
	 */
	@Override
	public View createQueryView(final String viewName, final String query, final lotus.domino.View templateView,
			final boolean prohibitDesignRefresh);

	/**
	 * Creates a replica of the current database at a new location. If a database with the specified file name already exists, an exception
	 * is thrown. The new replica has the same access control list as the current database.
	 *
	 * @param server
	 *            Creates a replica of the current database at a new location.
	 * @param dbFile
	 *            The file name of the replica.
	 * @return The new replica
	 */
	@Override
	public Database createReplica(final String server, final String dbFile);

	/**
	 * Creates a view in this database. The new view is based on another view checked as "Default design for new folders and views". If no
	 * such view exists, the new view is named "(untitled)", contains one column with "&commat;DocNumber" as its value and its selection
	 * formula is set to "SELECT &commat;All".
	 *
	 * @return The new View
	 */
	@Override
	public View createView();

	/**
	 * Creates a view in this database.
	 *
	 * @param viewName
	 *            A name for the view. Defaults to the "(untitled)" view. The view is created even if this name duplicates an existing view.
	 * @return The new View
	 */
	@Override
	public View createView(final String viewName);

	/**
	 * Creates a view in this database.
	 *
	 * @param viewName
	 *            A name for the view. Defaults to the "(untitled)" view. The view is created even if this name duplicates an existing view.
	 * @param selectionFormula
	 *            A selection formula for the new view
	 * @return The new View
	 */
	@Override
	public View createView(final String viewName, final String selectionFormula);

	/**
	 * Creates a view in this database based on another view overriding its selection formula.
	 *
	 * @param viewName
	 *            A name for the view. Defaults to the "(untitled)" view. The view is created even if this name duplicates an existing view.
	 * @param selectionFormula
	 *            A selection formula for the new view
	 * @param templateView
	 *            An existing view from which the new view is copied.
	 * @return The new View
	 */
	@Override
	public View createView(final String viewName, final String selectionFormula, final lotus.domino.View templateView);

	/**
	 * Creates a view in this database based on another view overriding its selection formula.
	 *
	 * @param viewName
	 *            A name for the view. Defaults to the "(untitled)" view. The view is created even if this name duplicates an existing view.
	 * @param selectionFormula
	 *            A selection formula for the new view
	 * @param templateView
	 *            An existing view from which the new view is copied.
	 * @param prohibitDesignRefresh
	 *            specify true to prohibit the view design from being refreshed
	 * @return The new View
	 */
	@Override
	public View createView(final String viewName, final String selectionFormula, final lotus.domino.View templateView,
			final boolean prohibitDesignRefresh);

	/**
	 * Ensures that a folder exists, creating it if necessary. If the folder exists, this method does nothing. If the folder does not exist,
	 * this method creates it.
	 *
	 * @param folder
	 *            The name of the folder
	 */
	@Override
	public void enableFolder(final String folder);

	/**
	 * Runs the Fixup task on a database. This method closes the target database, runs the Fixup task on it, waits for the Fixup task to
	 * complete, and reopens the database for program use.
	 *
	 */
	@Override
	public void fixup();

	/**
	 * Runs the Fixup task on a database. This method closes the target database, runs the Fixup task on it, waits for the Fixup task to
	 * complete, and reopens the database for program use.
	 *
	 * @param options
	 *            One or more of the following. Add options to combine them.
	 *            <ul>
	 *            <li>Database.FIXUP_INCREMENTAL (4) checks only documents since last Fixup</li>
	 *            <li>Database.FIXUP_NODELETE (16) prevents Fixup from deleting corrupted documents</li>
	 *            <li>Database.FIXUP_NOVIEWS (64) does not check views</li>
	 *            <li>Database.FIXUP_QUICK (2) checks documents more quickly but less thoroughly</li>
	 *            <li>Database.FIXUP_REVERT (32) reverts ID tables to the previous release format</li>
	 *            <li>Database.FIXUP_TXLOGGED (8) includes databases enabled for transaction logging</li>
	 *            <li>Database.FIXUP_VERIFY (1) makes no modifications</li>
	 *            </ul>
	 *
	 * @deprecated replaced by {@link org.openntf.domino.ext.Database#fixup(java.util.Set)} method.
	 */
	@Override
	@Deprecated
	public void fixup(final int options);

	/**
	 * Conducts a Domain Search, that is, a full-text search of all databases listed in a Domain Catalog and marked as included for
	 * multi-database indexing. The instance of this Database object must represent a Domain Catalog. The current Database object must
	 * represent a Domain Catalog.
	 * <h5>Query syntax</h5>
	 * <p>
	 * To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes. Remember to
	 * escape quotes if you are inside a literal. Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see
	 * "Refining a search query using operators" in Notes Help. Search for "query syntax" in the Domino Designer Eclipse help system or
	 * information center (for example, http://publib.boulder.ibm.com/infocenter/domhelp/v8r0/index.jsp), both of which include Notes.
	 * </p>
	 *
	 *
	 * @param query
	 *            The full-text query
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the query. Set this parameter to 0 to receive all matching
	 *            documents.
	 * @param sortOpt
	 *            Use one of the following constants to specify a sorting option
	 *            <ul>
	 *            <li>Database.FT_SCORES (default) sorts by relevance score. When the collection is sorted by relevance, the highest
	 *            relevance appears first. To access the relevance score of each document in the collection, use the FTSearchScore property
	 *            in Document.</li>
	 *            <li>Database.FT_DATE_DES sorts by document creation date in descending order.</li>
	 *            <li>Database.FT_DATE_ASC sorts by document creation date in ascending order.</li>
	 *
	 * @param otherOpt
	 *            Use the following constants to specify additional search options. To specify more than one option, use a logical or
	 *            operation.
	 *            <ul>
	 *            <li>Database.FT_DATABASE includes Domino databases in the search scope.</li>
	 *            <li>Database.FT_FILESYSTEM includes files other than Domino databases in the search scope.</li>
	 *            <li>Database.FT_FUZZY specifies a fuzzy search.</li>
	 *            <li>Database.FT_STEMS uses stem words as the basis of the search.</li>
	 *            </ul>
	 * @param start
	 *            The starting page to return.
	 * @param count
	 *            The starting page to return.
	 * @param entryForm
	 *            The name of the search form in the domain catalog, for example, "Domain Search".
	 * @return A document with a rich text field named "Body" that contains a table of matching document titles.
	 * @deprecated replaced by
	 *             {@link org.openntf.domino.ext.Database#FTDomainSearch(String, int, FTDomainSortOption, java.util.Set, int, int, String)}
	 *             method.
	 */
	@Override
	@Deprecated
	public Document FTDomainSearch(final String query, final int maxDocs, final int sortOpt, final int otherOpt, final int start,
			final int count, final String entryForm);

	/**
	 * Conducts a full-text search of all the documents in this database. If the database is not full-text indexed, this method works, but
	 * less efficiently. To test for an index, use the {@link #isFTIndexed()} property. To create an index on a local database, use the
	 * {@link #updateFTIndex()} method.
	 * <p>
	 * This method returns a maximum of 5,000 documents by default. The Notes.ini variable FT_MAX_SEARCH_RESULTS overrides this limit for
	 * indexed databases or databases that are not indexed but that are running in an agent on the client. For a database that is not
	 * indexed and is running in an agent on the server, you must set the TEMP_INDEX_MAX_DOC Notes.ini variable as well. The absolute
	 * maximum is 2,147,483,647.
	 * </p>
	 * <p>
	 * This method searches all documents in a database. To search only documents found in a particular view, use the FTSearch method in
	 * View. To search only documents found in a particular document collection, use the FTSearch method in DocumentCollection.
	 * </p>
	 *
	 * @param query
	 *            The full-text query.
	 * @return A collection of documents that match the full-text query, sorted by the selected option. If no matches are found, the
	 *         collection has a count of 0.
	 */
	@Override
	public DocumentCollection FTSearch(final String query);

	/**
	 * Conducts a full-text search of all the documents in this database.
	 *
	 * @param query
	 *            The full-text query.
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the query. Set this parameter to 0 to receive all matching
	 *            documents (up to 5,000).
	 *
	 * @return A collection of documents that match the full-text query, sorted by the selected option. If no matches are found, the
	 *         collection has a count of 0.
	 * @see Database#FTSearch(String)
	 */
	@Override
	public DocumentCollection FTSearch(final String query, final int maxDocs);

	/**
	 * Conducts a full-text search of all the documents in this database.
	 *
	 * @param query
	 *            The full-text query.
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the query. Set this parameter to 0 to receive all matching
	 *            documents (up to 5,000).
	 * @param sortOpt
	 *            Use one of the following constants to specify a sorting option:
	 *            <ul>
	 *            <li>Database.FT_SCORES (default) sorts by relevance score. When the collection is sorted by relevance, the highest
	 *            relevance appears first. To access the relevance score of each document in the collection, use the FTSearchScore property
	 *            in Document.</li>
	 *            <li>Database.FT_DATECREATED_DES sorts by document creation date in descending order.</li>
	 *            <li>Database.FT_DATECREATED_ASC sorts by document creation date in ascending order.</li>
	 *            <li>Database.FT_DATE_DES sorts by document date in descending order.</li>
	 *            <li>Database.FT_DATE_ASC sorts by document date in ascending order.</li>
	 *            </ul>
	 * @param otherOpt
	 *            Use the following constants to specify additional search options. To specify more than one option, use a logical or
	 *            operation.
	 *            <ul>
	 *            <li>Database.FT_FUZZY specifies a fuzzy search.</li>
	 *            <li>Database.FT_STEMS uses stem words as the basis of the search.</li>
	 *            </ul>
	 *
	 * @return A collection of documents that match the full-text query, sorted by the selected option. If no matches are found, the
	 *         collection has a count of 0.
	 * @see Database#FTSearch(String)
	 * @deprecated replaced by {@link org.openntf.domino.ext.Database#FTSearch(String, int, FTSortOption, java.util.Set)} method.
	 */
	@Override
	@Deprecated
	public DocumentCollection FTSearch(final String query, final int maxDocs, final int sortOpt, final int otherOpt);

	/**
	 * Conducts a full-text search of all the documents in this database. If the database is not full-text indexed, this method works, but
	 * less efficiently. To test for an index, use the {@link #isFTIndexed()} property. To create an index on a local database, use the
	 * {@link #updateFTIndex()} method.
	 * <p>
	 * This method returns a maximum of 5,000 documents by default. The Notes.ini variable FT_MAX_SEARCH_RESULTS overrides this limit for
	 * indexed databases or databases that are not indexed but that are running in an agent on the client. For a database that is not
	 * indexed and is running in an agent on the server, you must set the TEMP_INDEX_MAX_DOC Notes.ini variable as well. The absolute
	 * maximum is 2,147,483,647.
	 * </p>
	 * <p>
	 * This method searches all documents in a database. To search only documents found in a particular view, use the FTSearch method in
	 * View. To search only documents found in a particular document collection, use the FTSearch method in DocumentCollection.
	 * </p>
	 *
	 * @param query
	 *            The full-text query
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the query. Set this parameter to 0 to receive all matching
	 *            documents (up to 5,000).
	 * @param sortOpt
	 *            Use one of the following constants to specify a sorting option:
	 *            <ul>
	 *            <li>{@link FTSortOption#SCORES} (default) sorts by relevance score. When the collection is sorted by relevance, the
	 *            highest relevance appears first. To access the relevance score of each document in the collection, use the FTSearchScore
	 *            property in Document.</li>
	 *            <li>{@link FTSortOption#DATECREATED_DES} sorts by document creation date in descending order.</li>
	 *            <li>{@link FTSortOption#DATECREATED_ASC} sorts by document creation date in ascending order.</li>
	 *            <li>{@link FTSortOption#DATE_DES} sorts by document date in descending order.</li>
	 *            <li>{@link FTSortOption#DATE_ASC} sorts by document date in ascending order.</li>
	 *            </ul>
	 * @param otherOpt
	 *            Use the following constants to specify additional search options. To specify more than one option, use a logical or
	 *            operation.
	 *            <ul>
	 *            <li>Database.FT_FUZZY specifies a fuzzy search.</li>
	 *            <li>Database.FT_STEMS uses stem words as the basis of the search.</li>
	 *            </ul>
	 * @return A collection of documents that match the full-text query, sorted by the selected option. If no matches are found, the
	 *         collection has a count of 0.
	 */
	public DocumentCollection FTSearch(final String query, final int maxDocs, final FTSortOption sortOpt, final int otherOpt);

	/**
	 * Conducts a full-text search of all the documents in a database. This method is the same the same as {@link Database#FTSearch()} plus
	 * the start parameter.
	 *
	 * @param query
	 *            The full-text query
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the query. Set this parameter to 0 to receive all matching
	 *            documents.
	 * @param sortOpt
	 *            Use one of the following constants to specify a sorting option:
	 *            <ul>
	 *            <li>Database.FT_SCORES (default) sorts by relevance score. When the collection is sorted by relevance, the highest
	 *            relevance appears first. To access the relevance score of each document in the collection, use the FTSearchScore property
	 *            in Document.</li>
	 *            <li>Database.FT_DATECREATED_DES sorts by document creation date in descending order.</li>
	 *            <li>Database.FT_DATECREATED_ASC sorts by document creation date in ascending order.</li>
	 *            <li>Database.FT_DATE_DES sorts by document date in descending order.</li>
	 *            <li>Database.FT_DATE_ASC sorts by document date in ascending order.</li>
	 *            </ul>
	 * @param otherOpt
	 *            Use the following constants to specify additional search options. To specify more than one option, use a logical or
	 *            operation.
	 *            <ul>
	 *            <li>Database.FT_FUZZY specifies a fuzzy search.</li>
	 *            <li>Database.FT_STEMS uses stem words as the basis of the search.</li>
	 *            </ul>
	 * @param start
	 *            The starting document to return.
	 * @return A collection of documents that match the full-text query, sorted by the selected option. If no matches are found, the
	 *         collection has a count of 0.
	 * @deprecated replaced by {@link org.openntf.domino.ext.Database#FTSearchRange(String, int, FTSortOption, java.util.Set, int)} method
	 */
	@Override
	@Deprecated
	public DocumentCollection FTSearchRange(final String query, final int maxDocs, final int sortOpt, final int otherOpt, final int start);

	/**
	 * Conducts a full-text search of all the documents in a database. This method is the same the same as {@link Database#FTSearch()} plus
	 * the start parameter.
	 *
	 * @param query
	 *            The full-text query
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the query. Set this parameter to 0 to receive all matching
	 *            documents.
	 * @param sortOpt
	 *            Use one of the following constants to specify a sorting option:
	 *            <ul>
	 *            <li>{@link FTSortOption#SCORES} (default) sorts by relevance score. When the collection is sorted by relevance, the
	 *            highest relevance appears first. To access the relevance score of each document in the collection, use the FTSearchScore
	 *            property in Document.</li>
	 *            <li>{@link FTSortOption#DATECREATED_DES} sorts by document creation date in descending order.</li>
	 *            <li>{@link FTSortOption#DATECREATED_ASC} sorts by document creation date in ascending order.</li>
	 *            <li>{@link FTSortOption#DATE_DES} sorts by document date in descending order.</li>
	 *            <li>{@link FTSortOption#DATE_ASC} sorts by document date in ascending order.</li>
	 *            </ul>
	 * @param otherOpt
	 *            Use the following constants to specify additional search options. To specify more than one option, use a logical or
	 *            operation.
	 *            <ul>
	 *            <li>Database.FT_FUZZY specifies a fuzzy search.</li>
	 *            <li>Database.FT_STEMS uses stem words as the basis of the search.</li>
	 *            </ul>
	 * @param start
	 *            The starting document to return.
	 * @return A collection of documents that match the full-text query, sorted by the selected option. If no matches are found, the
	 *         collection has a count of 0.
	 */
	public DocumentCollection FTSearchRange(final String query, final int maxDocs, final FTSortOption sortOpt, final int otherOpt,
			final int start);

	/**
	 * The access control list for a database. The database must be open to use this property.
	 *
	 * @return the Access control list
	 */
	@Override
	public ACL getACL();

	/**
	 * Finds an agent in this database, given the agent name. The return value is null if the current user (as obtained by
	 * {@link Session#getUserName()} is not the owner of the private agent.
	 *
	 * @param name
	 *            The name of the agent.
	 * @return The agent whose name matches the parameter
	 */
	@Override
	public Agent getAgent(final String name);

	/**
	 * All of the agents in this database. If the program runs on a workstation or is remote (IIOP), the return vector includes shared
	 * agents and private agents that belong to the current user. If the program runs on a server, the return vector includes only shared
	 * agents.
	 *
	 * The database must be open to use this property.
	 *
	 * @return Collection of agents
	 *
	 */
	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<Agent> getAgents();

	/**
	 * An unsorted collection containing all the documents in a database. The FTSearch and search methods return smaller collections of
	 * documents that meet specific criteria. Using the AllDocuments property is more efficient than using the search method with an
	 * &commat;All formula. The database must be open to use this property.
	 *
	 *
	 */
	@Override
	public DocumentCollection getAllDocuments();

	/**
	 * Creates a document collection of all read documents in the database. If the database does not track unread marks, all documents are
	 * considered read.
	 *
	 */
	@Override
	public DocumentCollection getAllReadDocuments();

	/**
	 * Creates a document collection of all read documents in the database on behalf of the given name. If the database does not track
	 * unread marks, all documents are considered read.
	 *
	 * @param userName
	 *            Name of the user whose read documents to return.
	 */
	@Override
	public DocumentCollection getAllReadDocuments(final String userName);

	/**
	 * Creates a document collection of all unread documents in the database. Creates a document collection of all unread documents in the
	 * database.
	 *
	 */
	@Override
	public DocumentCollection getAllUnreadDocuments();

	/**
	 * Creates a document collection of all unread documents in the database on behalf of the given name. Creates a document collection of
	 * all unread documents in the database.
	 *
	 * @param userName
	 *            Name of the user whose unread documents to return.
	 */
	@Override
	public DocumentCollection getAllUnreadDocuments(final String userName);

	/**
	 * The categories under which a database appears in the Database Library. Multiple categories are separated by a comma or semicolon. A
	 * database retrieved through {@link DbDirectory#getFirstDatabase(org.openntf.domino.DbDirectory.Type)} or
	 * {@link DbDirectory#getNextDatabase()} in {@link DbDirectory} does not have to be open for getCategories. Otherwise, the database must
	 * be open.
	 */
	@Override
	public String getCategories();

	/**
	 * The date/time a database was created. The database must be open to use this property.
	 */
	@Override
	public DateTime getCreated();

	/**
	 * The current user's access level to this database. If a program runs on a workstation or is remote (IIOP), CurrentAccessLevel is
	 * determined by the access level of the current user. If a program runs on a server, CurrentAccessLevel is determined by the access
	 * level of the person who last saved the program (the owner). The database must be open to use this property.
	 *
	 * @return One of the values:
	 *         <ul>
	 *         <li>ACL.LEVEL_AUTHOR</li>
	 *         <li>ACL.LEVEL_DEPOSITOR</li>
	 *         <li>ACL.LEVEL_DESIGNER</li>
	 *         <li>ACL.LEVEL_EDITOR</li>
	 *         <li>ACL.LEVEL_MANAGER</li>
	 *         <li>ACL.LEVEL_NOACCESS</li>
	 *         <li>ACL.LEVEL_READER</li>
	 *         </ul>
	 */
	@Override
	public int getCurrentAccessLevel();

	/* (non-Javadoc)
	 * @see lotus.domino.Database#getDB2Schema()
	 */
	@Override
	public String getDB2Schema();

	/**
	 * The name of the design template from which a database inherits its design. If the database does not inherit its design from a design
	 * template, returns an empty string. If a database inherits a specific design element (such as a form) but not its entire design from a
	 * template, this property returns an empty string. A database does not need to be open to use this property.
	 */
	@Override
	public String getDesignTemplateName();

	/**
	 * Finds a document in this database, given the document note ID.
	 *
	 * @param noteid
	 *            The note ID of a document to find. If you get a note ID from &commat;NoteID, delete the "NT" prefix.
	 *
	 * @return The document whose note ID matches the parameter.
	 */
	@Override
	public Document getDocumentByID(final String noteid);

	/**
	 * Finds a document in a database, given the document universal ID (UNID). Not matching the UNID to a document in the database throws
	 * NotesError.NOTES_ERR_BAD_UNID (4091).
	 *
	 * @param unid
	 *            The universal ID of a document.
	 * @return The document whose universal ID matches the parameter.
	 */
	@Override
	public Document getDocumentByUNID(final String unid);

	/**
	 * Instantiates a document in this database and returns a Document object for it. This method is typically used for either the Server
	 * Web Navigator or Personal Web Navigator database, but can be called on any Database object.
	 *
	 * @param url
	 *            The desired uniform resource locator (URL), for example, http://www.acme.com. Specify the entire URL starting with http.
	 *            You can enter a maximum string length of 15K.
	 * @param reload
	 *            The desired uniform resource locator (URL), for example, http://www.acme.com. Specify the entire URL starting with http.
	 *            You can enter a maximum string length of 15K.
	 * @return The Notes document that represents the URL document you specified.
	 */
	@Override
	public Document getDocumentByURL(final String url, final boolean reload);

	/**
	 * Instantiates a document in this database and returns a Document object for it. This method is typically used for either the Server
	 * Web Navigator or Personal Web Navigator database, but can be called on any Database object.
	 *
	 * @param url
	 *            The desired uniform resource locator (URL), for example, http://www.acme.com. Specify the entire URL starting with http.
	 *            You can enter a maximum string length of 15K.
	 * @param reload
	 *            The desired uniform resource locator (URL), for example, http://www.acme.com. Specify the entire URL starting with http.
	 *            You can enter a maximum string length of 15K.
	 * @param reloadIfModified
	 *            Specify true to reload the page only if it has been modified on its Internet server, false to load the page from the
	 *            Internet only if it is not already in the Web Navigator database.
	 * @param urlList
	 *            Web pages can contain URL links to other Web pages. You can specify whether to save the URLs in a field called URLLinksn
	 *            in the Notes document. (The Web Navigator creates a new URLLinksn field each time the field size reaches 64K. For example,
	 *            the first URLLinks field is URLLinks1, the second is URLLinks2, and so on.) Specify true if you want to save the URLs in
	 *            the URLLinksn field(s). Specify false if you do not want to save the URLs in the URLLinksn field(s). If you save the URLs,
	 *            you can use them in agents. For example, you can create an agent that opens Web pages in the Web Navigator database and
	 *            then loads all the Web pages saved in each of the URLLinksn field(s). <br/>
	 *            CAUTION: Saving URLs in the URLLinksn field(s) may affect performance.
	 * @param charSet
	 *            Enter the MIME character set (for example, ISO-2022-JP for Japanese or ISO-8859-1 for United States) that you want the Web
	 *            Navigator to use when processing the Web page.
	 * @param webUser
	 *            Some Internet servers require you to obtain a username before you can access their pages. This parameter allows you to
	 *            enter the username that you previously obtained from the Full-text server.
	 * @param webPassword
	 *            Some full-text servers require you to obtain a password before you can access their pages. This parameter allows you to
	 *            enter the password that you previously obtained from the Internet server.
	 * @param proxyUser
	 *            Some proxy servers require that you specify a username in order to connect through them. This parameter allows you to
	 *            enter the username for the proxy server. See your administrator for the username required by the proxy.
	 * @param proxyPassword
	 *            Some proxy servers require that you specify a password in order to connect through them. This parameter allows you to
	 *            enter the password for the proxy server. See your administrator for the password required by the proxy.
	 * @param returnImmediately
	 *            Specify true to return immediately and not wait for completion of the retrieval. If you specify true, getDocumentByURL
	 *            does not return the Document object representing the URL document. This parameter is useful for offline storage purposes;
	 *            in this case, you do not need the Document object and do not have to wait for completion of the operation. This parameter
	 *            is ignored and false is forced if the database being opened is not local to the execution context.
	 * @return The Notes document that represents the URL document you specified.
	 */
	@Override
	public Document getDocumentByURL(final String url, final boolean reload, final boolean reloadIfModified, final boolean urlList,
			final String charSet, final String webUser, final String webPassword, final String proxyUser, final String proxyPassword,
			final boolean returnImmediately);

	/**
	 * The ODS (on-disk structure) version of a database. The database must be open to use this property.
	 */
	@Override
	public int getFileFormat();

	/**
	 * The file name of this database, excluding the path. A database does not need to be open to use this property.
	 */
	@Override
	public String getFileName();

	/**
	 * The path and file name of this database. If the database is open and on the Notes workstation, FilePath returns the complete path
	 * (for example, C:\Notes\data\sub\db.nsf).
	 *
	 * If the database is on a Domino server, or closed on the Notes workstation, FilePath returns the path relative to the data directory
	 * (for example, sub\db.nsf).
	 *
	 * If the database is accessed through a directory or database link, FilePath returns the link location if the code is running locally
	 * (even if the database is on a server) so that the database appears to be where the link is. FilePath returns the actual database
	 * location if the code is running on a server (for example, a scheduled agent).
	 */
	@Override
	public String getFilePath();

	/**
	 * Indicates whether this database maintains folder references for documents.
	 */
	@Override
	public boolean getFolderReferencesEnabled();

	/**
	 * Finds a form in a database, given the form name.
	 *
	 * @param name
	 *            The name or an alias of the form.
	 * @return The form whose name or alias matches the parameter.
	 */
	@Override
	public Form getForm(final String name);

	/**
	 * All the forms in this database. The database must be open to use this property.
	 *
	 * @return Vector of instances of the Form class representing all the forms.
	 */
	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<Form> getForms();

	/**
	 * Update frequency of this database's full-text index.This property applies only to databases on servers. The database must have a
	 * full-text index. The database must be open to use this property.
	 *
	 * @return The update frequency of the full-text index. One of:
	 *         <ul>
	 *         <li>Database.FTINDEX_DAILY (1)</li>
	 *         <li>Database.FTINDEX_HOURLY (3)</li>
	 *         <li>Database.FTINDEX_IMMEDIATE (4)</li>
	 *         <li>Database.FTINDEX_SCHEDULED (2)</li>
	 *         </ul>
	 */
	@Override
	public int getFTIndexFrequency();

	/**
	 * The Domino URL of this database when HTTP protocols are in effect. If HTTP protocols are not available, this property returns an
	 * empty string.
	 */
	@Override
	public String getHttpURL();

	/**
	 * The date that a database was last checked by the Fixup task. The database must be open to use this property.
	 */
	@Override
	public DateTime getLastFixup();

	/**
	 * The date that the database's full-text index was last updated. If the database does not have a full-text index, returns null. The
	 * database must be open to use this property.
	 */
	@Override
	public DateTime getLastFTIndexed();

	/**
	 * The date/time that the database was last modified. The database must be open to use this property.
	 */
	@Override
	public DateTime getLastModified();

	/**
	 * The maximum number of entries allowed in the $Revisions field. This property corresponds to "Limit entries in $Revisions fields" in
	 * the database advanced properties of the UI.
	 *
	 * This property must be an integer in the range 0 - 2147483647. When setting it:
	 * <ul>
	 * <li>Any fraction is truncated.</li>
	 * <li>A value less than 0 raises NotesError.NOTES_ERR_NEGATIVE_VALUE (4631) "Value can not be negative."</li>
	 * <li>A value greater than 2147483647 throws NotesError.NOTES_ERR_LONG_OVERFLOW (4673) "Value must be positive and less than
	 * 2147483648."</li>
	 * </ul>
	 * A value of 0 means no limit. When $Revisions reaches the limit, a new entry results in deletion of the oldest entry. <br/>
	 * The database must be open to use this property.
	 *
	 * @return The maximum number of entries allowed in the $Revisions field.
	 */
	@Override
	public double getLimitRevisions();

	/**
	 * The maximum number of entries allowed in the $UpdatedBy field. This property corresponds to "Limit entries in $UpdatedBy fields" in
	 * the database advanced properties of the UI.
	 *
	 * This property must be an integer in the range 0 - 2147483647. When setting it:
	 * <ul>
	 * <li>Any fraction is truncated.</li>
	 * <li>A value less than 0 throws NotesError.NOTES_ERR_NEGATIVE_VALUE (4631) "Value can not be negative."</li>
	 * <li>A value greater than 2147483647 throws NotesError.NOTES_ERR_LONG_OVERFLOW (4673) "Value must be positive and less than
	 * 2147483648."</li>
	 * <ul>
	 * A value of 0 means no limit. When $UpdatedBy reaches the limit, a new entry results in deletion of the oldest entry. <br/>
	 * The database must be open to use this property.
	 *
	 * @return The maximum number of entries allowed in the $UpdatedBy field.
	 */
	@Override
	public double getLimitUpdatedBy();

	/**
	 * Indicates whether a database appears in database catalogs. This property corresponds to "List in Database Catalog" in the database
	 * design properties of the UI.
	 *
	 * Categories determines the categories under which the database is listed.
	 *
	 * The database must be open to use this property.
	 *
	 * @return true, if the database appears in database catalogs or false if not
	 */
	@Override
	public boolean getListInDbCatalog();

	/**
	 * People, servers, and groups that have Manager access to a database. The database must be open to use this property.
	 */
	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<String> getManagers();

	/**
	 * The maximum size of a database in kilobytes. The database must be open to use this property.
	 */
	@Override
	public long getMaxSize();

	/**
	 * Gets all the documents in this database (because it defaults to the creation time of the database).
	 *
	 * @return DocumentCollection. A collection containing the modified documents.
	 */
	@Override
	public DocumentCollection getModifiedDocuments();

	/**
	 * Gets all the documents in this database that are modified since the specified time.
	 *
	 * @param since
	 *            The start time for collecting the modified documents.
	 * @return DocumentCollection. A collection containing the modified documents.
	 */
	@Override
	public DocumentCollection getModifiedDocuments(final lotus.domino.DateTime since);

	/**
	 * Gets all the documents of given type in this database that are modified since the specified time.
	 *
	 * @param since
	 *            The start time for collecting the modified documents.
	 * @param noteClass
	 *            One of the following to specify the type or types of document collected. You can combine types with a logical or.
	 *            <ul>
	 *            <li>Database.DBMOD_DOC_ACL (64)</li>
	 *            <li>Database.DBMOD_DOC_AGENT (512)</li>
	 *            <li>Database.DBMOD_DOC_ALL (32767)</li>
	 *            <li>Database.DBMOD_DOC_DATA (1)</li>
	 *            <li>Database.DBMOD_DOC_FORM (4)</li>
	 *            <li>Database.DBMOD_DOC_HELP (256)</li>
	 *            <li>Database.DBMOD_DOC_ICON (16)</li>
	 *            <li>Database.DBMOD_DOC_REPLFORMULA (2048)</li>
	 *            <li>Database.DBMOD_DOC_SHAREDFIELD (1024)</li>
	 *            <li>Database.DBMOD_DOC_VIEW (8)</li>
	 *            <ul>
	 *
	 * @return DocumentCollection. A collection containing the modified documents.
	 *
	 * @deprecated replaced by {@link org.openntf.domino.ext.Database#getModifiedDocuments(java.util.Date, ModifiedDocClass)} method.
	 */
	@Override
	@Deprecated
	public DocumentCollection getModifiedDocuments(final lotus.domino.DateTime since, final int noteClass);

	/**
	 * The Domino URL of this database when Notes protocols are in effect. If Notes protocols are not available, this property returns an
	 * empty string.
	 */
	@Override
	public String getNotesURL();

	/**
	 * Gets the value of a database option.
	 *
	 * @param optionname
	 *            One of the following:
	 *            <ul>
	 *            <li>Database.DBOPT_LZ1 uses LZ1 compression for attachments</li>
	 *            <li>Database.DBOPT_LZCOMPRESSION uses LZ1 compression for attachments</li>
	 *            <li>Database.DBOPT_MAINTAINLASTACCESSED maintains LastAcessed property</li>
	 *            <li>Database.DBOPT_MOREFIELDS allows more fields in database</li>
	 *            <li>Database.DBOPT_NOHEADLINEMONITORS doesn't allow headline monitoring</li>
	 *            <li>Database.DBOPT_NOOVERWRITE doesn't overwrite free space</li>
	 *            <li>Database.DBOPT_NORESPONSEINFO doesn't support specialized response hierarchy</li>
	 *            <li>Database.DBOPT_NOTRANSACTIONLOGGING disables transaction logging</li>
	 *            <li>Database.DBOPT_NOUNREAD doesn't maintain unread marks</li>
	 *            <li>Database.DBOPT_OPTIMIZATION enables document table bitmap optimization</li>
	 *            <li>Database.DBOPT_REPLICATEUNREADMARKSTOANY replicates unread marks to all servers</li>
	 *            <li>Database.DBOPT_REPLICATEUNREADMARKSTOCLUSTER replicates unread marks to clustered servers only</li>
	 *            <li>Database.DBOPT_SOFTDELETE allows soft deletions</li>
	 *            </ul>
	 * @return true if the option is enabled or false if not
	 * @deprecated replaced by {@link org.openntf.domino.ext.Database#getOption(DBOption)} method.
	 */
	@Override
	@Deprecated
	public boolean getOption(final int optionName);

	/**
	 * Gets an outline in the current database.
	 *
	 * @param outlineName
	 *            The name of the outline.
	 * @return The outline
	 */
	@Override
	public Outline getOutline(final String outlineName);

	/**
	 * The Notes session that contains this database.
	 */
	@Override
	public Session getParent();

	/**
	 * The percent of this database's total size that is occupied by real data (and not empty space).
	 */
	@Override
	public double getPercentUsed();

	/**
	 * Retrieves the profile documents associated with a profile form.
	 *
	 * @param profileName
	 *            The name or an alias of the profile form.
	 * @return The profile documents. No profile documents results in an empty collection.
	 */
	@Override
	public DocumentCollection getProfileDocCollection(final String profileName);

	/**
	 * Retrieves or creates a profile document.
	 *
	 * @param profileName
	 *            The name or an alias of the profile form.
	 * @param profileKey
	 *            The unique key associated with the profile document.
	 * @return The profile document for the specified key, or a new profile document if the document with the key does not exist.
	 */
	@Override
	public Document getProfileDocument(final String profileName, final String profileKey);

	/**
	 * A 16-digit hexadecimal number that represents the replica ID of a Notes database. Databases with the same replica ID are replicas of
	 * one another.
	 */
	@Override
	public String getReplicaID();

	/**
	 * The replication object associated with this database. Each Database instance contains one {@link Replication} object. The database
	 * must be open to use this property.
	 */
	@Override
	public Replication getReplicationInfo();

	/**
	 * The name of the server where a database resides. If the database is on a workstation, the property returns an empty string.
	 */
	@Override
	public String getServer();

	/**
	 * The size of a database, in bytes.
	 */
	@Override
	public double getSize();

	/**
	 * The size quota of a database, in kilobytes. The size quota for a database specifies the amount of disk space that the server
	 * administrator is willing to provide for the database. Therefore, the SizeQuota property can only be set by a program that has
	 * administrator access to the server on which the database resides. The size quota is not the same as the size limit that a user
	 * specifies when creating a new database.
	 *
	 * <p>
	 * If the database has no size quota, this property returns 0.
	 * </p>
	 */
	@Override
	public int getSizeQuota();

	/**
	 * The size warning threshold of a database, in kilobytes. The size warning threshold for a database specifies the amount of disk space
	 * that the server administrator is willing to provide for that database before displaying a warning; therefore, the SizeWarning
	 * property can only be set by a script that has administrator access to the server on which the database resides.
	 *
	 * <p>
	 * If there is no size warning threshold for the database, this property returns 0.
	 * </p>
	 * <p>
	 * In the Administration Client, use the "Set Quotas" tool to set the size warning.
	 * </p>
	 */
	@Override
	public long getSizeWarning();

	/**
	 * The template name, if this database is a template. If the database is not a template, returns an empty string.
	 */
	@Override
	public String getTemplateName();

	/**
	 * The title of this database.
	 */
	@Override
	public String getTitle();

	/**
	 * Database type.
	 *
	 * @return One of
	 *         <ul>
	 *         <li>Database.DBTYPE_ADDR_BOOK (10) - Domino Directory or Personal Address Book</li>
	 *         <li>Database.DBTYPE_IMAP_SVR_PROXY (6) - IMAP server proxy</li>
	 *         <li>Database.DBTYPE_LIBRARY (12) - database library</li>
	 *         <li>Database.DBTYPE_LIGHT_ADDR_BOOK (9) - Directory Catalog</li>
	 *         <li>Database.DBTYPE_MAILBOX (3) - mailbox</li>
	 *         <li>Database.DBTYPE_MAILFILE (2) - mail file</li>
	 *         <li>Database.DBTYPE_MULTIDB_SRCH (8) - Domain Catalog</li>
	 *         <li>Database.DBTYPE_NEWS_SVR_PROXY (5) - news server proxy</li>
	 *         <li>Database.DBTYPE_PERS_JOURNAL (11) - Personal Journal</li>
	 *         <li>Database.DBTYPE_PORTFOLIO (7) - portfolio</li>
	 *         <li>Database.DBTYPE_STANDARD (13) - standard</li>
	 *         <li>Database.DBTYPE_SUBSCRIPTIONS (4) - subscriptions</li>
	 *         <li>Database.DBTYPE_WEB_APP (1) - Web application</li>
	 *         </ul>
	 * @see lotus.domino.Database#getType()
	 * @deprecated replaced by {@link org.openntf.domino.ext.Database#getTypeEx()}
	 */
	@Override
	@Deprecated
	public int getType();

	/**
	 * The number of hours before soft deletions become hard deletions. This property corresponds to "Soft delete expire time in hours" in
	 * the database advanced properties of the UI.
	 */
	@Override
	public int getUndeleteExpireTime();

	/**
	 * Returns the Domino URL for its parent.
	 */
	@Override
	public String getURL();

	/**
	 * Gets the specific Hypertext Transfer Protocol (HTTP) header information from the Uniform Resource Locator (URL). A URL is a text
	 * string used for identifying and addressing a Web resource.
	 *
	 * @param url
	 *            The URL for the Web page you want information on, for example, http://www.acme.com/. Specify the entire URL starting with
	 *            http. You can enter a maximum string length of 15K.
	 * @param header
	 *            Enter a header string of the URL header value you want returned. The acceptable header strings are documented in the HTTP
	 *            specification (available at various locations on the Internet, such as http://www.w3.org/) and are subject to change based
	 *            on updated versions of the specification.
	 * @param webUser
	 *            Some Internet servers require you to obtain a username before you can access their pages. This parameter allows you to
	 *            enter the username that you previously obtained from the Internet server. Specify null if you don't need this parameter.
	 * @param webPassword
	 *            Some Internet servers require you to obtain a password before you can access their pages. This parameter allows you to
	 *            enter the password that you previously obtained from the Internet server. Specify null if you don't need this parameter.
	 * @param proxyUser
	 *            Some proxy servers require that you specify a username in order to connect through them. This parameter allows you to
	 *            enter the username for the proxy server. See your administrator for the username required by the proxy. Specify null if
	 *            you don't need this parameter.
	 * @param proxyPassword
	 *            Some proxy servers require that you specify a password in order to connect through them. This parameter allows you to
	 *            enter the password for the proxy server. See your administrator for the password required by the proxy. Specify null if
	 *            you don't need this parameter.
	 * @return The requested header, or null if the URL or the requested header value is not found. Any dashes are converted to underscores.
	 */
	@Override
	public String getURLHeaderInfo(final String url, final String header, final String webUser, final String webPassword,
			final String proxyUser, final String proxyPassword);

	/**
	 * Finds a view or folder in this database, given the name or alias of the view or folder. Using getView returns public views and
	 * folders and private views and folders that are owned by the effective id running the agent. Private views stored in the desktop are
	 * not returned.
	 * <p>
	 * When specifying the parameter, do not combine the view name and an alias. For example, specifying "By Author|AuthorView" does not
	 * work. Use either the view name ("By Author") or its alias ("AuthorView").
	 * </p>
	 *
	 * <p>
	 * When the view or folder name contains underscores to indicate menu accelerators, you have the option of including or excluding the
	 * underscores. The method works more efficiently, however, if you include the underscores.
	 * </p>
	 *
	 * @param name
	 *            The case-insensitive name of a view or folder in a database. Use either the entire name of the view or folder (including
	 *            backslashes for cascading views and folders), or an alias.
	 * @return The view or folder whose name or alias matches the parameter.
	 */
	@Override
	public View getView(final String name);

	/**
	 * The views and folders in this database. Each element of the vector represents a public view or folder in the database, or a private
	 * view or folder owned by the effective id running the code. Private views or folders stored in the desktop are not included.
	 *
	 * <p>
	 * The database must be open to use this property.
	 * </p>
	 */
	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<View> getViews();

	/**
	 * Modifies a database access control list to provide a specified level of access to a person, group, or server. If the name already
	 * exists in the ACL, this method updates it with the access. Otherwise, the name is added to the ACL with the level.
	 *
	 * <p>
	 * You can also use this method to deny access to a person, group, or server by assigning LEVEL_NOACCESS.
	 * </p>
	 *
	 * <p>
	 * This method sets ACL roles to their default values.
	 * </p>
	 *
	 * @param name
	 *            The name of the person, group, or server whose access level you want to provide or change. For a hierarchical name, the
	 *            full name must be specified but can be in abbreviated format.
	 * @param level
	 *            The level of access you're granting. Specify one of the following constants:
	 *            <ul>
	 *            <li>ACL.LEVEL_AUTHOR</li>
	 *            <li>ACL.LEVEL_DEPOSITOR</li>
	 *            <li>ACL.LEVEL_DESIGNER</li>
	 *            <li>ACL.LEVEL_EDITOR</li>
	 *            <li>ACL.LEVEL_MANAGER</li>
	 *            <li>ACL.LEVEL_NOACCESS</li>
	 *            <li>ACL.LEVEL_READER</li>
	 *            </ul>
	 * @deprecated replaced by {@link #grantAccess(String, org.openntf.domino.ACL.Level)}
	 */
	@Override
	@Deprecated
	public void grantAccess(final String name, final int level);

	/**
	 * Modifies a database access control list to provide a specified level of access to a person, group, or server. If the name already
	 * exists in the ACL, this method updates it with the access. Otherwise, the name is added to the ACL with the level.
	 *
	 * <p>
	 * You can also use this method to deny access to a person, group, or server by assigning LEVEL_NOACCESS.
	 * </p>
	 *
	 * <p>
	 * This method sets ACL roles to their default values.
	 * </p>
	 *
	 * @param name
	 *            The name of the person, group, or server whose access level you want to provide or change. For a hierarchical name, the
	 *            full name must be specified but can be in abbreviated format.
	 * @param level
	 *            The level of access you're granting.
	 */
	@Override
	public void grantAccess(final String name, final ACL.Level level);

	/**
	 * Indicates whether documents that are soft deleted can be opened.
	 *
	 * @return true if soft deleted documents can be opened or false if not
	 */
	@Override
	public boolean isAllowOpenSoftDeleted();

	/**
	 * Indicates whether cluster replication is in effect for a database on a server in a cluster. The database must be open to use this
	 * property.
	 *
	 * @return true, if cluster replication is in effect
	 */
	@Override
	public boolean isClusterReplication();

	/**
	 * Indicates whether a database is a Configuration Directory database. This property is available for Database objects retrieved from
	 * the AddressBooks property in Session. For other Database objects, this property has no value, and therefore evaluates to false when
	 * used in conditional statements.
	 *
	 * The database must be open to use this property.
	 *
	 * @return true if the database is a Configuration Directory, false if not
	 */
	@Override
	public boolean isConfigurationDirectory();

	/**
	 * Indicates whether the current user has public reader access in a database. The database must be open to use this property.
	 *
	 * @return true if the user has public reader access, false if not
	 */
	@Override
	public boolean isCurrentAccessPublicReader();

	/**
	 * Indicates whether the current user has public writer access in a database. The database must be open to use this property.
	 *
	 * @return true if the user has public writer access, false if not
	 */
	@Override
	public boolean isCurrentAccessPublicWriter();

	/**
	 * Indicates whether the current database is backed by DB2.
	 *
	 * @return true, if the current database is backed by DB2, false if not.
	 */
	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public boolean isDB2();

	/**
	 * Indicates whether updates to a server are delayed (batched) for better performance.
	 * <p>
	 * If DelayUpdates is false, the program waits for updates to the server to be posted. If you set DelayUpdates to true, server updates
	 * are cached and the program proceeds immediately. At a convenient time, the cached updates are posted. This makes for better
	 * performance but risks losing the cached updates in the event of a crash. This method applies to save and remove operations on
	 * documents.
	 * </p>
	 * <p>
	 * Set this property each time you open a database. The property is not saved.
	 * </p>
	 * <p>
	 * The database must be open to use this property.
	 * </p>
	 *
	 *
	 * @return true delays server updates; this is the default
	 */
	@Override
	public boolean isDelayUpdates();

	/**
	 * Indicates whether design locking is enabled for this database. The design elements that can be locked are agents, forms, and views.
	 *
	 * The database must be open to use this property.
	 *
	 * @return true if design locking is enabled, false if not
	 */
	@Override
	public boolean isDesignLockingEnabled();

	/**
	 * Indicates whether a database is a Directory Catalog database, also known as the Light Weight NAB, or the Enterprise Address Book.
	 * <p>
	 * This property is valid only for a database retrieved through the AddressBooks property of Session, and when the database is
	 * explicitly opened. For all other Database objects, this property returns false.
	 *
	 * The database must be open to use this property.
	 * </p>
	 *
	 * @return true if the database is a Directory Catalog, false if not
	 */
	@Override
	public boolean isDirectoryCatalog();

	/**
	 * Indicates whether document locking is enabled for a database. The database must be open to use this property.
	 *
	 * @return true if document locking is enabled. false if not
	 */
	@Override
	public boolean isDocumentLockingEnabled();

	/**
	 * Indicates whether or not a database has a full-text index. The database must be open to use this property.
	 *
	 * @return true if the database has a full-text index, false if not
	 */
	@Override
	public boolean isFTIndexed();

	/**
	 * Indicates whether a database can be included in multi-database indexing. This property corresponds to "Include in multi-database
	 * indexing" in the database design properties of the UI.
	 *
	 * The database must be open to use this property.
	 *
	 * @return true if the database allows inclusion in multi-database indexing
	 */
	@Override
	public boolean isInMultiDbIndexing();

	/**
	 * Indicates whether a database on a server in a cluster is accessible. {@link #markForDelete()} sets this property read-only with a
	 * value of false.
	 *
	 * The database must be open to use this property.
	 *
	 * @return Indicates whether a database on a server in a cluster is accessible.
	 */
	@Override
	public boolean isInService();

	/**
	 * Indicates whether a database is the target of a link. A link is a text file with an NSF extension whose only content is the full path
	 * name of a database. Accessing the link accesses the specified database.
	 *
	 * The target database appears to exist at the location of the link. For example, the {@link #getFilePath() FilePath} property returns
	 * the path of the link, not the target.
	 */
	@Override
	public boolean isLink();

	/**
	 * Indicates whether a database is of type "Multi DB Search." The database must be open to use this property.
	 */
	@Override
	public boolean isMultiDbSearch();

	/**
	 * Indicates whether the database is open. A database must be open to use the Database methods except: {@link #getCategories()},
	 * {@link #getDelayUpdates()}, {@link #getDesignTemplateName()}, {@link #getFileName()}, {@link #getFilePath()}, {@link #isOpen()},
	 * {@link #isPrivateAddressBook()}, {@link #isPublicAddressBook()}, {@link #getParent()}, {@link #getReplicaID()}, {@link #getServer()},
	 * {@link #getSize()}, {@link #getSizeQuota()}, {@link #getTemplateName()}, and {@link #getTitle()}.
	 * <p>
	 * The following methods do not open a database: {@link DbDirectory#getFirstDatabase(int)}, {@link DbDirectory#getNextDatabase()}, and
	 * {@link Session#getAddressBooks()}. You must explicitly call {@link Database#open()}.
	 * </p>
	 * <p>
	 * If a Database object must be open but is not, the following error occurs: "Database has not been opened yet." This error does not
	 * occur when the Database is created, but later when the attempt to use it occurs. Possible causes of the error are: the database as
	 * specified does not exist; the user does not have permission to access the database; the database is damaged.
	 * </p>
	 *
	 * @return true if the database is open.
	 */
	@Override
	public boolean isOpen();

	/**
	 * Indicates whether this database on a server in a cluster is marked for deletion. The database must be open to use this property.
	 */
	@Override
	public boolean isPendingDelete();

	/**
	 * Indicates if this database is a Personal Address Book. This property is available for Database objects retrieved by getAddressBooks
	 * in Session. For other Database objects, this property has no value and evaluates to false.
	 *
	 * The database must be open to use this property.
	 */
	@Override
	public boolean isPrivateAddressBook();

	/**
	 * Indicates if this database is a Domino Directory. This property is available for Database objects retrieved from
	 * {@link Session#getAddressBooks()} in Session. For other Database objects, this property has no value and evaluates to false.
	 *
	 * The database must be open to use this property.
	 */
	@Override
	public boolean isPublicAddressBook();

	/**
	 * Marks this database for deletion from a server in a cluster. Once a database is marked for deletion, it does not accept any new
	 * database open requests. After all active users are finished with it, the Cluster Manager pushes all changes to another replica (if
	 * there is another replica) and then deletes the database.
	 * <p>
	 * Use this method if you want to remove a database that is obsolete or if you are copying a database from one server to another and
	 * want to delete the database from the original server. If you want to delete a database and all its replicas from a cluster, each
	 * database on each server must be marked for deletion.
	 * </p>
	 * <p>
	 * This method cannot be undone. You cannot remove a mark for deletion from a database once this method is used.
	 * </p>
	 *
	 * <p>
	 * This method sets isPendingDelete to true and isInService to false.
	 * </p>
	 *
	 * <p>
	 * This method differs from the remove method in that the database must be in a cluster. If the database is not on a server in a
	 * cluster, this method does not return an error, but the database is not deleted. Additionally, the remove method fails if the database
	 * is in use. The markForDelete method waits for all current users to finish, then deletes the database. The Cluster Manager is
	 * responsible for deleting databases marked for deletion in the cluster; the Adminp task is not called.
	 * </p>
	 *
	 * <p>
	 * You can programmatically determine if a database is available on other servers in a cluster by querying the cldbdir.nsf database,
	 * which exists on every cluster and holds an up-to-date list of all the databases in the cluster and their replicas. The cldbdir.nsf
	 * database also tracks each database's enabled or disabled status.
	 * </p>
	 * <p>
	 * Use the {@link AdministrationProcess#deleteReplicas(String, String)} method if you want to delete a database and all replicas of it
	 * from the entire domain.
	 * </p>
	 *
	 * <p>
	 * This method requires Manager access privileges.
	 * </p>
	 */
	@Override
	public void markForDelete();

	/**
	 * Opens a database. A database must be open to use the Database properties and methods with some exceptions. Most methods that access a
	 * database open it, but some do not.
	 *
	 * <p>
	 * An error is returned if the user does not have access rights to the database or server.
	 * </p>
	 *
	 * @return true if the database exists and is opened or false if no database with this name exists
	 * @see {@link #isOpen()}
	 */
	@Override
	public boolean open();

	/**
	 * Given a server name and a replica ID, opens the specified database, if it exists. Use <code>Session.getDatabase(null, null)</code> to
	 * instantiate an empty <code>Database</code> object.
	 *
	 * @param server
	 *            The name of the server on which the database resides. Use null to indicate a database on the current computer.
	 * @param replicaId
	 *            The replica ID of the database that you want to open.
	 * @return when the replica was found and opened or false when the replica was not found on the server, or could not be opened
	 */
	@Override
	public boolean openByReplicaID(final String server, final String replicaId);

	/**
	 * Given a date, opens the specified database if it has been modified since that date. Use Session.getDatabase(null, null) to
	 * instantiate an empty Database object.
	 *
	 * @param server
	 *            The name of the server on which the database resides. Use null to indicate a database on the current computer.
	 * @param dbFile
	 *            The file name of the database.
	 * @param modifiedSince
	 *            A cutoff date. If one or more documents in the database has been modified since this date, the database is opened; if not,
	 *            it is not opened.
	 * @return true when the database was opened
	 */
	@Override
	public boolean openIfModified(final String server, final String dbFile, final lotus.domino.DateTime modifiedSince);

	/**
	 * Opens a database on a server.
	 *
	 * @param server
	 *            The name of the primary server on which the database resides.
	 * @param dbFile
	 *            The file name of the database to open.
	 * @return true when the database exists and was opened or false when there is no database with this name in the cluster.
	 */
	@Override
	public boolean openWithFailover(final String server, final String dbFile);

	/**
	 * Returns a person's, group's, or server's current access level to a database. If the <code>name</code> you specify is listed
	 * explicitly in the ACL, then queryAccess returns the access level for that ACL entry and does not check the groups.
	 * <p>
	 * If the <code>name</code> you specify is not listed explicitly in the ACL, queryAccess checks if the <code>name</code> is a member of
	 * a group in the Primary Address Book known to the computer on which the script is running. On a local workstation, that address book
	 * is the Personal Address Book. On a server, that address book is the Domino Directory. If the queryAccess method finds
	 * <code>name</code> in one or more groups, then it returns the highest access level among those groups.
	 * </p>
	 * <p>
	 * If the <code>name</code> you specify is not listed in the ACL either individually or as part of a group, queryAccess returns the
	 * default access level for the ACL.
	 * </p>
	 *
	 * @param name
	 *            The name of the person, group, or server. For a hierarchical name, the full name must be specified but can be in
	 *            abbreviated format.
	 * @return The current access level, which is one of the following:
	 *         <ul>
	 *         <li>ACL.LEVEL_AUTHOR</li>
	 *         <li>ACL.LEVEL_DEPOSITOR</li>
	 *         <li>ACL.LEVEL_DESIGNER</li>
	 *         <li>ACL.LEVEL_EDITOR</li>
	 *         <li>ACL.LEVEL_MANAGER</li>
	 *         <li>ACL.LEVEL_NOACCESS</li>
	 *         <li>ACL.LEVEL_READER</li>
	 *         </ul>
	 */
	@Override
	public int queryAccess(final String name);

	/**
	 * Returns the privileges of a person, group, or server in a database. If the <code>name</code> you specify is listed explicitly in the
	 * ACL, then queryAccessPrivileges returns the privileges for that ACL entry and does not check groups.
	 *
	 * <p>
	 * If the <code>name</code> you specify is not listed explicitly in the ACL, queryAccessPrivileges checks to see if the name is a member
	 * of a group in the primary address book where the program is running: on a workstation the Personal Address Book; on a server the
	 * Domino Directory.
	 * </p>
	 *
	 * @param name
	 *            The name of the person, group, or server. For a hierarchical name, the full name must be specified but can be in
	 *            abbreviated format.
	 * @return The current access privileges, a combination of the following (Individual privileges can be discerned through bitwise
	 *         operations):
	 *         <ul>
	 *         <li>Database.DBACL_CREATE_DOCS (1)</li>
	 *         <li>Database.DBACL_DELETE_DOCS (2)</li>
	 *         <li>Database.DBACL_CREATE_PRIV_AGENTS (4)</li>
	 *         <li>Database.DBACL_CREATE_PRIV_FOLDERS_VIEWS (8)</li>
	 *         <li>Database.DBACL_CREATE_SHARED_FOLDERS_VIEWS (16)</li>
	 *         <li>Database.DBACL_CREATE_SCRIPT_AGENTS (32)</li>
	 *         <li>Database.DBACL_READ_PUBLIC_DOCS (64)</li>
	 *         <li>Database.DBACL_WRITE_PUBLIC_DOCS (128)</li>
	 *         <li>Database.DBACL_REPLICATE_COPY_DOCS (256)</li>
	 *         </ul>
	 */
	@Override
	public int queryAccessPrivileges(final String name);

	/**
	 * Returns the roles of a person, group, or server in a database. If the name you specify is listed explicitly in the ACL, then
	 * queryAccessRoles returns the roles for that ACL entry and does not check groups.
	 * <p>
	 * If the name you specify is not listed explicitly in the ACL, queryAccessRoles checks to see if the name is a member of a group in the
	 * primary address book where the program is running: on a workstation the Personal Address Book; on a server the Domino Directory.
	 * </p>
	 *
	 * @param name
	 *            The name of the person, group, or server. For a hierarchical name, the full name must be specified but can be in
	 *            abbreviated format.
	 * @return A vector with elements of type String. If the name has roles, each element of the vector contains one role. If the name has
	 *         no roles, the vector has a size of 0.
	 */
	@Override
	public Vector<String> queryAccessRoles(final String name);

	/**
	 * Permanently deletes a database.
	 */
	@Override
	public void remove();

	/**
	 * Removes a full-text index from this database. No error occurs if the database does not have a full-text index.
	 *
	 * This method works only for local databases.
	 */
	@Override
	public void removeFTIndex();

	/**
	 * Replicates a local database with its replica(s) on a server. Successful replication does not necessarily mean that documents
	 * replicate. The replication settings are honored. For example, if replication is temporarily disabled on one of the databases, the
	 * replication task runs without error but no documents actually replicate.
	 *
	 * The source database must be local or an exception is thrown.
	 *
	 * @param server
	 *            The name of the server with which you want to replicate. Any replica of the source database that exists on the server will
	 *            replicate.
	 * @return true if the replication task runs without error
	 */
	@Override
	public boolean replicate(final String server);

	/**
	 * Removes a person, group, or server from a database access control list. This resets the access level for that person, group, or
	 * server to the Default setting for the database. Revoking access is different than assigning No Access (which you can do with the
	 * {@link #grantAccess(String, org.openntf.domino.ACL.Level)} method). When you revoke access, you remove the name from the ACL, but the
	 * person, group, or server can still access the database at the level specified for Default. When you use the <code>grantAccess</code>
	 * method to assign No Access, the name remains in the ACL, and the person, group, or server cannot access the database, regardless of
	 * the Default setting.
	 *
	 * <p>
	 * The name must be explicitly listed in the database ACL. If it isn't, revokeAccess throws an exception, even if the name is a member
	 * of a group that is listed in the ACL.
	 * </p>
	 *
	 * @param name
	 *            The name of the person, group, or server whose access you want to revoke. For a hierarchical name, the full name must be
	 *            specified but can be in abbreviated format.
	 */
	@Override
	public void revokeAccess(final String name);

	/**
	 * Given selection criteria for a document, returns all documents in a database that meet the criteria.
	 *
	 * @param formula
	 *            A Notes formula that specifies the selection criteria.
	 * @return An unsorted collection of documents that match the selection criteria.
	 */
	@Override
	public DocumentCollection search(final String formula);

	/**
	 * Given selection criteria for a document, returns all documents in a database that meet the criteria.
	 *
	 * @param formula
	 *            A Notes formula that specifies the selection criteria.
	 * @param startDate
	 *            The method searches only documents created or modified since the start date. Can be null to indicate no start date.
	 * @return An unsorted collection of documents that match the selection criteria.
	 */
	@Override
	public DocumentCollection search(final String formula, final lotus.domino.DateTime startDate);

	/**
	 * Given selection criteria for a document, returns all documents in a database that meet the criteria.
	 * <p>
	 * This method returns a maximum of 5,000 documents by default. The Notes.ini variable FT_MAX_SEARCH_RESULTS overrides this limit for
	 * indexed databases or databases that are not indexed but that are running an agent on the client. For a database that is not indexed
	 * and is running in an agent on the server, you must set the TEMP_INDEX_MAX_DOC Notes.ini variable as well. The absolute maximum is
	 * 2,147,483,647.
	 * </p>
	 *
	 * @param formula
	 *            A Notes formula that specifies the selection criteria.
	 * @param startDate
	 *            A start date. The method searches only documents created or modified since the start date. Can be null to indicate no
	 *            start date.
	 * @param maxDocs
	 *            The maximum number of documents you want returned. Specify 0 to receive all matching documents (up to 5,000).
	 * @return An unsorted collection of documents that match the selection criteria.
	 */
	@Override
	public DocumentCollection search(final String formula, final lotus.domino.DateTime startDate, final int maxDocs);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Database#setAllowOpenSoftDeleted(boolean)
	 */
	@Override
	public void setAllowOpenSoftDeleted(final boolean flag);

	/**
	 * Sets categories under which a database appears in the Database Library. Multiple categories are separated by a comma or a semicolon.
	 *
	 * @param categories
	 *            One or more categories separated by a comma (,) or a semicolon (;)
	 */
	@Override
	public void setCategories(final String categories);

	/**
	 * Sets whether updates to a server are delayed (batched) for better performance.
	 *
	 * @param flag
	 *            true to delay server updates
	 * @see Database#isDelayUpdates()
	 */
	@Override
	public void setDelayUpdates(final boolean flag);

	/**
	 * Sets whether design locking is enabled for a database.
	 *
	 * @param flag
	 *            true to enable design locking, false to disable it
	 */
	@Override
	public void setDesignLockingEnabled(final boolean flag);

	/**
	 * Sets whether document locking is enabled for a database.
	 *
	 * @param flag
	 *            true to enable document locking, false to disable it
	 */
	@Override
	public void setDocumentLockingEnabled(final boolean flag);

	/**
	 * Sets whether this database maintains folder references for documents.
	 *
	 * @param flag
	 *            true to maintain the folder references
	 * @see Database#getFolderReferencesEnabled()
	 */
	@Override
	public void setFolderReferencesEnabled(final boolean flag);

	/**
	 * Sets the update frequency of this database's full-text index.This property applies only to databases on servers. The database must
	 * have a full-text index. The database must be open to use this property.
	 *
	 * @param frequency
	 *            The update frequency of the full-text index. One of:
	 *            <ul>
	 *            <li>Database.FTINDEX_DAILY (1)</li>
	 *            <li>Database.FTINDEX_HOURLY (3)</li>
	 *            <li>Database.FTINDEX_IMMEDIATE (4)</li>
	 *            <li>Database.FTINDEX_SCHEDULED (2)</li>
	 *            </ul>
	 * @deprecated replaced by {@link org.openntf.domino.ext.Database#setFTIndexFrequency(FTIndexFrequency)} method.
	 */
	@Override
	@Deprecated
	public void setFTIndexFrequency(final int frequency);

	/**
	 * Sets whether a database can be included in multi-database indexing.
	 *
	 * @param flag
	 *            true to include the database in multi-database indexing
	 * @see Database#isInMultiDbIndexing()
	 */
	@Override
	public void setInMultiDbIndexing(final boolean flag);

	/**
	 * Sets whether a database on a server in a cluster is accessible.
	 *
	 * @param flag whether the database should be in service
	 * @see Database#isInService()
	 */
	@Override
	public void setInService(final boolean flag);

	/**
	 * Sets the maximum number of entries allowed in the $Revisions field.
	 *
	 * @param revisions the number of entries allowed
	 * @see Database#getLimitRevisions()
	 */
	@Override
	public void setLimitRevisions(final double revisions);

	/**
	 * The maximum number of entries allowed in the $UpdatedBy field.
	 *
	 * @param updatedBys the number of entries allowed
	 * @see Database#getLimitUpdatedBy()
	 */
	@Override
	public void setLimitUpdatedBy(final double updatedBys);

	/**
	 * Sets whether a database appears in database catalogs or not.
	 *
	 * @param flag
	 *            true to make the database appear in database catalogs, false to not make it appear in database catalogs
	 */
	@Override
	public void setListInDbCatalog(final boolean flag);

	/**
	 * Sets the value of a database option.
	 *
	 * @param optionname
	 *            One of the following:
	 *            <ul>
	 *            <li>Database.DBOPT_LZ1 uses LZ1 compression for attachments</li>
	 *            <li>Database.DBOPT_LZCOMPRESSION uses LZ1 compression for attachments</li>
	 *            <li>Database.DBOPT_MAINTAINLASTACCESSED maintains LastAcessed property</li>
	 *            <li>Database.DBOPT_MOREFIELDS allows more fields in database</li>
	 *            <li>Database.DBOPT_NOHEADLINEMONITORS doesn't allow headline monitoring</li>
	 *            <li>Database.DBOPT_NOOVERWRITE doesn't overwrite free space</li>
	 *            <li>Database.DBOPT_NORESPONSEINFO doesn't support specialized response hierarchy</li>
	 *            <li>Database.DBOPT_NOTRANSACTIONLOGGING disables transaction logging</li>
	 *            <li>Database.DBOPT_NOUNREAD doesn't maintain unread marks</li>
	 *            <li>Database.DBOPT_OPTIMIZATION enables document table bitmap optimization</li>
	 *            <li>Database.DBOPT_REPLICATEUNREADMARKSTOANY replicates unread marks to all servers</li>
	 *            <li>Database.DBOPT_REPLICATEUNREADMARKSTOCLUSTER replicates unread marks to clustered servers only</li>
	 *            <li>Database.DBOPT_SOFTDELETE allows soft deletions</li>
	 *            </ul>
	 * @param flag
	 *            true to enable the option, false to disable
	 *
	 * @deprecated replaced by {@link Database#setOption(DBOption, boolean)} method.
	 */
	@Override
	@Deprecated
	public void setOption(final int optionName, final boolean flag);

	@Override
	public void setOption(final DBOption optionName, final boolean flag);

	/**
	 * The size quota of a database, in kilobytes.
	 *
	 * @param quota the quota to set
	 */
	@Override
	public void setSizeQuota(final int quota);

	/**
	 * The size warning threshold of a database, in kilobytes.
	 *
	 * @param warning
	 *            in kilobytes
	 */
	@Override
	public void setSizeWarning(final int warning);

	/**
	 * The title of a database.
	 *
	 * @param title the database title to set
	 */
	@Override
	public void setTitle(final String title);

	/**
	 * Sets the number of hours before soft deletions become hard deletions.
	 *
	 * @param hours the number of hours to set
	 */
	@Override
	public void setUndeleteExpireTime(final int hours);

	/**
	 * Signs elements in a database with the signature of the current user. This method executes only on a workstation.
	 */
	@Override
	public void sign();

	/**
	 * Signs elements in a database with the signature of the current user. This method executes only on a workstation.
	 *
	 * @param documentType
	 *            One of the following constants.
	 *            <ul>
	 *            <li>Database.DBSIGN_DOC_ACL (64) signs the ACL</li>
	 *            <li>Database.DBSIGN_DOC_AGENT (512) signs all agents</li>
	 *            <li>Database.DBSIGN_DOC_ALL (32767) signs all elements</li>
	 *            <li>Database.DBSIGN_DOC_DATA (1) signs all data documents' active content (hotspots)</li>
	 *            <li>Database.DBSIGN_DOC_FORM (4) signs all forms</li>
	 *            <li>Database.DBSIGN_DOC_HELP (256) signs the "About Database" and "Using Database" documents</li>
	 *            <li>Database.DBSIGN_DOC_ICON (16) signs the icon</li>
	 *            <li>Database.DBSIGN_DOC_REPLFORMULA (2048) signs the replication formula</li>
	 *            <li>Database.DBSIGN_DOC_SHAREDFIELD (1024) signs all shared fields</li>
	 *            <li>Database.DBSIGN_DOC_VIEW (8) signs all views</li>
	 *            </ul>
	 */
	@Override
	public void sign(final int documentType);

	/**
	 * Signs elements in a database with the signature of the current user. This method executes only on a workstation.
	 *
	 * @param documentType
	 *            What documents to sign
	 */
	@Override
	public void sign(final SignDocType documentType);

	/**
	 * Signs elements in a database with the signature of the current user. This method executes only on a workstation.
	 *
	 * @param documentType
	 *            The type of documents to sign
	 *
	 * @param existingSigsOnly
	 *            true to sign only elements with existing signatures, false to sign all elements
	 *
	 * @deprecated replaced by {@link #sign(SignDocType, boolean)} method.
	 */
	@Override
	@Deprecated
	public void sign(final int documentType, final boolean existingSigsOnly);

	/**
	 * Signs elements in a database with the signature of the current user. This method executes only on a workstation.
	 *
	 * @param documentType
	 *            The type of documents to sign
	 * @param existingSigsOnly
	 *            true to sign only elements with existing signatures, false to sign all elements
	 */
	@Override
	public void sign(final SignDocType documentType, final boolean existingSigsOnly);

	/**
	 * Signs elements in a database with the signature of the current user. This method executes only on a workstation.
	 *
	 * @param documentType
	 *            The type of documents to sign
	 * @param existingSigsOnly
	 *            true to sign only elements with existing signatures, false to sign all elements
	 * @param name
	 *            Programmatic name or note ID of a single design element.
	 * @deprecated replaced by {@link Database#sign(SignDocType, boolean, String)} method.
	 */
	@Override
	@Deprecated
	public void sign(final int documentType, final boolean existingSigsOnly, final String name);

	/**
	 * Signs elements in a database with the signature of the current user. This method executes only on a workstation.
	 *
	 * @param documentType
	 *            The type of documents to sign
	 * @param existingSigsOnly
	 *            true to sign only elements with existing signatures, false to sign all elements
	 * @param name
	 *            Programmatic name or note ID of a single design element.
	 * @deprecated replaced by {@link Database#sign(SignDocType, boolean, String)} method.
	 */
	@Deprecated
	@Override
	public void sign(final SignDocType documentType, final boolean existingSigsOnly, final String name);

	/**
	 * Signs elements in a database with the signature of the current user. This method executes only on a workstation.
	 *
	 * @param documentType
	 *            The type of documents to sign
	 * @param existingSigsOnly
	 *            true to sign only elements with existing signatures, false to sign all elements
	 * @param name
	 *            Programmatic name or note ID of a single design element.
	 * @param namesIsNoteid
	 *            true if parameter 3 represents a note ID or false if parameter 3 represents a programmatic name.
	 * @deprecated replaced by {@link Database#sign(SignDocType, boolean, String, boolean)} method.
	 */
	@Override
	@Deprecated
	public void sign(final int documentType, final boolean existingSigsOnly, final String name, final boolean nameIsNoteid);

	/**
	 * Signs elements in a database with the signature of the current user. This method executes only on a workstation.
	 *
	 * @param documentType
	 *            The type of documents to sign
	 * @param existingSigsOnly
	 *            true to sign only elements with existing signatures, false to sign all elements
	 * @param name
	 *            Programmatic name or note ID of a single design element.
	 * @param namesIsNoteid
	 *            true if parameter 3 represents a note ID or false if parameter 3 represents a programmatic name.
	 */
	@Override
	public void sign(final SignDocType documentType, final boolean existingSigsOnly, final String name, final boolean nameIsNoteid);

	/**
	 * Updates the full-text index of a database. An exception is thrown if you attempt to create a full-text index on a database that is
	 * not local.
	 * <p>
	 * A database must contain at least one document in order for an index to be created, even if the create parameter is set to true.
	 * </p>
	 *
	 * @param create
	 *            Specify true if you want to create an index if none exists (valid only for local databases). Otherwise, specify false.
	 */
	@Override
	public void updateFTIndex(final boolean create);

	/**
	 * After calling this method on a database, any document that is opened within the database is decrypted using the encryption keys
	 * within the userID object, as specified in the document SecretEncryptionKeys field.
	 *
	 * @param userId
	 *            After setting the User id, documents in this database will be decrypted with encryption keys of this user id
	 * @since Domino 9.0.1 FP8
	 */
	@Override
	public void setUserIDForDecrypt(lotus.domino.UserID userId);

	/**
	 * After calling this method on a database, any document that is opened within the database is decrypted using the encryption keys
	 * within the userID object, as specified in the document SecretEncryptionKeys field.
	 *
	 * @param idFile
	 *            id file. Provides the file path of id file. After setting it, all documents in this database will be decrypted with
	 *            encryption keys of this id file.
	 * @param password
	 *            Password. After setting the User id, documents in this database will be decrypted with encryption keys of this user id
	 * @since Domino 9.0.1 FP8
	 */
	@Override
	public void setUserIDFileForDecrypt(String idFile, String password);

	/**
	 * Get a UserID for a user from the ID Vault, relevant for encryption support
	 *
	 * @param id
	 *            file. Provides the file path of id file. After setting it, all documents in this database will be decrypted with
	 *            encryption keys of this id file.
	 * @param password
	 *            Password. After setting the User id, documents in this database will be decrypted with encryption keys of this user id
	 * @since Domino 9.0.1 FP8
	 */
	@Override
	public UserID getUserID(final String userId, final String password);

	/**
	 * Creates a new DQL query processor.
	 *
	 * @since Domino 10.0.1
	 */
	@Override
	public DominoQuery createDominoQuery();

	/**
	 * Creates a new {@link QueryResultsProcessor} object that uses the current database
	 * for storage.
	 * 
	 * @return a newly-created {@link QueryResultsProcessor}
	 * @since 12.0.1
	 */
	@Override
	QueryResultsProcessor createQueryResultsProcessor();

	/**
	 * Retrieves a named document. This is as distinct from document IDs
	 * and profile document names.
	 * 
	 * @param name the name of the document
	 * @param userName the user name associated with the document
	 * @return the named document
	 * @since 12.0.1
	 */
	@Override
	Document getNamedDocument(String name, String userName);

	/**
	 * Retrieves a named document. This is as distinct from document IDs
	 * and profile document names.
	 * 
	 * @param name the name of the document
	 * @return the named document
	 * @since 12.0.1
	 */
	@Override
	Document getNamedDocument(String name);

	/**
	 * Retrieves the collection of all named documents in the database.
	 * 
	 * @return the named-document collection
	 * @since 12.0.1
	 * @see #getNamedDocument
	 */
	@Override
	DocumentCollection getNamedDocumentCollection();

	/**
	 * Retrieves the collection of all named documents in the database
	 * matching the given name.
	 * 
	 * @param name the name of the documents to retrieve
	 * @return the named-document collection
	 * @since 12.0.1
	 * @see #getNamedDocument
	 */
	@Override
	DocumentCollection getNamedDocumentCollection(String name);

	/**
	 * Removes all stored results saved in the database by {@link DominoQuery}.
	 * 
	 * @since 12.0.1
	 */
	@Override
	void removeAllQueryNamedResults();

	/**
	 * Begins a database transaction.
	 * 
	 * @since 12.0.0
	 */
	@Override
	void transactionBegin();

	/**
	 * Commits an active transaction to disk.
	 * 
	 * @since 12.0.0
	 */
	@Override
	void transactionCommit();

	/**
	 * Rolls back an active transaction.
	 * 
	 * @since 12.0.0
	 */
	@Override
	void transactionRollback();
	
	/**
	 * Decrypts the database.
	 * 
	 * @since 12.0.2
	 */
	@Override
	void decrypt();
	
	/**
	 * Decrypts the database, optionally deferring decryption to the next time the
	 * database is opened.
	 * 
	 * @param defer {@code true} to defer decryption to the next time the database
	 *              is opened; {@code false} to decrypt immediately
	 * @since 12.0.2
	 */
	@Override
	void decrypt(boolean defer);
	
	/**
	 * Encrypts the database with the default encryption level, currently
	 * {@link Database.EncryptionStrength#AES128}.
	 * 
	 * @since 12.0.2
	 */
	@Override
	void encrypt();
	
	/**
	 * Encrypts the database with the provided encryption strength.
	 * 
	 * @param encryptionStrenth the strength value of the encryption. See
	 *        the values of the {@link Database.EncryptionStrength}
	 *        constants
	 * @since 12.0.2
	 */
	@Override
	void encrypt(int encryptionStrength);
	
	/**
	 * Encrypts the database with the provided encryption strength.
	 * 
	 * @param encryptionStrenth the strength value of the encryption. See
	 *        the values of the {@link Database.EncryptionStrength}
	 *        constants
	 * @param {@code true} to defer encryption to the next time the database
	 *        is opened; {@code false} to encrypt immediately
	 * @since 12.0.2
	 */
	@Override
	void encrypt(int encryptionStrength, boolean defer);
	
	/**
	 * Retrieves the encryption strength value of the database.
	 * 
	 * @return the encryption strength of the database. See
	 *         the values of the {@link Database.EncryptionStrength}
	 *         constants
	 * @since 12.0.2
	 */
	@Override
	int getEncryptionStrength();

	/**
	 * Determines whether the database is encrypted on disk.
	 * 
	 * @return {@code true} if the database is encrypted; {@code false}
	 *         otherwise
	 * @since 12.0.2
	 */
	@Override
	boolean isLocallyEncrypted();
}
