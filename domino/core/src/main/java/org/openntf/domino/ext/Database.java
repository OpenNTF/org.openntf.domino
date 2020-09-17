/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
/**
 *
 */
package org.openntf.domino.ext;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.openntf.domino.ACL;
import org.openntf.domino.AutoMime;
import org.openntf.domino.Database.CompactOption;
import org.openntf.domino.Database.DBOption;
import org.openntf.domino.Database.DBPrivilege;
import org.openntf.domino.Database.FTDomainSearchOption;
import org.openntf.domino.Database.FTDomainSortOption;
import org.openntf.domino.Database.FTIndexFrequency;
import org.openntf.domino.Database.FTIndexOption;
import org.openntf.domino.Database.FTSearchOption;
import org.openntf.domino.Database.FTSortOption;
import org.openntf.domino.Database.FixupOption;
import org.openntf.domino.Database.ModifiedDocClass;
import org.openntf.domino.Database.SignDocType;
import org.openntf.domino.Database.Type;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.NoteCollection.SelectOption;
import org.openntf.domino.View;
import org.openntf.domino.annotations.Incomplete;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.events.EnumEvent;
import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.events.IDominoEventFactory;
import org.openntf.domino.schema.IDatabaseSchema;
import org.openntf.domino.transactions.DatabaseTransaction;

/**
 * OpenNTF Domino extensions to Database class
 *
 * @author withersp
 *
 *
 */
public interface Database extends Base {
	/**
	 *
	 * Enum for Database-level events, triggered by listeners.
	 *
	 * <p>
	 * Options are:
	 * <ul>
	 * <li>BEFORE_CREATE_DOCUMENT / AFTER_CREATE_DOCUMENT: triggered at the start / end of the Database.createDocument method, source and
	 * target will be Database (newly-created Document has no properties or Items set, so no point passing that</li>
	 * <li>BEFORE_DELETE_DOCUMENT / AFTER_DELETE_DOCUMENT: triggered at the start / end of the Document.remove or Document.removePermanently
	 * methods, source will be Document and target will be Database</li>
	 * <li>BEFORE_UPDATE_DOCUMENT / AFTER_UPDATE_DOCUMENT: triggered at the start / end of the Document.save methods (and its variants),
	 * source will be Document and target will be Database</li>
	 * <li>BEFORE_REPLICATION / AFTER_REPLICATION: triggered at the start / end of the Database.replicate method, source will be Database
	 * and target will be the server the replication is to be performed with</li>
	 * <li>BEFORE_RUN_AGENT / AFTER_RUN_AGENT: triggered at the start / end of the Agent.run method and its variants, source will be Agent,
	 * target will be Datatbase</li>
	 * </ul>
	 * </p>
	 *
	 * @author Nathan T Freeman
	 * @since org.openntf.domino 3.0.0
	 *
	 */
	public static enum Events implements EnumEvent {
		BEFORE_CREATE_DOCUMENT, AFTER_CREATE_DOCUMENT, BEFORE_DELETE_DOCUMENT, AFTER_DELETE_DOCUMENT, BEFORE_UPDATE_DOCUMENT,
		AFTER_UPDATE_DOCUMENT, BEFORE_REPLICATION, AFTER_REPLICATION, BEFORE_RUN_AGENT, AFTER_RUN_AGENT;
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
	 * @since org.openntf.domino 3.0.0
	 */
	public IDominoEvent generateEvent(EnumEvent event, org.openntf.domino.Base<?> source, Object payload);

	/**
	 * NOT YET FULLY IMPLEMENTED. If memory serves me correctly, there were problems creating a blank DocumentCollection and merging other
	 * documents into them. The core {@link lotus.domino.DocumentCollection.merge} does not work
	 *
	 * @return DocumentCollection ready to have Documents merged into it
	 */
	public DocumentCollection createMergeableDocumentCollection();

