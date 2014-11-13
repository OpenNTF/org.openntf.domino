package org.openntf.domino.utils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum BeanUtils {
	;

	private static Map<Class<?>, Map<String, Method>> PROP_REFLECT_MAP_GET = new ConcurrentHashMap<Class<?>, Map<String, Method>>();
	private static Map<Class<?>, Map<String, Method>> PROP_REFLECT_MAP_SET = new ConcurrentHashMap<Class<?>, Map<String, Method>>();

	public Object NULL_METHOD() {
		return null;
	}

	public static Method getNullMethod() {
		try {
			return BeanUtils.class.getMethod("NULL_METHOD", (Class<?>[]) null);
		} catch (Throwable t) {
			t.printStackTrace();	//this really should be impossible.
			return null;
		}
	}

	public static Class<?>[] toParameterType(final Object value) {
		if (value == null)
			return null;
		Class<?>[] result = new Class<?>[1];
		result[0] = value.getClass();
		return result;
	}

	public static Method findGetter(final Class<?> cls, final String prop) {
		Map<String, Method> clsMap = PROP_REFLECT_MAP_GET.get(cls);
		if (clsMap == null) {
			clsMap = new ConcurrentHashMap<String, Method>();
			synchronized (BeanUtils.class) {
				PROP_REFLECT_MAP_GET.put(cls, clsMap);
			}
		}
		Method crystal = clsMap.get(prop);
		if (crystal == null) {
			String nameGet = "get" + prop;
			String nameIs = "is" + prop;
			for (Method curMethod : cls.getMethods()) {
				String curName = curMethod.getName();
				if (nameGet.equalsIgnoreCase(curName) || nameIs.equalsIgnoreCase(curName)) {
					if (curMethod.getParameterTypes().length == 0) {
						synchronized (clsMap) {
							clsMap.put(prop, curMethod);
						}
						crystal = curMethod;
						break;	//stop looking. We found it.
					}
				}
			}
			if (crystal == null) {
				synchronized (clsMap) {
					clsMap.put(prop, BeanUtils.getNullMethod());
				}
				crystal = BeanUtils.getNullMethod();
			}

		}
		if (BeanUtils.getNullMethod().equals(crystal))
			return null;
		return crystal;
	}

	public static Method findSetter(final Class<?> cls, final String prop, final Class<?>[] paramTypes) {
		Map<String, Method> clsMap = PROP_REFLECT_MAP_SET.get(cls);
		if (clsMap == null) {
			clsMap = new ConcurrentHashMap<String, Method>();
			synchronized (BeanUtils.class) {
				PROP_REFLECT_MAP_SET.put(cls, clsMap);
			}
		}
		Method crystal = clsMap.get(prop);
		if (crystal == null) {
			String nameSet = "set" + prop;
			String nameSet2 = "";
			if (prop.toLowerCase().startsWith("is")) {
				nameSet2 = prop.substring(2);
			}

			for (Method curMethod : cls.getMethods()) {
				String curName = curMethod.getName();
				if (nameSet.equalsIgnoreCase(curName) || nameSet2.equalsIgnoreCase(curName)) {
					if (paramTypes == null || paramTypes.length == 0) {
						synchronized (clsMap) {
							clsMap.put(prop, curMethod);
						}
						crystal = curMethod;
						break;	//stop looking. We found it.
					} else if (paramTypes.length == 1) {
						if (curMethod.getParameterTypes().length == 1) {
							Class<?> paramClass = curMethod.getParameterTypes()[0];
							if (paramClass.isAssignableFrom(paramTypes[0])) {
								synchronized (clsMap) {
									clsMap.put(prop, curMethod);
								}
								crystal = curMethod;
								break;	//stop looking. We found it.
							}
						}
					} else {
						if (curMethod.getParameterTypes().length == paramTypes.length) {
							boolean paramMatch = true;
							for (int i = 0; i < paramTypes.length; i++) {
								if (!curMethod.getParameterTypes()[i].equals(paramTypes[i])) {
									paramMatch = false;
									break;
								}
							}
							if (paramMatch) {
								synchronized (clsMap) {
									clsMap.put(prop, curMethod);
								}
								crystal = curMethod;
								break;	//stop looking. We found it.	
							}
						}
					}
				}
			}
			if (crystal == null) {
				synchronized (clsMap) {
					clsMap.put(prop, BeanUtils.getNullMethod());
				}
				crystal = BeanUtils.getNullMethod();
			}

		}
		if (BeanUtils.getNullMethod().equals(crystal))
			return null;
		return crystal;
	}
}
