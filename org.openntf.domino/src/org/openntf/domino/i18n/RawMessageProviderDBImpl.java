package org.openntf.domino.i18n;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.openntf.domino.Database;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;

/*----------------------------------------------------------------------------*/
public class RawMessageProviderDBImpl extends RawMessageProvider {

	private Database iDB;
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
		iDB = db;
		iLookupView = lookupView;
		iColBundle = colBundle;
		iColKey = colKey;
		iColLanguage = colLanguage;
		iColText = colText;
	}

	/*----------------------------------------------------------------------------*/
	private static final String lLotusDefaultLocale = "*";

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
		if (iDB == null)
			return;
		try {
			View v = iDB.getView(iLookupView);
			if (v == null)
				return;
			v.setAutoUpdate(false);
			ViewNavigator nav = v.createViewNav();
			for (ViewEntry ve = nav.getFirst(); ve != null; ve = nav.getNext())
				oneDBMsgText(ve);
		} catch (Throwable t) {
			t.printStackTrace();
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
			lang = lang.substring(0, 2) + "_" + lang.substring(3, 5);
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
