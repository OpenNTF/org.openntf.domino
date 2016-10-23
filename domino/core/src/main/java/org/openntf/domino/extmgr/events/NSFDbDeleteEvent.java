package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public class NSFDbDeleteEvent extends DatabaseEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public NSFDbDeleteEvent() {
		super(EMEventIds.EM_NSFDBDELETE.getId());
	}

}
