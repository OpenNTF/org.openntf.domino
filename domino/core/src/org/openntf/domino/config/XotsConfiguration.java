package org.openntf.domino.config;

import java.util.Date;
import java.util.List;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xots.Tasklet;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

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

		String server = Factory.getLocalServerName();
		Database db = null;
		String unid;
		boolean dirty = false;
		if (isDatabase_) {
			db = odaDb_.getAncestorSession().getDatabase(apiPath_);
			unid = Configuration.computeUNID(taskletName_, db);
		} else {
			unid = Configuration.computeUNID(bundle_ + ":" + taskletName_, odaDb_); // use a valid ReplicaID
		}

		Document currentConfig_ = odaDb_.getDocumentByUNID(unid);
		if (currentConfig_ == null) {
			if (!create)
				return null;
			currentConfig_ = odaDb_.createDocument();
			currentConfig_.setUniversalID(unid);
			currentConfig_.replaceItemValue("Form", "XotsTasklet");
			currentConfig_.replaceItemValue("TaskletName", taskletName_);
			currentConfig_.replaceItemValue("ApiPaths", apiPath_); // APIPath is just for UI - internally we always use replica ID
			currentConfig_.replaceItemValue("Enabled", true);
			if (isDatabase_) {
				currentConfig_.replaceItemValue("ReplicaId", db.getReplicaID());
				currentConfig_.replaceItemValue("Location", "NSF");
			} else {
				currentConfig_.replaceItemValue("Bundle", bundle_);
				currentConfig_.replaceItemValue("Location", "BUNDLE");
			}

			currentConfig_.replaceItemValue("$ConflictAction", "3"); // merge - no conflicts
			dirty = true;
		}

		// update the DB-Title in document
		if (db != null && !db.getTitle().equals(currentConfig_.getItemValueString("ApplicationName"))) {
			currentConfig_.replaceItemValue("ApplicationName", db.getTitle());
			dirty = true;
		}
		// add all api-paths of all servers 
		boolean found = false;
		List<String> paths = currentConfig_.getItemValues("ApiPaths", String.class);
		for (String apiPath : paths) {
			if (apiPath.equalsIgnoreCase(apiPath_)) {
				found = true;
				break;
			}
		}

		if (!found) {
			paths.add(apiPath_);
			currentConfig_.replaceItemValue("ApiPaths", paths);
			dirty = true;
		}

		if (dirty) {
			currentConfig_.save();
		}
		return currentConfig_;
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
			Document doc = getDocument(true);
			if (doc != null) {
				for (Document resp : doc.getResponses()) {
					if (resp.getItemValueString("Server").equals(Factory.getLocalServerName())) {
						return resp;
					}
				}
				Document resp = doc.getAncestorDatabase().createDocument();
				resp.makeResponse(doc);
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
					doc.put(keys[i], values[i]);
				}
				doc.save();
			}
		}
	}

	public void logStart() {
		// TODO Auto-generated method stub
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

	protected String[] keys() {
		// TODO Auto-generated method stub
		return null;
	}
}
