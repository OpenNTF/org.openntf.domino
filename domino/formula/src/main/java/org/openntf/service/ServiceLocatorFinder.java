package org.openntf.service;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public enum ServiceLocatorFinder {
	;
	@SuppressWarnings("rawtypes")
	private static Map<Class, List> nonOSGIServicesCache;
	private static IServiceLocatorFactory serviceLocatorFactory = new ServiceLocatorFactory();

	public static IServiceLocator findServiceLocator() {
		return serviceLocatorFactory.createServiceLocator();
	}

	public static void setServiceLocatorFactory(final IServiceLocatorFactory slf) {
		serviceLocatorFactory = slf;
	}

	@SuppressWarnings("rawtypes")
	public static class ServiceLocatorFactory implements IServiceLocatorFactory {
		public IServiceLocator createServiceLocator() {
			// this is the non OSGI case:
			if (nonOSGIServicesCache == null) {
				nonOSGIServicesCache = new ConcurrentHashMap<Class, List>();
			}
			return new IServiceLocator() {
				@SuppressWarnings("unchecked")
				@Override
				public <T> List<T> findApplicationServices(final Class<T> serviceClazz) {
					List<T> ret = nonOSGIServicesCache.get(serviceClazz);
					if (ret == null) {
						ret = new ArrayList<T>();
						nonOSGIServicesCache.put(serviceClazz, ret);
						final List<T> fret = ret;

						AccessController.doPrivileged(new PrivilegedAction<Object>() {
							public Object run() {
								ClassLoader cl = Thread.currentThread().getContextClassLoader();
								if (cl != null) {
									ServiceLoader<T> loader = ServiceLoader.load(serviceClazz, cl);
									Iterator<T> it = loader.iterator();
									while (it.hasNext()) {
										fret.add(it.next());
									}
								}
								if (Comparable.class.isAssignableFrom(serviceClazz)) {
									Collections.sort((List<? extends Comparable>) fret);
								}
								return null;
							}
						});

					}
					return ret;
				}
			};
		}
	}
}
