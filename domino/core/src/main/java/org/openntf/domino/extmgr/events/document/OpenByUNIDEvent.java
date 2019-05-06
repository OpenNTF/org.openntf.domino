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
package org.openntf.domino.extmgr.events.document;

import org.openntf.domino.extmgr.EMBridgeEventParams;
import org.openntf.domino.extmgr.events.AbstractDocumentEvent;
import org.openntf.domino.extmgr.events.EMEventIds;

public class OpenByUNIDEvent extends AbstractDocumentEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Unid, EMBridgeEventParams.Flag,
			EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private String unid;
	private int openFlag;

	public OpenByUNIDEvent() {
		super(EMEventIds.EM_NSFNOTEOPENBYUNID.getId());
	}

	public String getUnid() {
		return unid;
	}

	public void setUnid(final String unid) {
		this.unid = unid;
	}

	public int getOpenFlag() {
		return openFlag;
	}

	public void setOpenFlag(final int openFlag) {
		this.openFlag = openFlag;
	}
}
