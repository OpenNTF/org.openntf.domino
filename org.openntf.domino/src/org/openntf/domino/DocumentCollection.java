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

import lotus.domino.Document;

import org.openntf.domino.annotations.Legacy;

// TODO: Auto-generated Javadoc
/**
 * The Interface DocumentCollection.
 */
public interface DocumentCollection extends lotus.domino.DocumentCollection, org.openntf.domino.Base<lotus.domino.DocumentCollection>,
		Iterable<org.openntf.domino.Document> {

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#getCount()
	 */
	public abstract int getCount();

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#getQuery()
	 */
	public abstract String getQuery();

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#getParent()
	 */
	public abstract org.openntf.domino.Database getParent();

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#getFirstDocument()
	 */
	public abstract org.openntf.domino.Document getFirstDocument();

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#getLastDocument()
	 */
	public abstract org.openntf.domino.Document getLastDocument();

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#getNextDocument(lotus.domino.Document)
	 */
	@Deprecated
	@Legacy(Legacy.ITERATION_WARNING)
	public abstract Document getNextDocument(Document doc);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#getPrevDocument(lotus.domino.Document)
	 */
	public abstract Document getPrevDocument(Document doc);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#getNthDocument(int)
	 */
	@Deprecated
	@Legacy(Legacy.ITERATION_WARNING)
	public abstract Document getNthDocument(int n);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#getNextDocument()
	 */
	@Deprecated
	@Legacy(Legacy.ITERATION_WARNING)
	public abstract org.openntf.domino.Document getNextDocument();

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#getPrevDocument()
	 */
	public abstract org.openntf.domino.Document getPrevDocument();

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#getDocument(lotus.domino.Document)
	 */
	public abstract Document getDocument(Document doc);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#addDocument(lotus.domino.Document)
	 */
	public abstract void addDocument(Document doc);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#addDocument(lotus.domino.Document, boolean)
	 */
	public abstract void addDocument(Document doc, boolean checkDups);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#deleteDocument(lotus.domino.Document)
	 */
	public abstract void deleteDocument(Document doc);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#FTSearch(java.lang.String)
	 */
	public abstract void FTSearch(String query);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#FTSearch(java.lang.String, int)
	 */
	public abstract void FTSearch(String query, int maxDocs);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#isSorted()
	 */
	public abstract boolean isSorted();

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#putAllInFolder(java.lang.String)
	 */
	public abstract void putAllInFolder(String folderName);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#putAllInFolder(java.lang.String, boolean)
	 */
	public abstract void putAllInFolder(String folderName, boolean createOnFail);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#removeAll(boolean)
	 */
	public abstract void removeAll(boolean force);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#removeAllFromFolder(java.lang.String)
	 */
	public abstract void removeAllFromFolder(String folderName);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#stampAll(java.lang.String, java.lang.Object)
	 */
	public abstract void stampAll(String itemName, Object value);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#updateAll()
	 */
	public abstract void updateAll();

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#getUntilTime()
	 */
	public abstract DateTime getUntilTime();

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#markAllRead(java.lang.String)
	 */
	public abstract void markAllRead(String userName);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#markAllUnread(java.lang.String)
	 */
	public abstract void markAllUnread(String userName);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#markAllRead()
	 */
	public abstract void markAllRead();

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#markAllUnread()
	 */
	public abstract void markAllUnread();

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#intersect(int)
	 */
	public abstract void intersect(int noteid);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#intersect(java.lang.String)
	 */
	public abstract void intersect(String noteid);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#intersect(lotus.domino.Base)
	 */
	public abstract void intersect(lotus.domino.Base doc);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#merge(int)
	 */
	public abstract void merge(int noteid);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#merge(java.lang.String)
	 */
	public abstract void merge(String noteid);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#merge(lotus.domino.Base)
	 */
	public abstract void merge(lotus.domino.Base doc);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#subtract(int)
	 */
	public abstract void subtract(int noteid);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#subtract(java.lang.String)
	 */
	public abstract void subtract(String noteid);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#subtract(lotus.domino.Base)
	 */
	public abstract void subtract(lotus.domino.Base doc);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#contains(int)
	 */
	public abstract boolean contains(int noteid);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#contains(java.lang.String)
	 */
	public abstract boolean contains(String noteid);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#contains(lotus.domino.Base)
	 */
	public abstract boolean contains(lotus.domino.Base doc);

	/* (non-Javadoc)
	 * @see lotus.domino.DocumentCollection#cloneCollection()
	 */
	public abstract DocumentCollection cloneCollection();

}
