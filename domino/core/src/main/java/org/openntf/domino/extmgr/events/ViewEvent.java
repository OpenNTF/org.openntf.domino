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
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.extmgr.EMBridgeEventParams;

public abstract class ViewEvent extends AbstractDatabaseEvent {

	public ViewEvent(final int eventId) {
		super(eventId);
	}

	public String getNoteId() {
		return (String) getEventValuesMap().get(EMBridgeEventParams.Noteid);
	}

	public Document getViewDocument(final Session session, final String serverName) {
		Database db = getDatabase(session, serverName);
		return db.getDocumentByID(getNoteId());
	}

	public String getViewUnid(final Session session, final String serverName) {
		Database db = getDatabase(session, serverName);
		return db.getUNID(getNoteId());
	}

	public View getView(final Session session, final String serverName) {
		Document document = getViewDocument(session, serverName);
		return document.getAncestorDatabase().getView(document);
	}

}
