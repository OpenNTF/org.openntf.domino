package de.foconis.lsext;

/*----------------------------------------------------------------------------*/
import java.util.Comparator;
import java.util.TreeSet;

/*----------------------------------------------------------------------------*/
public class StringSet extends TreeSet<String> {

	private static final long serialVersionUID = -5701938083812020299L;

	protected boolean iCaseInsensitive = false;

	/*----------------------------------------------------------------------------*/
	public StringSet(final boolean caseInsensitive) {
		super(new Comparator<String>() {
			public int compare(final String s1, final String s2) {
				return caseInsensitive ? s1.compareToIgnoreCase(s2) : s1.compareTo(s2);
			}
		});
		iCaseInsensitive = caseInsensitive;
	}

	public StringSet() {
		super();
	}

	/*----------------------------------------------------------------------------*/
	public boolean isCaseInsensitive() {
		return iCaseInsensitive;
	}
	/*----------------------------------------------------------------------------*/
}
