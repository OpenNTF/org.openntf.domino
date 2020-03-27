/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

	//	public void saveTokenLocationMap(CharSequence token, Object mapKey, Map<CharSequence, Set<CharSequence>> map);

	public void saveTokenLocationMap(Object mapKey, Map<CharSequence, Map<CharSequence, Set<CharSequence>>> fullMap,
			DocumentScanner scanner, ScanStatus status, long doccount, long docstoprocess);

	public Map<CharSequence, Set<CharSequence>> restoreNameLocationMap(CharSequence name, Object mapKey);

	//	public void saveNameLocationMap(CharSequence name, Object mapKey, Map<CharSequence, Set<CharSequence>> map);

	public void saveNameLocationMap(Object mapKey, Map<CharSequence, Map<CharSequence, Set<CharSequence>>> fullMap, DocumentScanner scanner,
			ScanStatus status, long doccount, long docstoprocess);

	public Map<CharSequence, Set<CharSequence>> restoreValueLocationMap(CharSequence value, Object mapKey);

	public Map<CharSequence, CharSequence> restoreRichTextLocationMap(CharSequence value, Object mapKey);

	//	public void saveValueLocationMap(CharSequence value, Object mapKey, Map<CharSequence, Set<CharSequence>> map);

	public void saveValueLocationMap(Object mapKey, Map<CharSequence, Map<CharSequence, Set<CharSequence>>> fullMap,
			DocumentScanner scanner, ScanStatus status, long doccount, long docstoprocess);

	public void saveRichTextLocationMap(Object mapKey, Map<CharSequence, Map<CharSequence, CharSequence>> fullMap, DocumentScanner scanner,
			ScanStatus status, long doccount, long docstoprocess);

	public Date getLastIndexDate(Object mapKey);

	public void setLastIndexDate(Object mapKey, Date date, ScanStatus status, long doccount, long docstoprocess);

}
