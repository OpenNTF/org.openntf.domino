/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
package org.openntf.domino.extmgr.events;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.extmgr.EMBridgeEventParams;

public abstract class AbstractDatabaseEvent extends AbstractEMBridgeEvent implements IEMBridgeEvent {

	public AbstractDatabaseEvent(final int eventId) {
		super(eventId);
	}

	public String getDbPath() {
		return (String) getEventValuesMap().get(EMBridgeEventParams.SourceDbpath);
	}

	public Database getDatabase(final Session session, String serverName) {
		if (serverName == null) {
			serverName = "";
		}
		return session.getDatabase(serverName, getDbPath());
	}

}
