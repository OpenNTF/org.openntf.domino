/**
 * 
 */
package org.openntf.domino.ext;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.openntf.domino.ACL;
import org.openntf.domino.AutoMime;
import org.openntf.domino.Database.CompactOption;
import org.openntf.domino.Database.DBOption;
import org.openntf.domino.Database.FTDomainSearchOption;
import org.openntf.domino.Database.FTDomainSortOption;
import org.openntf.domino.Database.FTIndexFrequency;
import org.openntf.domino.Database.FTIndexOption;
import org.openntf.domino.Database.FTSearchOption;
import org.openntf.domino.Database.FTSortOption;
import org.openntf.domino.Database.FixupOption;
import org.openntf.domino.Database.ModifiedDocClass;
import org.openntf.domino.Database.SignDocType;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.NoteCollection.SelectOption;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.events.EnumEvent;
import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.events.IDominoEventFactory;
import org.openntf.domino.schema.IDatabaseSchema;
import org.openntf.domino.transactions.DatabaseTransaction;

/**
 * @author withersp
 * 
 */
public interface Database extends Base {
	/**
	 * @author Nathan T Freeman
	 * 
	 *         Enum for Database-level events, triggered by listeners.
	 * 
	 *         Options are:
	 *         <ul>
	 *         <li>BEFORE_CREATE_DOCUMENT / AFTER_CREATE_DOCUMENT: triggered at the start / end of the Database.createDocument method,
	 *         source and target will be Database (newly-created Document has no properties or Items set, so no point passing that</li>
	 *         <li>BEFORE_DELETE_DOCUMENT / AFTER_DELETE_DOCUMENT: triggered at the start / end of the Document.remove or
	 *         Document.removePermanently methods, source will be Document and target will be Database</li>
	 *         <li>BEFORE_UPDATE_DOCUMENT / AFTER_UPDATE_DOCUMENT: triggered at the start / end of the Document.save methods (and its
	 *         variants), source will be Document and target will be Database</li>
	 *         <li>BEFORE_REPLICATION / AFTER_REPLICATION: triggered at the start / end of the Database.replicate method, source will be
	 *         Database and target will be the server the replication is to be performed with</li>
	 *         <li>BEFORE_RUN_AGENT / AFTER_RUN_AGENT: triggered at the start / end of the Agent.run method and its variants, source will be
	 *         Agent, target will be Datatbase</li>
	 *         </ul>
	 * @since org.openntf.domino 1.0.0
	 * 
	 */
	public static enum Events implements EnumEvent {
		BEFORE_CREATE_DOCUMENT, AFTER_CREATE_DOCUMENT, BEFORE_DELETE_DOCUMENT, AFTER_DELETE_DOCUMENT, BEFORE_UPDATE_DOCUMENT, AFTER_UPDATE_DOCUMENT, BEFORE_REPLICATION, AFTER_REPLICATION, BEFORE_RUN_AGENT, AFTER_RUN_AGENT;
	}

	/**
	 * Gets the factory that manages processing of the IDominoEvents
	 * 
	 * @return IDominoEventFactory containing the IDominoEvents
	 * @since org.openntf.domino 1.0.0
	 */
	public IDominoEventFactory getEventFactory();

	/**
	 * Sets the factory for managing processing of the IDominoEvents
	 * 
	 * @param factory
	 *            IDominoEventFactory containing the IDominoEvents
	 * @since org.openntf.domino 1.0.0
	 */
	public void setEventFactory(IDominoEventFactory factory);

	/**
	 * Generates an IDominoEvent into the IDominoEventFactory. The IDominoEvent will be for a specific EnumEvent, e.g.
	 * BEFORE_CREATE_DOCUMENT. This method basically triggers the EnumEvent, passing the relevant Objects that are currently being acted
	 * upon. <br/>
	 * <br/>
	 * EnumEvent types and contents can be found in {@link org.openntf.domino.ext.Database.Events}<br/>
	 * <br/>
	 * The target should not be passed into this method, but the implementation should pass {@code this} to as the target to
	 * {@link org.openntf.domino.events.IDominoEventFactory.generate}
	 * 
	 * @param event
	 *            EnumEvent being triggered, e.g. BEFORE_CREATE_DOCUMENT.
	 * @param source
	 *            The source object for the event to be run on. The relevant
	 * @param payload
	 *            Object a payload that can be passed along with the Event
	 * @return An IDominoEvent which will be passed to {@link org.openntf.domino.ext.Base.fireListener}
	 * @since org.openntf.domino 1.0.0
	 */
	public IDominoEvent generateEvent(EnumEvent event, org.openntf.domino.Base source, Object payload);

