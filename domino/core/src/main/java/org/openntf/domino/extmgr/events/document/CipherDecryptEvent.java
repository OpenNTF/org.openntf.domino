package org.openntf.domino.extmgr.events.document;

import org.openntf.domino.extmgr.EMBridgeEventParams;
import org.openntf.domino.extmgr.events.AbstractDocumentEvent;
import org.openntf.domino.extmgr.events.EMEventIds;

public class CipherDecryptEvent extends AbstractDocumentEvent {
	private static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Noteid, EMBridgeEventParams.Flag,
			EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public CipherDecryptEvent() {
		super(EMEventIds.EM_NSFNOTECIPHERDECRYPT.getId());
	}

	public long getDecryptFlags() {
		return (Long) getEventValuesMap().get(EMBridgeEventParams.Flag);
	}

}
