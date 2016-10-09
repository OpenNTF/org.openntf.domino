package org.openntf.domino.extmgr;

import org.openntf.domino.extmgr.events.IEMBridgeEvent;

public interface IEMBridgeSubscriber {
	public IEMBridgeEvent parseMessage(String eventMessage);

}
