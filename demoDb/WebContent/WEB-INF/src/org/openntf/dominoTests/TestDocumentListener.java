package org.openntf.dominoTests;

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
