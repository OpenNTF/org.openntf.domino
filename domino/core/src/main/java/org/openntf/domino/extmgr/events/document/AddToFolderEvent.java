package org.openntf.domino.extmgr.events.document;

import org.openntf.domino.extmgr.EMBridgeEventParams;
import org.openntf.domino.extmgr.events.AbstractDocumentEvent;
import org.openntf.domino.extmgr.events.EMEventIds;

public class AddToFolderEvent extends AbstractDocumentEvent {
	private static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.DataDbpath,
			EMBridgeEventParams.FolderNoteid, EMBridgeEventParams.Noteid, EMBridgeEventParams.AddOperation, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public AddToFolderEvent() {
		super(EMEventIds.EM_NSFADDTOFOLDER.getId());
	}

	public String getDataDbPath() {
		return (String) getEventValuesMap().get(EMBridgeEventParams.DataDbpath);
	}

	public String getFolderNoteID() {
		return (String) getEventValuesMap().get(EMBridgeEventParams.FolderNoteid);
	}

	public boolean isAddOperation() {
		return (Boolean) getEventValuesMap().get(EMBridgeEventParams.AddOperation);
	}

}
