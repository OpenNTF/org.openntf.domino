package org.openntf.domino;

import java.util.Vector;

public interface NotesProperty extends Base<lotus.domino.NotesProperty>, lotus.domino.NotesProperty {

	@Override
	public void clear();

	public lotus.domino.NotesProperty getDelegate();

	public String getDescription();

	@Override
	public String getName();

	@Override
	public String getNamespace();

	@Override
	public String getTitle();

	@Override
	public String getTypeName();

	@Override
	public Vector getValues();

	@Override
	public String getValueString();

	@Override
	public boolean isInput();

	@Override
	public void publish();

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
