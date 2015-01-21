package org.openntf.domino.graph2;

import java.util.Map;
import java.util.Set;

import org.openntf.domino.DateTime;
import org.openntf.domino.graph2.impl.DEdgeList;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("rawtypes")
public interface DGraph extends com.tinkerpop.blueprints.Graph, com.tinkerpop.blueprints.MetaGraph,
com.tinkerpop.blueprints.TransactionalGraph {
	public static enum Utils {
		;

		public static String convertToFormula(final String key, final Object value) {
			String result = "";
			if (key == null)
				return "";
			if (value instanceof Number) {
				result = key + "=" + value;
			} else if (value instanceof DateTime) {
				result = key + "=[" + ((DateTime) value).getLocalTime() + "]";
			} else {
				result = key + "=\"" + String.valueOf(value) + "\"";
			}
			return result;
		}

		public static String getFormulaForFrame(final Class<?> kind) {
			String classname = kind.getSimpleName();
			return "Form =\"" + classname + "\"";
		}

		public static String getVertexFormula(final String key, final Object value) {
			String filterFormula = convertToFormula(key, value);
			return DVertex.FORMULA_FILTER + (filterFormula.length() > 0 ? " & " + filterFormula : "");
		}

		public static String getEdgeFormula(final String key, final Object value) {
			String filterFormula = convertToFormula(key, value);
			return DEdge.FORMULA_FILTER + (filterFormula.length() > 0 ? " & " + filterFormula : "");
		}

		public static String getFramedVertexFormula(final Class<?> kind) {
			return getFormulaForFrame(kind) + " & " + DVertex.FORMULA_FILTER;
		}

		public static String getFramedEdgeFormula(final Class<?> kind) {
			return getFormulaForFrame(kind) + " & " + DEdge.FORMULA_FILTER;
		}

		public static String getFramedVertexFormula(final String key, final Object value, final Class<?> kind) {
			String filterFormula = convertToFormula(key, value);
			return getFormulaForFrame(kind) + " & " + DVertex.FORMULA_FILTER + (filterFormula.length() > 0 ? " & " + filterFormula : "");
		}

		public static String getFramedEdgeFormula(final String key, final Object value, final Class<?> kind) {
			String filterFormula = convertToFormula(key, value);
			return getFormulaForFrame(kind) + " & " + DEdge.FORMULA_FILTER + (filterFormula.length() > 0 ? " & " + filterFormula : "");
		}
	}

	public void startTransaction(final Element elem);

	public Map<String, Object> findDelegate(Object delegateKey);

	public void removeDelegate(Element element);

	public Map<Long, DElementStore> getElementStores();

	public DElementStore findElementStore(Element element);

	public DElementStore findElementStore(Class<?> type);

	public DElementStore findElementStore(Object delegateKey);

	public DElementStore getDefaultElementStore();

	public DEdgeList getEdgesFromIds(Vertex source, final Set<String> set);

	public DEdgeList getEdgesFromIds(Vertex source, final Set<String> set, final String... labels);

	public Object getStoreDelegate(DElementStore store);

	public Object getProxyStoreDelegate(DElementStore store);

	public Object getStoreDelegate(DElementStore store, Object provisionalKey);

	public Object getProxyStoreDelegate(DElementStore store, Object provisionalKey);

}
