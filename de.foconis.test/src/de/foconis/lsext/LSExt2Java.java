package de.foconis.lsext;

/*----------------------------------------------------------------------------*/
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.openntf.formula.DateTime;
import org.openntf.formula.Formatter;
import org.openntf.formula.impl.FormatterImpl;

/*----------------------------------------------------------------------------*/
public class LSExt2Java {
	private String iLSExtStr;
	private int iLSExtStrLh;

	/*----------------------------------------------------------------------------*/
	public LSExt2Java(final String lsExtStr) {
		iLSExtStr = lsExtStr;
		iLSExtStrLh = lsExtStr.length();
	}

	/*----------------------------------------------------------------------------*/
	private ArrayList<String> iStringList;
	private ArrayList<Object> iObjectList;
	private int iActPos = 0;
	private Formatter iFormatter = null;

	/*----------------------------------------------------------------------------*/
	public Object toJavaObj() {
		String start = LSExt2JavaConsts.HEADER;
		if (!iLSExtStr.startsWith(start))
			throwExc("LS stream doesn't start with " + start);
		iActPos = start.length();
		assureAvailable(8);
		String hexVersion = iLSExtStr.substring(iActPos, iActPos + 8);
		long version = parseHexStr(hexVersion);
		if (version != LSExt2JavaConsts.VERSION)
			throwExc("Can't convert old LS ext stream version " + version);
		iActPos += 8;
		assureAvailable(1);
		if (iLSExtStr.charAt(iActPos++) != '$')
			throwExc("'$' expected after hex version");
		iStringList = new ArrayList<String>(256);
		iObjectList = new ArrayList<Object>(256);
		Object ret = toJavaObjRec();
		iStringList = null;
		iObjectList = null;
		if (iActPos != iLSExtStrLh)
			throwExc("End of stream expected");
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	private Object toJavaObjRec() {
		String start = readString();
		if (start.isEmpty()) // STRING
			return toCache(readString());
		if (start.equals("N"))
			return null;
		String focIF = null;
		if (start.charAt(0) == ':') {
			focIF = start.substring(1);
			start = readString();
		}
		if (start.equals("@")) {
			int vInd = readInteger();
			if (vInd >= iObjectList.size())
				throwExc("Reference to Object " + vInd + " can't be satisfied");
			return iObjectList.get(vInd);
		}
		if (start.startsWith(LSExt2JavaConsts.BOX_PREFIX)) // Some Box
			return toCache(fromBox(start));
		if (start.equals(LSExt2JavaConsts.ARRAYLIST))
			return fromArrayList(focIF); // Cache before cache List elements!
		int strMapCase = -1;
		if (start.equals(LSExt2JavaConsts.STRINGMAP))
			strMapCase = 0;
		else if (start.equals(LSExt2JavaConsts.STRINGMAP_IC))
			strMapCase = 1;
		if (strMapCase >= 0)
			return fromStringMap(strMapCase == 1, focIF); // Cache before cache
		// Map elements!
		throwExc("Foc-Class " + start + " not yet supported");
		return null;
	}

	/*----------------------------------------------------------------------------*/
	private Object fromBox(final String focClass) {
		int boxType = readInteger();
		if (boxType == LSExt2JavaConsts.INTEGER || boxType == LSExt2JavaConsts.LONG)
			return fromBoxLong();
		if (boxType == LSExt2JavaConsts.DOUBLE || boxType == LSExt2JavaConsts.SINGLE)
			return fromBoxDouble();
		if (boxType == LSExt2JavaConsts.DATE)
			return fromBoxDate();
		throwExc("FocBox-Class " + focClass + " (type=" + boxType + ") not yet supported");
		return null;
	}

	/*----------------------------------------------------------------------------*/
	private Long fromBoxLong() {
		return new Long(readLong());
	}

	/*----------------------------------------------------------------------------*/
	private Double fromBoxDouble() {
		String dStr = readString();
		try {
			return Double.valueOf(dStr);
		} catch (NumberFormatException ne) {
			throwExc("Error converting " + dStr + " to Double: " + ne.getMessage());
		}
		return null;
	}

	/*----------------------------------------------------------------------------*/
	private DateTime fromBoxDate() {
		int year = readInteger();
		int month = readInteger();
		int day = readInteger();
		int hour = readInteger();
		int minute = readInteger();
		int second = readInteger();
		if (iFormatter == null)
			iFormatter = new FormatterImpl(Locale.getDefault());
		DateTime ret = iFormatter.getNewSDTInstance();
		ret.setLocalDate(year, month, day);
		ret.setLocalTime(hour, minute, second, 0);
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	private List<Object> fromArrayList(final String focIF) {
		int listVersion = readInteger();
		if (listVersion != LSExt2JavaConsts.ARRAYLIST_VERSION)
			throwExc("FocArrayList version " + listVersion + " is not supported");
		int modCount = readInteger();
		int capacity = readInteger();
		int capacityIncrement = readInteger();
		int elementCount = readInteger();
		LSFocArrayList ret = new LSFocArrayList(elementCount, modCount, capacity, capacityIncrement, focIF);
		toCache(ret);
		for (int i = 0; i < elementCount; i++)
			ret.add(toJavaObjRec());
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	private Map<String, Object> fromStringMap(final boolean caseInsensitive, final String focIF) {
		int mapVersion = readInteger();
		if (mapVersion != LSExt2JavaConsts.STRINGMAP_VERSION)
			throwExc("FocStringMap version " + mapVersion + " is not supported");
		int modCount = readInteger();
		int elementCount = readInteger();
		LSFocStringMap ret = new LSFocStringMap(caseInsensitive, modCount, focIF);
		toCache(ret);
		for (int i = 0; i < elementCount; i++) {
			String key = readString();
			ret.put(key, toJavaObjRec());
		}
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	private Object toCache(final Object what) {
		iObjectList.add(what);
		return what;
	}

	/*----------------------------------------------------------------------------*/
	private String readString() {
		int sLh = readInteger();
		if (sLh == 0)
			return "";
		if (sLh < 0) {
			int vInd = (-sLh) - 1;
			if (vInd >= iStringList.size())
				throwExc("Reference to String " + sLh + " can't be satisfied");
			return iStringList.get(vInd);
		}
		assureAvailable(sLh);
		String ret = iLSExtStr.substring(iActPos, iActPos + sLh);
		iActPos += sLh;
		if (sLh >= LSExt2JavaConsts.MINCACHESTRLH && sLh <= LSExt2JavaConsts.MAXCACHESTRLH)
			iStringList.add(ret);
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	private long readLong() {
		assureAvailable(1);
		char firstSign = iLSExtStr.charAt(iActPos++);
		boolean negative;
		if (firstSign == '-') {
			negative = true;
			assureAvailable(1);
			firstSign = iLSExtStr.charAt(iActPos++);
		} else
			negative = false;
		if (firstSign == '!' || firstSign == '\'' || firstSign == '&') {
			if (negative)
				throwExc("Invalid number format: " + '-' + firstSign);
			if (firstSign == '!')
				return -1;
			if (firstSign == '\'')
				return LSExt2JavaConsts.MIN_LS_INTEGER;
			return LSExt2JavaConsts.MIN_LS_LONG;
		}
		int needBytes;
		long ret;
		if (firstSign == '$')
			needBytes = 2;
		else if (firstSign == '%')
			needBytes = 4;
		else if (firstSign == '#')
			needBytes = 8;
		else
			needBytes = 0;
		if (needBytes == 0) {
			int aux = firstSign - '0';
			if (aux < 0 || aux > LSExt2JavaConsts.MAX_INT_1BYTE)
				throwExc("Invalid 1-character number: " + firstSign);
			ret = aux;
		} else {
			assureAvailable(needBytes);
			String hexStr = iLSExtStr.substring(iActPos, iActPos + needBytes);
			ret = parseHexStr(hexStr);
			iActPos += needBytes;
		}
		return negative ? -ret : ret;
	}

	/*----------------------------------------------------------------------------*/
	private int readInteger() {
		return (int) readLong();
	}

	/*----------------------------------------------------------------------------*/
	private void assureAvailable(final int howMany) {
		if (iActPos + howMany > iLSExtStrLh)
			throwExc("No more " + howMany + " byte(s) available in LS stream");
	}

	/*----------------------------------------------------------------------------*/
	private int parseHexStr(final String hexStr) {
		try {
			return Integer.parseInt(hexStr, 16);
		} catch (NumberFormatException ne) {
			throwExc("Error converting hex string " + hexStr + ": " + ne.getMessage());
		}
		return -1;
	}

	/*----------------------------------------------------------------------------*/
	private void throwExc(final String cause) {
		throw new LSExtConvertException(iActPos, cause);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * Testunterstuetzung: Dump des Ergebnis-Objekts
	 */
	/*----------------------------------------------------------------------------*/
	private PrintStream iDumpPS = null;

	public void dumpObject(final PrintStream ps, final Object o) {
		iDumpPS = ps;
		dumpObject(o, 0);
	}

	private void dumpObject(final Object o, final int level) {
		printLevelPrefix(level);
		if (o == null) {
			iDumpPS.println("*** NULL ***");
			return;
		}
		if (o instanceof List) {
			List<?> l = (List<?>) o;
			int sz = l.size();
			int modCount = -1;
			int capacity = -1;
			int capacityIncrement = -1;
			String focIF = null;
			if (o instanceof LSFocArrayList) {
				modCount = ((LSFocArrayList) o).getModCount();
				capacity = ((LSFocArrayList) o).getCapacity();
				capacityIncrement = ((LSFocArrayList) o).getCapacityIncrement();
				focIF = ((LSFocArrayList) o).getFocIF();
			}
			iDumpPS.println("LIST, size=" + sz + " FocIF=" + focIF + ", MC=" + modCount + "/CAP=" + capacity + "/CAPI=" + capacityIncrement);
			for (int i = 0; i < sz; i++) {
				printLevelPrefix(level + 1);
				iDumpPS.println("List element " + (i + 1));
				dumpObject(l.get(i), level + 2);
			}
			return;
		}
		if (o instanceof Map) {
			Map<?, ?> m = (Map<?, ?>) o;
			int sz = m.size();
			int modCount = -1;
			boolean caseInsensitive = false;
			String focIF = null;
			if (o instanceof LSFocStringMap) {
				modCount = ((LSFocStringMap) o).getModCount();
				caseInsensitive = ((LSFocStringMap) o).isCaseInsensitive();
				focIF = ((LSFocStringMap) o).getFocIF();
			}
			iDumpPS.println("MAP, size=" + sz + ", CaseInsensitive=" + caseInsensitive + ", FocIF=" + focIF + ", MC=" + modCount);
			int i = 0;
			Set<?> s = m.entrySet();
			for (Object so : s) {
				Map.Entry<?, ?> me = (Map.Entry<?, ?>) so;
				printLevelPrefix(level + 1);
				iDumpPS.println("Map element " + (i + 1) + ", Key=" + me.getKey());
				dumpObject(me.getValue(), level + 2);
				i++;
			}
			return;
		}
		if (o instanceof String) {
			String s = (String) o;
			iDumpPS.println("(String)=" + s);
			return;
		}
		if (o instanceof Long) {
			Long l = (Long) o;
			iDumpPS.println("(Long)=" + l.toString());
			return;
		}
		if (o instanceof Double) {
			Double d = (Double) o;
			iDumpPS.println("(Double)=" + d.toString());
			return;
		}
		if (o instanceof DateTime) {
			DateTime d = (DateTime) o;
			iDumpPS.println("(DateTime)=" + d.toString());
			return;
		}
		throw new LSExtConvertException("Unsupported Java type: " + o.getClass().getName());
	}

	private void printLevelPrefix(int i) {
		while (i-- != 0)
			iDumpPS.print("  ");
	}

	/*----------------------------------------------------------------------------*/
}
