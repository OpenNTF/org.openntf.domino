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
package org.openntf.domino.i18n;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

/*----------------------------------------------------------------------------*/
@SuppressWarnings("nls")
public class RawMessageProviderDBImpl extends RawMessageProvider {

	private Database iDB;
	private String iDBServer;
	private String iDBPath;
	private String iLookupView;
	private int iColBundle;
	private int iColKey;
	private int iColLanguage;
	private int iColText;
	/*----------------------------------------------------------------------------*/
	private Map<String, Map<String, Map<String, String>>> iMsgCache = null;

	// Bundle          - Locale - Key          - Text
	// de.foconis.test - de     - Common.text1 - "Irgendein Text"
	/*----------------------------------------------------------------------------*/

	protected RawMessageProviderDBImpl(final Database db, final String lookupView, final int colBundle, final int colKey,
			final int colLanguage, final int colText) {
		if ((iDB = db) != null) {
			iDBServer = iDB.getServer();
			iDBPath = iDB.getFilePath();
		}
		iLookupView = lookupView;
		iColBundle = colBundle;
		iColKey = colKey;
		iColLanguage = colLanguage;
		iColText = colText;
	}

	/*----------------------------------------------------------------------------*/
	private static final String lLotusDefaultLocale = "*"; //$NON-NLS-1$

	@Override
	public String getRawText(final String bundleName, final String key, final Locale loc) {
		if (iMsgCache == null)
			buildMsgCache();
		Map<String, Map<String, String>> cmBundle = iMsgCache.get(bundleName);
		if (cmBundle == null)
			return null;
		String localeString = loc.toString();
		if (localeString.length() > 5)
			localeString = localeString.substring(0, 5);
		Map<String, String> map4Loc;
		int lh;
		while ((lh = localeString.length()) >= 2) {
			String ret;
			if ((map4Loc = cmBundle.get(localeString)) != null && (ret = map4Loc.get(key)) != null)
				return ret;
			if (lh <= 2)
				break;
			localeString = localeString.substring(0, 2);
		}
		return ((map4Loc = cmBundle.get(lLotusDefaultLocale)) == null) ? null : map4Loc.get(key);
	}

	/*----------------------------------------------------------------------------*/
	private void buildMsgCache() {
		iMsgCache = new TreeMap<String, Map<String, Map<String, String>>>();
		/*
		 * To avoid PANIC-s from WrapperFactory.recacheLotusObject (when buildMsgCache is called again),
		 * this was partly modified 
		 */
		try {
			if (iDB == null) {
				if (iDBPath == null)
					return;
				Session s = Factory.getSession_unchecked(SessionType.CURRENT);
				if (s == null || (iDB = s.getDatabase(iDBServer, iDBPath)) == null) {
					System.err.println("RawMessageProviderDBImpl: Can't open DB " + iDBServer + "!!" + iDBPath);
					return;
				}
			}
			View v = iDB.getView(iLookupView);
			if (v == null)
				return;
			v.setAutoUpdate(false);
			ViewNavigator nav = v.createViewNav();
			for (ViewEntry ve = nav.getFirst(); ve != null; ve = nav.getNext())
				oneDBMsgText(ve);
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			iDB = null;
		}
	}

	/*----------------------------------------------------------------------------*/
	private void oneDBMsgText(final ViewEntry ve) {
		Vector<Object> cols = ve.getColumnValues();
		String bundle, lang, key, text;
		if ((bundle = getVEString(cols, iColBundle)) == null || (lang = getVEString(cols, iColLanguage)) == null)
			return;
		if ((key = getVEString(cols, iColKey)) == null || (text = getVEString(cols, iColText)) == null)
			return;
		int lh = lang.length();
		if (lh >= 5)
			lang = lang.substring(0, 2) + "_" + lang.substring(3, 5); //$NON-NLS-1$
		else if (lh >= 2)
			lang = lang.substring(0, 2);
		Map<String, Map<String, String>> cmBundle = iMsgCache.get(bundle);
		if (cmBundle == null) {
			cmBundle = new TreeMap<String, Map<String, String>>();
			iMsgCache.put(bundle, cmBundle);
		}
		Map<String, String> cmLang = cmBundle.get(lang);
		if (cmLang == null) {
			cmLang = new TreeMap<String, String>();
			cmBundle.put(lang, cmLang);
		}
		cmLang.put(key, text);
	}

	/*----------------------------------------------------------------------------*/
	private String getVEString(final Vector<Object> cols, final int viewColNum) {
		Object o = cols.get(viewColNum);
		return o instanceof String ? (String) o : null;
	}

	/*----------------------------------------------------------------------------*/
	@Override
	public void resetCache() {
		iMsgCache = null;
	}
	/*----------------------------------------------------------------------------*/
}