	/**
	 * Some core XPages controls require a database path in format server!!filePath. This method will extract the relevant components and
	 * return that format.
	 *
	 * @return String in format server!!filePath, useful for XPages components
	 * @since org.openntf.domino 4.5.0
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
	 * Create a document and directly set item values. Parameter KeyValuePairs is a variable arguments consisting of arbitrary number of
	 * pairs of item name and the value like createDocument(item1, value1, item2, value2, ..., itemN, valueN).
	 * <p>
	 * The parameter can also be a single Map instance. Keys will be used for item names, values for item values.
	 * </p>
	 * <h5>Examples:</h5>
	 *
	 * <pre>
	 * Document doc = database.createDocument("Form", "Order", "CustomerID", customer.getID(), "OrderDate",
	 * 		Calendar.getInstance().getTime(), "Items", items);
	 * doc.save(true, false);
	 * </pre>
	 *
	 * or
	 *
	 * <pre>
	 * Map<String, Object> params = new HashMap<String, Object>();
	 * params.put("Form", "Order");
	 * params.put("CustomerID", customer.getID());
	 * params.put("OrderDate", Calendar.getInstance().getTime());
	 * params.put("Items", items);
	 *
	 * doc = db.createDocument(params);
	 * doc.save(true, false);
	 * </pre>
	 *
	 * @param keyValuePairs
	 *            Series of item names and their values with which to initialize the document or<br/>
	 *            Map of item names and values with which to initialize the document
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
	 *            Set&lt;FTIndexOption&gt; full text index options that can be applied
	 * @param recreate
	 *            boolean whether or not the full text index should be recreated
	 * @since org.openntf.domino 2.5.0
	 */
	public void createFTIndex(final Set<FTIndexOption> options, final boolean recreate);

	/**
	 * The core {@link lotus.domino.Database#fixup(int)} method takes an int worked out by adding the integer value for all the relevant
	 * options the developer wishes to apply.<br/>
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
	 * {@link org.openntf.domino.Database.FTDomainSearchOption} objects and a Set of
	 * {@link org.openntf.domino.Database.FTDomainSortOption}.<br/>
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
	 * @since org.openntf.domino 2.5.0
	 */
	public Document FTDomainSearch(final String query, final int maxDocs, final FTDomainSortOption sortOpt,
			final Set<FTDomainSearchOption> otherOpt, final int start, final int count, final String entryForm);

	/**
	 * FT domain search.
	 *
	 * @param query
	 *            the query
	 * @param maxDocs
	 *            the max docs
	 * @param sortOpt
	 *            the sort opt
	 * @param otherOpt
	 *            the other opt
	 * @param start
	 *            the start
	 * @param count
	 *            the count
	 * @param entryForm
	 *            the entry form
	 * @return the document
	 */
	public Document FTDomainSearch(final String query, final int maxDocs, final FTDomainSortOption sortOpt, final int otherOpt,
			final int start, final int count, final String entryForm);

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
	 * @since org.openntf.domino 2.5.0
	 */
	public DocumentCollection FTSearch(final String query, final int maxDocs, final FTSortOption sortOpt,
			final Set<FTSearchOption> otherOpt);

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
	 * @since org.openntf.domino 2.5.0
	 */
	public DocumentCollection FTSearchRange(final String query, final int maxDocs, final FTSortOption sortOpt,
			final Set<FTSearchOption> otherOpt, final int start);

	/**
	 * Gets a {@link org.openntf.domino design.DatabaseDesign} object providing access to various design elements of this database. The
	 * class also has helper methods to create some design resources, e.g. {@link org.openntf.domino.design.DatabaseDesign#createView()}
	 *
	 * @return DatabaseDesign object
	 * @since org.openntf.domino 1.0.0
	 */
	public DatabaseDesign getDesign();

	/**
	 * Returns the shared XPage Design Template (if this is a Single Copy XPage Database)
	 *
	 * @return the shared XPage Design Template (or null, if this is no SCXD-DB)
	 * @throws FileNotFoundException
	 *             if the Template could not be found
	 * @since org.openntf.domino 5.0.0
	 */
	public org.openntf.domino.Database getXPageSharedDesignTemplate() throws FileNotFoundException;

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
	public Document getDocumentWithKey(final Serializable key);

	/**
	 * @deprecated use {@link #getDocumentWithKey(Serializable)} instead
	 */
	@Deprecated
	public Document getDocumentByKey(Serializable key);

	/**
	 * @deprecated use {@link #getDocumentWithKey(Serializable, boolean)} instead.
	 */
	@Deprecated
	public Document getDocumentByKey(Serializable key, boolean createOnFail);

