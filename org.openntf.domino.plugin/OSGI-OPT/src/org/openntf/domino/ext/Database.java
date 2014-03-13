/**
 * 
 */
package org.openntf.domino.ext;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.openntf.domino.ACL;
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
	public static enum Events implements EnumEvent {
		BEFORE_CREATE_DOCUMENT, AFTER_CREATE_DOCUMENT, BEFORE_DELETE_DOCUMENT, AFTER_DELETE_DOCUMENT, BEFORE_UPDATE_DOCUMENT, AFTER_UPDATE_DOCUMENT, BEFORE_REPLICATION, AFTER_REPLICATION, BEFORE_RUN_AGENT, AFTER_RUN_AGENT;
	}

	public IDominoEventFactory getEventFactory();

	public void setEventFactory(IDominoEventFactory factory);

	public IDominoEvent generateEvent(EnumEvent event, org.openntf.domino.Base source, Object payload);

	public DocumentCollection createMergableDocumentCollection();

	public String getApiPath();

	public int compactWithOptions(final Set<CompactOption> options);

	public int compactWithOptions(final Set<CompactOption> options, final String spaceThreshold);

	/**
	 * @param itemValues
	 *            Map of fields and values with which to initialize a document
	 * @return the newly created document
	 */
	public Document createDocument(final Map<String, Object> itemValues);

	/**
	 * @param keyValuePairs
	 *            an object of key value pairs with which to initialize a document
	 * @return the newly created document
	 */
	public Document createDocument(final Object... keyValuePairs);

	public void createFTIndex(final Set<FTIndexOption> options, final boolean recreate);

	public void fixup(final Set<FixupOption> options);

	/**
	 * @param query
	 *            the query
	 * @param maxDocs
	 *            the max docs
	 * @param sortOpt
	 *            the sort option
	 * @param otherOpt
	 *            the other option
	 * @param start
	 *            the start
	 * @param count
	 *            the count
	 * @param entryForm
	 *            the entry form
	 * @return a document
	 */
	public Document FTDomainSearch(final String query, final int maxDocs, final FTDomainSortOption sortOpt,
			final Set<FTDomainSearchOption> otherOpt, final int start, final int count, final String entryForm);

	/**
	 * @param query
	 *            the query
	 * @param maxDocs
	 *            the max docs
	 * @param sortOpt
	 *            the sort option
	 * @param otherOpt
	 *            the other option
	 * @return a DocumentCollection
	 */
	public DocumentCollection FTSearch(final String query, final int maxDocs, final FTSortOption sortOpt, final Set<FTSearchOption> otherOpt);

	/**
	 * @param query
	 *            the query
	 * @param maxDocs
	 *            the max docs
	 * @param sortOpt
	 *            the sort option
	 * @param otherOpt
	 *            the other option
	 * @param start
	 *            the start
	 * @return a DocumentCollection
	 */
	public DocumentCollection FTSearchRange(final String query, final int maxDocs, final FTSortOption sortOpt,
			final Set<FTSearchOption> otherOpt, final int start);

	/**
	 * @return A DatabaseDesign object representing the various design elements of this database.
	 */
	public DatabaseDesign getDesign();

	/**
	 * Retrieves a document by a String key.
	 * <p>
	 * The key is hased using MD5 and treated as a UNID.
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

	public boolean isAutoMime();

	public void setAutoMime(boolean autoMime);

}