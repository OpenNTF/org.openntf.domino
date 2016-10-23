package org.openntf.domino.extmgr;

import java.util.Collection;

import org.openntf.domino.extmgr.events.EMEventIds;
//import org.openntf.domino.extmgr.events.IEMBridgeEvent;

public interface IEMBridgeSubscriber {
	//	public IEMBridgeEvent parseMessage(String eventMessage);

	public Collection<EMEventIds> getSubscribedEventIds();

	public void handleMessage(EMEventIds eventid, String eventMessage);

}