	/**
	 * Retrieves a document by a String key, allowing for creation of a new document if no match was found.
	 * <p>
	 * The key is hashed using MD5 and treated as a UNID.
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
	public Document getDocumentWithKey(final Serializable key, final boolean createOnFail);

	// TODO: Combine the
	/**
	 * Gets any documents of a specific note type modified since a given date, using {@link org.openntf.domino.Database.ModifiedDocClass}
	 * enum
	 *
	 * @param since
	 *            DateTime after which documents should have been modified
	 * @param noteClass
	 *            ModifiedDocClass of notes to include in collection
	 * @return DocumentCollection of notes modified since the given date
	 * @since org.openntf.domino 2.5.0
	 */
	public DocumentCollection getModifiedDocuments(final lotus.domino.DateTime since, final ModifiedDocClass noteClass);

	/**
	 * Gets any documents of a specific note type modified since a given date, using {@link org.openntf.domino.Database.ModifiedDocClass}
	 * enum
	 *
	 * @param since
	 *            Date after which documents should have been modified
	 * @param noteClass
	 *            ModifiedDocClass of notes to include in collection
	 * @return DocumentCollection of notes modified since the given date
	 * @since org.openntf.domino 5.0.0
	 */
	public DocumentCollection getModifiedDocuments(final java.util.Date since, final ModifiedDocClass noteClass);

	/**
	 * Gets any notes (design or data) modified since a given date
	 *
	 * @param since
	 *            Date after which documents should have been modified
	 * @return DocumentCollection of notes modified since the given date
	 * @since org.openntf.domino 5.0.0
	 */
	public DocumentCollection getModifiedDocuments(final java.util.Date since);

	/**
	 * Gets the number of modified notes of relevant note types, using {@link org.openntf.domino.NoteCollection.SelectOption}
	 *
	 * @param since
	 *            Date since when to check for modified notes
	 * @param noteClass
	 *            Set<SelectOption> of note types to include
	 * @return int number of modified notes
	 * @since org.openntf.domino 4.5.0
	 */
	public int getModifiedNoteCount(final java.util.Date since, final Set<SelectOption> noteClass);

	/**
	 * Gets the number of modified notes of all note types, using {@link org.openntf.domino.NoteCollection.SelectOption}
	 *
	 * @param since
	 *            Date since when to check for modified notes
	 * @return int number of modified notes
	 * @since org.openntf.domino 4.5.0
	 */
	public int getModifiedNoteCount(final java.util.Date since);

	/**
	 * Returns a number of documents in this database.
	 *
	 */
	public int getNoteCount();

	/**
	 * Gets the date a Database was last modified, as a Java Date
	 *
	 * @return Date
	 * @since org.openntf.domino 4.5.0
	 */
	public Date getLastModifiedDate();

	/**
	 * Gets the last date a Fixup was run on the database
	 *
	 * @return Date
	 * @since org.openntf.domino 1.0.0
	 */
	public Date getLastFixupDate();

	/**
	 * Gets the date the Full Text Index was last updated
	 *
	 * @return Date
	 * @since org.openntf.domino 1.0.0
	 */
	public Date getLastFTIndexedDate();

	/**
	 * The core {@link lotus.domino.Database#getOption(int)} method takes an int worked out by adding the integer value for all the relevant
	 * options the developer wishes to apply.<br/>
	 * <br/>
	 * To make code clearer and easier to support, this overloaded method has been added taking a
	 * {@link org.openntf.domino.Database.DbOption} object.
	 *
	 * @see org.openntf.domino.Database.DBOption
	 * @param optionName
	 *            DbOption database option to be applied
	 * @since org.openntf.domino 1.0.0
	 */
	public boolean getOption(final DBOption optionName);

	/**
	 * Modify ACL of this database to set/change the ACL level for the given entry.
	 *
	 * @param name
	 *            name of a user to grant or modify access to
	 * @param level
	 *            ACL.Level to set/change for give name
	 * @since org.openntf.domino 2.5.0
	 */
	public void grantAccess(final String name, final ACL.Level level);

