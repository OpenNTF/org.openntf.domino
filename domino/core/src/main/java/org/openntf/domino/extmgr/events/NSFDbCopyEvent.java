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

public class NSFDbCopyEvent extends AbstractEMBridgeEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.DestDbpath,
			EMBridgeEventParams.SinceTimeDate, EMBridgeEventParams.NoteClass };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private String sinceTimeDate;
	private int noteClassMask;

	/**
	 * @param eventId
	 */
	public NSFDbCopyEvent(final int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public NSFDbCopyEvent() {
		super(IEMBridgeEvent.EM_NSFDBCOPY);
	}

	/**
	 * @return the sinceTimeDate
	 */
	public String getSinceTimeDate() {
		return sinceTimeDate;
	}

	/**
	 * @param sinceTimeDate
	 *            the sinceTimeDate to set
	 */
	private void setSinceTimeDate(final String sinceTimeDate) {
		this.sinceTimeDate = sinceTimeDate;
	}

	/**
	 * @return the noteClassMask
	 */
	public int getNoteClassMask() {
		return noteClassMask;
	}

	/**
	 * @param noteClassMask
	 *            the noteClassMask to set
	 */
	private void setNoteClassMask(final int noteClassMask) {
		this.noteClassMask = noteClassMask;
	}

}
