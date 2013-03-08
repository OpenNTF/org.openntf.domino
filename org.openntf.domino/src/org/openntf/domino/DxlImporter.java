package org.openntf.domino;

import lotus.domino.Database;
import lotus.domino.RichTextItem;
import lotus.domino.Stream;

public interface DxlImporter extends Base<lotus.domino.DxlImporter>, lotus.domino.DxlImporter {

	@Override
	public int getAclImportOption();

	@Override
	public boolean getCompileLotusScript();

	@Override
	public boolean getCreateFTIndex();

	@Override
	public lotus.domino.DxlImporter getDelegate();

	@Override
	public int getDesignImportOption();

	@Override
	public int getDocumentImportOption();

	@Override
	public boolean getExitOnFirstFatalError();

	@Override
	public String getFirstImportedNoteID();

	@Override
	public int getImportedNoteCount();

	@Override
	public int getInputValidationOption();

	@Override
	public String getLog();

	@Override
	public String getLogComment();

	@Override
	public String getNextImportedNoteID(String noteid);

	@Override
	public boolean getReplaceDbProperties();

	@Override
	public boolean getReplicaRequiredForReplaceOrUpdate();

	@Override
	public int getUnknownTokenLogOption();

	@Override
	public void importDxl(RichTextItem rtitem, Database database);

	@Override
	public void importDxl(Stream stream, Database database);

	@Override
	public void importDxl(String dxl, Database database);

	@Override
	public void setAclImportOption(int option);

	@Override
	public void setCompileLotusScript(boolean flag);

	@Override
	public void setCreateFTIndex(boolean flag);

	@Override
	public void setDesignImportOption(int option);

	@Override
	public void setDocumentImportOption(int option);

	@Override
	public void setExitOnFirstFatalError(boolean flag);

	@Override
	public void setInputValidationOption(int option);

	@Override
	public void setLogComment(String comment);

	@Override
	public void setReplaceDbProperties(boolean flag);

	@Override
	public void setReplicaRequiredForReplaceOrUpdate(boolean flag);

	@Override
	public void setUnknownTokenLogOption(int option);
}
