/**
 * 
 */
package org.openntf.domino.ext;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.openntf.domino.ACL;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
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
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.transactions.DatabaseTransaction;

/**
 * @author withersp
 * 
 */
public interface Database {

	public int compactWithOptions(Set<CompactOption> options);

	public int compactWithOptions(Set<CompactOption> options, String spaceThreshold);

	/*
	 * (non-Javadoc)
	 * 
	 * 
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key);

	/**
	 * @param itemValues
	 *            Map of fields and values with which to initialize a document
	 * @return the newly created document
	 */
	public Document createDocument(Map<String, Object> itemValues);

	/**
	 * @param keyValuePairs
	 *            an object of key value pairs with which to initialize a document
	 * @return the newly created document
	 */
	public Document createDocument(Object... keyValuePairs);

	public void createFTIndex(Set<FTIndexOption> options, boolean recreate);

	public void fixup(Set<FixupOption> options);

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
	public Document FTDomainSearch(String query, int maxDocs, FTDomainSortOption sortOpt, Set<FTDomainSearchOption> otherOpt, int start,
			int count, String entryForm);

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
	public DocumentCollection FTSearch(String query, int maxDocs, FTSortOption sortOpt, Set<FTSearchOption> otherOpt);

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
	public DocumentCollection FTSearchRange(String query, int maxDocs, FTSortOption sortOpt, Set<FTSearchOption> otherOpt, int start);

	/*
	 * (non-Javadoc)
	 * 
	 * 
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public Document get(Object key);

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
	public Document getDocumentByKey(Serializable key);

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
	public Document getDocumentByKey(Serializable key, boolean createOnFail);

	public DocumentCollection getModifiedDocuments(lotus.domino.DateTime since, ModifiedDocClass noteClass);

	public boolean getOption(DBOption optionName);

	/**
	 * @param name
	 *            name of a user to grant access to
	 * @param level
	 *            ACL.Level for access
	 */
	public void grantAccess(String name, ACL.Level level);

	public void setFTIndexFrequency(FTIndexFrequency frequency);

	/**
	 * @param optionName
	 *            DBOption option name
	 * @param flag
	 *            the flag
	 */
	public void setOption(DBOption optionName, boolean flag);

	/**
	 * @param documentType
	 *            sign document type
	 */
	public void sign(SignDocType documentType);

	public void sign(SignDocType documentType, boolean existingSigsOnly);

	/**
	 * @param documentType
	 *            sign document type
	 * @param existingSigsOnly
	 *            whether to only update existing signatures
	 * @param name
	 *            the name
	 */
	public void sign(SignDocType documentType, boolean existingSigsOnly, String name);

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
	public void sign(SignDocType documentType, boolean existingSigsOnly, String name, boolean nameIsNoteid);

	/**
	 * @return Database transaction
	 */
	public DatabaseTransaction startTransaction();

	public void closeTransaction();

	/**
	 * @return Database transaction
	 */
	public DatabaseTransaction getTransaction();

	public lotus.notes.addins.DominoServer getDominoServer();

	public void refreshDesign();
}