/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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
package org.openntf.domino.xsp.helpers;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;

import org.openntf.service.IServiceLocator;
import org.openntf.service.IServiceLocatorFactory;

import com.ibm.commons.extension.ExtensionManager;
import com.ibm.xsp.application.ApplicationEx;

public class OsgiServiceLocatorFactory implements IServiceLocatorFactory {

	@Override
	public IServiceLocator createServiceLocator() {
		return AccessController.doPrivileged((PrivilegedAction<IServiceLocator>) () -> {
			ApplicationFactory aFactory = (ApplicationFactory) FactoryFinder.getFactory("javax.faces.application.ApplicationFactory"); //$NON-NLS-1$
			final ApplicationEx app_ = aFactory == null ? null : (ApplicationEx) aFactory.getApplication();

			if (app_ == null) {
				return new IServiceLocator() {
					final Map<Class<?>, List<?>> cache = new HashMap<Class<?>, List<?>>();

					@SuppressWarnings({ "rawtypes", "unchecked" })
					@Override
					public <T> List<T> findApplicationServices(final Class<T> serviceClazz) {
						List<T> ret = (List<T>) cache.get(serviceClazz);

						if (ret == null) {
							ret = AccessController.doPrivileged(new PrivilegedAction<List<T>>() {
								@Override
								public List<T> run() {
									return (List<T>) ExtensionManager.findApplicationServices(null, Thread.currentThread()
											.getContextClassLoader(), serviceClazz.getName());
								}
							});
							if (Comparable.class.isAssignableFrom(serviceClazz)) {
								Collections.sort((List<? extends Comparable>) ret);
							}
							cache.put(serviceClazz, ret);
						}
						return ret;
					}
				};

			} else {

				return new IServiceLocator() {
					final Map<Class<?>, List<?>> cache = new HashMap<Class<?>, List<?>>();

					@SuppressWarnings({ "unchecked", "rawtypes" })
					@Override
					public <T> List<T> findApplicationServices(final Class<T> serviceClazz) {
						List<T> ret = (List<T>) cache.get(serviceClazz);

						if (ret == null) {
							ret = AccessController.doPrivileged((PrivilegedAction<List<T>>) () -> app_.findServices(serviceClazz.getName()));
							if (Comparable.class.isAssignableFrom(serviceClazz)) {
								Collections.sort((List<? extends Comparable>) ret);
							}
							cache.put(serviceClazz, ret);
						}
						return ret;
					}
				};
			}

		});
	}

}