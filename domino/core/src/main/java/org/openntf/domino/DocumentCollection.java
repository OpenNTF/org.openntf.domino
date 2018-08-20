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

import org.openntf.domino.annotations.Legacy;
import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.types.FactorySchema;

/**
 * The Interface DocumentCollection represents a collection of documents from a database, selected according to specific criteria.
 *
 * <h3>Notable enhancements</h3>
 * <p>
 * <ul>
 * <li>Supports the foreach construct to iterate over the documents</li>
 * <li>Filter the collection using one of the {@link org.openntf.domino.ext.DocumentCollection#filter(Object) filter} methods (methods
 * return new collections)</li>
 * <li>Use the {@link org.openntf.domino.ext.DocumentCollection#stampAll(java.util.Map)} to change multiple items at once in all documents
 * in the collection</li>
 * </ul>
 * </p>
 * <h3>Examples</h3>
 * <dl>
 * <dt>Processing documents in the collection:</dt>
 * <dd>
 *
 * <pre>
 * DocumentCollection docs = v.getAllDocuments();
 * for (Document doc : docs) {
 * 	//process the document in a separate method
 * }
 * </pre>
 * </dl>
 * <h3>Access</h3>
 * <p>
 * A <code>DocumentCollection</code> represents a subset of all the documents in a database. The documents in the subset are determined by
 * the method or property you use to search the database, which can be any of the following:
 * </p>
 * <ul>
 * <li>{@link Database#getAllDocuments()}</li>
 * <li>{@link Database#FTSearch(String)}</li>
 * <li>{@link Database#FTSearchRange(String, int, org.openntf.domino.Database.FTSortOption, int, int)}</li>
 * <li>{@link Database#search(String)}</li>
 * <li>{@link Database#getProfileDocCollection(String)}</li>
 * <li>{@link Database#getModifiedDocuments()}</li>
 * <li>{@link View#getAllDocumentsByKey(Object)}</li>
 * <li>{@link AgentContext#getUnprocessedDocuments()}</li>
 * <li>{@link AgentContext#unprocessedFTSearch(String, int)}</li>
 * <li>{@link AgentContext#unprocessedFTSearchRange(String, int, int)}</li>
 * <li>{@link AgentContext#unprocessedFTSearch(String, int)}</li>
 * <li>{@link Document#getResponses()}</li>
 * </ul>
 * <h3>Usage</h3>
 * <p>
 * <code>DocumentCollection</code>, {@link ViewEntryCollection}, and {@link ViewNavigator} objects provide access to documents in a
 * database. Use a <code>DocumentCollection</code> object if:
 * </p>
 * <ul>
 * <li>You want to act on a specific set of documents that meet certain criteria.</li>
 * <li>There is no view in the database that contains every document you need to search.</li>
 * <li>You do not need to navigate the documents' response hierarchies.</li>
 * </ul>
 * <p>
 * Views are a more efficient means of accessing documents because they are already indexed by the database itself. However, they do not
 * necessarily provide access to the documents that you want. {@link ViewEntryCollection}, and {@link ViewNavigator} provide access to view
 * entries, which contain {@link ViewEntry} as well as {@link Document} information. {@link ViewNavigator} provides access to categories and
 * totals as well as documents.
 * </p>
 * <h3>Sorted collections</h3>
 * <p>
 * The documents in a collection are not sorted unless the collection results from a search. By contrast, documents accessed through
 * {@link ViewEntryCollection} and {@link ViewNavigator} are in view order.
 * </p>
 * <h3>Current pointer</h3>
 * <p>
 * A current pointer is maintained for document collections. All navigation methods set the current pointer to the retrieved document with
 * the following exceptions: add and delete methods do not move the current pointer. The following methods set the current pointer to the
 * first document: {@link #FTSearch(String)}, {@link #removeAll(boolean)} (remote IIOP only), {@link #putAllInFolder(String)},
 * {@link #removeAllFromFolder(String)}, and {@link #stampAll(String, Object)}.
 * </p>
 * <h3>Deletion stubs</h3>
 * <p>
 * A deletion stub is returned for a document deleted after creation of the collection or for a document to which you do not have Reader
 * access. Use {@link Document#isValid()} to check whether a document is real (true) or a deletion stub (false).
 * </p>
 */
