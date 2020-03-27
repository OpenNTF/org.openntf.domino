/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public class SMTPMessageAcceptEvent extends AbstractEMBridgeEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SessionId, EMBridgeEventParams.SourceDbpath,
			EMBridgeEventParams.Noteid, EMBridgeEventParams.SMTPReply, EMBridgeEventParams.SMTPReplyLength, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private long sessionID;
	private String SMTPReply;
	private long SMTPReplyLength;

	public SMTPMessageAcceptEvent() {
		super(EMEventIds.EM_SMTPMESSAGEACCEPT.getId());
	}

	/**
	 * @return the sessionID
	 */
	public long getSessionID() {
		return sessionID;
	}

	/**
	 * @param sessionID
	 *            the sessionID to set
	 */
	private void setSessionID(final long sessionID) {
		this.sessionID = sessionID;
	}

	/**
	 * @return the sMTPReply
	 */
	public String getSMTPReply() {
		return SMTPReply;
	}

	/**
	 * @param sMTPReply
	 *            the sMTPReply to set
	 */
	private void setSMTPReply(final String sMTPReply) {
		SMTPReply = sMTPReply;
	}

	/**
	 * @return the sMTPReplyLength
	 */
	public long getSMTPReplyLength() {
		return SMTPReplyLength;
	}

	/**
	 * @param sMTPReplyLength
	 *            the sMTPReplyLength to set
	 */
	private void setSMTPReplyLength(final long sMTPReplyLength) {
		SMTPReplyLength = sMTPReplyLength;
	}

}
