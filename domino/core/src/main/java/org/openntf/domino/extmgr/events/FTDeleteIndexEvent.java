package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public class FTDeleteIndexEvent extends DatabaseEvent {
	private static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public FTDeleteIndexEvent() {
		super(EMEventIds.EM_FTDELETEINDEX.getId());
	}

}
