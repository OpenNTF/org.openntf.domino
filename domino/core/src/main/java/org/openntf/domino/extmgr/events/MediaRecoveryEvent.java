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

/**
 * @author dtaieb
 * 
 */
public class MediaRecoveryEvent extends AbstractEMBridgeEvent {
	private static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Noteid,
			EMBridgeEventParams.Insert, EMBridgeEventParams.Update, EMBridgeEventParams.Delete };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private boolean isInsert;
	private boolean isUpdate;
	private boolean isDelete;

	/**
	 * @param eventId
	 */
	public MediaRecoveryEvent(final int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public MediaRecoveryEvent() {
		super(IEMBridgeEvent.EM_MEDIARECOVERY_NOTE);
	}

	/**
	 * @return the isInsert
	 */
	public boolean isInsert() {
		return isInsert;
	}

	/**
	 * @param isInsert
	 *            the isInsert to set
	 */
	private void setInsert(final boolean isInsert) {
		this.isInsert = isInsert;
	}

	/**
	 * @return the isUpdate
	 */
	public boolean isUpdate() {
		return isUpdate;
	}

	/**
	 * @param isUpdate
	 *            the isUpdate to set
	 */
	private void setUpdate(final boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	/**
	 * @return the isDelete
	 */
	public boolean isDelete() {
		return isDelete;
	}

	/**
	 * @param isDelete
	 *            the isDelete to set
	 */
	private void setDelete(final boolean isDelete) {
		this.isDelete = isDelete;
	}

}
