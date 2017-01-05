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

public class UnknownEMEvent implements IEMBridgeEvent {
	public static EMBridgeEventParams[] params = {};

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	/**
	 * 
	 */
	public UnknownEMEvent() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.event.IExtensionManagerEvent#getEventId()
	 */
	@Override
	public int getEventId() {
		return 0;
	}

	@Override
	public void setEventValue(final EMBridgeEventParams param, final Object value) {
		// TODO Auto-generated method stub

	}

}
