/*
 * � Copyright IBM Corp. 2009,2010
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
package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

/**
 * @author dtaieb
 * 
 */
public class NSFAddToFolderEvent extends AbstractEMBridgeEvent {
	private static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.DataDbpath,
			EMBridgeEventParams.FolderNoteid, EMBridgeEventParams.Noteid, EMBridgeEventParams.AddOperation };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private String dataDbPath;
	private String folderNoteID;
	private String noteID;
	private boolean isAddOperation;

	/**
	 * @param eventId
	 */
	public NSFAddToFolderEvent(final int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public NSFAddToFolderEvent() {
		super(IEMBridgeEvent.EM_NSFADDTOFOLDER);
	}

	/**
	 * @return the dataDbPath
	 */
	public String getDataDbPath() {
		return dataDbPath;
	}

	/**
	 * @param dataDbPath
	 *            the dataDbPath to set
	 */
	private void setDataDbPath(final String dataDbPath) {
		this.dataDbPath = dataDbPath;
	}

	/**
	 * @return the folderNoteID
	 */
	public String getFolderNoteID() {
		return folderNoteID;
	}

	/**
	 * @param folderNoteID
	 *            the folderNoteID to set
	 */
	private void setFolderNoteID(final String folderNoteID) {
		this.folderNoteID = folderNoteID;
	}

	/**
	 * @return the noteID
	 */
	public String getNoteID() {
		return noteID;
	}

	/**
	 * @param noteID
	 *            the noteID to set
	 */
	private void setNoteID(final String noteID) {
		this.noteID = noteID;
	}

	/**
	 * @return the isAddOperation
	 */
	public boolean isAddOperation() {
		return isAddOperation;
	}

	/**
	 * @param isAddOperation
	 *            the isAddOperation to set
	 */
	private void setAddOperation(final boolean isAddOperation) {
		this.isAddOperation = isAddOperation;
	}

}