	/**
	 * NOT YET FULLY IMPLEMENTED. If memory serves me correctly, there were problems creating a blank DocumentCollection and merging other
	 * documents into them. The core {@link lotus.domino.DocumentCollection.merge} does not work
	 * 
	 * @return DocumentCollection ready to have Documents merged into it
	 */
	public DocumentCollection createMergableDocumentCollection();

	/**
	 * Some core XPages controls require a database path in format server!!filePath. This method will extract the relevant components and
	 * return that format.
	 * 
	 * @return String in format server!!filePath, useful for XPages components
	 * @since org.openntf.domino 1.0.0
	 */
	public String getApiPath();

	/**
	 * The core {@link lotus.domino.Database.compactWithOptions} method takes an int worked out by adding the integer value for all the
	 * relevant options the developer wishes to apply.<br/>
	 * <br/>
	 * To make code clearer and easier to support, this overloaded method has been added taking a Set of
	 * {@link org.openntf.domino.CompactOption} objects.
	 * 
	 * @see org.openntf.domino.Database.CompactOption for options
	 * 
	 * @param options
	 *            Set<CompactOption> of compact options you wish to apply, e.g. CompactOption.COPYSTYLE
	 * @return int the difference in bytes between the size of the database before and after compacting
	 * @since org.openntf.domino 1.0.0
	 */
	public int compactWithOptions(final Set<CompactOption> options);

	/**
	 * @see org.openntf.domino.ext.Database#compactWithOptions(Set) for why overloaded methods for
	 *      {@link lotus.domino.Database.compactWithOptions}
	 * @see org.openntf.domino.Database.CompactOption for options
	 * 
	 * @param options
	 *            Set<CompactOption> of compact options you wish to apply, e.g. CompactOption.COPYSTYLE
	 * @param spaceThreshold
	 *            The value of the S option for a compact, but without the S. "10" for 10 percent, 10K for 10 kilobytes, 10M for 10
	 *            megabytes
	 * @return int the difference in bytes between the size of the database before and after compacting
	 * @since org.openntf.domino 1.0.0
	 */
	public int compactWithOptions(final Set<CompactOption> options, final String spaceThreshold);

	/**
	 * @param itemValues
	 *            Map of fields and values with which to initialize a document
	 * @return the newly created document
	 * @since org.openntf.domino 1.0.0
	 */
	public Document createDocument(final Map<String, Object> itemValues);

	/**
	 * @param keyValuePairs
	 *            an object of key value pairs with which to initialize a document
	 * @return the newly created document
	 * @since org.openntf.domino 1.0.0
	 */
	public Document createDocument(final Object... keyValuePairs);

	/**
	 * The core {@link lotus.domino.Database.createFTIndex} method takes an int worked out by adding the integer value for all the relevant
	 * options the developer wishes to apply.<br/>
	 * <br/>
	 * To make code clearer and easier to support, this overloaded method has been added taking a Set of
	 * {@link org.openntf.domino.Database.FTIndexOption} objects.
	 * 
	 * @see org.openntf.domino.Database.FTIndexOption
	 * @param options
	 *            Set<FTIndexOption> full text index options that can be applied
	 * @param recreate
	 *            boolean whether or not the full text index should be recreated
	 * @since org.openntf.domino 1.0.0
	 */
	public void createFTIndex(final Set<FTIndexOption> options, final boolean recreate);

	/**
	 * The core {@link lotus.domino.Database.fixup} method takes an int worked out by adding the integer value for all the relevant options
	 * the developer wishes to apply.<br/>
	 * <br/>
	 * To make code clearer and easier to support, this overloaded method has been added taking a Set of
	 * {@link org.openntf.domino.Database.FixupOption} objects.
	 * 
	 * @see org.openntf.domino.Database.FixupOption
	 * 
	 * @param options
	 *            Set<FixupOption>
	 * @since org.openntf.domino 1.0.0
	 */
	public void fixup(final Set<FixupOption> options);

