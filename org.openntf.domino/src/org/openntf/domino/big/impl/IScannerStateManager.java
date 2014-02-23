package org.openntf.domino.big.impl;

import java.util.Date;
import java.util.Map;
import java.util.Observer;
import java.util.Set;

import org.openntf.domino.helpers.DocumentScanner;
import org.openntf.domino.types.CaseInsensitiveString;

public interface IScannerStateManager extends Observer {
	public enum ScanStatus {
		NEW, RUNNING, ERROR, INTERRUPTED, COMPLETE;
	}

	public Map<CaseInsensitiveString, Set<String>> restoreTokenLocationMap(CaseInsensitiveString token, Object mapKey);

	public void saveTokenLocationMap(CaseInsensitiveString token, Object mapKey, Map<CaseInsensitiveString, Set<String>> map);

	public void saveTokenLocationMap(Object mapKey, Map<CaseInsensitiveString, Map<CaseInsensitiveString, Set<String>>> fullMap,
			DocumentScanner scanner);

	public Date getLastIndexDate(Object mapKey);

	public void setLastIndexDate(Object mapKey, Date date);

}
