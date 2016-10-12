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

public class NSFNoteUpdateExtendedEvent extends AbstractEMBridgeEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Noteid, EMBridgeEventParams.Flag,
			EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private int updateFlag;

	public NSFNoteUpdateExtendedEvent(final EMEventIds id) {
		super(id.getId());
	}

	public NSFNoteUpdateExtendedEvent() {
		super(EMEventIds.EM_NSFNOTEUPDATEXTENDED.getId());
	}

	/**
	 * @param sFlag
	 */
	private void setFlag(final String sFlag) {
		this.updateFlag = parseInt(sFlag);
	}

	/**
	 * @return
	 */
	public int getUpdateFlag() {
		return updateFlag;
	}
}
