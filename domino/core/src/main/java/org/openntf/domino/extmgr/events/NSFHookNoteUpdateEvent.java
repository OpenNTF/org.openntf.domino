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

public class NSFHookNoteUpdateEvent extends AbstractEMBridgeEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.Username, EMBridgeEventParams.SourceDbpath,
			EMBridgeEventParams.Noteid, EMBridgeEventParams.Flag };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private String userName;
	private int updateFlag;

	/**
	 * @param eventId
	 */
	public NSFHookNoteUpdateEvent(final int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public NSFHookNoteUpdateEvent() {
		super(IEMBridgeEvent.HOOK_EVENT_NOTE_UPDATE);
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	private void setUserName(final String userName) {
		this.userName = userName;
	}

	/**
	 * @return the updateFlag
	 */
	public int getUpdateFlag() {
		return updateFlag;
	}

	/**
	 * @param updateFlag
	 *            the updateFlag to set
	 */
	private void setUpdateFlag(final int updateFlag) {
		this.updateFlag = updateFlag;
	}

}
