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

// TODO: Auto-generated Javadoc
/**
 * The Interface Outline.
 */
public interface Outline extends Base<lotus.domino.Outline>, lotus.domino.Outline {

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#addEntry(lotus.domino.OutlineEntry, lotus.domino.OutlineEntry)
	 */
	@Override
	@Deprecated
	public void addEntry(lotus.domino.OutlineEntry entry, lotus.domino.OutlineEntry referenceEntry);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#addEntry(lotus.domino.OutlineEntry, lotus.domino.OutlineEntry, boolean)
	 */
	@Override
	@Deprecated
	public void addEntry(lotus.domino.OutlineEntry entry, lotus.domino.OutlineEntry referenceEntry, boolean after);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#addEntry(lotus.domino.OutlineEntry, lotus.domino.OutlineEntry, boolean, boolean)
	 */
	@Override
	@Deprecated
	public void addEntry(lotus.domino.OutlineEntry entry, lotus.domino.OutlineEntry referenceEntry, boolean after, boolean asChild);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#createEntry(lotus.domino.OutlineEntry)
	 */
	@Override
	public OutlineEntry createEntry(lotus.domino.OutlineEntry fromEntry);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#createEntry(lotus.domino.OutlineEntry, lotus.domino.OutlineEntry)
	 */
	@Override
	public OutlineEntry createEntry(lotus.domino.OutlineEntry fromEntry, lotus.domino.OutlineEntry referenceEntry);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#createEntry(lotus.domino.OutlineEntry, lotus.domino.OutlineEntry, boolean)
	 */
	@Override
	public OutlineEntry createEntry(lotus.domino.OutlineEntry fromEntry, lotus.domino.OutlineEntry referenceEntry, boolean after);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#createEntry(lotus.domino.OutlineEntry, lotus.domino.OutlineEntry, boolean, boolean)
	 */
	@Override
	public OutlineEntry createEntry(lotus.domino.OutlineEntry fromEntry, lotus.domino.OutlineEntry referenceEntry, boolean after,
			boolean asChild);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#createEntry(java.lang.String)
	 */
	@Override
	public OutlineEntry createEntry(String entryName);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#createEntry(java.lang.String, lotus.domino.OutlineEntry)
	 */
	@Override
	public OutlineEntry createEntry(String entryName, lotus.domino.OutlineEntry referenceEntry);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#createEntry(java.lang.String, lotus.domino.OutlineEntry, boolean)
	 */
	@Override
	public OutlineEntry createEntry(String entryName, lotus.domino.OutlineEntry referenceEntry, boolean after);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#createEntry(java.lang.String, lotus.domino.OutlineEntry, boolean, boolean)
	 */
	@Override
	public OutlineEntry createEntry(String entryName, lotus.domino.OutlineEntry referenceEntry, boolean after, boolean asChild);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#getAlias()
	 */
	@Override
	public String getAlias();

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#getChild(lotus.domino.OutlineEntry)
	 */
	@Override
	public OutlineEntry getChild(lotus.domino.OutlineEntry entry);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#getComment()
	 */
	@Override
	public String getComment();

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#getFirst()
	 */
	@Override
	public OutlineEntry getFirst();

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#getLast()
	 */
	@Override
	public OutlineEntry getLast();

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#getName()
	 */
	@Override
	public String getName();

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#getNext(lotus.domino.OutlineEntry)
	 */
	@Override
	public OutlineEntry getNext(lotus.domino.OutlineEntry entry);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#getNextSibling(lotus.domino.OutlineEntry)
	 */
	@Override
	public OutlineEntry getNextSibling(lotus.domino.OutlineEntry entry);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#getParent(lotus.domino.OutlineEntry)
	 */
	@Override
	public OutlineEntry getParent(lotus.domino.OutlineEntry entry);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#getParentDatabase()
	 */
	@Override
	public Database getParentDatabase();

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#getPrev(lotus.domino.OutlineEntry)
	 */
	@Override
	public OutlineEntry getPrev(lotus.domino.OutlineEntry entry);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#getPrevSibling(lotus.domino.OutlineEntry)
	 */
	@Override
	public OutlineEntry getPrevSibling(lotus.domino.OutlineEntry entry);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#moveEntry(lotus.domino.OutlineEntry, lotus.domino.OutlineEntry)
	 */
	@Override
	public void moveEntry(lotus.domino.OutlineEntry entry, lotus.domino.OutlineEntry referenceEntry);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#moveEntry(lotus.domino.OutlineEntry, lotus.domino.OutlineEntry, boolean)
	 */
	@Override
	public void moveEntry(lotus.domino.OutlineEntry entry, lotus.domino.OutlineEntry referenceEntry, boolean after);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#moveEntry(lotus.domino.OutlineEntry, lotus.domino.OutlineEntry, boolean, boolean)
	 */
	@Override
	public void moveEntry(lotus.domino.OutlineEntry entry, lotus.domino.OutlineEntry referenceEntry, boolean after, boolean asChild);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#removeEntry(lotus.domino.OutlineEntry)
	 */
	@Override
	public void removeEntry(lotus.domino.OutlineEntry entry);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#save()
	 */
	@Override
	public int save();

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#setAlias(java.lang.String)
	 */
	@Override
	public void setAlias(String alias);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#setComment(java.lang.String)
	 */
	@Override
	public void setComment(String comment);

	/* (non-Javadoc)
	 * @see lotus.domino.Outline#setName(java.lang.String)
	 */
	@Override
	public void setName(String name);

}
