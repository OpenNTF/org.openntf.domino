package de.foconis.lsext;

import java.util.Set;

import org.openntf.formula.impl.StringMap;
import org.openntf.formula.impl.StringSet;

/*----------------------------------------------------------------------------*/
public class LSFocStringMap extends StringMap<Object> {

	private static final long serialVersionUID = -5701938083812020297L;

	private int iModCount = 1;
	private String iFocIF = null;
	private StringSet iWrittenKeys = null;
	private StringSet iRemovedKeys = null;

	/*----------------------------------------------------------------------------*/
	public LSFocStringMap(final boolean caseInsensitive, final int modCount, final String focIF) {
		super(caseInsensitive);
		iModCount = modCount;
		iFocIF = focIF;
	}

	public LSFocStringMap() {
		super();
	}

	public LSFocStringMap(final boolean caseInsensitive) {
		super(caseInsensitive);
	}

	/*----------------------------------------------------------------------------*/
	public int getModCount() {
		return iModCount;
	}

	public String getFocIF() {
		return iFocIF;
	}

	/*----------------------------------------------------------------------------*/
	public void activateChangeControl() {
		iWrittenKeys = new StringSet(iCaseInsensitive);
		iRemovedKeys = new StringSet(iCaseInsensitive);
	}

	public Set<String> getWrittenKeys() {
		return iWrittenKeys;
	}

	public Set<String> getRemovedKeys() {
		return iRemovedKeys;
	}

	/*----------------------------------------------------------------------------*/
	@Override
	public Object put(final String key, final Object value) {
		if (iWrittenKeys != null)
			iWrittenKeys.add(key);
		return super.put(key, value);
	}

	/*----------------------------------------------------------------------------*/
	@Override
	public Object remove(final Object key) {
		if (iRemovedKeys != null && containsKey(key) && key instanceof String)
			iRemovedKeys.add((String) key);
		return super.remove(key);
	}
	/*----------------------------------------------------------------------------*/
}