	/**
	 * The core {@link lotus.domino.Database#setFTIndexFrequency(int)} method takes an int worked out by adding the integer value for all
	 * the relevant options the developer wishes to apply.<br/>
	 * <br/>
	 * To make code clearer and easier to support, this overloaded method has been added taking a
	 * {@link org.openntf.domino.Database.FTIndexFrequency} object.
	 *
	 * @see org.openntf.domino.Database.FTIndexFrequency
	 * @param frequency
	 *            FTIndexFrequency how frequently to perform the full text indexing
	 * @since org.openntf.domino 2.5.0
	 */
	public void setFTIndexFrequency(final FTIndexFrequency frequency);

	/**
	 * Sets a database option to true or false, using {@link org.openntf.domino.Database.DBOption}
	 *
	 * @param optionName
	 *            DBOption option name
	 * @param flag
	 *            the flag
	 * @since org.openntf.domino 1.0.0
	 */
	public void setOption(final DBOption optionName, final boolean flag);

	/**
	 * Signs all notes corresponding to the {@link org.openntf.domino.Database.SignDocType}
	 *
	 * @param documentType
	 *            SignDocType note type to sign
	 * @since org.openntf.domino 1.0.0
	 */
	public void sign(final SignDocType documentType);

	/**
	 * Signs all notes corresponding to the {@link org.openntf.domino.Database.SignDocType}, choosing whether to only update existing valid
	 * signatures
	 *
	 * @param documentType
	 *            SignDocType note type to sign
	 * @param existingSigsOnly
	 *            boolean whether to update only existing signatures
	 * @since org.openntf.domino 1.0.0
	 */
	public void sign(final SignDocType documentType, final boolean existingSigsOnly);

	/**
	 * Signs all notes corresponding to the {@link org.openntf.domino.Database.SignDocType}, choosing whether to only update existing valid
	 * signatures
	 *
	 * @param documentType
	 *            SignDocType note type to sign
	 * @param existingSigsOnly
	 *            boolean whether to update only existing signatures
	 * @param name
	 *            String the programmatic name or note id of a signle design element to update
	 * @since org.openntf.domino 1.0.0
	 */
	public void sign(final SignDocType documentType, final boolean existingSigsOnly, final String name);

	/**
	 * Signs all notes corresponding to the {@link org.openntf.domino.Database.SignDocType}, choosing whether to only update existing valid
	 * signatures
	 *
	 * @param documentType
	 *            SignDocType note type to sign
	 * @param existingSigsOnly
	 *            boolean whether to update only existing signatures
	 * @param name
	 *            String the programmatic name or note id of a signle design element to update
	 * @param nameIsNoteid
	 *            boolean whether or not the name is a note id
	 * @since org.openntf.domino 1.0.0
	 */
	public void sign(final SignDocType documentType, final boolean existingSigsOnly, final String name, final boolean nameIsNoteid);

	/**
	 * Creates and initiates a transaction on this database
	 *
	 * @return DatabaseTransaction initiated on this Database object
	 * @since org.openntf.domino 2.5.0
	 */
	public DatabaseTransaction startTransaction();

	/**
	 * Closes the transaction on this database. If you have made any changes to documents in this database since the transaction started,
	 * you have to save the documents yourself. Use {@link DatabaseTransaction#commit()} instead to save all changed documents.
	 *
	 * @since org.openntf.domino 2.5.0
	 */
	public void closeTransaction();

	/**
	 * Gets an already initiated transaction for this database
	 *
	 * @return DatabaseTransaction or null
	 * @since org.openntf.domino 2.5.0
	 */
	public DatabaseTransaction getTransaction();

	/**
	 * Returns Universal ID of a note identified by noteid
	 *
	 * @param noteid
	 *            Note ID to use to locate a note
	 * @return Universal ID of a note identified by noteid
	 */
	public String getUNID(String noteid);

	/**
	 * Returns Universal ID of a note identified by noteid
	 *
	 * @param noteid
	 *            Note ID to use to locate a note
	 * @return Universal ID of a note identified by noteid
	 */
	public String getUNID(int noteid);

	/**
	 * Returns a document representing the Icon Note
	 *
	 */
	public Document getIconNote();

	/**
	 * Returns a document representing the ACL Note
	 */
	public Document getACLNote();

