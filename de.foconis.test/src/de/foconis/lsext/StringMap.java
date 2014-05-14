package de.foconis.lsext;

/*----------------------------------------------------------------------------*/
import java.util.Comparator;
import java.util.TreeMap;

/*----------------------------------------------------------------------------*/
public class StringMap<V> extends TreeMap<String, V> {

	private static final long serialVersionUID = -5701938083812020294L;

	protected boolean iCaseInsensitive = false;

	/*----------------------------------------------------------------------------*/
	public StringMap(final boolean caseInsensitive) {
		super(new Comparator<String>() {
			public int compare(final String s1, final String s2) {
				return caseInsensitive ? s1.compareToIgnoreCase(s2) : s1.compareTo(s2);
			}
		});
		iCaseInsensitive = caseInsensitive;
	}

	public StringMap() {
		super();
	}

	/*----------------------------------------------------------------------------*/
	public boolean isCaseInsensitive() {
		return iCaseInsensitive;
	}
	/*----------------------------------------------------------------------------*/
}
