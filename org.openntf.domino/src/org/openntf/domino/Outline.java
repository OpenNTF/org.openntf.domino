package org.openntf.domino;

public interface Outline extends Base<lotus.domino.Outline>, lotus.domino.Outline {

	@Override
	@Deprecated
	public void addEntry(lotus.domino.OutlineEntry entry, lotus.domino.OutlineEntry referenceEntry);

	@Override
	@Deprecated
	public void addEntry(lotus.domino.OutlineEntry entry, lotus.domino.OutlineEntry referenceEntry, boolean after);

	@Override
	@Deprecated
	public void addEntry(lotus.domino.OutlineEntry entry, lotus.domino.OutlineEntry referenceEntry, boolean after, boolean asChild);

	@Override
	public OutlineEntry createEntry(lotus.domino.OutlineEntry fromEntry);

	@Override
	public OutlineEntry createEntry(lotus.domino.OutlineEntry fromEntry, lotus.domino.OutlineEntry referenceEntry);

	@Override
	public OutlineEntry createEntry(lotus.domino.OutlineEntry fromEntry, lotus.domino.OutlineEntry referenceEntry, boolean after);

	@Override
	public OutlineEntry createEntry(lotus.domino.OutlineEntry fromEntry, lotus.domino.OutlineEntry referenceEntry, boolean after,
			boolean asChild);

	@Override
	public OutlineEntry createEntry(String entryName);

	@Override
	public OutlineEntry createEntry(String entryName, lotus.domino.OutlineEntry referenceEntry);

	@Override
	public OutlineEntry createEntry(String entryName, lotus.domino.OutlineEntry referenceEntry, boolean after);

	@Override
	public OutlineEntry createEntry(String entryName, lotus.domino.OutlineEntry referenceEntry, boolean after, boolean asChild);

	@Override
	public String getAlias();

	@Override
	public OutlineEntry getChild(lotus.domino.OutlineEntry entry);

	@Override
	public String getComment();

	@Override
	public OutlineEntry getFirst();

	@Override
	public OutlineEntry getLast();

	@Override
	public String getName();

	@Override
	public OutlineEntry getNext(lotus.domino.OutlineEntry entry);

	@Override
	public OutlineEntry getNextSibling(lotus.domino.OutlineEntry entry);

	@Override
	public OutlineEntry getParent(lotus.domino.OutlineEntry entry);

	@Override
	public Database getParentDatabase();

	@Override
	public OutlineEntry getPrev(lotus.domino.OutlineEntry entry);

	@Override
	public OutlineEntry getPrevSibling(lotus.domino.OutlineEntry entry);

	@Override
	public void moveEntry(lotus.domino.OutlineEntry entry, lotus.domino.OutlineEntry referenceEntry);

	@Override
	public void moveEntry(lotus.domino.OutlineEntry entry, lotus.domino.OutlineEntry referenceEntry, boolean after);

	@Override
	public void moveEntry(lotus.domino.OutlineEntry entry, lotus.domino.OutlineEntry referenceEntry, boolean after, boolean asChild);

	@Override
	public void removeEntry(lotus.domino.OutlineEntry entry);

	@Override
	public int save();

	@Override
	public void setAlias(String alias);

	@Override
	public void setComment(String comment);

	@Override
	public void setName(String name);

}
