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

public class FTSearchEvent extends AbstractEMBridgeEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Query, EMBridgeEventParams.Options,
			EMBridgeEventParams.Limit, EMBridgeEventParams.DocsReturned };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	private String query;
	private long options;
	private int limit;
	private long retNumDocs;

	/**
	 * @param eventId
	 */
	public FTSearchEvent(final int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public FTSearchEvent() {
		super(IEMBridgeEvent.EM_FTSEARCH);
	}

	/**
	 * @param query
	 */
	private void setQuery(final String query) {
		this.query = query;
	}

	/**
	 * @return
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @return the options
	 */
	public long getOptions() {
		return options;
	}

	/**
	 * @param options
	 *            the options to set
	 */
	private void setOptions(final long options) {
		this.options = options;
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
	 * @return the retNumDocs
	 */
	public long getRetNumDocs() {
		return retNumDocs;
	}

	/**
	 * @param retNumDocs
	 *            the retNumDocs to set
	 */
	private void setRetNumDocs(final long retNumDocs) {
		this.retNumDocs = retNumDocs;
	}

}
