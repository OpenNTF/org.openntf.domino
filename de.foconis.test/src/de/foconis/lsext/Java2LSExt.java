package de.foconis.lsext;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.openntf.formula.DateTime;
import org.openntf.formula.Formatter;
import org.openntf.formula.impl.FormatterImpl;
import org.openntf.formula.impl.StringMap;

public class Java2LSExt {

	private Object iJavaObj;
	private StringBuilder iSB;
	private Map<String, Integer> iStringList;
	private Map<Object, Integer> iObjectList;
	private Formatter iFormatter = null;

	/*----------------------------------------------------------------------------*/
	public Java2LSExt(final Object o) {
		iJavaObj = o;
	}

	/*----------------------------------------------------------------------------*/
	public String toLSExt() {
		iSB = new StringBuilder(1024);
		iSB.append(LSExt2JavaConsts.HEADER);
		iSB.append(LSExt2JavaConsts.VERSION_STR);
		iSB.append('$');
		iStringList = new HashMap<String, Integer>(256);
		iObjectList = new HashMap<Object, Integer>(256);
		toLSExt(iJavaObj);
		iStringList = null;
		iObjectList = null;
		return iSB.toString();
	}

	/*----------------------------------------------------------------------------*/
	private void toLSExt(final Object o) {
		if (o == null) {
			writeString("N");
			return;
		}
		int modCount = 0;
		int capacity = 128;
		int capacityIncrement = 128;
		String focIF = null;
		if (o instanceof LSFocArrayList) {
			modCount = ((LSFocArrayList) o).getModCount();
			capacity = ((LSFocArrayList) o).getCapacity();
			capacityIncrement = ((LSFocArrayList) o).getCapacityIncrement();
			focIF = ((LSFocArrayList) o).getFocIF();
		} else if (o instanceof LSFocStringMap) {
			modCount = ((LSFocStringMap) o).getModCount();
			focIF = ((LSFocStringMap) o).getFocIF();
		}
		if (focIF != null)
			writeString(":" + focIF);
		Integer i = iObjectList.get(o);
		if (i != null) {
			writeString("@");
			writeLong(i);
			return;
		}
		iObjectList.put(o, iObjectList.size());
		if (o instanceof CharSequence)
			writeCharSequence((CharSequence) o);
		else if (o instanceof Number)
			writeNumber((Number) o);
		else if (o instanceof Boolean)
			writeNumber((Boolean) o ? -1 : 0);
		else if (o instanceof DateTime)
			writeDateTime(((DateTime) o).toJavaDate());
		else if (o instanceof Date)
			writeDateTime((Date) o);
		else if (o instanceof List)
			writeList((List<?>) o, modCount, capacity, capacityIncrement);
		else if (o instanceof Map)
			writeMap((Map<?, ?>) o, modCount);
		else
			cantSer4LS("Java class " + o.getClass().getName());
	}

	/*----------------------------------------------------------------------------*/
	private void writeCharSequence(final CharSequence cs) {
		writeString(LSExt2JavaConsts.BOX_STRING);
		writeString(cs.toString());
	}

	/*----------------------------------------------------------------------------*/
	private void writeNumber(final Number n) {
		long l = 0;
		double d = 0;
		boolean caseLong = true;
		if (n instanceof Long || n instanceof Integer || n instanceof Short || n instanceof Byte)
			l = n.longValue();
		else if (n instanceof Float || n instanceof Double) {
			caseLong = false;
			d = n.doubleValue();
		} else
			throwExc("Unsupported Number class: " + n.getClass().getName());
		if (caseLong && (l < LSExt2JavaConsts.MIN_LS_LONG || l > LSExt2JavaConsts.MAX_LS_LONG)) {
			caseLong = false;
			d = l;
		}
		if (caseLong) {
			writeString(LSExt2JavaConsts.BOX_INTEGER);
			writeLong(LSExt2JavaConsts.LONG);
			writeLong(l);
		} else {
			writeString(LSExt2JavaConsts.BOX_FLOAT);
			writeLong(LSExt2JavaConsts.DOUBLE);
			if (iFormatter == null)
				iFormatter = new FormatterImpl(Locale.getDefault());
			writeString(iFormatter.formatNumber(d).replace(',', '.'));
		}
	}

