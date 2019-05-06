package org.openntf.domino.extmgr.events.document;

import org.openntf.domino.extmgr.EMBridgeEventParams;
import org.openntf.domino.extmgr.events.AbstractDocumentEvent;
import org.openntf.domino.extmgr.events.EMEventIds;

public class OpenExtendedEvent extends AbstractDocumentEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Noteid, EMBridgeEventParams.Flag,
			EMBridgeEventParams.SinceSeqNum, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private long flags;
	private long sinceSeqNum;

	public OpenExtendedEvent() {
		super(EMEventIds.EM_NSFNOTEOPENEXTENDED.getId());
	}

	@SuppressWarnings("unused")
	private void setFlags(final long flags) {
		this.flags = flags;
	}

	public long getFlags() {
		return flags;
	}

	@SuppressWarnings("unused")
	private void setSinceSeqNum(final long sinceSeqNum) {
		this.sinceSeqNum = sinceSeqNum;
	}

	public long getSinceSeqNum() {
		return sinceSeqNum;
	}
}
