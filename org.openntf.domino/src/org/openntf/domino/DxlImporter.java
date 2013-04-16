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

import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface DxlImporter.
 */
public interface DxlImporter extends Base<lotus.domino.DxlImporter>, lotus.domino.DxlImporter, org.openntf.domino.ext.DxlImporter,
		SessionDescendant {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#getAclImportOption()
	 */
	@Override
	public int getAclImportOption();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#getCompileLotusScript()
	 */
	@Override
	public boolean getCompileLotusScript();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#getCreateFTIndex()
	 */
	@Override
	public boolean getCreateFTIndex();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#getDesignImportOption()
	 */
	@Override
	public int getDesignImportOption();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#getDocumentImportOption()
	 */
	@Override
	public int getDocumentImportOption();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#getExitOnFirstFatalError()
	 */
	@Override
	public boolean getExitOnFirstFatalError();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#getFirstImportedNoteID()
	 */
	@Override
	public String getFirstImportedNoteID();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#getImportedNoteCount()
	 */
	@Override
	public int getImportedNoteCount();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#getInputValidationOption()
	 */
	@Override
	public int getInputValidationOption();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#getLog()
	 */
	@Override
	public String getLog();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#getLogComment()
	 */
	@Override
	public String getLogComment();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#getNextImportedNoteID(java.lang.String)
	 */
	@Override
	public String getNextImportedNoteID(String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#getReplaceDbProperties()
	 */
	@Override
	public boolean getReplaceDbProperties();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#getReplicaRequiredForReplaceOrUpdate()
	 */
	@Override
	public boolean getReplicaRequiredForReplaceOrUpdate();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#getUnknownTokenLogOption()
	 */
	@Override
	public int getUnknownTokenLogOption();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#importDxl(lotus.domino.RichTextItem, lotus.domino.Database)
	 */
	@Override
	public void importDxl(lotus.domino.RichTextItem rtitem, lotus.domino.Database database);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#importDxl(lotus.domino.Stream, lotus.domino.Database)
	 */
	@Override
	public void importDxl(lotus.domino.Stream stream, lotus.domino.Database database);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#importDxl(java.lang.String, lotus.domino.Database)
	 */
	@Override
	public void importDxl(String dxl, lotus.domino.Database database);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#setAclImportOption(int)
	 */
	@Override
	public void setAclImportOption(int option);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#setCompileLotusScript(boolean)
	 */
	@Override
	public void setCompileLotusScript(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#setCreateFTIndex(boolean)
	 */
	@Override
	public void setCreateFTIndex(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#setDesignImportOption(int)
	 */
	@Override
	public void setDesignImportOption(int option);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#setDocumentImportOption(int)
	 */
	@Override
	public void setDocumentImportOption(int option);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#setExitOnFirstFatalError(boolean)
	 */
	@Override
	public void setExitOnFirstFatalError(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#setInputValidationOption(int)
	 */
	@Override
	public void setInputValidationOption(int option);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#setLogComment(java.lang.String)
	 */
	@Override
	public void setLogComment(String comment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#setReplaceDbProperties(boolean)
	 */
	@Override
	public void setReplaceDbProperties(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#setReplicaRequiredForReplaceOrUpdate(boolean)
	 */
	@Override
	public void setReplicaRequiredForReplaceOrUpdate(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlImporter#setUnknownTokenLogOption(int)
	 */
	@Override
	public void setUnknownTokenLogOption(int option);
}
