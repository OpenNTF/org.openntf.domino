package org.openntf.domino.config;

import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xots.Tasklet;

public class XotsConfiguration extends ConfigurationObject {

	private String apiPath_;
	private String taskletName_;
	private boolean isDatabase_;
	private String bundle_;

	public XotsConfiguration(final String path, final String taskletName, final boolean isDatabase) {
		if (isDatabase) {
			bundle_ = "";
			if (path.indexOf("!!") < 0) {
				apiPath_ = Factory.getLocalServerName() + "!!" + path;
			} else {
				apiPath_ = path;
			}
		} else {
			bundle_ = path;
			apiPath_ = Factory.getLocalServerName() + "!!bundle:" + path;
		}
		taskletName_ = taskletName;
		isDatabase_ = isDatabase;
	}

	@Override
	protected Object[] schema() {
		// @formatter:off
		return new Object[] {
				"Schedules",        String[].class,
				"SchedulesDefault", String[].class,
				"OnAllServers", 	Boolean.class,
				"RunOnServer",      String[].class,
				"TaskletName", 		String.class,
				"ApiPaths", 		String[].class,
				"Enabled", 			Boolean.class,
				"Location", 		String.class,
				"ReplicaId", 		String.class,
				"Bundle", 			String.class,
				"ApplicationName",	String.class
		};
		// @formatter:on
	}

	public String[] getSchedules() {
		return get("Schedules");
	}

	public String[] getSchedulesDefault() {
		return get("SchedulesDefault");
	}

	public void setSchedulesDefault(final String[] schedules) {
		if (get("Schedules") == null) {
			put("Schedules", schedules);
		}
		put("SchedulesDefault", schedules);
	}

	public boolean isOnAllServers() {
		return get("OnAllServers");
	}

	public void setOnAllServers(final boolean onAllServers) {
		if (onAllServers) {
			put("RunOnServer", "*"); // not changeable
		} else {
			if (get("RunOnServer") == null) {
				put("RunOnServer", "$CLUSTER1");
			}
		}
		put("OnAllServers", onAllServers);
	}

	public String getTaskletName() {
		return get("TaskletName");
	}

	public String[] getApiPaths() {
		return get("ApiPaths");
	}

	public boolean isEnabled() {
		return get("Enabled");
	}

	public String getLocation() {
		return get("Location");
	}

	public String getReplicaId() {
		return get("ReplicaId");
	}

	public String getBundle() {
		return get("Bundle");
	}

	public String getApplicationName() {
		return get("ApplicationName");
	}

	/**
	 * Returns the document for this server
	 * 
	 * @return
	 */
	@Override
	protected Document getDocument(final boolean create) {
		Database odaDb_ = Configuration.getOdaDb();
		if (odaDb_ == null)
			return null;

		Database db = null;
		String unid;
		boolean dirty = false;
		if (isDatabase_) {
			db = odaDb_.getAncestorSession().getDatabase(apiPath_);
			unid = Configuration.computeUNID(taskletName_, db);
		} else {
			unid = Configuration.computeUNID(bundle_ + ":" + taskletName_, odaDb_); // use a valid ReplicaID
		}

		Document currentConfig = odaDb_.getDocumentByUNID(unid);
		if (currentConfig == null) {
			if (!create)
				return null;
			currentConfig = odaDb_.createDocument();
			currentConfig.setUniversalID(unid);
			currentConfig.replaceItemValue("Form", "XotsTasklet");
			currentConfig.replaceItemValue("TaskletName", taskletName_);
			currentConfig.replaceItemValue("ApiPaths", apiPath_); // APIPath is just for UI - internally we always use replica ID
			currentConfig.replaceItemValue("Enabled", true);
			if (isDatabase_) {
				if (db != null) {
					// Java's wrong about this potentially being null
					currentConfig.replaceItemValue("ReplicaId", db.getReplicaID());
				}
				currentConfig.replaceItemValue("Location", "NSF");
			} else {
				currentConfig.replaceItemValue("Bundle", bundle_);
				currentConfig.replaceItemValue("Location", "BUNDLE");
			}

			currentConfig.replaceItemValue("$ConflictAction", "3"); // merge - no conflicts
			dirty = true;
		}

		// update the DB-Title in document
		if (db != null && !db.getTitle().equals(currentConfig.getItemValueString("ApplicationName"))) {
			currentConfig.replaceItemValue("ApplicationName", db.getTitle());
			dirty = true;
		}
		// add all api-paths of all servers 
		boolean found = false;
		List<String> paths = currentConfig.getItemValues("ApiPaths", String.class);
		for (String apiPath : paths) {
			if (apiPath.equalsIgnoreCase(apiPath_)) {
				found = true;
				break;
			}
		}

		if (!found) {
			paths.add(apiPath_);
			currentConfig.replaceItemValue("ApiPaths", paths);
			dirty = true;
		}

		if (dirty) {
			currentConfig.save();
		}
		return currentConfig;
	}

