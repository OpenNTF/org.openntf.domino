package org.openntf.domino.big.impl;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.openntf.domino.types.CaseInsensitiveString;

public interface IScannerStateManager {

	public Map<CaseInsensitiveString, Set<String>> restoreTokenLocationMap(CaseInsensitiveString token, Object mapKey);

	public void saveTokenLocationMap(CaseInsensitiveString token, Object mapKey, Map<CaseInsensitiveString, Set<String>> map);

	public void saveTokenLocationMap(Object mapKey, Map<CaseInsensitiveString, Map<CaseInsensitiveString, Set<String>>> fullMap,
			Date lastTimestamp);

}