	/*----------------------------------------------------------------------------*/
	private void writeDateTime(final Date jDate) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(jDate);
		writeString(LSExt2JavaConsts.BOX_DATETIME);
		writeLong(LSExt2JavaConsts.DATE);
		writeLong(cal.get(Calendar.YEAR));
		writeLong(cal.get(Calendar.MONTH) + 1);
		writeLong(cal.get(Calendar.DAY_OF_MONTH));
		writeLong(cal.get(Calendar.HOUR_OF_DAY));
		writeLong(cal.get(Calendar.MINUTE));
		writeLong(cal.get(Calendar.SECOND));
	}

	/*----------------------------------------------------------------------------*/
	private void writeList(final List<?> l, final int modCount, final int capacity, final int capacityIncrement) {
		writeString(LSExt2JavaConsts.ARRAYLIST);
		writeLong(LSExt2JavaConsts.ARRAYLIST_VERSION);
		writeLong(modCount);
		writeLong(capacity);
		writeLong(capacityIncrement);
		int sz = l.size();
		writeLong(sz);
		for (int i = 0; i < sz; i++)
			toLSExt(l.get(i));
	}

	/*----------------------------------------------------------------------------*/
	private void writeMap(final Map<?, ?> m, final int modCount) {
		boolean caseInsensitive = false;
		if (m instanceof StringMap)
			caseInsensitive = ((StringMap<?>) m).isCaseInsensitive();
		writeString(caseInsensitive ? LSExt2JavaConsts.STRINGMAP_IC : LSExt2JavaConsts.STRINGMAP);
		writeLong(LSExt2JavaConsts.STRINGMAP_VERSION);
		writeLong(modCount);
		writeLong(m.size());
		Set<?> s = m.entrySet();
		for (Object so : s) {
			Map.Entry<?, ?> me = (Map.Entry<?, ?>) so;
			Object key = me.getKey();
			if (!(key instanceof CharSequence))
				cantSer4LS("Map contains a key of class " + key.getClass().getName());
			String k = ((CharSequence) key).toString();
			if (caseInsensitive)
				k = k.toLowerCase();
			writeString(k);
			toLSExt(me.getValue());
		}
	}

	/*----------------------------------------------------------------------------*/
	private void writeString(final String s) {
		int lh = s.length();
		if (lh >= LSExt2JavaConsts.MINCACHESTRLH && lh <= LSExt2JavaConsts.MAXCACHESTRLH) {
			Integer i = iStringList.get(s);
			if (i != null)
				lh = -i - 1;
			else {
				int sz = iStringList.size();
				if (sz <= LSExt2JavaConsts.MAXSTRCACHESIZE)
					iStringList.put(s, sz);
			}
		}
		writeLong(lh);
		if (lh > 0)
			iSB.append(s);
	}

	/*----------------------------------------------------------------------------*/
	private void writeLong(long l) {
		if (l < LSExt2JavaConsts.MIN_LS_LONG || l > LSExt2JavaConsts.MAX_LS_LONG)
			cantSer4LS("Long " + l);
		char toAppend;
		if (l < 0) {
			toAppend = (l == -1) ? '!' : (l == LSExt2JavaConsts.MIN_LS_INTEGER) ? '\'' : (l == LSExt2JavaConsts.MIN_LS_LONG) ? '&' : '-';
			iSB.append(toAppend);
			if (toAppend != '-')
				return;
			l = -l;
		}
		toAppend = (l <= LSExt2JavaConsts.MAX_INT_1BYTE) ? (char) ('0' + l) : (l <= LSExt2JavaConsts.MAX_INT_2BYTE) ? '$'
				: (l <= LSExt2JavaConsts.MAX_INT_4BYTE) ? '%' : '#';
		iSB.append(toAppend);
		if (l > LSExt2JavaConsts.MAX_INT_1BYTE)
			writeLongHex(l);
	}

	/*----------------------------------------------------------------------------*/
	private void writeLongHex(final long l) {
		String hexStr = Long.toHexString(l).toUpperCase();
		int lh = hexStr.length();
		if (lh > 4)
			lh = 8 - lh;
		else if (lh > 2)
			lh = 4 - lh;
		else
			lh = 2 - lh;
		while (lh-- > 0)
			iSB.append('0');
		iSB.append(hexStr);
	}

	/*----------------------------------------------------------------------------*/
	private void cantSer4LS(final String what) {
		throwExc("Can't serialize for LotusScript: " + what);
	}

	/*----------------------------------------------------------------------------*/
	private void throwExc(final String cause) {
		throw new LSExtConvertException(cause);
	}
	/*----------------------------------------------------------------------------*/
}
