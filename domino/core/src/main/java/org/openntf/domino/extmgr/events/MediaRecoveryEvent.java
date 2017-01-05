package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public class MediaRecoveryEvent extends AbstractDocumentEvent {
	private static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Noteid,
			EMBridgeEventParams.Insert, EMBridgeEventParams.Update, EMBridgeEventParams.Delete, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public MediaRecoveryEvent() {
		super(EMEventIds.EM_MEDIARECOVERY_NOTE.getId());
	}

	public boolean isInsert() {
		return (Boolean) getEventValuesMap().get(EMBridgeEventParams.Insert);
	}

	public boolean isUpdate() {
		return (Boolean) getEventValuesMap().get(EMBridgeEventParams.Update);
	}

	public boolean isDelete() {
		return (Boolean) getEventValuesMap().get(EMBridgeEventParams.Delete);
	}

}
