package org.openntf.domino.extmgr.events.document;

import org.openntf.domino.extmgr.EMBridgeEventParams;
import org.openntf.domino.extmgr.events.AbstractDocumentEvent;
import org.openntf.domino.extmgr.events.EMEventIds;

public class UpdateExtendedEvent extends AbstractDocumentEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Noteid, EMBridgeEventParams.Flag,
			EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private int updateFlag;

	public UpdateExtendedEvent(final EMEventIds id) {
		super(id.getId());
	}

	public UpdateExtendedEvent() {
		super(EMEventIds.EM_NSFNOTEUPDATEXTENDED.getId());
	}

	@SuppressWarnings("unused")
	private void setFlag(final String sFlag) {
		this.updateFlag = parseInt(sFlag);
	}

	public int getUpdateFlag() {
		return updateFlag;
	}
}
