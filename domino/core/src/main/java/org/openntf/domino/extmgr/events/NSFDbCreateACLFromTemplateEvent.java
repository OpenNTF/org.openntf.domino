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

public class NSFDbCreateACLFromTemplateEvent extends AbstractEMBridgeEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.DestDbpath,
			EMBridgeEventParams.Manager, EMBridgeEventParams.DefaultAccess };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private String manager;
	private int defaultAccessLevel;

	/**
	 * @param eventId
	 */
	public NSFDbCreateACLFromTemplateEvent(final int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public NSFDbCreateACLFromTemplateEvent() {
		super(IEMBridgeEvent.EM_NSFDBCREATEACLFROMTEMPLATE);
	}

	/**
	 * @return the manager
	 */
	public String getManager() {
		return manager;
	}

	/**
	 * @param manager
	 *            the manager to set
	 */
	private void setManager(final String manager) {
		this.manager = manager;
	}

	/**
	 * @return the defaultAccessLevel
	 */
	public int getDefaultAccessLevel() {
		return defaultAccessLevel;
	}

	/**
	 * @param defaultAccessLevel
	 *            the defaultAccessLevel to set
	 */
	private void setDefaultAccessLevel(final int defaultAccessLevel) {
		this.defaultAccessLevel = defaultAccessLevel;
	}
}