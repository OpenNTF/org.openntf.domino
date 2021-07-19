/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.extmgr.events.database;

import org.openntf.domino.extmgr.EMBridgeEventParams;
import org.openntf.domino.extmgr.events.AbstractDatabaseEvent;
import org.openntf.domino.extmgr.events.EMEventIds;

/**
 * @author dtaieb
 * 
 */
public class CompactEvent extends AbstractDatabaseEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Options,
			EMBridgeEventParams.SizeBefore, EMBridgeEventParams.SizeAfter, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public CompactEvent() {
		super(EMEventIds.EM_NSFDBCOMPACT.getId());
	}

	public int getCompactOptions() {
		return (Integer) getEventValuesMap().get(EMBridgeEventParams.Options);
	}

	public long getBeforeSize() {
		return (Long) getEventValuesMap().get(EMBridgeEventParams.SizeBefore);
	}

	public long getAfterSize() {
		return (Long) getEventValuesMap().get(EMBridgeEventParams.SizeAfter);
	}

}
