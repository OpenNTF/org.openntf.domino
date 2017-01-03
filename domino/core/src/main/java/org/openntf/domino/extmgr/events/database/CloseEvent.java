package org.openntf.domino.extmgr.events.database;

import org.openntf.domino.extmgr.EMBridgeEventParams;
import org.openntf.domino.extmgr.events.AbstractDocumentEvent;
import org.openntf.domino.extmgr.events.EMEventIds;

public class CloseEvent extends AbstractDocumentEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public CloseEvent() {
		super(EMEventIds.EM_NSFDBCLOSE.getId());
	}

}
