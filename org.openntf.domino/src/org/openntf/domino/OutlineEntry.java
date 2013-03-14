package org.openntf.domino;

public interface OutlineEntry extends Base<lotus.domino.OutlineEntry>, lotus.domino.OutlineEntry {

	@Override
	public String getAlias();

	public Database getDatabase();

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
	public boolean setAction(String action);

	@Override
	public void setAlias(String alias);

	@Override
	public void setFrameText(String frameText);

	@Override
	public void setHidden(boolean flag);

	@Override
	public void setHiddenFromNotes(boolean flag);

	@Override
	public void setHiddenFromWeb(boolean flag);

	@Override
	public void setHideFormula(String formula);

	@Override
	public void setImagesText(String imagesText);

	@Override
	public void setKeepSelectionFocus(boolean flag);

	@Override
	public void setLabel(String label);

	@Override
	public boolean setNamedElement(lotus.domino.Database db, String elementName, int entryClass);

	@Override
	public boolean setNoteLink(lotus.domino.Database db);

	@Override
	public boolean setNoteLink(lotus.domino.Document doc);

	@Override
	public boolean setNoteLink(lotus.domino.View view);

	@Override
	public boolean setURL(String url);

	@Override
	public void setUseHideFormula(boolean flag);
}
