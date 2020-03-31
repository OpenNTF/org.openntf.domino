/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
package org.openntf.domino.rest.json;

import com.ibm.commons.ResourceHandler;
import com.ibm.commons.util.IteratorWrapper;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonException;
// import com.ibm.domino.services.util.*;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonObject;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openntf.domino.types.CaseInsensitiveString;

public class JsonGraphFactory extends JsonJavaFactory {
	public static final JsonGraphFactory instance = new JsonGraphFactory();

	public static interface IJsonWriterAdapter {
		public Object toJson(Object obj);

		public Collection<Class<?>> getAdapterClassList();
	}

	protected Map<Class<?>, IJsonWriterAdapter> adapterMap_ = new ConcurrentHashMap<Class<?>, IJsonWriterAdapter>();

	public JsonGraphFactory() {

	}

	public void addJsonWriterAdapter(IJsonWriterAdapter adapter) {
		for (Class<?> klazz : adapter.getAdapterClassList()) {
			adapterMap_.put(klazz, adapter);
		}
	}

	public IJsonWriterAdapter getJsonWriterAdapter(Class<?> objClass) {
		for (Class<?> klazz : adapterMap_.keySet()) {
			if (klazz.isAssignableFrom(objClass)) {
				return adapterMap_.get(klazz);
			}
		}
		return null;
	}

	@Override
	public Object createObject(Object paramObject, String paramString) throws JsonException {
		return new JsonJavaObject();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object createArray(Object paramObject, String paramString, Collection<Object> paramList)
			throws JsonException {
		if (paramList instanceof List) {
			return super.createArray(paramObject, paramString, (List) paramList);
		}
		return (paramList);
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public boolean isArray(Object arg0) throws JsonException {
		if (arg0 instanceof List) {
			return super.isArray(arg0);
		} else if (arg0 instanceof Collection) {
			boolean result = super.isArray(((Collection) arg0).toArray());
			// System.out.println("TEMP DEBUG Checking array status of a Collection of type "
			// + arg0.getClass().getName()
			// + ": " + String.valueOf(result));
			return result;
		}
		return super.isArray(arg0);
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public int getArrayCount(Object paramObject) throws JsonException {
		if (paramObject instanceof List) {
			return super.getArrayCount(paramObject);
		} else if (paramObject instanceof Collection) {
			return ((Collection) paramObject).size();
		}
		return super.getArrayCount(paramObject);
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public Object getArrayItem(Object paramObject, int paramInt) throws JsonException {
		if (paramObject instanceof List) {
			return super.getArrayItem(paramObject, paramInt);
		} else if (paramObject instanceof Collection) {
			return ((Collection) paramObject).toArray()[paramInt];
		}
		return super.getArrayItem(paramObject, paramInt);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Iterator<Object> iterateArrayValues(Object paramObject) throws JsonException {
		// System.out.println("TEMP DEBUG iterating array values from a " +
		// paramObject.getClass().getName());
		// if (paramObject.getClass().isArray()) {
		// Object[] a = ((Object[]) paramObject);
		// System.out.println("TEMP DEBUG array is length: " + a.length);
		// }
		if (paramObject instanceof List) {
			return super.iterateArrayValues(paramObject);
		} else if (paramObject instanceof Collection) {
			return ((Collection) paramObject).iterator();
		}
		return super.iterateArrayValues(paramObject);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Iterator<String> iterateObjectProperties(Object object) throws JsonException {
		Iterator it = super.iterateObjectProperties(object);
		Iterator<String> result = new IteratorWrapper(it) {
			@Override
			protected String wrap(Object object) {
				if (object instanceof CharSequence) {
					return ((CharSequence) object).toString();
				} else {
					return String.valueOf(object);
				}
			}

			@Override
			public Object next() {
				Object result = super.next();
				// System.out.println("TEMP DEBUG ObjectProperties Iterator returning a "
				// + result.getClass().getName()
				// + " of " + String.valueOf(result));
				return result;
			}
		};
		return result;
	}

	private static Class<?> getKeyType(Map map) {
		if (map != null && !map.isEmpty()) {
			Object firstKey = map.keySet().iterator().next();
			if (firstKey != null) {
				return firstKey.getClass();
			}
		}
		return null;
	}

	@Override
	public Object getProperty(Object paramObject, String paramString) throws JsonException {
		// NTF Can't use the super method because we may be working with a
		// CaseInsensitiveHashMap and all of IBM's code insists on String
		// instead of CharSequence
		// Object result = super.getProperty(paramObject, paramString);
		Object result = null;
		if (paramObject instanceof Map) {
			Class<?> keyType = getKeyType((Map) paramObject);
			if (CaseInsensitiveString.class.equals(keyType)) {
				CaseInsensitiveString localKey = new CaseInsensitiveString(paramString);
				result = ((Map) paramObject).get(localKey);
			} else {
				result = ((Map) paramObject).get(paramString);
			}
		} else if (paramObject instanceof JsonObject) {
			result = ((JsonObject) paramObject).getJsonProperty(paramString);
		} else {
			throw new IllegalArgumentException(StringUtil.format(
					ResourceHandler.getString("JsonJavaFactory.InvalidJsonobjectclass0.1"),
					new Object[] { (paramObject != null) ? paramObject.getClass().toString() : "null" }));
		}
		// System.out.println("TEMP DEBUG Getting a property of " + paramString
		// + " from an object of type "
		// + paramObject.getClass().getName() + " yielding a "
		// + (result == null ? "NULL" : result.getClass().getName()));
		return result;
	}

}
