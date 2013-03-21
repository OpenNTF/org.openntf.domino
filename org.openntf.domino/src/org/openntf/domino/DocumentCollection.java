/*
 * Copyright OpenNTF 2013
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

import org.openntf.domino.annotations.Legacy;
import org.openntf.domino.types.DatabaseDescendant;

/**
 * The Interface DocumentCollection represents a collection of documents from a database, selected according to specific criteria.
 */
public interface DocumentCollection extends lotus.domino.DocumentCollection, org.openntf.domino.Base<lotus.domino.DocumentCollection>,
		Iterable<org.openntf.domino.Document>, DatabaseDescendant {

	/**
	 * The number of documents in a collection.
	 * 
	 * @return A {@link java.lang.Integer} set to the number of documents in the collection.
	 */
	public abstract int getCount();

	/**
	 * The text of the query that produced a document collection if the collection results from a full-text or other search.
	 * 
	 * @return Returns a {@link java.lang.String} of the query that produced the collection.
	 */
	public abstract String getQuery();

	/**
	 * The database that contains the document collection
	 * 
	 * @return Returns the {@link org.openntf.domino.Database} in which this document collection was created.
	 */
	public abstract org.openntf.domino.Database getParent();

	/**
	 * Gets the first document in a collection.
	 * 
	 * @return Returns the first {@link org.openntf.domino.Document} in the collection.
	 */
	public abstract org.openntf.domino.Document getFirstDocument();

	/**
	 * Gets the last document in a collection.
	 * 
	 * @return Returns the last {@link org.openntf.domino.Document} in the collection.
	 */
	public abstract org.openntf.domino.Document getLastDocument();

	/**
	 * Gets the next document in the collection that occurs after the current document.
	 * 
	 * @deprecated Replaced by iterator. Use <code>'for (Document doc : DocumentCollection) {}'</code> instead to process a document collection.
	 * 
	 * @param doc
	 *            Any document in the collection. Cannot be <code>null</code>.
	 * 
	 * @return Returns the next {@link org.openntf.domino.Document} in the collection. If there is no next document, returns
	 *         <code>null</code>.
	 */
	@Deprecated
	@Legacy(Legacy.ITERATION_WARNING)
	public abstract Document getNextDocument(lotus.domino.Document doc);

	/**
	 * Gets the previous document in the collection that occurs before the current document.
	 * 
	 * @param doc
	 *            Any document in the collection. Cannot be <code>null</code>.
	 * 
	 * @return Returns the previous {@link org.openntf.domino.Document} in the collection. If there is no previous document, returns
	 *         <code>null</code>.
	 */
	public abstract Document getPrevDocument(lotus.domino.Document doc);

	/**
	 * Gets the nTh document in the collection.
	 * 
	 * @deprecated Replaced by iterator. Use <code>'for (Document doc : DocumentCollection) {}'</code> instead to process a document collection.
	 * 
	 * @param n
	 *            A number indicating the document to return. Use 1 to indicate the first document in the collection, 2 to indicate the
	 *            second document, and so on.
	 * 
	 * 
	 * @return Returns the next {@link org.openntf.domino.Document} in the collection. If there is no nTH document, returns
	 *         <code>null</code>.
	 */
	@Deprecated
	@Legacy(Legacy.ITERATION_WARNING)
	public abstract Document getNthDocument(int n);

	/**
	 * Gets the next document in the collection.
	 * 
	 * @deprecated Replaced by iterator. Use <code>'for (Document doc : DocumentCollection) {}'</code> instead to process a document collection.
	 * 
	 * @return Returns the next {@link org.openntf.domino.Document} in the collection. If there is no next document, returns
	 *         <code>null</code>.
	 */
	@Deprecated
	@Legacy(Legacy.ITERATION_WARNING)
	public abstract org.openntf.domino.Document getNextDocument();

	/**
	 * Gets the previous document in the collection.
	 * 
	 * @return Returns the previous {@link org.openntf.domino.Document} in the collection. If there is no previous document, returns
	 *         <code>null</code>.
	 */
	public abstract org.openntf.domino.Document getPrevDocument();

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
	public abstract Document getDocument(lotus.domino.Document doc);

	/**
	 * Adds a document to a collection.
	 * 
	 * @param doc
	 *            The document to be added. Cannot be <code>null</code>.
	 */
	public abstract void addDocument(lotus.domino.Document doc);

	/**
	 * Adds a document to a collection.
	 * 
	 * @param doc
	 *            The document to be added. Cannot be <code>null</code>.
	 * @param checkDups
	 *            has no effect on local calls and only applies to Remote IIOP operations
	 */
	public abstract void addDocument(lotus.domino.Document doc, boolean checkDups);

	/**
	 * Deletes a document from a collection.
	 * 
	 * @param doc
	 *            The document to be deleted. Cannot be <code>null</code>.
	 */
	public abstract void deleteDocument(lotus.domino.Document doc);

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
	 * {@link org.openntf.domino.Database#isFTIndexed()}. To create an index on a local database, use
	 * {@link org.openntf.domino.Database#updateFTIndex(boolean)}
	 * </p>
	 * 
	 * <p>
	 * This method searches all documents in a document collection. To search all documents in a database, use
	 * {@link org.openntf.domino.Database#FTSearch(String)} in Database. To search only documents found in a particular view, use
	 * {@link org.openntf.domino.View#FTSearch(String)} in View or {@link org.openntf.domino.ViewEntryCollection#FTSearch(String)} in
	 * ViewEntryCollection.
	 * </p>
	 * 
	 * <p>
	 * <b>Query syntax</b><br>
	 * To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes. Remember to
	 * escape quotes if you are inside a literal.
	 * </p>
	 * 
	 * <p>
	 * Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see "Refining a search query using operators" in
	 * Notes� Help. Search for "query syntax" in the Domino� Designer Eclipse help system or information center (for example,
	 * http://publib.boulder.ibm.com/infocenter/domhelp/v8r0/index.jsp), both of which include Notes.
	 * </p>
	 * 
	 * @param query
	 *            The full-text query.
	 */
	public abstract void FTSearch(String query);

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
	 * {@link org.openntf.domino.Database#isFTIndexed()}. To create an index on a local database, use
	 * {@link org.openntf.domino.Database#updateFTIndex(boolean)}
	 * </p>
	 * 
	 * <p>
	 * This method searches all documents in a document collection. To search all documents in a database, use
	 * {@link org.openntf.domino.Database#FTSearch(String)} in Database. To search only documents found in a particular view, use
	 * {@link org.openntf.domino.View#FTSearch(String)} in View or {@link org.openntf.domino.ViewEntryCollection#FTSearch(String)} in
	 * ViewEntryCollection.
	 * </p>
	 * 
	 * <p>
	 * <b>Query syntax</b><br>
	 * To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes. Remember to
	 * escape quotes if you are inside a literal.
	 * </p>
	 * 
	 * <p>
	 * Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see "Refining a search query using operators" in
	 * Notes� Help. Search for "query syntax" in the Domino� Designer Eclipse help system or information center (for example,
	 * http://publib.boulder.ibm.com/infocenter/domhelp/v8r0/index.jsp), both of which include Notes.
	 * </p>
	 * 
	 * @param query
	 *            The full-text query.
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the query. Set this parameter to 0 to receive all matching
	 *            documents.
	 */
	public abstract void FTSearch(String query, int maxDocs);

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
	public abstract boolean isSorted();

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
	public abstract void putAllInFolder(String folderName);

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
	public abstract void putAllInFolder(String folderName, boolean createOnFail);

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
	public abstract void removeAll(boolean force);

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
	public abstract void removeAllFromFolder(String folderName);

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
	public abstract void stampAll(String itemName, Object value);

	/**
	 * Marks all documents in a collection as processed by an agent.
	 * 
	 */
	public abstract void updateAll();

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
	 * {@link org.openntf.domino.Database#getModifiedDocuments()} where you want to get all modified documents since the most recent call.
	 * </p>
	 * 
	 * 
	 * 
	 * 
	 * @return Returns the end time for a collection obtained through {@link org.openntf.domino.Database#getModifiedDocuments()}, For
	 *         collections not produced through {@link org.openntf.domino.Database#getModifiedDocuments()}, this property returns
	 *         <code>null</code>.
	 * 
	 */
	public abstract DateTime getUntilTime();

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
	public abstract void markAllRead(String userName);

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
	public abstract void markAllUnread(String userName);

	/**
	 * Marks all the documents in a collection read for the current user.
	 * 
	 * <p>
	 * If the database does not track unread marks, all documents are considered read, and this method has no effect.
	 * </p>
	 */
	public abstract void markAllRead();

	/**
	 * Marks all the documents in a collection unread for the current user.
	 * 
	 * <p>
	 * If the database does not track unread marks, all documents are considered read, and this method has no effect.
	 * </p>
	 */
	public abstract void markAllUnread();

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
	public abstract void intersect(int noteid);

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
	public abstract void intersect(String noteid);

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
	public abstract void intersect(lotus.domino.Base doc);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DocumentCollection#merge(int)
	 */
	public abstract void merge(int noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DocumentCollection#merge(java.lang.String)
	 */
	public abstract void merge(String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DocumentCollection#merge(lotus.domino.Base)
	 */
	public abstract void merge(lotus.domino.Base doc);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DocumentCollection#subtract(int)
	 */
	public abstract void subtract(int noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DocumentCollection#subtract(java.lang.String)
	 */
	public abstract void subtract(String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DocumentCollection#subtract(lotus.domino.Base)
	 */
	public abstract void subtract(lotus.domino.Base doc);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DocumentCollection#contains(int)
	 */
	public abstract boolean contains(int noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DocumentCollection#contains(java.lang.String)
	 */
	public abstract boolean contains(String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DocumentCollection#contains(lotus.domino.Base)
	 */
	public abstract boolean contains(lotus.domino.Base doc);

	/**
	 * Returns a collection object which is a copy of the original collection.
	 * 
	 * @return a DocumentCollection which is copy of the original.
	 */
	public abstract DocumentCollection cloneCollection();

}
