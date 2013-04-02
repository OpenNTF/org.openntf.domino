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

import org.openntf.domino.types.DatabaseDescendant;

// TODO: Auto-generated Javadoc
/**
 * The Interface ViewNavigator.
 */
public interface ViewNavigator extends Base<lotus.domino.ViewNavigator>, lotus.domino.ViewNavigator, DatabaseDescendant {

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
	public ViewEntry getChild(lotus.domino.ViewEntry entry);

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
	public ViewEntry getNext(lotus.domino.ViewEntry entry);

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
	public ViewEntry getNextSibling(lotus.domino.ViewEntry entry);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewNavigator#getNth(int)
	 */
	@Override
	public ViewEntry getNth(int n);

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
	public ViewEntry getParent(lotus.domino.ViewEntry entry);

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
	public ViewEntry getPos(String pos, char separator);

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
	public ViewEntry getPrev(lotus.domino.ViewEntry entry);

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
	public ViewEntry getPrevSibling(lotus.domino.ViewEntry entry);

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
	public boolean gotoChild(lotus.domino.ViewEntry entry);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewNavigator#gotoEntry(java.lang.Object)
	 */
	@Override
	public boolean gotoEntry(Object entry);

	/*
	 * (non-Javadoc)
	 * 
	 * <<<<<<< HEAD =======
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
	public boolean gotoEntry(Document document);

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
	public boolean gotoEntry(ViewEntry entry);

	/*
	 * (non-Javadoc)
	 * 
	 * >>>>>>> refs/remotes/origin/timtripcony
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
	public boolean gotoNext(lotus.domino.ViewEntry entry);

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
	public boolean gotoNextSibling(lotus.domino.ViewEntry entry);

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
	public boolean gotoParent(lotus.domino.ViewEntry entry);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewNavigator#gotoPos(java.lang.String, char)
	 */
	@Override
	public boolean gotoPos(String pos, char separator);

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
	public boolean gotoPrev(lotus.domino.ViewEntry entry);

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
	public boolean gotoPrevSibling(lotus.domino.ViewEntry entry);

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
	public void markAllRead(String userName);

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
	public void markAllUnread(String userName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewNavigator#setAutoExpandGuidance(int, int[], int[])
	 */
	@Override
	// TODO Figure out what these parameters are
	public void setAutoExpandGuidance(int arg0, int[] arg1, int[] arg2);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewNavigator#setAutoExpandGuidance(int, lotus.domino.NoteCollection, lotus.domino.NoteCollection)
	 */
	@Override
	public void setAutoExpandGuidance(int maxEntries, lotus.domino.NoteCollection collapsedNoteIds,
			lotus.domino.NoteCollection expandedNoteIds);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewNavigator#setBufferMaxEntries(int)
	 */
	@Override
	public void setBufferMaxEntries(int entryCount);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewNavigator#setCacheGuidance(int)
	 */
	@Override
	// TODO Figure out what these parameters are
	public void setCacheGuidance(int arg0);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewNavigator#setCacheGuidance(int, int)
	 */
	@Override
	// TODO Figure out what these parameters are
	public void setCacheGuidance(int arg0, int arg1);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewNavigator#setCacheSize(int)
	 */
	@Override
	public void setCacheSize(int size);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewNavigator#setEntryOptions(int)
	 */
	@Override
	public void setEntryOptions(int options);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewNavigator#setMaxLevel(int)
	 */
	@Override
	public void setMaxLevel(int maxLevel);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewNavigator#skip(int)
	 */
	@Override
	public int skip(int count);
}
