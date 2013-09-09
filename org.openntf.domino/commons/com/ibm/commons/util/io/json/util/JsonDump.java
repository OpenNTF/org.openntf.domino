/*
 * © Copyright IBM Corp. 2012-2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package com.ibm.commons.util.io.json.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ibm.commons.util.EmptyIterator;
import com.ibm.commons.util.io.TextOutputStream;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonFactory;
import com.ibm.commons.util.print.DumpObject;
import com.ibm.commons.util.print.DumpObject.Adapter;
import com.ibm.commons.util.print.DumpObject.AdapterFactory;
import com.ibm.commons.util.print.DumpObject.IFilter;
import com.ibm.commons.util.print.DumpObject.NullAdapter;

/**
 * Dump adapter for Json Objects.
 * 
 * @author Philippe Riand
 */
public final class JsonDump {

	// ///////////////////////////////////////////////////////////////////
	// Object instance
	// ///////////////////////////////////////////////////////////////////

	public static void dumpObject(JsonFactory factory, Object object) throws IOException {
		DumpObject d = new DumpObject(new JsonAdapterFactory(factory));
		d.dump(object);
	}

	public static void dumpObject(TextOutputStream w, JsonFactory factory, Object object) throws IOException {
		DumpObject d = new DumpObject(new JsonAdapterFactory(factory));
		d.setTextOutputStream(w);
		d.dump(object);
	}

	public static class JsonAdapterFactory implements AdapterFactory {
		private JsonFactory factory;

		public JsonAdapterFactory(JsonFactory factory) {
			this.factory = factory;
		}

		public Adapter createAdapter(Object o) {
			try {
				if (factory.isNull(o)) {
					return new NullAdapter();
				}
				if (factory.isArray(o)) {
					return new ArrayAdapter(factory, o);
				}
				if (factory.isObject(o)) {
					return new ObjectAdapter(factory, o);
				}
				if (factory.isBoolean(o)) {
					return new DumpObject.PrimitiveAdapter(factory.getBoolean(o));
				}
				if (factory.isNumber(o)) {
					return new DumpObject.PrimitiveAdapter(factory.getNumber(o));
				}
				if (factory.isString(o)) {
					return new DumpObject.PrimitiveAdapter(factory.getString(o));
				}
			} catch (JsonException ex) {
				ex.printStackTrace();
			}
			return DumpObject.defaultFactory.createAdapter(o);
		}
	}

	public static class ObjectAdapter extends Adapter {
		JsonFactory factory;
		Object instance;

		public ObjectAdapter(JsonFactory factory, Object instance) {
			this.factory = factory;
			this.instance = instance;
		}

		@Override
		public String getValue() {
			return "Object";
		}

		@Override
		public boolean isObject() {
			return true;
		}

		@Override
		public Map getPropertyMap(IFilter filter) {
			HashMap map = new HashMap();
			try {
				for (Iterator<String> it = factory.iterateObjectProperties(instance); it.hasNext();) {
					String prop = it.next();
					if (filter != null && !filter.acceptProperty(prop, null)) {
						continue;
					}
					Object value = factory.getProperty(instance, prop);
					map.put(prop, value);
				}
			} catch (JsonException ex) {
				ex.printStackTrace();
			}
			return map;
		}
	}

	public static class ArrayAdapter extends Adapter {
		JsonFactory factory;
		Object instance;

		public ArrayAdapter(JsonFactory factory, Object instance) {
			this.factory = factory;
			this.instance = instance;
		}

		@Override
		public boolean isArray() {
			return true;
		}

		@Override
		public int arrayCount() {
			try {
				return factory.getArrayCount(instance);
			} catch (JsonException e) {
				e.printStackTrace();
				return 0;
			}
		}

		@Override
		public Iterator arrayIterator() {
			try {
				return factory.iterateArrayValues(instance);
			} catch (JsonException e) {
				e.printStackTrace();
				return EmptyIterator.getInstance();
			}
		}
	}
}