	/**
	 * Queuing and synchronization is indispensable to avoid that LogEntries overtake each other (in case of a very soon finishing task) and
	 * thus wrong data are written to the XotsLog-document.
	 */
	protected LinkedBlockingQueue<_LogEntry> _logQueue = new LinkedBlockingQueue<_LogEntry>();

	protected class _LogEntry {
		String[] _keys;
		Object[] _values;

		_LogEntry(final Object... kvPairs) {
			int numPairs = kvPairs.length >>> 1;
			_keys = new String[numPairs];
			_values = new Object[numPairs];
			for (int i = 0; i < numPairs; i++) {
				_keys[i] = (String) kvPairs[2 * i];
				_values[i] = kvPairs[2 * i + 1];
			}
		}
	}

	@Tasklet(session = Tasklet.Session.NATIVE, threadConfig = Tasklet.ThreadConfig.STRICT)
	protected class _Logger implements Runnable {

		protected Document getLogDocument() {
			Document taskletDoc = getDocument(true);
			if (taskletDoc == null)
				return null;
			for (Document resp : taskletDoc.getResponses())
				if (resp.getItemValueString("Server").equals(Factory.getLocalServerName()))
					return resp;
			Document srvDoc = new ServerConfiguration(Factory.getLocalServerName()).getDocument(true);
			Document resp = taskletDoc.getAncestorDatabase().createDocument();
			resp.makeResponse(srvDoc, "$REFServer");
			resp.makeResponse(taskletDoc);
			resp.replaceItemValue("Server", Factory.getLocalServerName()).setNames(true);
			resp.replaceItemValue("Form", "XotsLog");
			resp.replaceItemValue("Tasklet", taskletName_);
			resp.replaceItemValue("ApiPath", apiPath_);
			resp.save();
			return resp;
		}

		@Override
		public void run() {
			synchronized (_logQueue) {
				Document doc = getLogDocument();
				if (doc == null)
					return;
				_LogEntry next;
				while ((next = _logQueue.poll()) != null) {
					for (int i = 0; i < next._keys.length; i++) {
						System.out.println("Writing " + next._keys[i] + "=" + next._values[i]);
						doc.put(next._keys[i], next._values[i]);
					}
				}
				doc.save();
			}
		}
	}

	public void logStart() {
		System.out.println("### LOGSTART");
		logCommon("Start", new Date(), //
				"Stop", null, //
				"State", "RUNNING");
	}

	public void logError(final Exception e) {
		System.out.println("### LOGERR " + e);
		logCommon("Stop", new Date(), //
				"State", "ERROR", //
				"ErrorDate", new Date(), //
				"ErrorMessage", "[" + e.getClass().getName() + "]: " + e.getMessage());
	}

	public void logSuccess() {
		System.out.println("### LOGSUCCESS");
		logCommon("Stop", new Date(), //
				"State", "IDLE");
	}

	private void logCommon(final Object... kvPairs) {
		DominoExecutor executor = Configuration.getExecutor();
		if (executor == null)
			return;
		synchronized (_logQueue) {
			for (int i = 0; i < 100; i++) {
				try {
					_logQueue.put(new _LogEntry(kvPairs));
					break;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		executor.submit(new _Logger());
	}
}
