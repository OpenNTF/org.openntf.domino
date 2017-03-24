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

import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.types.FactorySchema;

/**
 * Provides access to all or a subset of the entries in a view.
 *
 * <h3>Notable enhancements</h3>
 * <ul>
 * <li>Supports the foreach construct to iterate over the entries</li>
 * </ul>
 *
 * <h3>Snippets</h3>
 * <dl>
 * <dt>Iterating over all entries</dt>
 * <dd>
 *
 * <pre>
 * View view = database.getView("view_name");
 * ViewNavigator navigator = view.createViewNav();
 *
 * for (ViewEntry entry : navigator) {
 * 	//process the entry in a separate method
 * }
 * </pre>
 *
 * </dd>
 * </dl>
 * </p>
 */
public interface ViewNavigator extends Base<lotus.domino.ViewNavigator>, lotus.domino.ViewNavigator, org.openntf.domino.ext.ViewNavigator,
		Iterable<ViewEntry>, DatabaseDescendant {

	static enum Types {
		NONE, FROM, CATEGORY, CHILDREN, DESCENDANTS, UNREAD, MAXLEVEL, KEYS
	}

	public static class Schema extends FactorySchema<ViewNavigator, lotus.domino.ViewNavigator, View> {
		@Override
		public Class<ViewNavigator> typeClass() {
			return ViewNavigator.class;
		}

		@Override
		public Class<lotus.domino.ViewNavigator> delegateClass() {
			return lotus.domino.ViewNavigator.class;
		}

		@Override
		public Class<View> parentClass() {
			return View.class;
		}
	};

	public static final Schema SCHEMA = new Schema();
	public static final char DEFAULT_SEPARATOR = '.';

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getBufferMaxEntries()
	 */
	@Override
	public int getBufferMaxEntries();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getCacheSize()
	 */
	@Override
	public int getCacheSize();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getChild()
	 */
	@Override
	public ViewEntry getChild();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getChild(lotus.domino.ViewEntry)
	 */
	@Override
	public ViewEntry getChild(final lotus.domino.ViewEntry entry);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getCount()
	 */
	@Override
	public int getCount();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getCurrent()
	 */
	@Override
	public ViewEntry getCurrent();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getEntryOptions()
	 */
	@Override
	public int getEntryOptions();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getFirst()
	 */
	@Override
	public ViewEntry getFirst();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getFirstDocument()
	 */
	@Override
	public ViewEntry getFirstDocument();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getLast()
	 */
	@Override
	public ViewEntry getLast();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getLastDocument()
	 */
	@Override
	public ViewEntry getLastDocument();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getMaxLevel()
	 */
	@Override
	public int getMaxLevel();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getNext()
	 */
	@Override
	public ViewEntry getNext();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getNext(lotus.domino.ViewEntry)
	 */
	@Override
	public ViewEntry getNext(final lotus.domino.ViewEntry entry);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getNextCategory()
	 */
	@Override
	public ViewEntry getNextCategory();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getNextDocument()
	 */
	@Override
	public ViewEntry getNextDocument();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getNextSibling()
	 */
	@Override
	public ViewEntry getNextSibling();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getNextSibling(lotus.domino.ViewEntry)
	 */
	@Override
	public ViewEntry getNextSibling(final lotus.domino.ViewEntry entry);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getNth(int)
	 */
	@Override
	public ViewEntry getNth(final int n);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getParent()
	 */
	@Override
	public ViewEntry getParent();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getParent(lotus.domino.ViewEntry)
	 */
	@Override
	public ViewEntry getParent(final lotus.domino.ViewEntry entry);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getParentView()
	 */
	@Override
	public View getParentView();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getPos(java.lang.String, char)
	 */
	@Override
	public ViewEntry getPos(final String pos, final char separator);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getPrev()
	 */
	@Override
	public ViewEntry getPrev();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getPrev(lotus.domino.ViewEntry)
	 */
	@Override
	public ViewEntry getPrev(final lotus.domino.ViewEntry entry);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getPrevCategory()
	 */
	@Override
	public ViewEntry getPrevCategory();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getPrevDocument()
	 */
	@Override
	public ViewEntry getPrevDocument();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getPrevSibling()
	 */
	@Override
	public ViewEntry getPrevSibling();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#getPrevSibling(lotus.domino.ViewEntry)
	 */
	@Override
	public ViewEntry getPrevSibling(final lotus.domino.ViewEntry entry);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoChild()
	 */
	@Override
	public boolean gotoChild();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoChild(lotus.domino.ViewEntry)
	 */
	@Override
	public boolean gotoChild(final lotus.domino.ViewEntry entry);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoEntry(java.lang.Object)
	 */
	@Override
	public boolean gotoEntry(final Object entry);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoEntry(java.lang.Object)
	 */
	/**
	 * Goto entry.
	 *
	 * @param document
	 *            the document
	 * @return true, if successful
	 */
	public boolean gotoEntry(final Document document);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoEntry(java.lang.Object)
	 */
	/**
	 * Goto entry.
	 *
	 * @param entry
	 *            the entry
	 * @return true, if successful
	 */
	public boolean gotoEntry(final ViewEntry entry);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoFirst()
	 */
	@Override
	public boolean gotoFirst();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoFirstDocument()
	 */
	@Override
	public boolean gotoFirstDocument();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoLast()
	 */
	@Override
	public boolean gotoLast();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoLastDocument()
	 */
	@Override
	public boolean gotoLastDocument();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoNext()
	 */
	@Override
	public boolean gotoNext();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoNext(lotus.domino.ViewEntry)
	 */
	@Override
	public boolean gotoNext(final lotus.domino.ViewEntry entry);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoNextCategory()
	 */
	@Override
	public boolean gotoNextCategory();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoNextDocument()
	 */
	@Override
	public boolean gotoNextDocument();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoNextSibling()
	 */
	@Override
	public boolean gotoNextSibling();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoNextSibling(lotus.domino.ViewEntry)
	 */
	@Override
	public boolean gotoNextSibling(final lotus.domino.ViewEntry entry);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoParent()
	 */
	@Override
	public boolean gotoParent();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoParent(lotus.domino.ViewEntry)
	 */
	@Override
	public boolean gotoParent(final lotus.domino.ViewEntry entry);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoPos(java.lang.String, char)
	 */
	@Override
	public boolean gotoPos(final String pos, final char separator);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoPrev()
	 */
	@Override
	public boolean gotoPrev();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoPrev(lotus.domino.ViewEntry)
	 */
	@Override
	public boolean gotoPrev(final lotus.domino.ViewEntry entry);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoPrevCategory()
	 */
	@Override
	public boolean gotoPrevCategory();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoPrevDocument()
	 */
	@Override
	public boolean gotoPrevDocument();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoPrevSibling()
	 */
	@Override
	public boolean gotoPrevSibling();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#gotoPrevSibling(lotus.domino.ViewEntry)
	 */
	@Override
	public boolean gotoPrevSibling(final lotus.domino.ViewEntry entry);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#markAllRead()
	 */
	@Override
	public void markAllRead();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#markAllRead(java.lang.String)
	 */
	@Override
	public void markAllRead(final String userName);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#markAllUnread()
	 */
	@Override
	public void markAllUnread();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#markAllUnread(java.lang.String)
	 */
	@Override
	public void markAllUnread(final String userName);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#setAutoExpandGuidance(int, int[], int[])
	 */
	@Override
	public void setAutoExpandGuidance(final int maxEntries, final int[] collapsedNoteIds, final int[] expandedNoteIds);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#setAutoExpandGuidance(int, lotus.domino.NoteCollection, lotus.domino.NoteCollection)
	 */
	@Override
	public void setAutoExpandGuidance(final int maxEntries, final lotus.domino.NoteCollection collapsedNoteIds,
			final lotus.domino.NoteCollection expandedNoteIds);

	/**
	 * @see lotus.domino.ViewNavigator#setBufferMaxEntries(int)
	 * @deprecated use {@link org.openntf.domino.ViewNavigator#setCacheGuidance(int)} instead
	 */
	@Override
	@Deprecated
	public void setBufferMaxEntries(final int entryCount);

	/**
	 * Enable the cache to speed up traversing the view.
	 *
	 * <p>
	 * Enable the backend ViewNavigator cache to reduce the number of server transactions and associated network overhead when navigating
	 * and reading ColumnValues information from the Documents and Entries in a View. Performance gains are most profound when accessing a
	 * View residing on a server from a client, however retrieval from local Views will also be greatly improved.
	 * </p>
	 *
	 * @param maxEntries
	 *            value between 2 and 400. This is the number of entries that will be retrieved whenever the cache is populated. If you
	 *            intend to read out all ViewEntries in the View, use the maximum of 400.
	 * @see <a href="https://www.mindoo.com/web/blog.nsf/dx/17.01.2013085308KLEB9S.htm">Fast Notes view reading via Java API:New
	 *      ViewNavigator cache methods in 8.5.3</a>
	 * @see <a href="https://www-10.lotus.com/ldd/ddwiki.nsf/dx/Fast_Retrieval_of_View_Data_Using_the_ViewNavigator_Cache">Fast Retrieval of
	 *      View Data Using the ViewNavigator Cache</a>
	 */
	@Override
	public void setCacheGuidance(final int maxEntries);

	/**
	 * Enable the cache to speed up traversing the view.
	 *
	 * @param maxEntries
	 *            value between 2 and 400. This is the number of entries that will be retrieved whenever the cache is populated. If you
	 *            intend to read out all ViewEntries in the View, use the maximum of 400.
	 * @param readMode
	 *            One of ViewNavigator.VN_CACHEGUIDANCE_READALL or ViewNavigator.VN_CACHEGUIDANCE_READSELECTIVE. VN_CACHEGUIDANCE_READALL
	 *            preloads the next maxEntries entries of the view as if a categorized view would be totally expanded.
	 *            VN_CACHEGUIDANCE_READSELECTIVE fills the cache with entries based on the getter method that you call next
	 * @see #setCacheGuidance(int) setCacheGuidance method for details and external links
	 */
	@Override
	public void setCacheGuidance(final int maxEntries, final int readMode);

	/**
	 *
	 * @see lotus.domino.ViewNavigator#setCacheSize(int)
	 * @deprecated use {@link #setCacheGuidance(int)} instead
	 */
	@Override
	@Deprecated
	public void setCacheSize(final int size);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#setEntryOptions(int)
	 */
	@Override
	public void setEntryOptions(final int options);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#setMaxLevel(int)
	 */
	@Override
	public void setMaxLevel(final int maxLevel);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewNavigator#skip(int)
	 */
	@Override
	public int skip(final int count);
}
