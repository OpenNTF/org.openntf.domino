package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public class NSFDbCopyACLEvent extends DatabaseEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.DestDbpath,
			EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public NSFDbCopyACLEvent() {
		super(EMEventIds.EM_NSFDBCOPYACL.getId());
	}

	public String getDestDbpath() {
		return (String) getEventValuesMap().get(EMBridgeEventParams.DestDbpath);
	}

}
