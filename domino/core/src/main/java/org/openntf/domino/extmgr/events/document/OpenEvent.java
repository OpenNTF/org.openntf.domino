package org.openntf.domino.extmgr.events.document;

import org.openntf.domino.extmgr.EMBridgeEventParams;
import org.openntf.domino.extmgr.events.AbstractDocumentEvent;
import org.openntf.domino.extmgr.events.EMEventIds;

public class OpenEvent extends AbstractDocumentEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Noteid, EMBridgeEventParams.Flag,
			EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private int openFlag;

	public OpenEvent() {
		super(EMEventIds.EM_NSFNOTEOPEN.getId());
	}

	public int getOpenFlag() {
		return openFlag;
	}

	public void setOpenFlag(final int openFlag) {
		this.openFlag = openFlag;
	}

}
