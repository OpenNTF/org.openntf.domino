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

public class NSFDbCreateAndCopyEvent extends AbstractEMBridgeEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.DestDbpath,
			EMBridgeEventParams.NoteClass, EMBridgeEventParams.Limit, EMBridgeEventParams.Flag };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private int noteClass;
	private int limit;
	private long flags;

	/**
	 * @param eventId
	 */
	public NSFDbCreateAndCopyEvent(final int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public NSFDbCreateAndCopyEvent() {
		super(IEMBridgeEvent.EM_NSFDBCREATEANDCOPY);
	}

	/**
	 * @return the noteClass
	 */
	public int getNoteClass() {
		return noteClass;
	}

	/**
	 * @param noteClass
	 *            the noteClass to set
	 */
	private void setNoteClass(final int noteClass) {
		this.noteClass = noteClass;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	private void setLimit(final int limit) {
		this.limit = limit;
	}

	/**
	 * @return the flags
	 */
	public long getFlags() {
		return flags;
	}

	/**
	 * @param flags
	 *            the flags to set
	 */
	private void setFlags(final long flags) {
		this.flags = flags;
	}

}
