package org.openntf.domino;

import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.Outline;
import lotus.domino.View;

public interface OutlineEntry extends Base<lotus.domino.OutlineEntry>, lotus.domino.OutlineEntry {

	@Override
	public String getAlias();

	public lotus.domino.Database getDatabase();

	@Override
	public Document getDocument();

	@Override
	public int getEntryClass();

	@Override
	public String getFormula();

	@Override
	public String getFrameText();

	@Override
	public String getHideFormula();

	@Override
	public String getImagesText();

	@Override
	public boolean getKeepSelectionFocus();

	@Override
	public String getLabel();

	@Override
	public int getLevel();

	@Override
	public String getNamedElement();

	@Override
	public Outline getParent();

	@Override
	public int getType();

	@Override
	public String getURL();

	@Override
	public boolean getUseHideFormula();

	@Override
	public View getView();

	@Override
	public boolean hasChildren();

	@Override
	public boolean isHidden();

	@Override
	public boolean isHiddenFromNotes();

	@Override
	public boolean isHiddenFromWeb();

	@Override
	public boolean isInThisDB();

	@Override
	public boolean isPrivate();

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public boolean setAction(String arg0);

	@Override
	public void setAlias(String arg0);

	@Override
	public void setFrameText(String arg0);

	@Override
	public void setHidden(boolean arg0);

	@Override
	public void setHiddenFromNotes(boolean arg0);

	@Override
	public void setHiddenFromWeb(boolean arg0);

	@Override
	public void setHideFormula(String arg0);

	@Override
	public void setImagesText(String arg0);

	@Override
	public void setKeepSelectionFocus(boolean arg0);

	@Override
	public void setLabel(String arg0);

	@Override
	public boolean setNamedElement(Database arg0, String arg1, int arg2);

	@Override
	public boolean setNoteLink(Database arg0);

	@Override
	public boolean setNoteLink(lotus.domino.Document arg0);

	@Override
	public boolean setNoteLink(View arg0);

	@Override
	public boolean setURL(String arg0);

	@Override
	public void setUseHideFormula(boolean arg0);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
