package org.openntf.domino.config;

import java.util.concurrent.Callable;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xots.Tasklet;

import com.ibm.commons.util.StringUtil;

public class ServerConfigurationProperties extends ConfigurationProperties {

	private String serverName_;

	public ServerConfigurationProperties(final String serverName) {
		serverName_ = serverName;
	}

	/**
	 * Returns the document for this server
	 * 
	 * @return
	 */
	protected Document getDocument() {
		Database odaDb_ = Configuration.getOdaDb();
		if (odaDb_ == null)
			return null;

		View luView = odaDb_.getView("$lookupConfiguration");
		if (luView == null) {
			Factory.println("ServerConfiguration", "ERROR: View '$lookupConfiguration' not found in " + odaDb_);
			return null;
		}
		luView.refresh();
		Document currentConfig_ = luView.getFirstDocumentByKey(serverName_);
		if (currentConfig_ == null) {
			currentConfig_ = odaDb_.createDocument();
			currentConfig_.replaceItemValue("Form", "Configuration");
			currentConfig_.replaceItemValue("ServerName", serverName_).setNames(true);
			currentConfig_.save();
			luView.refresh();
		}
		return currentConfig_;
	}

	@Override
	protected Callable<Object> queryValue(final String key) {
		return new _Getter(key);
	}

	@Override
	protected Runnable setValue(final String key, final Object value) {
		return new _Setter(key, value);
	}

	@Tasklet(session = Tasklet.Session.NATIVE, threadConfig = Tasklet.ThreadConfig.STRICT)
	protected class _Getter implements Callable<Object> {

		private String key;

		public _Getter(final String key) {
			super();
			this.key = key;
		}

		@Override
		public Object call() {
			Session sess = Factory.getSession(SessionType.CURRENT);
			String s = sess.getEnvironmentString("ODA_" + key);
			if (!StringUtil.isEmpty(s)) {
				Factory.println("INFO", "using notes.ini value ODA_" + key + "=" + s);
				return s;
			} else {
				Document doc = getDocument();
				return doc == null ? null : doc.get(key);
			}
		}

	}

	@Tasklet(session = Tasklet.Session.NATIVE, threadConfig = Tasklet.ThreadConfig.STRICT)
	protected class _Setter implements Runnable {

		private String key;
		private Object value;

		public _Setter(final String key, final Object value) {
			super();
			this.key = key;
			this.value = value;
		}

		@Override
		public void run() {
			Document doc = getDocument();
			if (doc != null) {
				doc.put(key, value);
				doc.save();
			}
		}

	}
}
