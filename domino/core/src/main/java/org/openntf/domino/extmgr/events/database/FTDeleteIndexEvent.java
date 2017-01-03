package org.openntf.domino.extmgr.events.database;

import org.openntf.domino.extmgr.EMBridgeEventParams;
import org.openntf.domino.extmgr.events.AbstractDatabaseEvent;
import org.openntf.domino.extmgr.events.EMEventIds;

public class FTDeleteIndexEvent extends AbstractDatabaseEvent {
	private static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public FTDeleteIndexEvent() {
		super(EMEventIds.EM_FTDELETEINDEX.getId());
	}

}
