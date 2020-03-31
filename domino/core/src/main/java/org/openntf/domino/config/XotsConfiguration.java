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
package org.openntf.domino.config;

import java.util.Date;
import java.util.List;

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
				"SchedulesDefault", String[].class,
				"OnAllServers", 	Boolean.class,
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

	@Tasklet(session = Tasklet.Session.NATIVE, threadConfig = Tasklet.ThreadConfig.STRICT)
	protected class _Logger implements Runnable {

		private String[] keys;
		private Object[] values;

		public _Logger(final String[] keys, final Object[] values) {
			super();
			this.keys = keys;
			this.values = values;
		}

		protected Document getLogDocument() {
			Document taskletDoc = getDocument(true);
			if (taskletDoc != null) {
				for (Document resp : taskletDoc.getResponses()) {
					if (resp.getItemValueString("Server").equals(Factory.getLocalServerName())) {
						return resp;
					}
				}
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
			return null;
		}

		@Override
		public void run() {
			Document doc = getLogDocument();
			if (doc != null) {
				for (int i = 0; i < keys.length; i++) {
					System.out.println("Writing " + keys[i] + "=" + values[i]);
					doc.put(keys[i], values[i]);
				}
				doc.save();
			}
		}
	}

	public void logStart() {
		DominoExecutor executor = Configuration.getExecutor();
		if (executor != null) {
			String[] keys = new String[3];
			Object[] values = new Object[3];

			keys[0] = "Start";
			values[0] = new Date();

			keys[1] = "Stop";
			values[1] = null;

			keys[2] = "State";
			values[2] = "RUNNING";
			executor.submit(new _Logger(keys, values));
		}
	}

	public void logError(final Exception e) {
		DominoExecutor executor = Configuration.getExecutor();
		if (executor != null) {
			String[] keys = new String[4];
			Object[] values = new Object[4];

			keys[0] = "Stop";
			values[0] = new Date();

			keys[1] = "State";
			values[1] = "ERROR";

			keys[2] = "ErrorDate";
			values[2] = new Date();

			keys[3] = "ErrorMessage";
			values[3] = e.getMessage();
			executor.submit(new _Logger(keys, values));
		}
	}

	public void logSuccess() {
		DominoExecutor executor = Configuration.getExecutor();
		if (executor != null) {
			String[] keys = new String[2];
			Object[] values = new Object[2];
			keys[0] = "Stop";
			values[0] = new Date();

			keys[1] = "State";
			values[1] = "IDLE";

			executor.submit(new _Logger(keys, values));
		}
	}

}
