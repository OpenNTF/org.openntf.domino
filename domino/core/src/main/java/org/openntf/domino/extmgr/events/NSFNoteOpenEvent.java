package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public class NSFNoteOpenEvent extends AbstractEMBridgeEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Noteid, EMBridgeEventParams.Flag,
			EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private int openFlag;

	public NSFNoteOpenEvent() {
		super(EMEventIds.EM_NSFNOTEOPEN.getId());
	}

	/**
	 * @return
	 */
	public int getOpenFlag() {
		return openFlag;
	}

	/**
	 * @param openFlag
	 */
	public void setOpenFlag(final int openFlag) {
		this.openFlag = openFlag;
	}

}
