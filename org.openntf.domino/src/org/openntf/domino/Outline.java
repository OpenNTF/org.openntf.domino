package org.openntf.domino;

import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.OutlineEntry;

public interface Outline extends Base<lotus.domino.Outline>, lotus.domino.Outline {

	@Override
	public void addEntry(lotus.domino.OutlineEntry arg0, lotus.domino.OutlineEntry arg1);

	@Override
	public void addEntry(lotus.domino.OutlineEntry arg0, lotus.domino.OutlineEntry arg1, boolean arg2);

	@Override
	public void addEntry(OutlineEntry arg0, OutlineEntry arg1, boolean arg2, boolean arg3);

	@Override
	public OutlineEntry createEntry(OutlineEntry arg0);

	@Override
	public OutlineEntry createEntry(OutlineEntry arg0, OutlineEntry arg1);

	@Override
	public OutlineEntry createEntry(OutlineEntry arg0, OutlineEntry arg1, boolean arg2);

	@Override
	public OutlineEntry createEntry(OutlineEntry arg0, OutlineEntry arg1, boolean arg2, boolean arg3);

	@Override
	public OutlineEntry createEntry(String arg0);

	@Override
	public OutlineEntry createEntry(String arg0, OutlineEntry arg1);

	@Override
	public OutlineEntry createEntry(String arg0, OutlineEntry arg1, boolean arg2);

	@Override
	public OutlineEntry createEntry(String arg0, OutlineEntry arg1, boolean arg2, boolean arg3);

	@Override
	public String getAlias();

	@Override
	public OutlineEntry getChild(OutlineEntry arg0);

	@Override
	public String getComment();

	@Override
	public lotus.domino.Outline getDelegate();

	@Override
	public OutlineEntry getFirst();

	@Override
	public OutlineEntry getLast();

	@Override
	public String getName();

	@Override
	public OutlineEntry getNext(OutlineEntry arg0);

	@Override
	public OutlineEntry getNextSibling(OutlineEntry arg0);

	@Override
	public OutlineEntry getParent(OutlineEntry arg0);

	@Override
	public Database getParentDatabase();

	@Override
	public OutlineEntry getPrev(OutlineEntry arg0);

	@Override
	public OutlineEntry getPrevSibling(OutlineEntry arg0);

	@Override
	public void moveEntry(OutlineEntry arg0, OutlineEntry arg1);

	@Override
	public void moveEntry(OutlineEntry arg0, OutlineEntry arg1, boolean arg2);

	@Override
	public void moveEntry(OutlineEntry arg0, OutlineEntry arg1, boolean arg2, boolean arg3);

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public void removeEntry(OutlineEntry arg0);

	@Override
	public int save();

	@Override
	public void setAlias(String arg0);

	@Override
	public void setComment(String arg0);

	@Override
	public void setName(String arg0);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