	/**
	 * Get a document by its Universal ID. If deferDelegate is true, the mechanism does not actually load the document from the database.
	 * The document is loaded later when needed (when accessing an item's value for example).
	 *
	 * @param unid
	 *            Universal ID of the document
	 * @param deferDelegate
	 *            specify true to defer the loading until later
	 * @return Document with the given unid
	 */
	public Document getDocumentByUNID(String unid, boolean deferDelegate);

	/**
	 * Get a document by its Note ID. If deferDelegate is true, the mechanism does not actually load the document from the database. The
	 * document is loaded later when needed (when accessing an item's value for example).
	 *
	 * @param noteid
	 *            Note ID of the document
	 * @param deferDelegate
	 *            specify true to defer the loading until later
	 * @return Document with the given noteid
	 */
	public Document getDocumentByID(String noteid, boolean deferDelegate);

	/**
	 * Get a document by its Note ID specified as an integer (NoteCollections and Event MessageQueue returns ID as int). If deferDelegate is
	 * true, the mechanism does not actually load the document from the database. The document is loaded later when needed (when accessing
	 * an item's value for example).
	 *
	 * @param noteid
	 *            Note ID of the document as a number
	 * @param deferDelegate
	 *            specify true to defer the loading until later
	 * @return Document with the given noteid
	 */
	public Document getDocumentByID(int noteid, boolean deferDelegate);

	/**
	 * NoteCollections and Event MessageQueue returns ID as int, not hex string. This method converts the int to hex and gets the document
	 * based on that.
	 *
	 * @param noteid
	 *            int decimal note ID
	 * @return Document matching the note ID
	 *
	 * @since ODA 3.2.0
	 */
	public Document getDocumentByID(int noteid);

	/**
	 * Single method to get a document regardless of whether it's being passed a note ID or UNID
	 *
	 * @param id
	 *            String Note or Universal ID
	 * @return Document
	 * @since org.openntf.domino 2.5.0
	 */
	public Document getDocumentByID_Or_UNID(String id);

	/**
	 * Passes a DatabaseTransaction to this Database. This allows a single Transaction to be used to process activity across multiple
	 * databases. You can't set a transaction if a transaction was already started for this database. In that case use
	 * {@link #closeTransaction()} or {@link DatabaseTransaction#commit()} first.
	 *
	 * @param txn
	 *            DatabaseTransaction to be passed to this database
	 * @since org.openntf.domino 4.5.0
	 * @throws TransactionAlreadySetException
	 *             if a transaction was started already
	 *
	 */
	public void setTransaction(DatabaseTransaction txn);

	/**
	 * Creates a new {@link lotus.notes.addins.DominoServer} object for the server the database is on
	 *
	 * @return DominoServer running on the current Domino server`
	 * @since org.openntf.domino 2.5.0
	 */
	public lotus.notes.addins.DominoServer getDominoServer();

	/**
	 * Refreshes the design of this database. The template must be available on the same server.
	 *
	 * @since org.openntf.domino 2.5.0
	 */
	public void refreshDesign();

	/**
	 * Opens the mail database for the relevant user loading it into this Database object
	 *
	 * @since org.openntf.domino 4.5.0
	 */
	public void openMail();

	/**
	 * Gets the Mail database for the relevant user
	 *
	 * @return Database for the relevant user's mail database
	 * @since org.openntf.domino 4.5.0
	 */
	public org.openntf.domino.Database getMail();

	/**
	 * Gets a Map view of the documents in the database, keyed according to getDocumentByKey
	 *
	 * @return Map<Serializable, Document>
	 * @since org.openntf.domino 5.0.0
	 */
	public Map<Serializable, org.openntf.domino.Document> getDocumentMap();

	/**
	 * Gets the schema for the database. Not yet complete
	 *
	 * @return instance of IDatabaseSchema interface
	 * @since org.openntf.domino 2.5.0
	 */
	@Incomplete
	public IDatabaseSchema getSchema();

	/**
	 * Sets the schema for the database. Not yet complete
	 *
	 * @param schema
	 *            instance of IDatabaseSchema interface
	 * @since org.openntf.domino 2.5.0
	 */
	public void setSchema(IDatabaseSchema schema);

	/**
	 * Checks whether replication is disabled for this database
	 *
	 * @return boolean
	 * @since org.openntf.domino 4.5.0
	 */
	public boolean isReplicationDisabled();

