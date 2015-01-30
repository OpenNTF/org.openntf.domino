package org.openntf.domino.config;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javolution.util.FastMap;

import org.openntf.domino.thread.DominoExecutor;

/**
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public abstract class ConfigurationProperties {
	private Map<String, Object> cache_ = new FastMap<String, Object>().atomic();

	public Object get(final String key) {
		if (cache_.containsKey(key))
			return cache_.get(key);

		DominoExecutor executor = Configuration.getExecutor();
		Object value = null;
		if (executor != null) {

			Future<Object> f = executor.submit(queryValue(key));
			try {
				value = f.get(2, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
			if (value != null)
				cache_.put(key, value);
		}
		return value;
	}

	public void put(final String key, final Object value) {
		cache_.put(key, value);
		DominoExecutor executor = Configuration.getExecutor();
		if (executor != null) {
			executor.submit(setValue(key, value));
		}
		return;
	}

	protected abstract Callable<Object> queryValue(final String key);

	protected abstract Runnable setValue(final String key, Object value);

	public int getConfigValueInt(final String name, final int def) {
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
}
