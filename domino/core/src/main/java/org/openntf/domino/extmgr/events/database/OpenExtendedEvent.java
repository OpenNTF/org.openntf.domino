package org.openntf.domino.extmgr.events.database;

import org.openntf.domino.extmgr.EMBridgeEventParams;
import org.openntf.domino.extmgr.events.AbstractDatabaseEvent;
import org.openntf.domino.extmgr.events.EMEventIds;

public class OpenExtendedEvent extends AbstractDatabaseEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Options,
			EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public OpenExtendedEvent() {
		super(EMEventIds.EM_NSFDBOPENEXTENDED.getId());
	}

	public int getOptions() {
		return (Integer) getEventValuesMap().get(EMBridgeEventParams.Options);
	}

}
