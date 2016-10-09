package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public class FTDeleteIndexEvent extends AbstractEMBridgeEvent {
	private static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public FTDeleteIndexEvent() {
		super(IEMBridgeEvent.EM_FTDELETEINDEX);
	}

}
