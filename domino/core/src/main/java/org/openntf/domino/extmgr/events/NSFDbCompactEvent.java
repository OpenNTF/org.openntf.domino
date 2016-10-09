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
public class NSFDbCompactEvent extends AbstractEMBridgeEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Options,
			EMBridgeEventParams.SizeBefore, EMBridgeEventParams.SizeAfter };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private int compactOptions;
	private long beforeSize;
	private long afterSize;

	/**
	 * @param eventId
	 */
	public NSFDbCompactEvent(final int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public NSFDbCompactEvent() {
		super(IEMBridgeEvent.EM_NSFDBCOMPACT);
	}

	/**
	 * @return the compactOptions
	 */
	public int getCompactOptions() {
		return compactOptions;
	}

	/**
	 * @param compactOptions
	 *            the compactOptions to set
	 */
	private void setCompactOptions(final int compactOptions) {
		this.compactOptions = compactOptions;
	}

	/**
	 * @return the beforeSize
	 */
	public long getBeforeSize() {
		return beforeSize;
	}

	/**
	 * @param beforeSize
	 *            the beforeSize to set
	 */
	private void setBeforeSize(final long beforeSize) {
		this.beforeSize = beforeSize;
	}

	/**
	 * @return the afterSize
	 */
	public long getAfterSize() {
		return afterSize;
	}

	/**
	 * @param afterSize
	 *            the afterSize to set
	 */
	private void setAfterSize(final long afterSize) {
		this.afterSize = afterSize;
	}

}
