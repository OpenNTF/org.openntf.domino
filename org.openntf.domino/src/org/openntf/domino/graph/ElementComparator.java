/**
 * 
 */
package org.openntf.domino.graph;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.Logger;

import org.openntf.domino.DateTime;

import com.tinkerpop.blueprints.Element;

/**
 * @author nfreeman
 * 
 */
public class ElementComparator implements Comparator<Element>, Serializable {
	private static final Logger log_ = Logger.getLogger(ElementComparator.class.getName());
	private static final long serialVersionUID = 1L;

	private String[] props_;
	private IDominoProperties[] dProps_;
	private final boolean caseSensitive_;

	public ElementComparator(final IDominoProperties... props) {
		dProps_ = props;
		caseSensitive_ = false;
	}

	public ElementComparator(final String... props) {
		if (props == null || props.length == 0) {
			props_ = new String[0];
		} else {
			props_ = props;
		}
		caseSensitive_ = false;
	}

	public ElementComparator(final boolean caseSensitive, final String... props) {
		if (props == null || props.length == 0) {
			props_ = new String[0];
		} else {
			props_ = props;
		}
		caseSensitive_ = caseSensitive;
	}

	@Override
	public int compare(final Element arg0, final Element arg1) {
		int result = 0;
		if (dProps_ != null && dProps_.length > 0) {
			result = compareProps((IDominoElement) arg0, (IDominoElement) arg1);
		} else {
			result = compareStrs((IDominoElement) arg0, (IDominoElement) arg1);
		}
		return result;
	}

	private int compareProps(final IDominoElement arg0, final IDominoElement arg1) {
		int result = 0;

		for (IDominoProperties key : dProps_) {
			java.lang.Object v0 = DominoElement.getReflectiveProperty(arg0, key);
			java.lang.Object v1 = DominoElement.getReflectiveProperty(arg1, key);
			if (v0 == null && v1 == null) {
				result = 0;
			} else if (v0 == null) {
				return -1;
			} else if (v1 == null) {
				return 1;
			}
			if (v0 instanceof Number && v1 instanceof Number) {
				double d0 = ((Number) v0).doubleValue();
				double d1 = ((Number) v1).doubleValue();
				if (d0 > d1) {
					result = 1;
				} else if (d1 > d0) {
					result = -1;
				}
			} else if (v0 instanceof String && v1 instanceof String) {
				String s0 = (String) v0;
				String s1 = (String) v1;
				if (caseSensitive_) {
					result = s0.compareTo(s1);
				} else {
					result = s0.compareToIgnoreCase(s1);
				}
			} else if (v0 instanceof Date && v1 instanceof Date) {
				Date d0 = (Date) v0;
				Date d1 = (Date) v1;
				result = d0.compareTo(d1);
			} else if (v0 instanceof DateTime && v1 instanceof DateTime) {
				DateTime d0 = (DateTime) v0;
				DateTime d1 = (DateTime) v1;
				result = d0.compareTo(d1);
			} else if (v0 != null && v1 != null) {
				if (v1.getClass() == v0.getClass() && Comparable.class.isAssignableFrom(v0.getClass())) {
					result = ((Comparable) v0).compareTo((Comparable) v1);
				}
			}
			if (result != 0) {
				break;
			}
		}
		if (result == 0) {
			result = ((Comparable) arg0.getId()).compareTo(arg1.getId());
			//			if (result == 0) {
			//				System.out.println("Element comparator still ended up with match!??!");
			//				result = -1;
			//			}
		}
		return result;
	}

	private int compareStrs(final IDominoElement arg0, final IDominoElement arg1) {
		int result = 0;

		for (String key : props_) {
			java.lang.Object v0 = DominoElement.getReflectiveProperty(arg0, key);
			java.lang.Object v1 = DominoElement.getReflectiveProperty(arg1, key);
			if (v0 == null && v1 == null) {
				result = 0;
			} else if (v0 == null) {
				return -1;
			} else if (v1 == null) {
				return 1;
			}
			if (v0 instanceof Number && v1 instanceof Number) {
				double d0 = ((Number) v0).doubleValue();
				double d1 = ((Number) v1).doubleValue();
				if (d0 > d1) {
					result = 1;
				} else if (d1 > d0) {
					result = -1;
				}
			} else if (v0 instanceof String && v1 instanceof String) {
				String s0 = (String) v0;
				String s1 = (String) v1;
				if (caseSensitive_) {
					result = s0.compareTo(s1);
				} else {
					result = s0.compareToIgnoreCase(s1);
				}
			} else if (v0 instanceof Date && v1 instanceof Date) {
				Date d0 = (Date) v0;
				Date d1 = (Date) v1;
				result = d0.compareTo(d1);
			} else if (v0 instanceof DateTime && v1 instanceof DateTime) {
				DateTime d0 = (DateTime) v0;
				DateTime d1 = (DateTime) v1;
				result = d0.compareTo(d1);
			} else if (v0 != null && v1 != null) {
				Class<?> cl0 = v0.getClass();
				Class<?> cl1 = v1.getClass();
				if (v0 instanceof Comparable && v1 instanceof Comparable) {
					result = ((Comparable) v0).compareTo((Comparable) v1);
				}
			}
			if (result != 0) {
				break;
			}
		}
		if (result == 0) {
			result = ((Comparable) arg0.getId()).compareTo(arg1.getId());
			//			if (result == 0) {
			//				System.out.println("Element comparator still ended up with match!??!");
			//				result = -1;
			//			}
		}
		return result;
	}
}
