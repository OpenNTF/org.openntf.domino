package org.openntf.domino.config;

import java.util.List;
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

import com.ibm.commons.util.StringUtil;

/**
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public abstract class ConfigurationObject {

	private long nextDocAccess = 0;
	private Map<String, Object> cache_ = new FastMap<String, Object>().atomic();
	private Set<String> dirty_ = new FastSet<String>();

	protected abstract String[] keys();

	protected abstract Document getDocument(boolean create);

	protected Object get(final String key) {
		updateCache();
		return cache_.get(key);
	}

	protected boolean put(final String key, final Object value) {
		updateCache();
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

	protected synchronized void updateCache() {
		if (System.currentTimeMillis() < nextDocAccess) {
			DominoExecutor executor = Configuration.getExecutor();
			if (executor != null) {
				Future<Void> f = executor.submit(new DocAccessor());
				try {
					f.get(2, TimeUnit.SECONDS);
				} catch (Exception e) {
					nextDocAccess = Long.MAX_VALUE; // do not read the doc again before Sun Aug 17 17:12:55 EST 292278994
					e.printStackTrace();
				}
			}
		}
	}

	@Tasklet(session = Tasklet.Session.NATIVE, threadConfig = Tasklet.ThreadConfig.STRICT)
	protected class DocAccessor implements Callable<Void> {

		@Override
		public Void call() {
			Database odaDb_ = Configuration.getOdaDb();
			if (odaDb_ == null) {
				// we don't have an ODA-DB. so it's useless to query it again;
				nextDocAccess = Long.MAX_VALUE;
				return null;
			}

			synchronized (dirty_) {
				Document doc = getDocument(!dirty_.isEmpty()); // Create the document, if cache is dirty;

				for (String dirtyKey : dirty_) {
					doc.put(dirtyKey, cache_.get(dirtyKey));
				}
				dirty_.clear();
				for (String key : keys()) {
					cache_.put(key, doc.get(key));
				}
				nextDocAccess = System.currentTimeMillis() + 15 * 1000;
			}
			return null;
			//			String s = getOverrideIni(key);
			//			if (s != null) {
			//				return s;
			//			} else {
			//				Document doc = getDocument(false);
			//				return doc == null ? null : doc.get(key);
			//			}
		}

	}

	public void flush() {
		// TODO Auto-generated method stub

	}

	//	public Object get(final String key) {
	//		if (cache_.containsKey(key))
	//			return cache_.get(key);
	//
	//		DominoExecutor executor = Configuration.getExecutor();
	//		Object value = null;
	//		if (executor != null) {
	//
	//			Future<Object> f = executor.submit(new _Getter(key));
	//			try {
	//				value = f.get(2, TimeUnit.SECONDS);
	//			} catch (InterruptedException e) {
	//				e.printStackTrace();
	//			} catch (ExecutionException e) {
	//				e.printStackTrace();
	//			} catch (TimeoutException e) {
	//				e.printStackTrace();
	//			}
	//			if (value != null)
	//				cache_.put(key, value);
	//		}
	//		return value;
	//	}
	//
	//	public void putIfAbsent(final String key, final Object value) {
	//		Object ret = get(key);
	//		if (ret instanceof List) {
	//			List<?> l = (List<?>) ret;
	//			ret = l.size() > 0 ? l.get(0) : null;
	//		}
	//		if (ret != null) {
	//			String s = String.valueOf(ret);
	//			if (!StringUtil.isEmpty(s)) {
	//				return;
	//			}
	//		}
	//
	//		put(key, value);
	//	}
	//
	//	public void put(final String key, final Object value) {
	//		cache_.put(key, value);
	//		DominoExecutor executor = Configuration.getExecutor();
	//		if (executor != null) {
	//			executor.submit(new _Setter(key, value));
	//		}
	//		return;
	//	}
	//
	protected int getConfigValueInt(final String name, final int def) {
		Object ret = get(name);
		if (ret instanceof List) {
			List<?> l = (List<?>) ret;
			ret = l.size() > 0 ? l.get(0) : null;
		}
		if (ret instanceof Number) {
			return ((Number) ret).intValue();
		}

		if (ret instanceof String) {
			try {
				return Integer.parseInt((String) ret);
			} catch (NumberFormatException e) {
			}
		}
		put(name, def);
		return def;
	}

	protected String getConfigValueString(final String name, final String def) {
		Object ret = get(name);
		if (ret instanceof List) {
			List<?> l = (List<?>) ret;
			ret = l.size() > 0 ? l.get(0) : null;
		}
		if (ret != null) {
			String s = String.valueOf(ret);
			if (!StringUtil.isEmpty(s)) {
				return s;
			}
		}
		put(name, def);
		return def;
	}

	//
	//	protected String getOverrideIni(final String key) {
	//		return null;
	//	}
	//

	//
	//	@Tasklet(session = Tasklet.Session.NATIVE, threadConfig = Tasklet.ThreadConfig.STRICT)
	//	protected class _Setter implements Runnable {
	//
	//		private String key;
	//		private Object value;
	//
	//		public _Setter(final String key, final Object value) {
	//			super();
	//			this.key = key;
	//			this.value = value;
	//		}
	//
	//		@Override
	//		public void run() {
	//			Document doc = getDocument(true);
	//			if (doc != null) {
	//				// TODO  better dirty check
	//				if (value instanceof String && doc.hasItem(key)) {
	//					if (value.equals(doc.getItemValue(key, String.class)))
	//						return;
	//				}
	//				doc.put(key, value);
	//				doc.save();
	//			}
	//		}
	//
	//	}
}
