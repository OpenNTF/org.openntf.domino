package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public class NSFDbRenameEvent extends AbstractEMBridgeEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.FromName, EMBridgeEventParams.ToName, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public NSFDbRenameEvent() {
		super(EMEventIds.EM_NSFDBRENAME.getId());
	}

	public String getToPath() {
		return (String) getEventValuesMap().get(EMBridgeEventParams.ToName);
	}

	public String getFromPath() {
		return (String) getEventValuesMap().get(EMBridgeEventParams.FromName);
	}
}
