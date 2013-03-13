package org.openntf.domino;

import lotus.domino.Database;
import lotus.domino.OutlineEntry;

public interface Outline extends Base<lotus.domino.Outline>, lotus.domino.Outline {

	@Override
	@Deprecated
	public void addEntry(lotus.domino.OutlineEntry entry, lotus.domino.OutlineEntry referenceEntry);

	@Override
	@Deprecated
	public void addEntry(lotus.domino.OutlineEntry entry, lotus.domino.OutlineEntry referenceEntry, boolean after);

	@Override
	@Deprecated
	public void addEntry(OutlineEntry entry, OutlineEntry referenceEntry, boolean after, boolean asChild);

	@Override
	public OutlineEntry createEntry(OutlineEntry fromEntry);

	@Override
	public OutlineEntry createEntry(OutlineEntry fromEntry, OutlineEntry referenceEntry);

	@Override
	public OutlineEntry createEntry(OutlineEntry fromEntry, OutlineEntry referenceEntry, boolean after);

	@Override
	public OutlineEntry createEntry(OutlineEntry fromEntry, OutlineEntry referenceEntry, boolean after, boolean asChild);

	@Override
	public OutlineEntry createEntry(String entryName);

	@Override
	public OutlineEntry createEntry(String entryName, OutlineEntry referenceEntry);

	@Override
	public OutlineEntry createEntry(String entryName, OutlineEntry referenceEntry, boolean after);

	@Override
	public OutlineEntry createEntry(String entryName, OutlineEntry referenceEntry, boolean after, boolean asChild);

	@Override
	public String getAlias();

	@Override
	public OutlineEntry getChild(OutlineEntry entry);

	@Override
	public String getComment();

	@Override
	public OutlineEntry getFirst();

	@Override
	public OutlineEntry getLast();

	@Override
	public String getName();

	@Override
	public OutlineEntry getNext(OutlineEntry entry);

	@Override
	public OutlineEntry getNextSibling(OutlineEntry entry);

	@Override
	public OutlineEntry getParent(OutlineEntry entry);

	@Override
	public Database getParentDatabase();

	@Override
	public OutlineEntry getPrev(OutlineEntry entry);

	@Override
	public OutlineEntry getPrevSibling(OutlineEntry entry);

	@Override
	public void moveEntry(OutlineEntry entry, OutlineEntry referenceEntry);

	@Override
	public void moveEntry(OutlineEntry entry, OutlineEntry referenceEntry, boolean after);

	@Override
	public void moveEntry(OutlineEntry entry, OutlineEntry referenceEntry, boolean after, boolean asChild);

	@Override
	public void removeEntry(OutlineEntry entry);

	@Override
	public int save();

	@Override
	public void setAlias(String alias);

	@Override
	public void setComment(String comment);

	@Override
	public void setName(String name);

}
