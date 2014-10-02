package org.openntf.domino.big.impl;

import java.util.Date;
import java.util.Map;
import java.util.Observer;
import java.util.Set;

import org.openntf.domino.helpers.DocumentScanner;

public interface IScannerStateManager extends Observer {
	public enum ScanStatus {
		NEW, RUNNING, ERROR, INTERRUPTED, COMPLETE;
	}

	public Map<CharSequence, Set<CharSequence>> restoreTokenLocationMap(CharSequence token, Object mapKey);

	public void saveTokenLocationMap(CharSequence token, Object mapKey, Map<CharSequence, Set<CharSequence>> map);

	public void saveTokenLocationMap(Object mapKey, Map<CharSequence, Map<CharSequence, Set<CharSequence>>> fullMap, DocumentScanner scanner);

	public Map<CharSequence, Set<CharSequence>> restoreNameLocationMap(CharSequence name, Object mapKey);

	public void saveNameLocationMap(CharSequence name, Object mapKey, Map<CharSequence, Set<CharSequence>> map);

	public void saveNameLocationMap(Object mapKey, Map<CharSequence, Map<CharSequence, Set<CharSequence>>> fullMap, DocumentScanner scanner);

	public Date getLastIndexDate(Object mapKey);

	public void setLastIndexDate(Object mapKey, Date date);

}
