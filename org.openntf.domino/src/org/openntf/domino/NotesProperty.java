package org.openntf.domino;

import java.util.Vector;

public interface NotesProperty extends Base<lotus.domino.NotesProperty>, lotus.domino.NotesProperty {

	@Override
	public void clear();

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

}
