/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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

public class SMTPConnectEvent extends AbstractEMBridgeEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SessionId, EMBridgeEventParams.RemoteIP,
			EMBridgeEventParams.RemoteHost, EMBridgeEventParams.PossibileRelay, EMBridgeEventParams.SMTPGreeting,
			EMBridgeEventParams.SMTPMaxGreetingLen, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private long sessionID;
	private String remoteIP;
	private String remoteHost;
	private boolean possibleRelay;
	private String greeting;
	private long maxGreetingLen;

	public SMTPConnectEvent() {
		super(EMEventIds.EM_SMTPCONNECT.getId());
	}

	/**
	 * @return the sessionID
	 */
	public long getSessionID() {
		return sessionID;
	}

	/**
	 * @return the remoteIP
	 */
	public String getRemoteIP() {
		return remoteIP;
	}

	/**
	 * @return the remoteHost
	 */
	public String getRemoteHost() {
		return remoteHost;
	}

	/**
	 * @return the possibleRelay
	 */
	public boolean isPossibleRelay() {
		return possibleRelay;
	}

	/**
	 * @return the greeting
	 */
	public String getGreeting() {
		return greeting;
	}

	/**
	 * @return the maxGreetingLen
	 */
	public long getMaxGreetingLen() {
		return maxGreetingLen;
	}

}
