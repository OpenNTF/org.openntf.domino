package org.openntf.domino.graph2.builtin;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.Logger;

import org.openntf.domino.DateTime;
import org.openntf.domino.graph.ElementComparator;

/**
 * @author withersp
 *
 */
public class DVertexFrameComparator implements Comparator<DVertexFrame>, Serializable {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(ElementComparator.class.getName());
	private static final long serialVersionUID = 1L;

	private String[] props_;
	private final boolean caseSensitive_;

	public DVertexFrameComparator(final String... props) {
		if (props == null || props.length == 0) {
			props_ = new String[0];
		} else {
			props_ = props;
		}
		caseSensitive_ = false;
	}

	public DVertexFrameComparator(final boolean caseSensitive, final String... props) {
		if (props == null || props.length == 0) {
			props_ = new String[0];
		} else {
			props_ = props;
		}
		caseSensitive_ = caseSensitive;
	}

	@Override
	public int compare(final DVertexFrame o1, final DVertexFrame o2) {
		int result = 0;
		result = compareStrs(o1, o2);
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private int compareStrs(final DVertexFrame arg0, final DVertexFrame arg1) {
		int result = 0;

		if (!arg0.getClass().equals(arg1.getClass())) {
			return -1;
		}

		for (String key : props_) {
			// First try to get a property
			if (arg0.asMap().containsKey(key)) {
				java.lang.Object v0 = arg0.asMap().get(key);
				java.lang.Object v1 = arg1.asMap().get(key);
				result = compareObjects(v0, v1);
			} else if (arg0.asVertex().getPropertyKeys().contains(key)) {
				java.lang.Object v0 = arg0.asVertex().getProperty(key);
				java.lang.Object v1 = arg1.asVertex().getProperty(key);
				result = compareObjects(v0, v1);
			} else {
				// Try get + key
				Method[] meths = arg0.getClass().getDeclaredMethods();
				for (Method crystal : meths) {
					if (crystal.getName().equalsIgnoreCase("get" + key)) {
						try {
							java.lang.Object v0 = crystal.invoke(arg0, null);
							java.lang.Object v1 = crystal.invoke(arg1, null);
							result = compareObjects(v0, v1);
						} catch (IllegalAccessException e) {
							// TODO: handle exception
						} catch (InvocationTargetException e) {
							// TODO: handle exception
						}
						break;
					}
				}
				// Final fallback is getProperty()
				java.lang.Object v0 = arg0.asVertex().getProperty(key);
				java.lang.Object v1 = arg1.asVertex().getProperty(key);
				result = compareObjects(v0, v1);
			}
			if (result != 0) {
				break;
			}
		}
		if (result == 0) {
			result = ((Comparable) arg0.asVertex().getId()).compareTo(arg1.asVertex().getId());
			//			if (result == 0) {
			//				System.out.println("Element comparator still ended up with match!??!");
			//				result = -1;
			//			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private int compareObjects(final Object arg0, final Object arg1) {
		int result = 0;
		if (arg0 == null && arg1 == null) {
			result = 0;
		} else if (arg0 == null) {
			return -1;
		} else if (arg1 == null) {
			return 1;
		}
		if (arg0 instanceof Number && arg1 instanceof Number) {
			double d0 = ((Number) arg0).doubleValue();
			double d1 = ((Number) arg1).doubleValue();
			if (d0 > d1) {
				result = 1;
			} else if (d1 > d0) {
				result = -1;
			}
		} else if (arg0 instanceof String && arg1 instanceof String) {
			String s0 = (String) arg0;
			String s1 = (String) arg1;
			if (caseSensitive_) {
				result = s0.compareTo(s1);
			} else {
				result = s0.compareToIgnoreCase(s1);
			}
		} else if (arg0 instanceof Date && arg1 instanceof Date) {
			Date d0 = (Date) arg0;
			Date d1 = (Date) arg1;
			result = d0.compareTo(d1);
		} else if (arg0 instanceof DateTime && arg1 instanceof DateTime) {
			DateTime d0 = (DateTime) arg0;
			DateTime d1 = (DateTime) arg1;
			result = d0.compareTo(d1);
		} else if (arg0 != null && arg1 != null) {
			if (arg0 instanceof Comparable && arg1 instanceof Comparable) {
				result = ((Comparable<Object>) arg0).compareTo(arg1);
			}
		}
		return result;
	}
}
