package org.openntf.domino.xots;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public enum XotsScheduler {
	INSTANCE;
	private final Map<String, Set<TaskletDefinition>> scheduledTasklets = new ConcurrentHashMap<String, Set<TaskletDefinition>>();

	public void unregisterTasklets(final String apiPath) {
		scheduledTasklets.remove(apiPath);
	}

	public void registerTasklets(final String apiPath, final Set<TaskletDefinition> taskletDefinitions) {
		scheduledTasklets.put(apiPath, taskletDefinitions);

	}

}
