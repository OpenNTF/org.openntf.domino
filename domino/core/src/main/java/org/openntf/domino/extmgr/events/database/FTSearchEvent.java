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
package org.openntf.domino.extmgr.events.database;

import org.openntf.domino.extmgr.EMBridgeEventParams;
import org.openntf.domino.extmgr.events.AbstractDatabaseEvent;
import org.openntf.domino.extmgr.events.EMEventIds;

public class FTSearchEvent extends AbstractDatabaseEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Query, EMBridgeEventParams.Options,
			EMBridgeEventParams.Limit, EMBridgeEventParams.DocsReturned, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public FTSearchEvent() {
		super(EMEventIds.EM_FTSEARCH.getId());
	}

	public String getQuery() {
		return (String) getEventValuesMap().get(EMBridgeEventParams.Query);
	}

	public long getOptions() {
		return (Long) getEventValuesMap().get(EMBridgeEventParams.Options);
	}

	public int getLimit() {
		return (Integer) getEventValuesMap().get(EMBridgeEventParams.Limit);
	}

	public long getRetNumDocs() {
		return (Long) getEventValuesMap().get(EMBridgeEventParams.DocsReturned);
	}

}
