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

package com.ibm.commons.util.print;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

//import com.ibm.commons.Platform;
import com.ibm.commons.util.QuickSort;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.TextOutputStream;

/**
 * Dumping an object.
 * 
 * @ibm-not-published
 */
public class DumpObject extends TablePrinter {
	private static final Logger log_ = Logger.getLogger(DumpObject.class.getName());

	public interface IFilter {
		public boolean acceptField(Field field);

		public boolean acceptProperty(String name, Object property);
	}

	public static DefaultAdapterFactory defaultFactory = new DefaultAdapterFactory();
	public static DefaultFilter defaultFilter = new DefaultFilter();

	private AdapterFactory adapterFactory = defaultFactory;

	private TextOutputStream ps = new TextOutputStream(System.out);
	private int depth = 10;
	private int indent = 2;
	private boolean printType;
	private IFilter filter = defaultFilter;

	public DumpObject() {

	}

	public DumpObject(final AdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
	}

	public AdapterFactory getAdapterFactory() {
		return adapterFactory;
	}

	public void setAdapterFactory(final AdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(final int depth) {
		this.depth = depth;
	}

	public TextOutputStream getTextOutputStream() {
		return ps;
	}

	public void setTextOutputStream(final TextOutputStream ps) {
		this.ps = ps;
	}

	public IFilter getFilter() {
		return filter;
	}

	public void setFilter(final IFilter propertyFilter) {
		this.filter = propertyFilter;
	}

	public boolean isPrintType() {
		return printType;
	}

	public void setPrintType(final boolean printType) {
		this.printType = printType;
	}

	public int getIndent() {
		return indent;
	}

	public void setIndent(final int indent) {
		this.indent = indent;
	}

	public void dump(final Object o) throws IOException {
		if (o != null) {
			dump(new HashMap(), new ArrayList(), null, o, depth);
		} else {
			ps.println("<null>");
		}
		ps.flush();
	}

	public static interface AdapterFactory {
		public Adapter createAdapter(Object o);
	}

	public static class FactoryWrapper implements AdapterFactory {
		private AdapterFactory wrapped;

		public FactoryWrapper(final AdapterFactory wrapped) {
			this.wrapped = wrapped;
		}

		public Adapter createAdapter(final Object o) {
			return wrapped.createAdapter(o);
		}
	}

	public static abstract class Adapter {
		public String getTypeAsString() {
			return "";
		}

		public boolean isValue() {
			return false;
		}

		public String getValue() {
			return null;
		}

		public boolean isArray() {
			return false;
		}

		public Iterator arrayIterator() {
			return null;
		}

		public int arrayCount() {
			return 0;
		}

		public boolean isObject() {
			return false;
		}

		public Map getPropertyMap(final IFilter filter) {
			return null;
		}
	}

	public static class AdapterWrapper extends Adapter {
		private Adapter wrapped;

		public AdapterWrapper(final Adapter wrapped) {
			this.wrapped = wrapped;
		}

		@Override
		public String getTypeAsString() {
			return wrapped.getTypeAsString();
		}

		@Override
		public boolean isValue() {
			return wrapped.isValue();
		}

		@Override
		public String getValue() {
			return wrapped.getValue();
		}

		@Override
		public boolean isArray() {
			return wrapped.isArray();
		}

		@Override
		public Iterator arrayIterator() {
			return wrapped.arrayIterator();
		}

		@Override
		public int arrayCount() {
			return wrapped.arrayCount();
		}

		@Override
		public boolean isObject() {
			return wrapped.isObject();
		}

		@Override
		public Map getPropertyMap(final IFilter filter) {
			return wrapped.getPropertyMap(filter);
		}
	}

	public static class DefaultFilter implements IFilter {
		public boolean acceptField(final Field field) {
			return true;
		}

		public boolean acceptProperty(final String name, final Object property) {
			if (property instanceof PropertyDescriptor) {
				if (name.equals("class")) {
					return false;
				}
				return true;
			}
			return true;
		}
	}

	public static class DefaultAdapterFactory implements AdapterFactory {
		private boolean beans;

		public DefaultAdapterFactory() {
			this.beans = true;
		}

		public DefaultAdapterFactory(final boolean beans) {
			this.beans = beans;
		}

		public Adapter createAdapter(final Object o) {
			if (o == null) {
				return new NullAdapter();
			}

			Class c = o.getClass();

			// Check for a simple value
			if (o instanceof String) {
				return new PrimitiveAdapter(o);
			}
			if (o instanceof Number) {
				return new PrimitiveAdapter(o);
			}
			if (o instanceof Boolean) {
				return new PrimitiveAdapter(o);
			}
			if (o instanceof Character) {
				return new PrimitiveAdapter(o);
			}
			if (o instanceof Date) {
				return new PrimitiveAdapter(o);
			}
			if (o instanceof Calendar) {
				return new PrimitiveAdapter(new Date(((Calendar) o).getTimeInMillis()));
			}

			// Check for an array/collection
			if (c.isArray()) {
				return new ArrayAdapter(o);
			}
			if (o instanceof Collection) {
				return new CollectionAdapter(o);
			}
			if (o instanceof Map) {
				return new MapAdapter(o);
			}

			// XML document
			if (o instanceof Document) {
				Node node = (Node) o;
				return new XmlAdapter(node);
			}

			// Try a Java bean
			if (beans) {
				try {
					BeanInfo bi = java.beans.Introspector.getBeanInfo(c);
					PropertyDescriptor[] desc = bi.getPropertyDescriptors();
					return new JavaBeanAdapter(desc, o);
				} catch (Exception ex) {
				}
			}

			// Regular Java class
			return new JavaObjectAdapter(o);
		}
	}

	public static class NullAdapter extends Adapter {
		public NullAdapter() {
		}

		@Override
		public boolean isValue() {
			return true;
		}

		@Override
		public String getValue() {
			return "<null>"; // $NON-NLS-1$
		}
	}

	public static class PrimitiveAdapter extends Adapter {
		Object value;

		public PrimitiveAdapter(final Object value) {
			this.value = value;
		}

		@Override
		public String getTypeAsString() {
			String cName = value.getClass().getName();
			return cName;
		}

		@Override
		public boolean isValue() {
			return true;
		}

		@Override
		public String getValue() {
			return value.toString();
		}
	}

	public static class JavaBeanAdapter extends Adapter {
		PropertyDescriptor[] desc;
		Object instance;

		public JavaBeanAdapter(final PropertyDescriptor[] desc, final Object instance) {
			this.desc = desc;
			this.instance = instance;
		}

		@Override
		public boolean isObject() {
			return true;
		}

		@Override
		public Map getPropertyMap(final IFilter filter) {
			HashMap map = new HashMap();
			for (int i = 0; i < desc.length; i++) {
				if (filter != null && !filter.acceptProperty(desc[i].getName(), desc[i])) {
					continue;
				}
				String name = desc[i].getName();
				Object value;
				try {
					Method read = desc[i].getReadMethod();
					if (read == null) {
						value = StringUtil.format("<error: No bean read method>"); // $NON-NLS-1$
					} else {
						value = read.invoke(instance, (Object[]) null);
					}
				} catch (Exception e) {
					value = StringUtil.format("<error: {0}>", e.getMessage()); // $NON-NLS-1$
				}
				map.put(name, value);
			}
			return map;
		}
	}

	public static class JavaObjectAdapter extends Adapter {
		Field[] fields;
		Object instance;

		public JavaObjectAdapter(final Object instance) {
			this.fields = instance.getClass().getFields();
			this.instance = instance;
		}

		@Override
		public boolean isObject() {
			return true;
		}

		@Override
		public Map getPropertyMap(final IFilter filter) {
			HashMap map = new HashMap();
			for (int i = 0; i < fields.length; i++) {
				if (filter != null && !filter.acceptProperty(fields[i].getName(), fields[i])) {
					continue;
				}
				String name = fields[i].getName();
				Object value;
				try {
					value = fields[i].get(instance);
				} catch (Exception e) {
					value = StringUtil.format("<error: {0}>", e.getMessage()); // $NON-NLS-1$
				}
				map.put(name, value);
			}
			return map;
		}
	}

	public static class ArrayAdapter extends Adapter {
		Object instance;

		public ArrayAdapter(final Object instance) {
			this.instance = instance;
		}

		@Override
		public boolean isArray() {
			return true;
		}

		@Override
		public Iterator arrayIterator() {
			return new Iterator() {
				int current = 0;
				int length = Array.getLength(instance);

				public boolean hasNext() {
					return current < length;
				}

				public Object next() {
					if (current < length) {
						return Array.get(instance, current++);
					}
					return null;
				}

				public void remove() {
				}
			};
		}

		@Override
		public int arrayCount() {
			return Array.getLength(instance);
		}
	}

	public static class CollectionAdapter extends Adapter {
		Object instance;

		public CollectionAdapter(final Object instance) {
			this.instance = instance;
		}

		@Override
		public boolean isArray() {
			return true;
		}

		@Override
		public Iterator arrayIterator() {
			return ((Collection) instance).iterator();
		}

		@Override
		public int arrayCount() {
			return ((Collection) instance).size();
		}
	}

	public static class MapAdapter extends Adapter {
		Object instance;

		public MapAdapter(final Object instance) {
			this.instance = instance;
		}

		@Override
		public boolean isArray() {
			return true;
		}

		@Override
		public Iterator arrayIterator() {
			return ((Map) instance).entrySet().iterator();
		}

		@Override
		public int arrayCount() {
			return ((Map) instance).size();
		}
	}

	public static class XmlAdapter extends Adapter {
		Node instance;

		public XmlAdapter(final Node instance) {
			this.instance = instance;
		}

		@Override
		public String getTypeAsString() {
			return "XML Node";
		}

		@Override
		public boolean isValue() {
			return true;
		}

		@Override
		public String getValue() {
			try {
				return writeToString(instance);
			} catch (Exception e) {
				return StringUtil.format("<XML error: {0}>", e.getMessage());
			}
		}

		private String writeToString(final Node node) {
			try {
				Transformer serializer = TransformerFactory.newInstance().newTransformer();
				StringWriter writer = new StringWriter();
				serializer.transform(new DOMSource(node), new StreamResult(writer));
				return writer.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private void dump(final HashMap map, final ArrayList hierarchy, final String propertyName, final Object v, final int depth)
			throws IOException {
		Adapter adapter = adapterFactory.createAdapter(v);

		// Limit the depth...
		if (depth <= 0) {
			return;
		}

		// Get the children list
		// Print the indentation
		for (int i = 0; i < hierarchy.size(); i++) {
			switch (indent) {
			case 0:
				break;
			case 1:
				ps.print(" ");break;//$NON-NLS-1$
			case 2:
				ps.print("  ");break;//$NON-NLS-1$
			case 3:
				ps.print("   ");break;//$NON-NLS-1$
			case 4:
				ps.print("    ");break;//$NON-NLS-1$
			default: {
				for (int id = 0; id < indent; id++) {
					ps.print(' ');
				}
			}
			}
		}

		hierarchy.add(v);
		try {
			// Print the value itself
			if (hierarchy.size() > 1) {
				ps.print("+- "); //$NON-NLS-1$
			}

			// Display the value
			if (StringUtil.isNotEmpty(propertyName)) {
				ps.print(propertyName);
				ps.print(": "); //$NON-NLS-1$
			}

			if (adapter.isValue()) {
				String vs = valueAndType(adapter.getValue(), adapter.getTypeAsString());
				if (!StringUtil.isEmpty(vs)) {
					ps.print(vs);
				}
				String type = adapter.getTypeAsString();
				if (printType && StringUtil.isNotEmpty(type)) {
					ps.print(" (");
					ps.print(type);
					ps.print(")");
				}
				ps.print("\n"); //$NON-NLS-1$
			}

			// Display Object's property
			if (adapter.isObject()) {
				if (!inHierarchy(hierarchy, v)) {
					ps.print(StringUtil.format("{0}\n", valueAndType(adapter.getValue(), adapter.getTypeAsString())));

					Map pMap = adapter.getPropertyMap(filter);

					ArrayList names = new ArrayList();
					for (Iterator it = pMap.keySet().iterator(); it.hasNext();) {
						String name = (String) it.next();
						names.add(name);
					}
					(new QuickSort.JavaList(names)).sort();

					int count = names.size();
					for (int i = 0; i < count; i++) {
						String pName = (String) names.get(i);
						Object pValue = pMap.get(pName);
						dump(map, hierarchy, pName, pValue, depth - 1);
					}
				} else {
					ps.print(" Object already in hierarchy\n"); //$NON-NLS-1$
				}
			}
			// Display java arrays
			if (adapter.isArray()) {
				if (!inHierarchy(hierarchy, v)) {
					ps.print(" Array [" + adapter.arrayCount() + "]\n");
					int i = 0;
					for (Iterator it = adapter.arrayIterator(); it.hasNext(); i++) {
						Object value = it.next();
						dump(map, hierarchy, "[" + i + "]", value, depth - 1); //$NON-NLS-1$ //$NON-NLS-2$
					}
				} else {
					ps.print(" Array already in hierarchy\n"); //$NON-NLS-1$
				}
			}
		} finally {
			hierarchy.remove(hierarchy.size() - 1);
		}
	}

	private boolean inHierarchy(final ArrayList hierarchy, final Object instance) {
		int count = hierarchy.size() - 1; // Ignore the current one...
		for (int i = 0; i < count; i++) {
			if (hierarchy.get(i) == instance) {
				return true;
			}
		}
		return false;
	}

	private String valueAndType(final String value, final String typeAsString) {
		StringBuilder b = new StringBuilder();
		if (StringUtil.isNotEmpty(value)) {
			b.append(value);
		} else {
			b.append("<empty>");
		}
		if (StringUtil.isNotEmpty(typeAsString) && !typeAsString.equals("java.lang.String")) {
			b.append(" (");
			b.append(typeAsString);
			b.append(")");
		}

		return b.toString();
	}

	public void dump(final DataInputStream ios) {
		dump(ios, Integer.MAX_VALUE);
	}

	public void dump(final DataInputStream ios, final int length) {
		try {
			String s;
			String a = "";
			for (int i = 0, j = 0; i < length; i++) {
				int val = ios.read();
				if (val < 0) {
					ps.flush();
					return;
				}
				if (j == 0) {
					ps.println("  " + a);
					a = "";
					s = Integer.toHexString(i);
					s = s + "      ".substring(s.length());
					ps.print(s + "  ");
				}
				if (j == 8)
					ps.print("  ");
				s = Integer.toHexString(val);
				if (s.length() < 2)
					s = "0" + s;
				ps.print(s + " ");
				if ((val > 32) && (val < 128))
					a += (char) val;
				else
					a += ' ';
				if (++j == 16)
					j = 0;
			}
			ps.println("");
			ps.flush();
			return;
		} catch (IOException e) {
			log_.log(Level.WARNING, e.getMessage(), e);
			//           Platform.getInstance().log(e);
		}
	}

}