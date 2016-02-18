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
