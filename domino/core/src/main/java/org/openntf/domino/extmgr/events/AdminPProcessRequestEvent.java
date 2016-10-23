package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public class AdminPProcessRequestEvent extends DatabaseEvent {
	private static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.RequestNoteid,
			EMBridgeEventParams.ResponseNoteid, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private String noteIdResponse;
	private String noteIdRequest;

	public AdminPProcessRequestEvent(final int eventId) {
		super(eventId);
	}

	public AdminPProcessRequestEvent() {
		super(EMEventIds.EM_ADMINPPROCESSREQUEST.getId());
	}

	public String getNoteIdResponse() {
		return noteIdResponse;
	}

	public String getNoteIdRequest() {
		return noteIdRequest;
	}

}
