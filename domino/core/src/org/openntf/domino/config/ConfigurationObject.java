package org.openntf.domino.config;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javolution.util.FastMap;
import javolution.util.FastSet;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.xots.Tasklet;

/**
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public abstract class ConfigurationObject {

	private long nextDocAccess = 0;
	private Map<String, Object> cache_ = new FastMap<String, Object>().atomic();
	private Set<String> dirty_ = new FastSet<String>();

	// pair of value name & class
	protected abstract Object[] schema();

	protected abstract Document getDocument(boolean create);

	protected <T> T get(final String key) {
		initCache();
		return (T) cache_.get(key);
	}

	protected <T> T get(final String key, final T defaultValue) {
		T ret = get(key);
		if (ret == null) {
			ret = defaultValue;
			put(key, ret);
		}
		return ret;
	}

	protected boolean put(final String key, final Object value) {
		initCache();
		if (value.equals(cache_.get(key))) {
			return false;
		} else {
			if (nextDocAccess == Long.MAX_VALUE) {
				cache_.put(key, value);
			} else {
				synchronized (dirty_) {
					dirty_.add(key);
					cache_.put(key, value);
				}
				// Notify the configuration, that this object is dirty
				Configuration.addDirty(this);
			}

			return true;
		}
	}

	@Tasklet(session = Tasklet.Session.NATIVE, threadConfig = Tasklet.ThreadConfig.STRICT)
	protected class CacheSyncer implements Callable<Void> {
		@Override
		public Void call() {
			syncCache();
			return null;

		}
	}

	/**
	 * initializes the chache
	 */
	protected synchronized void initCache() {
		if (System.currentTimeMillis() > nextDocAccess) {
			nextDocAccess = System.currentTimeMillis() + 15 * 1000;
			DominoExecutor executor = Configuration.getExecutor();
			if (executor != null) {
				Future<Void> f = executor.submit(new CacheSyncer());
				try {
					f.get(2, TimeUnit.SECONDS);
				} catch (Exception e) {
					//nextDocAccess = Long.MAX_VALUE; // do not read the doc again before Sun Aug 17 17:12:55 EST 292278994
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Syncs the <code>cache_<code> with the backend document. must be called from a proper initialized thread!
	 */
	public void syncCache() {
		Database odaDb_ = Configuration.getOdaDb();
		if (odaDb_ == null) {
			// we don't have an ODA-DB. so it's useless to query it again;
			nextDocAccess = Long.MAX_VALUE; // do not read the doc again before Sun Aug 17 17:12:55 EST 292278994
			return;
		}

		synchronized (dirty_) {
			Document doc = getDocument(true); // Create the document, if cache is dirty;
			if (doc == null)
				return;
			// write back all dirty keys to the document
			if (!dirty_.isEmpty()) {
				for (String dirtyKey : dirty_) {
					doc.put(dirtyKey, cache_.get(dirtyKey));
				}
				doc.save();
				dirty_.clear();
			}

			// read all keys from the document that are specified in the schema 
			Object[] s = schema();
			for (int i = 0; i < s.length; i += 2) {
				cache_.put((String) s[i], doc.getItemValue((String) s[i], (Class<?>) s[i + 1]));
			}
		}
	}

}
