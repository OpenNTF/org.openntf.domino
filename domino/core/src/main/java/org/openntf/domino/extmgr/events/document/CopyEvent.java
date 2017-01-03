package org.openntf.domino.extmgr.events.document;

import org.openntf.domino.extmgr.EMBridgeEventParams;
import org.openntf.domino.extmgr.events.AbstractEMBridgeEvent;
import org.openntf.domino.extmgr.events.EMEventIds;

public class CopyEvent extends AbstractEMBridgeEvent {
	private static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Noteid,
			EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public CopyEvent() {
		super(EMEventIds.EM_NSFNOTECOPY.getId());
	}

}
