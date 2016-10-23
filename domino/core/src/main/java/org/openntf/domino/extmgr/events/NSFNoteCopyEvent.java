package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public class NSFNoteCopyEvent extends AbstractEMBridgeEvent {
	private static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Noteid,
			EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public NSFNoteCopyEvent() {
		super(EMEventIds.EM_NSFNOTECOPY.getId());
	}

}