public interface DocumentCollection extends lotus.domino.DocumentCollection, org.openntf.domino.ext.DocumentCollection,
		org.openntf.domino.Base<lotus.domino.DocumentCollection>, Collection<org.openntf.domino.Document>, DatabaseDescendant {

	public static class Schema extends FactorySchema<DocumentCollection, lotus.domino.DocumentCollection, Database> {
		@Override
		public Class<DocumentCollection> typeClass() {
			return DocumentCollection.class;
		}

		@Override
		public Class<lotus.domino.DocumentCollection> delegateClass() {
			return lotus.domino.DocumentCollection.class;
		}

		@Override
		public Class<Database> parentClass() {
			return Database.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Adds a document to a collection.
	 * <p>
	 * This method throws an exception if:
	 * <ul>
	 * <li>The document is a duplicate of one already in the collection.</li>
	 * <li>The document collection is the result of a multi-database full-text search.</li>
	 * </ul>
	 * </p>
	 *
	 * @param doc
	 *            The document to be added. Cannot be <code>null</code>.
	 */
	@Override
	public abstract void addDocument(final lotus.domino.Document doc);

	/**
	 * Adds a document to a collection.
	 * <p>
	 * This method throws an exception if:
	 * <ul>
	 * <li>The document is a duplicate of one already in the collection.</li>
	 * <li>The document collection is the result of a multi-database full-text search.</li>
	 * </ul>
	 * </p>
	 *
	 * @param doc
	 *            The document to be added. Cannot be <code>null</code>.
	 * @param checkDups
	 *            If true, forces a remote (IIOP) add to be made immediately rather than on the next navigation or other method (such as
	 *            stampAll) that calls the server, so that a duplicate exception can be thrown immediately. Has no effect on local calls.
	 */
	@Override
	public abstract void addDocument(final lotus.domino.Document doc, final boolean checkDups);

	/**
	 * Returns a collection object which is a copy of the original collection.
	 *
	 * @return a DocumentCollection which is copy of the original.
	 */
	@Override
	public abstract DocumentCollection cloneCollection();

	/**
	 * Indicates whether or not a DocumentCollection contains the given Document.
	 * <p>
	 * The document whose containment this method determines must be in the same database as the original collection. Otherwise, the method
	 * will return the error "the specified note or notes do not exist in the database" or, if a noteID was passed to the method that
	 * matches a noteID in the original collection's database, the method will use the unintended document.
	 * </p>
	 * <p>
	 * If collection is an empty document collection, this method will return True.
	 * </p>
	 *
	 * @param noteid
	 *            A single noteID belonging to the DocumentCollection's database.
	 * @return true if this DocumentCollection contains the document specified by the noteid
	 */
	@Override
	public abstract boolean contains(final int noteid);

	/**
	 * Indicates whether or not a DocumentCollection contains the given Documents.
	 * <p>
	 * The document or documents whose containment this method determines must be in the same database as the original collection.
	 * Otherwise, the method will return the error "the specified note or notes do not exist in the database" or, if a noteID was passed to
	 * the method that matches a noteID in the original collection's database, the method will use the unintended document.
	 *
	 * </p>
	 * <p>
	 * If collection is an empty document collection, this method will return True.
	 * </p>
	 *
	 * @param doc
	 *            An object of type {@link Document}, {@link DocumentCollection}, {@link ViewEntry}, or {@link ViewEntryCollection}. View
	 *            entries must point to documents.
	 * @return true if this DocumentCollection contains the document specified by the noteid
	 */
	@Override
	public abstract boolean contains(final lotus.domino.Base doc);

	/**
	 * Indicates whether or not a DocumentCollection contains the given Document.
	 * <p>
	 * The document whose containment this method determines must be in the same database as the original collection. Otherwise, the method
	 * will return the error "the specified note or notes do not exist in the database" or, if a noteID was passed to the method that
	 * matches a noteID in the original collection's database, the method will use the unintended document.
	 *
	 * </p>
	 * <p>
	 * If collection is an empty document collection, this method will return True.
	 * </p>
	 *
	 * @param noteid
	 *            A single noteID belonging to the DocumentCollection's database.
	 * @return true if this DocumentCollection contains the document specified by the noteid
	 */
	@Override
	public abstract boolean contains(final String noteid);

	/**
	 * Deletes a document from a collection.
	 * <p>
	 * This method throws an exception if:
	 * </p>
	 * <ul>
	 * <li>The document is already deleted.</li>
	 * <li>The document cannot be retrieved from this collection.</li>
	 * <li>The document collection is the result of a multi-database full-text search.</li>
	 * </ul>
	 *
	 * @param doc
	 *            The document to be deleted. Cannot be <code>null</code>.
	 */
	@Override
	public abstract void deleteDocument(final lotus.domino.Document doc);

	/**
	 * Conducts a full-text search of all the documents in a document collection, and reduces the collection to a sorted collection of those
	 * documents that match.
	 *
	 * <p>
	 * Note: This method moves the current pointer to the first document in the collection.
	 * </p>
	 *
	 * <p>
	 * The collection of documents that match the full-text query are sorted by relevance, with highest relevance first. You can access the
	 * relevance score of each document in the collection using {@link org.openntf.domino.Document#getFTSearchScore()} Document.
	 * </p>
	 *
	 * <p>
	 * If the database is not full-text indexed, this method works, but less efficiently. To test for an index, use
	 *
	 * @param query
	 *            The full-text query. {@link org.openntf.domino.Database#isFTIndexed()}. To create an index on a local database, use
	 *            {@link org.openntf.domino.Database#updateFTIndex(boolean)}
	 *            </p>
	 *
	 *            <p>
	 *            This method searches all documents in a document collection. To search all documents in a database, use
	 *            {@link org.openntf.domino.Database#FTSearch(String)} in Database. To search only documents found in a particular view, use
	 *            {@link org.openntf.domino.View#FTSearch(String)} in View or
	 *            {@link org.openntf.domino.ViewEntryCollection#FTSearch(String)} in ViewEntryCollection.
	 *            </p>
	 *
	 *            <p>
	 *            <b>Query syntax</b><br>
	 *            To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes.
	 *            Remember to escape quotes if you are inside a literal.
	 *            </p>
	 *
	 *            <p>
	 *            Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see "Refining a search query using
	 *            operators" in Notes� Help. Search for "query syntax" in the Domino� Designer Eclipse help system or information center
	 *            (for example, http://publib.boulder.ibm.com/infocenter/domhelp/v8r0/index.jsp), both of which include Notes.
	 *            </p>
	 */
	@Override
	public abstract void FTSearch(final String query);

	/**
	 * Conducts a full-text search of all the documents in a document collection, and reduces the collection to a sorted collection of those
	 * documents that match.
	 *
	 * <p>
	 * Note: This method moves the current pointer to the first document in the collection.
	 * </p>
	 *
	 * <p>
	 * The collection of documents that match the full-text query are sorted by relevance, with highest relevance first. You can access the
	 * relevance score of each document in the collection using {@link org.openntf.domino.Document#getFTSearchScore()} Document.
	 * </p>
	 *
	 * <p>
	 * If the database is not full-text indexed, this method works, but less efficiently. To test for an index, use
	 *
	 * @param query
	 *            The full-text query.
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the query. Set this parameter to 0 to receive all matching
	 *            documents. {@link org.openntf.domino.Database#isFTIndexed()}. To create an index on a local database, use
	 *            {@link org.openntf.domino.Database#updateFTIndex(boolean)}
	 *            </p>
	 *
	 *            <p>
	 *            This method searches all documents in a document collection. To search all documents in a database, use
	 *            {@link org.openntf.domino.Database#FTSearch(String)} in Database. To search only documents found in a particular view, use
	 *            {@link org.openntf.domino.View#FTSearch(String)} in View or
	 *            {@link org.openntf.domino.ViewEntryCollection#FTSearch(String)} in ViewEntryCollection.
	 *            </p>
	 *
	 *            <p>
	 *            <b>Query syntax</b><br>
	 *            To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes.
	 *            Remember to escape quotes if you are inside a literal.
	 *            </p>
	 *
	 *            <p>
	 *            Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see "Refining a search query using
	 *            operators" in Notes� Help. Search for "query syntax" in the Domino� Designer Eclipse help system or information center
	 *            (for example, http://publib.boulder.ibm.com/infocenter/domhelp/v8r0/index.jsp), both of which include Notes.
	 *            </p>
	 */
	@Override
	public abstract void FTSearch(final String query, final int maxDocs);

	/**
	 * The number of documents in a collection.
	 *
	 * @return A {@link java.lang.Integer} set to the number of documents in the collection.
	 */
	@Override
	public abstract int getCount();

	/**
	 * Gets a specified document in a collection.
	 *
	 * <p>
	 * This method gets a document in a document collection that is the same as a reference document that does not necessarily come from the
	 * collection (for example, a document retrieved from another collection).
	 * </p>
	 *
	 * @param doc
	 *            The {@link org.openntf.domino.Document} you are looking for
	 *
	 * @return The specified {@link org.openntf.domino.Document} from the collection,If the reference document is not in the collection, you
	 *         get a <code>null</code> return.
	 */
	@Override
	public abstract Document getDocument(final lotus.domino.Document doc);

	/**
	 * Gets the first document in a collection.
	 *
	 * @return Returns the first {@link org.openntf.domino.Document} in the collection.
	 */
	@Override
	public abstract org.openntf.domino.Document getFirstDocument();

	/**
	 * Gets the last document in a collection.
	 *
	 * @return Returns the last {@link org.openntf.domino.Document} in the collection.
	 */
	@Override
	public abstract org.openntf.domino.Document getLastDocument();

	/**
	 * Gets the next document in the collection.
	 *
	 * @deprecated Replaced by iterator. Use <code>'for (Document doc : DocumentCollection) {}'</code> instead to process a document
	 *             collection.
	 *
	 * @return Returns the next {@link org.openntf.domino.Document} in the collection. If there is no next document, returns
	 *         <code>null</code>.
	 */
	@Override
	@Deprecated
	@Legacy(Legacy.ITERATION_WARNING)
	public abstract org.openntf.domino.Document getNextDocument();

	/**
	 * Gets the next document in the collection that occurs after the current document.
	 *
	 * @deprecated Replaced by iterator. Use <code>'for (Document doc : DocumentCollection) {}'</code> instead to process a document
	 *             collection.
	 *
	 * @param doc
	 *            Any document in the collection. Cannot be <code>null</code>.
	 *
	 * @return Returns the next {@link org.openntf.domino.Document} in the collection. If there is no next document, returns
	 *         <code>null</code>.
	 */
	@Override
	@Deprecated
	@Legacy(Legacy.ITERATION_WARNING)
	public abstract Document getNextDocument(final lotus.domino.Document doc);

	/**
	 * Gets the nTh document in the collection.
	 *
	 * @deprecated Replaced by iterator. Use <code>'for (Document doc : DocumentCollection) {}'</code> instead to process a document
	 *             collection.
	 *
	 * @param n
	 *            A number indicating the document to return. Use 1 to indicate the first document in the collection, 2 to indicate the
	 *            second document, and so on.
	 *
	 *
	 * @return Returns the next {@link org.openntf.domino.Document} in the collection. If there is no nTH document, returns
	 *         <code>null</code>.
	 * @throws NthDocumentMethodNotPermittedException
	 *             when a Fixes.BLOCK_NTH_DOCUMENT fix is enabled
	 */
	@Override
	@Deprecated
	@Legacy(Legacy.ITERATION_WARNING)
	public abstract Document getNthDocument(final int n);

	/**
	 * The database that contains the document collection
	 *
	 * @return Returns the {@link org.openntf.domino.Database} in which this document collection was created.
	 */
	@Override
	public abstract org.openntf.domino.Database getParent();

	/**
	 * Gets the previous document in the collection.
	 *
	 * @return Returns the previous {@link org.openntf.domino.Document} in the collection. If there is no previous document, returns
	 *         <code>null</code>.
	 */
	@Override
	public abstract org.openntf.domino.Document getPrevDocument();

	/**
	 * Gets the previous document in the collection that occurs before the current document.
	 *
	 * @param doc
	 *            Any document in the collection. Cannot be <code>null</code>.
	 *
	 * @return Returns the previous {@link org.openntf.domino.Document} in the collection. If there is no previous document, returns
	 *         <code>null</code>.
	 */
	@Override
	public abstract Document getPrevDocument(final lotus.domino.Document doc);

	/**
	 * The text of the query that produced a document collection if the collection results from a full-text or other search.
	 *
	 * @return Returns a {@link java.lang.String} of the query that produced the collection.
	 */
	@Override
	public abstract String getQuery();

	/**
	 * The database end time for a collection obtained through {@link org.openntf.domino.Database#getModifiedDocuments()} in Database.
	 *
	 * <p>
	 * This time should be specified as the "since" time in a subsequent call to {@link org.openntf.domino.Database#getModifiedDocuments()}
	 * where you want to get all modified documents since the most recent call.
	 * </p>
	 *
	 * <p>
	 * The database time may differ from the system time. Do not use the system time in
	 *
	 * @return Returns the end time for a collection obtained through {@link org.openntf.domino.Database#getModifiedDocuments()}, For
	 *         collections not produced through {@link org.openntf.domino.Database#getModifiedDocuments()}, this property returns
	 *         <code>null</code>. {@link org.openntf.domino.Database#getModifiedDocuments()} where you want to get all modified documents
	 *         since the most recent call.
	 *         </p>
	 */
	@Override
	public abstract DateTime getUntilTime();

	/**
	 * Removes from a document collection any documents not also contained in a second collection.
	 *
	 * <p>
	 * The document or documents being intersected by this method must be in the same database as the original collection. Otherwise, the
	 * method will return the error "the specified note or notes do not exist in the database" or, if a noteID was passed to the method that
	 * matches a noteID in the original collection's database, the method will use the unintended document.
	 * </p>
	 *
	 * <p>
	 * On successful completion of this method, the original document collection will contain only the documents it contained prior to the
	 * call which are also contained in the input argument.
	 * </p>
	 *
	 * @param noteid
	 *            A single noteID belonging to the DocumentCollection's database.
	 *
	 */
	@Override
	public abstract void intersect(final int noteid);

	/**
	 * Removes from a document collection any documents not also contained in a second collection.
	 *
	 * <p>
	 * The document or documents being intersected by this method must be in the same database as the original collection. Otherwise, the
	 * method will return the error "the specified note or notes do not exist in the database" or, if a noteID was passed to the method that
	 * matches a noteID in the original collection's database, the method will use the unintended document.
	 * </p>
	 *
	 * <p>
	 * On successful completion of this method, the original document collection will contain only the documents it contained prior to the
	 * call which are also contained in the input argument.
	 * </p>
	 *
	 * @param doc
	 *            A single document belonging to the DocumentCollection's database.
	 *
	 */
	@Override
	public abstract void intersect(final lotus.domino.Base doc);

	/**
	 * Removes from a document collection any documents not also contained in a second collection.
	 *
	 * <p>
	 * The document or documents being intersected by this method must be in the same database as the original collection. Otherwise, the
	 * method will return the error "the specified note or notes do not exist in the database" or, if a noteID was passed to the method that
	 * matches a noteID in the original collection's database, the method will use the unintended document.
	 * </p>
	 *
	 * <p>
	 * On successful completion of this method, the original document collection will contain only the documents it contained prior to the
	 * call which are also contained in the input argument.
	 * </p>
	 *
	 * @param noteid
	 *            A single noteID belonging to the DocumentCollection's database.
	 *
	 */
	@Override
	public abstract void intersect(final String noteid);

	/**
	 * Indicates whether the documents in a collection are sorted. A collection is sorted only when it results from a full-text search.
	 *
	 * <p>
	 * When a collection is sorted, the documents are sorted by relevance score with the most relevant document appearing first. A relevance
	 * score is a number assigned to each document that matches a particular full-text search query. The number is related to the number of
	 * matches that were found in the document.
	 * </p>
	 *
	 * @return Returns <code>true</code> if the collection is sorted and <code>false</code> if it is not sorted.
	 */
	@Override
	public abstract boolean isSorted();

	/**
	 * Marks all the documents in a collection read for the current user.
	 *
	 * <p>
	 * If the database does not track unread marks, all documents are considered read, and this method has no effect.
	 * </p>
	 */
	@Override
	public abstract void markAllRead();

	/**
	 * Marks all the documents in a collection read.
	 *
	 * <p>
	 * If the database does not track unread marks, all documents are considered read, and this method has no effect.
	 * </p>
	 *
	 * @param userName
	 *            Marks all the documents in the collection as read on behalf of the given name.
	 */
	@Override
	public abstract void markAllRead(final String userName);

	/**
	 * Marks all the documents in a collection unread for the current user.
	 *
	 * <p>
	 * If the database does not track unread marks, all documents are considered read, and this method has no effect.
	 * </p>
	 */
	@Override
	public abstract void markAllUnread();

	/**
	 * Marks all the documents in a collection unread.
	 *
	 * <p>
	 * If the database does not track unread marks, all documents are considered read, and this method has no effect.
	 * </p>
	 *
	 * @param userName
	 *            Marks all the documents in the collection as unread on behalf of the given name.
	 */
	@Override
	public abstract void markAllUnread(final String userName);

	/**
	 * Adds to a document collection a document specified by its noteid.
	 * <p>
	 * The document being merged by this method must be in the same database as the original collection. Otherwise, the method will return
	 * the error "the specified note or notes do not exist in the database" or, if a noteID was passed to the method that matches a noteID
	 * in the original collection's database, the method will use the unintended document.
	 * </p>
	 * <p>
	 * This method performs a union of the two document collections.
	 * </p>
	 *
	 * @param noteID
	 *            A single noteID belonging to the DocumentCollection's database.
	 *
	 */
	@Override
	public abstract void merge(final int noteid);

	/**
	 * Adds to a document collection any documents not already in the collection that are contained in a second collection.
	 * <p>
	 * The document or documents being merged by this method must be in the same database as the original collection. Otherwise, the method
	 * will return the error "the specified note or notes do not exist in the database" or, if a noteID was passed to the method that
	 * matches a noteID in the original collection's database, the method will use the unintended document.
	 * </p>
	 * <p>
	 * This method performs a union of the two document collections.
	 * </p>
	 *
	 * @param doc
	 *            A single document belonging to the DocumentCollection's database or a documentCollection, all documents of which must
	 *            belong to the DocumentCollection's database.
	 *
	 */
	@Override
	public abstract void merge(final lotus.domino.Base doc);

	/**
	 * Adds to a document collection a document specified by its noteid.
	 * <p>
	 * The document being merged by this method must be in the same database as the original collection. Otherwise, the method will return
	 * the error "the specified note or notes do not exist in the database" or, if a noteID was passed to the method that matches a noteID
	 * in the original collection's database, the method will use the unintended document.
	 * </p>
	 * <p>
	 * This method performs a union of the two document collections.
	 * </p>
	 *
	 * @param noteID
	 *            A single noteID belonging to the DocumentCollection's database.
	 */
	@Override
	public abstract void merge(final String noteid);

	/**
	 * Adds all the documents in the collection to the specified folder. If the folder does not exist in the document's database, it is
	 * created.
	 *
	 * <p>
	 * If a document is already inside the folder you specify, putAllInFolder does nothing for that document.
	 * </p>
	 *
	 * @param folderName
	 *            The name of the folder in which to place the documents. If the folder is within another folder, specify a path to it,
	 *            separating folder names with backward slashes, for example, "Vehicles\\Bikes".
	 */
	@Override
	public abstract void putAllInFolder(final String folderName);

	/**
	 * Adds all the documents in the collection to the specified folder. If the folder does not exist in the document's database, it is
	 * created.
	 *
	 * <p>
	 * If a document is already inside the folder you specify, putAllInFolder does nothing for that document.
	 * </p>
	 *
	 * @param folderName
	 *            The name of the folder in which to place the documents. If the folder is within another folder, specify a path to it,
	 *            separating folder names with backward slashes, for example, "Vehicles\\Bikes".
	 * @param createOnFail
	 *            If true (default), creates the folder if it does not exist.
	 */
	@Override
	public abstract void putAllInFolder(final String folderName, final boolean createOnFail);

	/**
	 * Permanently removes the documents in a collection from a database.
	 *
	 * <p>
	 * This method moves the current pointer to the first document in the collection.
	 * </p>
	 *
	 * <p>
	 * All documents removed from the database as a result of this operation are also removed from the collection.
	 * </p>
	 *
	 * @param force
	 *            If <code>true</code>, a document is removed even if another user modifies the document after it is retrieved. If
	 *            <code>false</code>, a document is not removed if another user modifies it first.
	 */
	@Override
	public abstract void removeAll(final boolean force);

	/**
	 * Removes all documents in the collection from the specified folder.
	 *
	 * <p>
	 * This method moves the current pointer to the first document in the collection.
	 * </p>
	 *
	 * <p>
	 * The method does nothing for documents not in the folder you specify. This method does nothing if the folder you specify does not
	 * exist.
	 * </p>
	 *
	 * @param folderName
	 *            The name of the folder from which to remove the document. If the folder is within another folder, specify a path to it,
	 *            separating folder names with backward slashes. For example, "Vehicles\\Bikes".
	 */
	@Override
	public abstract void removeAllFromFolder(final String folderName);

	/**
	 * Replaces the value of a specified item in all documents in a collection.
	 *
	 * <p>
	 * This method moves the current pointer to the first document in the collection.
	 * </p>
	 *
	 * <p>
	 * If the item does not exist, it is created.
	 * </p>
	 *
	 * <p>
	 * The item values are immediately written to the server documents. You do not have to use the save method of Document after stampAll.
	 * However, any documents modified by your script must be saved before calling stampAll.
	 * </p>
	 *
	 * <p>
	 * This method does not modify existing Document objects. Documents must be retrieved again to see the changes.
	 * </p>
	 *
	 * @param itemName
	 *            The name of the item.
	 * @param value
	 *            A value appropriate for the item type. @see org.openntf.domino.Document#replaceItemValue(String, Object)}.
	 *
	 */
	@Override
	public abstract void stampAll(final String itemName, final Object value);

	/**
	 * Removes from a document collection document specified by its noteid
	 * <p>
	 * The document being subtracted by this method must be in the same database as the original collection. Otherwise, the method will
	 * return the error "the specified note or notes do not exist in the database" or, if a noteID was passed to the method that matches a
	 * noteID in the original collection's database, the method will use the unintended document.
	 * </p>
	 * <p>
	 * On successful completion of this method, the original document collection will contain only the documents it contained prior to the
	 * call which are not specified by the input argument.
	 * </p>
	 *
	 *
	 * @param noteID
	 *            A single noteID belonging to the DocumentCollection's database.
	 */
	@Override
	public abstract void subtract(final int noteid);

	/**
	 * Removes from a document collection any documents contained in a second collection.
	 * <p>
	 * The document or documents being subtracted by this method must be in the same database as the original collection. Otherwise, the
	 * method will return the error "the specified note or notes do not exist in the database" or, if a noteID was passed to the method that
	 * matches a noteID in the original collection's database, the method will use the unintended document.
	 * </p>
	 * <p>
	 * On successful completion of this method, the original document collection will contain only the documents it contained prior to the
	 * call which are not specified by the input argument.
	 * </p>
	 *
	 * @param document
	 *            A single document belonging to the DocumentCollection's database or a documentCollection, all documents of which must
	 *            belong to the DocumentCollection's database.
	 *
	 * @return
	 */
	@Override
	public abstract void subtract(final lotus.domino.Base doc);

	/**
	 * Removes from a document collection document specified by its noteid
	 * <p>
	 * The document being subtracted by this method must be in the same database as the original collection. Otherwise, the method will
	 * return the error "the specified note or notes do not exist in the database" or, if a noteID was passed to the method that matches a
	 * noteID in the original collection's database, the method will use the unintended document.
	 * </p>
	 * <p>
	 * On successful completion of this method, the original document collection will contain only the documents it contained prior to the
	 * call which are not specified by the input argument.
	 * </p>
	 *
	 *
	 * @param noteID
	 *            A single noteID belonging to the DocumentCollection's database.
	 */
	@Override
	public abstract void subtract(final String noteid);

	/**
	 * Marks all documents in a collection as processed by an agent.
	 * <p>
	 * See {@link AgentContext#updateProcessedDoc(lotus.domino.Document)} for a description of the update process.
	 * </p>
	 */
	@Override
	public abstract void updateAll();

}
