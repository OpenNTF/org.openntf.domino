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
public class NIFOpenCollectionEvent extends AbstractEMBridgeEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.DataDbpath,
			EMBridgeEventParams.Noteid, EMBridgeEventParams.Flag };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private String dataDbPath;
	private String viewNoteID;
	private int openFlags;

	/**
	 * @param eventId
	 */
	public NIFOpenCollectionEvent(final int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public NIFOpenCollectionEvent() {
		super(IEMBridgeEvent.EM_NIFOPENCOLLECTION);
	}

	/**
	 * @return the dataDbPath
	 */
	public String getDataDbPath() {
		return dataDbPath;
	}

	/**
	 * @param dataDbPath
	 *            the dataDbPath to set
	 */
	private void setDataDbPath(final String dataDbPath) {
		this.dataDbPath = dataDbPath;
	}

	/**
	 * @return the viewNoteID
	 */
	public String getViewNoteID() {
		return viewNoteID;
	}

	/**
	 * @param viewNoteID
	 *            the viewNoteID to set
	 */
	private void setViewNoteID(final String viewNoteID) {
		this.viewNoteID = viewNoteID;
	}

	/**
	 * @return the openFlags
	 */
	public int getOpenFlags() {
		return openFlags;
	}

	/**
	 * @param openFlags
	 *            the openFlags to set
	 */
	private void setOpenFlags(final int openFlags) {
		this.openFlags = openFlags;
	}

}
