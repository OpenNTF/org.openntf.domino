package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public class AdminPProcessRequestEvent extends AbstractEMBridgeEvent {
	private static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.RequestNoteid,
			EMBridgeEventParams.ResponseNoteid };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private String noteIdResponse;
	private String noteIdRequest;

	/**
	 * @param eventId
	 */
	public AdminPProcessRequestEvent(final int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public AdminPProcessRequestEvent() {
		super(IEMBridgeEvent.EM_ADMINPPROCESSREQUEST);
	}

	/**
	 * @param noteId
	 */
	private void setNoteIdResponse(final String noteId) {
		this.noteIdResponse = noteId;
	}

	/**
	 * @return
	 */
	public String getNoteIdResponse() {
		return noteIdResponse;
	}

	/**
	 * @return
	 */
	public String getNoteIdRequest() {
		return noteIdRequest;
	}

	/**
	 * @param noteId
	 */
	private void setNoteIdRequest(final String noteId) {
		this.noteIdRequest = noteId;
	}

}
