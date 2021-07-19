/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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
/**
 * 
 */
package org.openntf.domino.graph;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tinkerpop.blueprints.Element;

/**
 * @author nfreeman
 * 
 */
@Deprecated
@SuppressWarnings("nls")
public interface IDominoProperties {
	public enum Reflect {
		;
		private static IDominoProperties[] EMPTY_PROPS = new IDominoProperties[0];
		private static Map<Class<?>, IDominoProperties[]> MAPPED_PROPERTIES = new ConcurrentHashMap<Class<?>, IDominoProperties[]>();

		public static IDominoProperties[] getMappedProperties(final Class<? extends Element> cls) {
			if (MAPPED_PROPERTIES.get(cls) == null) {
				try {
					Method crystal = cls.getMethod("getMappedProperties", (Class[]) null);
					if (crystal != null) {
						Object raw = crystal.invoke(null, (Object[]) null);
						if (IDominoProperties[].class.isAssignableFrom(raw.getClass())) {
							synchronized (MAPPED_PROPERTIES) {
								MAPPED_PROPERTIES.put(cls, (IDominoProperties[]) raw);
							}
							return (IDominoProperties[]) raw;
						}
					}
				} catch (Exception e) {
				}
				MAPPED_PROPERTIES.put(cls, EMPTY_PROPS);
			}
			return MAPPED_PROPERTIES.get(cls);
		}

		public static IDominoProperties findMappedProperty(final Class<? extends Element> cls, final String prop) {
			IDominoProperties result = null;
			for (IDominoProperties curProp : getMappedProperties(cls)) {
				if (curProp.getName().equalsIgnoreCase(prop)) {
					result = curProp;
					break;
				}
			}
			return result;
		}

	}

	public String getName();

	public Class<?> getType();
}
