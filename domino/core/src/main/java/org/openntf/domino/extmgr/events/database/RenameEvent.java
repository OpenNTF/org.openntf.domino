package org.openntf.domino.extmgr.events.database;

import org.openntf.domino.extmgr.EMBridgeEventParams;
import org.openntf.domino.extmgr.events.AbstractDatabaseEvent;
import org.openntf.domino.extmgr.events.EMEventIds;

public class RenameEvent extends AbstractDatabaseEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.FromName, EMBridgeEventParams.ToName, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public RenameEvent() {
		super(EMEventIds.EM_NSFDBRENAME.getId());
	}

	public String getToPath() {
		return (String) getEventValuesMap().get(EMBridgeEventParams.ToName);
	}

	public String getFromPath() {
		return (String) getEventValuesMap().get(EMBridgeEventParams.FromName);
	}
}
