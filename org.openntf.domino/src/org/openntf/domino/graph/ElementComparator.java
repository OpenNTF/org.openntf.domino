/**
 * 
 */
package org.openntf.domino.graph;

import java.util.Comparator;
import java.util.Date;
import java.util.logging.Logger;

import org.openntf.domino.DateTime;

import com.tinkerpop.blueprints.Element;

/**
 * @author nfreeman
 * 
 */
public class ElementComparator implements Comparator<Element> {
	private static final Logger log_ = Logger.getLogger(ElementComparator.class.getName());
	private static final long serialVersionUID = 1L;

	private String[] props_;

	public ElementComparator(final String... props) {
		if (props == null || props.length == 0) {
			props_ = new String[0];
		} else {
			props_ = props;
		}
	}

	@Override
	public int compare(final Element arg0, final Element arg1) {
		int result = 0;
		if (props_.length > 0) {
			for (String key : props_) {
				java.lang.Object v0 = arg0.getProperty(key);
				java.lang.Object v1 = arg1.getProperty(key);
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
					result = s0.compareTo(s1);
				} else if (v0 instanceof Date && v1 instanceof Date) {
					Date d0 = (Date) v0;
					Date d1 = (Date) v1;
					result = d0.compareTo(d1);
				} else if (v0 instanceof DateTime && v1 instanceof DateTime) {
					DateTime d0 = (DateTime) v0;
					DateTime d1 = (DateTime) v1;
					result = d0.compareTo(d1);
				} else {
					if (v1.getClass() == v0.getClass() && Comparable.class.isAssignableFrom(v0.getClass())) {
						result = ((Comparable) v0).compareTo((Comparable) v1);
					}
				}
				if (result != 0) {
					break;
				}
			}
		}
		return result;
	}
}
