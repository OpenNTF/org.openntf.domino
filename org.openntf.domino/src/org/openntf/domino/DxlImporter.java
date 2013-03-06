package org.openntf.domino;

import java.util.Vector;

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
	public String getNextImportedNoteID(String arg0);

	@Override
	public boolean getReplaceDbProperties();

	@Override
	public boolean getReplicaRequiredForReplaceOrUpdate();

	@Override
	public int getUnknownTokenLogOption();

	@Override
	public void importDxl(RichTextItem arg0, Database arg1);

	@Override
	public void importDxl(Stream arg0, Database arg1);

	@Override
	public void importDxl(String arg0, Database arg1);

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public void setAclImportOption(int arg0);

	@Override
	public void setCompileLotusScript(boolean arg0);

	@Override
	public void setCreateFTIndex(boolean arg0);

	@Override
	public void setDesignImportOption(int arg0);

	@Override
	public void setDocumentImportOption(int arg0);

	@Override
	public void setExitOnFirstFatalError(boolean arg0);

	@Override
	public void setInputValidationOption(int arg0);

	@Override
	public void setLogComment(String arg0);

	@Override
	public void setReplaceDbProperties(boolean arg0);

	@Override
	public void setReplicaRequiredForReplaceOrUpdate(boolean arg0);

	@Override
	public void setUnknownTokenLogOption(int arg0);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
