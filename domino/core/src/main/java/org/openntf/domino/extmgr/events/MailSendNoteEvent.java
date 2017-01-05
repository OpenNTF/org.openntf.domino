package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public class MailSendNoteEvent extends AbstractEMBridgeEvent {
	private static EMBridgeEventParams[] params = { EMBridgeEventParams.From, EMBridgeEventParams.Flag, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private String from;
	private int flags;

	public MailSendNoteEvent() {
		super(EMEventIds.EM_MAILSENDNOTE.getId());
	}

	public String getFrom() {
		return (String) getEventValuesMap().get(EMBridgeEventParams.From);
	}

	public int getFlags() {
		return (Integer) getEventValuesMap().get(EMBridgeEventParams.Flag);
	}

}
