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

public class FTIndexEvent extends AbstractEMBridgeEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Options,
			EMBridgeEventParams.Stopfile, EMBridgeEventParams.DocsAdded, EMBridgeEventParams.DocsUpdated, EMBridgeEventParams.DocsDelete,
			EMBridgeEventParams.BytesIndexed };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private int options;
	private String stopFile;
	private long docsAdded;
	private long docsUpdated;
	private long docsDeleted;
	private long bytesIndexed;

	/**
	 * @param eventId
	 */
	public FTIndexEvent(final int eventId) {
		super(eventId);
	}

	/**
	* 
	*/
	public FTIndexEvent() {
		super(IEMBridgeEvent.EM_FTINDEX);
	}

	/**
	 * @param options
	 */
	private void setOptions(final int options) {
		this.options = options;
	}

	/**
	 * @return
	 */
	public int getOptions() {
		return options;
	}

	/**
	 * @param stopFile
	 */
	private void setStopFile(final String stopFile) {
		this.stopFile = stopFile;
	}

	/**
	 * @return
	 */
	public String getStopFile() {
		return stopFile;
	}

	/**
	 * @param docsAdded
	 */
	public void setDocsAdded(final long docsAdded) {
		this.docsAdded = docsAdded;
	}

	/**
	 * @return
	 */
	public long getDocsAdded() {
		return docsAdded;
	}

	/**
	 * @param docsDeleted
	 */
	public void setDocsDeleted(final long docsDeleted) {
		this.docsDeleted = docsDeleted;
	}

	/**
	 * @return
	 */
	public long getDocsDeleted() {
		return docsDeleted;
	}

	/**
	 * @param docsUpdated
	 */
	public void setDocsUpdated(final long docsUpdated) {
		this.docsUpdated = docsUpdated;
	}

	/**
	 * @return
	 */
	public long getDocsUpdated() {
		return docsUpdated;
	}

	/**
	 * @param bytesIndexed
	 */
	public void setBytesIndexed(final long bytesIndexed) {
		this.bytesIndexed = bytesIndexed;
	}

	/**
	 * @return
	 */
	public long getBytesIndexed() {
		return bytesIndexed;
	}

}