	/**
	 * The core {@link lotus.domino.Database.FTDomainSearch} method takes an int worked out by adding the integer value for all the relevant
	 * search and sort options the developer wishes to apply.<br/>
	 * <br/>
	 * To make code clearer and easier to support, this overloaded method has been added taking a Set of
	 * {@link org.openntf.domino.Database.FTDomainSearchOption} objects and a Set of {@link org.openntf.domino.Database.FTDomainSortOption}.<br/>
	 * <br/>
	 * The current database must be a Domain Catalog
	 * 
	 * @see org.openntf.domino.Database.FTDomainSearchOption
	 * @see org.openntf.domino.Database.FTDomainSortOption
	 * @param query
	 *            String the search query to use. Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see
	 *            "Refining a search query using operators" in Notes Help. Search for "query syntax" in the Domino Designer Eclipse help
	 * @param maxDocs
	 *            int the maximum number of documents to return. NOTE: the maximum will also be restricted by the setting on the server
	 *            document, "Maximum search result limit" on the Internet Protocols..., Domino Web Engine tab
	 * @param sortOpt
	 *            Set<FTDomainSortOption> a Set of possible {@link org.openntf.domino.Database.FTDomainSortOption} options, e.g. date
	 *            descending. If nothing is specified, relevance score will be the sorting option applied.
	 * @param otherOpt
	 *            Set<FTDomainSearchOption> a Set of possible {@link org.openntf.domino.Database.FTDomainSearchOption} options, e.g. fuzzy
	 *            search
	 * @param start
	 *            int the page from which to start showing
	 * @param count
	 *            int the count of pages to show
	 * @param entryForm
	 *            String the name of the Domain Search entry form in the domain catalog. This will be a traditional Domino web form, not an
	 *            XPage form.
	 * @return Document with a rich text field called "Body" that contains a table of matching document titles
	 * @since org.openntf.domino 1.0.0
	 */
	public Document FTDomainSearch(final String query, final int maxDocs, final FTDomainSortOption sortOpt,
			final Set<FTDomainSearchOption> otherOpt, final int start, final int count, final String entryForm);

	/**
	 * The core {@link lotus.domino.Database.FTSearch} method takes an int worked out by adding the integer value for all the relevant
	 * search and sort options the developer wishes to apply.<br/>
	 * <br/>
	 * To make code clearer and easier to support, this overloaded method has been added taking a Set of
	 * {@link org.openntf.domino.Database.FTSearchOption} objects and a Set of {@link org.openntf.domino.Database.FTSortOption}.<br/>
	 * <br/>
	 * The current database must be a Domain Catalog
	 * 
	 * @see org.openntf.domino.Database.FTSearchOption
	 * @see org.openntf.domino.Database.FTSortOption
	 * @param query
	 *            String the search query to use. Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see
	 *            "Refining a search query using operators" in Notes Help. Search for "query syntax" in the Domino Designer Eclipse help
	 * @param maxDocs
	 *            int the maximum number of documents to return. NOTE: the maximum will also be restricted by the setting on the server
	 *            document, "Maximum search result limit" on the Internet Protocols..., Domino Web Engine tab
	 * @param sortOpt
	 *            Set<FTDomainSortOption> a Set of possible {@link org.openntf.domino.Database.FTDomainSortOption} options, e.g. date
	 *            descending. If nothing is specified, relevance score will be the sorting option applied.
	 * @param otherOpt
	 *            Set<FTDomainSearchOption> a Set of possible {@link org.openntf.domino.Database.FTDomainSearchOption} options, e.g. fuzzy
	 *            search
	 * @return a DocumentCollection containing the documents matching the search criteria
	 * @since org.openntf.domino 1.0.0
	 */
	public DocumentCollection FTSearch(final String query, final int maxDocs, final FTSortOption sortOpt, final Set<FTSearchOption> otherOpt);

	/**
	 * The core {@link lotus.domino.Database.FTSearch} method takes an int worked out by adding the integer value for all the relevant
	 * search and sort options the developer wishes to apply.<br/>
	 * <br/>
	 * To make code clearer and easier to support, this overloaded method has been added taking a Set of
	 * {@link org.openntf.domino.Database.FTSearchOption} objects and a Set of {@link org.openntf.domino.Database.FTSortOption}.<br/>
	 * <br/>
	 * The current database must be a Domain Catalog
	 * 
	 * @see org.openntf.domino.Database.FTSearchOption
	 * @see org.openntf.domino.Database.FTSortOption
	 * @param query
	 *            String the search query to use. Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see
	 *            "Refining a search query using operators" in Notes Help. Search for "query syntax" in the Domino Designer Eclipse help
	 * @param maxDocs
	 *            int the maximum number of documents to return. NOTE: the maximum will also be restricted by the setting on the server
	 *            document, "Maximum search result limit" on the Internet Protocols..., Domino Web Engine tab
	 * @param sortOpt
	 *            Set<FTDomainSortOption> a Set of possible {@link org.openntf.domino.Database.FTDomainSortOption} options, e.g. date
	 *            descending. If nothing is specified, relevance score will be the sorting option applied.
	 * @param otherOpt
	 *            Set<FTDomainSearchOption> a Set of possible {@link org.openntf.domino.Database.FTDomainSearchOption} options, e.g. fuzzy
	 *            search
	 * @param start
	 *            the start number of the document in the collection from which to show
	 * @return a DocumentCollection of matching documents, starting at the number entered
	 * @since org.openntf.domino 1.0.0
	 */
	public DocumentCollection FTSearchRange(final String query, final int maxDocs, final FTSortOption sortOpt,
			final Set<FTSearchOption> otherOpt, final int start);

