package org.openntf.dominoTests;

/*
 	Copyright 2014 OpenNTF Domino API Team Licensed under the Apache License, Version 2.0
	(the "License"); you may not use this file except in compliance with the
	License. You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
	or agreed to in writing, software distributed under the License is distributed
	on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
	express or implied. See the License for the specific language governing
	permissions and limitations under the License
	
*/

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openntf.domino.events.EnumEvent;
import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.events.IDominoListener;
import org.openntf.domino.ext.Database.Events;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class TestDocumentListener implements IDominoListener {

	// Required method, triggered when Listener fires
	public boolean eventHappened(IDominoEvent event) {
		try {
			if (event.getEvent().equals(Events.AFTER_CREATE_DOCUMENT)) {
				return incrementCreateCount();
			}
			if (event.getEvent().equals(Events.AFTER_UPDATE_DOCUMENT)) {
				return incrementUpdateCount();
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	// Required method, lists events to attach listeners for
	public List<EnumEvent> getEventTypes() {
		ArrayList<EnumEvent> eventList = new ArrayList<EnumEvent>();
		eventList.add(Events.AFTER_CREATE_DOCUMENT);
		eventList.add(Events.AFTER_UPDATE_DOCUMENT);
		return eventList;
	}

	private boolean incrementCreateCount() {
		try {
			Integer docsCreated;
			Map<String, Object> appScope = ExtLibUtil.getApplicationScope();
			if (appScope.containsKey("docsCreatedCount")) {
				docsCreated = (Integer) appScope.get("docsCreatedCount");
				docsCreated += 1;
			} else {
				docsCreated = 1;
			}
			appScope.put("docsCreatedCount", docsCreated);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean incrementUpdateCount() {
		try {
			Integer docsUpdated;
			Map<String, Object> appScope = ExtLibUtil.getApplicationScope();
			if (appScope.containsKey("docsUpdatedCount")) {
				docsUpdated = (Integer) appScope.get("docsUpdatedCount");
				docsUpdated += 1;
			} else {
				docsUpdated = 1;
			}
			appScope.put("docsUpdatedCount", docsUpdated);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
