package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public class NSFNoteCipherDecryptEvent extends DocumentEvent {
	private static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Noteid, EMBridgeEventParams.Flag,
			EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public NSFNoteCipherDecryptEvent() {
		super(EMEventIds.EM_NSFNOTECIPHERDECRYPT.getId());
	}

	public long getDecryptFlags() {
		return (Long) getEventValuesMap().get(EMBridgeEventParams.Flag);
	}

}
