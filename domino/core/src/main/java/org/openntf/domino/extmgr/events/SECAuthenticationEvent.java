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

public class SECAuthenticationEvent extends AbstractEMBridgeEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.WEvent, EMBridgeEventParams.RemoteName, EMBridgeEventParams.Flag,
			EMBridgeEventParams.NetProtocol, EMBridgeEventParams.NetAddress, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private int wEvent;
	private String remoteName;
	private long flags;
	private int netProtocol;
	private String netAddress;

	public SECAuthenticationEvent() {
		super(EMEventIds.EM_SECAUTHENTICATION.getId());
	}

	/**
	 * @return the wEvent
	 */
	public int getwEvent() {
		return wEvent;
	}

	/**
	 * @return the remoteName
	 */
	public String getRemoteName() {
		return remoteName;
	}

	/**
	 * @return the flags
	 */
	public long getFlags() {
		return flags;
	}

	/**
	 * @return the netProtocol
	 */
	public int getNetProtocol() {
		return netProtocol;
	}

	/**
	 * @return the netAddress
	 */
	public String getNetAddress() {
		return netAddress;
	}

}
