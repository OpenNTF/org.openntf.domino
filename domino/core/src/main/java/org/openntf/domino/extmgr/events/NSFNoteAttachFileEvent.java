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

public class NSFNoteAttachFileEvent extends AbstractEMBridgeEvent {
	private static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Noteid,
			EMBridgeEventParams.Filename, EMBridgeEventParams.OriginalFilename, EMBridgeEventParams.EncodingType };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private String fileName;
	private String orgFileName;
	private int encodingType;

	/**
	 * @param eventId
	 */
	public NSFNoteAttachFileEvent(final int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public NSFNoteAttachFileEvent() {
		super(IEMBridgeEvent.EM_NSFNOTEATTACHFILE);
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	private void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the orgFileName
	 */
	public String getOrgFileName() {
		return orgFileName;
	}

	/**
	 * @param orgFileName
	 *            the orgFileName to set
	 */
	private void setOrgFileName(final String orgFileName) {
		this.orgFileName = orgFileName;
	}

	/**
	 * @return the encodingType
	 */
	public int getEncodingType() {
		return encodingType;
	}

	/**
	 * @param encodingType
	 *            the encodingType to set
	 */
	private void setEncodingType(final int encodingType) {
		this.encodingType = encodingType;
	}

}