	/**
	 * Gets a DatabaseDesign object representing the various design elements of this database.
	 * 
	 * @return DatabaseDesign object
	 * @since org.openntf.domino 1.0.0
	 */
	public DatabaseDesign getDesign();

	/**
	 * Retrieves a document by a String key.
	 * <p>
	 * The key is hashed using MD5 and treated as a UNID.
	 * </p>
	 * 
	 * @param key
	 *            The arbitrary-length string key.
	 * 
	 * @return The Document corresponding to the key, or null if no matching document exists.
	 * @since org.openntf.domino 1.0.0
	 */
	public Document getDocumentByKey(final Serializable key);

	/**
	 * Retrieves a document by a String key, allowing for creation of a new document if no match was found.
	 * <p>
	 * The key is hased using MD5 and treated as a UNID.
	 * </p>
	 * 
	 * @param key
	 *            The arbitrary-length string key.
	 * @param createOnFail
	 *            Whether or not a new document should be created when the key was not found. Defaults to false.
	 * 
	 * @return The Document corresponding to the key, or null if no matching document exists and createOnFail is false.
	 * @since org.openntf.domino 1.0.0
	 */
	public Document getDocumentByKey(final Serializable key, final boolean createOnFail);

	public DocumentCollection getModifiedDocuments(final lotus.domino.DateTime since, final ModifiedDocClass noteClass);

	public DocumentCollection getModifiedDocuments(final java.util.Date since, final ModifiedDocClass noteClass);

	public DocumentCollection getModifiedDocuments(final java.util.Date since);

	public int getModifiedNoteCount(final java.util.Date since, final Set<SelectOption> noteClass);

	public int getModifiedNoteCount(final java.util.Date since);

	public Date getLastModifiedDate();

	public Date getLastFixupDate();

	public Date getLastFTIndexedDate();

	public boolean getOption(final DBOption optionName);

	/**
	 * @param name
	 *            name of a user to grant access to
	 * @param level
	 *            ACL.Level for access
	 */
	public void grantAccess(final String name, final ACL.Level level);

	public void setFTIndexFrequency(final FTIndexFrequency frequency);

	/**
	 * @param optionName
	 *            DBOption option name
	 * @param flag
	 *            the flag
	 */
	public void setOption(final DBOption optionName, final boolean flag);

	/**
	 * @param documentType
	 *            sign document type
	 */
	public void sign(final SignDocType documentType);

	public void sign(final SignDocType documentType, final boolean existingSigsOnly);

	/**
	 * @param documentType
	 *            sign document type
	 * @param existingSigsOnly
	 *            whether to only update existing signatures
	 * @param name
	 *            the name
	 */
	public void sign(final SignDocType documentType, final boolean existingSigsOnly, final String name);

	/**
	 * @param documentType
	 *            sign document type
	 * @param existingSigsOnly
	 *            whether to only update existing signatures
	 * @param name
	 *            the name
	 * @param nameIsNoteid
	 *            whether or not the name is a note id
	 */
	public void sign(final SignDocType documentType, final boolean existingSigsOnly, final String name, final boolean nameIsNoteid);

	/**
	 * @return Database transaction
	 */
	public DatabaseTransaction startTransaction();

	public void closeTransaction();

	/**
	 * @return Database transaction
	 */
	public DatabaseTransaction getTransaction();

	public void setTransaction(DatabaseTransaction txn);

	public lotus.notes.addins.DominoServer getDominoServer();

	public void refreshDesign();

	public void openMail();

	public org.openntf.domino.Database getMail();

	/**
	 * @return a Map view of the documents in the database, keyed according to getDocumentByKey
	 */
	public Map<Serializable, org.openntf.domino.Document> getDocumentMap();

	public IDatabaseSchema getSchema();

	public void setSchema(IDatabaseSchema schema);

	public boolean isReplicationDisabled();

	public String getHttpURL(final boolean usePath);

	public AutoMime getAutoMime();

	public void setAutoMime(AutoMime autoMime);

}