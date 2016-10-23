package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public class NSFDbNoteLockEvent extends DocumentEvent {
	private static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Noteid, EMBridgeEventParams.Flag,
			EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public NSFDbNoteLockEvent() {
		super(EMEventIds.EM_NSFDBNOTELOCK.getId());
	}

	public long getFlags() {
		return (Long) getEventValuesMap().get(EMBridgeEventParams.Flag);
	}

}