	/**
	 * Gets the web URL for this database, specifying whether or not to include the path. If the path is not to be included, the replica ID
	 * will be used in the URL instead. Works only if the HTTP protocol is in effect.
	 *
	 * @param usePath
	 *            boolean specify false to return URL like http://server/__replica.nsf?OpenDatabase or specify true to return URL in the
	 *            form http://server/path/to/file.nsf?OpenDatabase
	 * @return String url for the database
	 * @since org.openntf.domino 5.0.0
	 */
	public String getHttpURL(final boolean usePath);

	/**
	 * Gets MIME behavior for the session, whether to wrap all mime, wrap if over 32k, or wrap none
	 *
	 * @return AutoMime format for the session
	 * @since org.openntf.domino 5.0.0
	 */
	public AutoMime getAutoMime();

	/**
	 * Sets the MIME behavior for the session, using {@link org.openntf.domino.AutoMime}
	 *
	 * @param autoMime
	 *            AutoMime format, WRAP_ALL, WRAP_32K, WRAP_NONE
	 * @since org.openntf.domino 5.0.0
	 */
	public void setAutoMime(AutoMime autoMime);

	/**
	 * Gets the $DefaultLanguage stored in the icon note and converts it to a locale
	 *
	 * @return the Locale stored in the Notes database
	 * @since org.openntf.domino 5.0.0
	 */
	public Locale getLocale();

	/**
	 *
	 * Gets the meta replica ID, an ID in the format serverName!!replicaId, first portion of metaversal ID
	 *
	 * @return the meta replica id
	 * @since org.openntf.domino 5.0.0
	 */
	public String getMetaReplicaID();

	/**
	 * Returns the type of this database as Type object
	 *
	 * @return a {@link Type} Object
	 */
	Type getTypeEx();

	/**
	 * Based on a View design element's document or note, this method retrieves the View Domino object from the Database. Basically, it gets
	 * the $Title field (name and all aliases), calls {@linkplain org.openntf.domino.Database#getView(String)} for each, ensure's the View
	 * is not a different view with the same name, and returns that View object.
	 *
	 * @param viewDocument
	 *            the View design element
	 * @return View object
	 */
	public View getView(final Document viewDocument);

	/**
	 * SHAREDPRIVATEONFIRSTUSE views have multiple Views with the same title, the "template" view and the private versions for each user
	 * that the ID used to run with can access. The default {@link lotus.domino.Database#getView(String)} method just gets the current
	 * user's view or, when running as the server, the version for the first alphabetical user. This method gets all available under the
	 * current authority.
	 *
	 * @param name
	 *            the String title or alias
	 * @return List of Views matching the name
	 * @since ODA 4.1.0
	 */
	public List<View> getViews(String name);

	/**
	 * Returns a {@link Set} of {@link DBPrivilege}s for the provided user.
	 *
	 * <p>
	 * This is an enum-ified version of {@link org.openntf.domino.Database#queryAccessPrivileges(String)}.
	 * </p>
	 *
	 * @param user
	 *            the user name to query
	 * @return a <code>Set</code> of the user's privileges
	 */
	public Set<DBPrivilege> queryAccessPrivilegesEx(String user);

	/**
	 * Returns a set of roles assigned to the effective User Name in the ACL of this database.
	 *
	 * @return Set of role names (with brackets)
	 */
	public Set<String> getCurrentRoles();

	/**
	 * Returns privileges assigned to the effective User Name for this database. Privileges are specified in the ACL and define what the
	 * person can do - create and delete documents, create agents and views and so on.
	 * <p>
	 * <h5>Example</h5> The following example can be used in an XPage page controller to decide whether to render a button which creates a
	 * new document:
	 *
	 * <pre>
	 * public boolean canCreateDocumens() {
	 * 	EnumSet<Privilege> privileges = Factory.getSession().getCurrentDatabase().getCurrentPrivileges();
	 * 	return privileges.contains(Privilege.CREATE_DOCS);
	 * }
	 * </pre>
	 * </p>
	 *
	 * @return ACL privileges for the effective user
	 */
	public EnumSet<ACL.Privilege> getCurrentPrivileges();

}
