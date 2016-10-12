package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public class NSFDbOpenExtendedEvent extends DatabaseEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Options,
			EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public NSFDbOpenExtendedEvent() {
		super(EMEventIds.EM_NSFDBOPENEXTENDED.getId());
	}

	public int getOptions() {
		return (Integer) getEventValuesMap().get(EMBridgeEventParams.Options);
	}

}
