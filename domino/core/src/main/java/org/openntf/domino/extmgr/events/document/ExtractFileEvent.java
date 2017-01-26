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
package org.openntf.domino.extmgr.events.document;

import org.openntf.domino.extmgr.EMBridgeEventParams;
import org.openntf.domino.extmgr.events.AbstractDocumentEvent;
import org.openntf.domino.extmgr.events.EMEventIds;

public class ExtractFileEvent extends AbstractDocumentEvent {
	private static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Noteid,
			EMBridgeEventParams.Itemname, EMBridgeEventParams.Filename, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private String itemName;
	private String fileName;

	public ExtractFileEvent() {
		super(EMEventIds.EM_NSFNOTEEXTRACTFILE.getId());
	}

	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * @param itemName
	 *            the itemName to set
	 */
	private void setItemName(final String itemName) {
		this.itemName = itemName;
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

}
