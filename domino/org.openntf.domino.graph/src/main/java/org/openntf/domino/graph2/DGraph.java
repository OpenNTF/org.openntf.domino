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
package org.openntf.domino.graph2;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openntf.domino.DateTime;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@SuppressWarnings({ "rawtypes", "nls" })
public interface DGraph
		extends com.tinkerpop.blueprints.Graph, com.tinkerpop.blueprints.MetaGraph, com.tinkerpop.blueprints.TransactionalGraph {
	public static enum Utils {
		;

		public static String convertToFormula(final String key, final Object value) {
			String result = "";
			if (key == null) {
				return "";
			}
			if ("@".equals(key)) {
				return String.valueOf(value);
			}
			if (value instanceof Number) {
				result = key + "=" + value;
			} else if (value instanceof DateTime) {
				result = key + "=[" + ((DateTime) value).getLocalTime() + "]";
			} else if (value instanceof Enum<?>) {
				result = key + "=\"" + ((Enum<?>) value).getDeclaringClass().getName() + " " + ((Enum<?>) value).name() + "\"";
			} else {
				result = key + "=\"" + String.valueOf(value) + "\"";
			}
			return result;
		}

		public static String convertToPartialFormula(final String key, final Object value) {
			String result = "";
			if (key == null) {
				return "";
			}
			if ("@".equals(key)) {
				return String.valueOf(value);
			}
			String strValue = String.valueOf(value);
			if (value instanceof Enum<?>) {
				strValue = ((Enum<?>) value).getDeclaringClass().getName() + " " + ((Enum<?>) value).name();
			}
			result = "@Contains(@LowerCase(" + key + "); \"" + strValue.toLowerCase() + "\")";
			return result;
		}

		public static String convertToStartsFormula(final String key, final Object value) {
			String result = "";
			if (key == null) {
				return "";
			}
			if ("@".equals(key)) {
				return String.valueOf(value);
			}
			String strValue = String.valueOf(value);
			if (value instanceof Enum<?>) {
				strValue = ((Enum<?>) value).getDeclaringClass().getName() + " " + ((Enum<?>) value).name();
			}
			result = "@Begins(@LowerCase(" + key + "); \"" + strValue.toLowerCase() + "\")";
			return result;
		}

		//TODO make this more robust by using the TypeRegistry
		public static String getFormulaForFrame(final Class<?> kind) {
			String formname = kind.getSimpleName();
			TypeValue tv = kind.getAnnotation(TypeValue.class);
			if (tv != null) {
				formname = tv.value();
			}
			return "@LowerCase(Form) = \"" + formname.toLowerCase() + "\"";
		}

		//		public static String getFormulaForFrameName(final String classname) {
		//			return "Form =\"" + classname + "\"";
		//		}

		public static String getVertexFormula(final String key, final Object value) {
			String filterFormula = convertToFormula(key, value);
			return DVertex.FORMULA_FILTER + (filterFormula.length() > 0 ? " & " + filterFormula : "");
		}

		public static String getEdgeFormula(final String key, final Object value) {
			String filterFormula = convertToFormula(key, value);
			return DEdge.FORMULA_FILTER + (filterFormula.length() > 0 ? " & " + filterFormula : "");
		}

		public static String getElementFormula(final String key, final Object value) {
			String filterFormula = convertToFormula(key, value);
			return DElement.FORMULA_FILTER + (filterFormula.length() > 0 ? " & " + filterFormula : "");
		}

		public static String getFramedVertexFormula(final Class<?> kind) {
			return getFormulaForFrame(kind) + " & " + DVertex.FORMULA_FILTER;
		}

		public static String getFramedEdgeFormula(final Class<?> kind) {
			return getFormulaForFrame(kind) + " & " + DEdge.FORMULA_FILTER;
		}

		public static String getFramedElementFormula(final Class<?> kind) {
			return getFormulaForFrame(kind);
		}

		//		public static String getFramedElementFormula(final String classname) {
		//			return getFormulaForFrameName(classname);
		//		}

		public static String getFramedVertexFormula(final String key, final Object value, final Class<?> kind) {
			String filterFormula = convertToFormula(key, value);
			return getFormulaForFrame(kind) + " & " + DVertex.FORMULA_FILTER + (filterFormula.length() > 0 ? " & " + filterFormula : "");
		}

		public static String getFramedEdgeFormula(final String key, final Object value, final Class<?> kind) {
			String filterFormula = convertToFormula(key, value);
			return getFormulaForFrame(kind) + " & " + DEdge.FORMULA_FILTER + (filterFormula.length() > 0 ? " & " + filterFormula : "");
		}

		public static String getFramedElementFormula(final List<String> keys, final List<Object> values, final Class<?> kind) {
			String filterFormula = "";
			for (int i = 0; i < keys.size(); i++) {
				String key = keys.get(i);
				Object value = values.get(i);
				String curformula = convertToFormula(key, value);
				filterFormula = filterFormula + (filterFormula.length() > 0 ? " & " : "") + curformula;
			}
			return getFormulaForFrame(kind) + (filterFormula.length() > 0 ? " & " + filterFormula : "");
		}

		public static String getFramedElementPartialFormula(final List<String> keys, final List<Object> values, final Class<?> kind) {
			String filterFormula = "";
			for (int i = 0; i < keys.size(); i++) {
				String key = keys.get(i);
				Object value = values.get(i);
				String curformula = convertToPartialFormula(key, value);
				filterFormula = filterFormula + (filterFormula.length() > 0 ? " & " : "") + curformula;
			}
			return getFormulaForFrame(kind) + (filterFormula.length() > 0 ? " & " + filterFormula : "");
		}

		public static String getFramedElementStartsFormula(final List<String> keys, final List<Object> values, final Class<?> kind) {
			String filterFormula = "";
			for (int i = 0; i < keys.size(); i++) {
				String key = keys.get(i);
				Object value = values.get(i);
				String curformula = convertToStartsFormula(key, value);
				filterFormula = filterFormula + (filterFormula.length() > 0 ? " & " : "") + curformula;
			}
			return getFormulaForFrame(kind) + (filterFormula.length() > 0 ? " & " + filterFormula : "");
		}

		public static String getFramedVertexFormula(final List<String> keys, final List<Object> values, final Class<?> kind) {
			String filterFormula = "";
			for (int i = 0; i < keys.size(); i++) {
				String key = keys.get(i);
				Object value = values.get(i);
				String curformula = convertToFormula(key, value);
				filterFormula = filterFormula + (filterFormula.length() > 0 ? " & " : "") + curformula;
			}
			return getFormulaForFrame(kind) + " & " + DVertex.FORMULA_FILTER + (filterFormula.length() > 0 ? " & " + filterFormula : "");
		}

		public static String getFramedEdgeFormula(final List<String> keys, final List<Object> values, final Class<?> kind) {
			String filterFormula = "";
			for (int i = 0; i < keys.size(); i++) {
				String key = keys.get(i);
				Object value = values.get(i);
				String curformula = convertToFormula(key, value);
				filterFormula = filterFormula + (filterFormula.length() > 0 ? " & " : "") + curformula;
			}
			return getFormulaForFrame(kind) + " & " + DEdge.FORMULA_FILTER + (filterFormula.length() > 0 ? " & " + filterFormula : "");
		}

	}

	public void startTransaction(final Element elem);

	public Object findDelegate(Object delegateKey);

	public void removeDelegate(Element element);

	public Map<Long, DElementStore> getElementStores();

	public DElementStore findElementStore(Element element);

	public DElementStore findElementStore(Class<?> type);

	public DElementStore findElementStore(Object delegateKey);

	public Element getElement(Object id);

	public DElementStore getDefaultElementStore();

	public DEdgeList getEdgesFromIds(Vertex source, final Set<String> set);

	public DEdgeList getEdgesFromIds(Vertex source, final Set<String> set, final String... labels);

	public Object getStoreDelegate(DElementStore store);

	public Object getProxyStoreDelegate(DElementStore store);

	public Object getStoreDelegate(DElementStore store, Object provisionalKey);

	public Object getProxyStoreDelegate(DElementStore store, Object provisionalKey);

	public DKeyResolver getKeyResolver(Class<?> type);

	public void addKeyResolver(DKeyResolver keyResolver);

	public Graph getExtendedGraph();

	public void setExtendedGraph(Graph graph);

	public void flushCache();

	public void flushCache(final String id);

	public Vertex addVertex(Object id, boolean temporary);

}
