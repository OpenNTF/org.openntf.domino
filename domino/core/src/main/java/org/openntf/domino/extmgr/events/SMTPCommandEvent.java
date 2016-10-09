/*
 * � Copyright IBM Corp. 2009,2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public class SMTPCommandEvent extends AbstractEMBridgeEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SessionId, EMBridgeEventParams.Command,
			EMBridgeEventParams.MaxCommandLen, EMBridgeEventParams.SMTPReply, EMBridgeEventParams.SMTPReplyLength };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private long sessionID;
	private String command;
	private long maxCommandLength;
	private String SMTPReply;
	private long SMTPReplyLength;

	/**
	 * @param eventId
	 */
	public SMTPCommandEvent(final int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public SMTPCommandEvent() {
		super(IEMBridgeEvent.EM_SMTPCOMMAND);
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
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param command
	 *            the command to set
	 */
	private void setCommand(final String command) {
		this.command = command;
	}

	/**
	 * @return the maxCommandLength
	 */
	public long getMaxCommandLength() {
		return maxCommandLength;
	}

	/**
	 * @param maxCommandLength
	 *            the maxCommandLength to set
	 */
	private void setMaxCommandLength(final long maxCommandLength) {
		this.maxCommandLength = maxCommandLength;
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
