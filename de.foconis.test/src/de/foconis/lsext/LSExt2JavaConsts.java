package de.foconis.lsext;

public class LSExt2JavaConsts {
	public static final String HEADER = "$FOCONIS Object Stream$";
	public static final int VERSION = 3;
	public static final String VERSION_STR = "00000003";

	public static final int MINCACHESTRLH = 4;
	public static final int MAXCACHESTRLH = 128;
	public static final int MAXSTRCACHESIZE = 8000;

	public static final int LIST = 1;
	public static final int ARRAY = 2;
	public static final int LONG = 3;
	public static final int INTEGER = 4;
	public static final int STRING = 5;
	public static final int SINGLE = 6;
	public static final int DOUBLE = 7;
	public static final int CURRENCY = 8;
	public static final int DATE = 9;
	public static final int BOOLEAN = 10;
	public static final int NULL = 11;
	public static final int EMPTY = 12;
	public static final int NOTHING = 13;
	public static final int OTHER = 14;

	public static final long MIN_LS_INTEGER = -32768;
	public static final long MAX_LS_INTEGER = 32767;
	public static final long MIN_LS_LONG = -2147483648;
	public static final long MAX_LS_LONG = 2147483647;

	public static final long MAX_INT_1BYTE = 78;
	public static final long MAX_INT_2BYTE = 255;
	public static final long MAX_INT_4BYTE = 65535;

	public static final String ARRAYLIST = "..collection.impl.FocArrayList";
	public static final String STRINGMAP = "..collection.impl.FocStringMap";
	public static final String STRINGMAP_IC = "..collection.impl.FocCaseInsensitiveStringMap";

	public static final int ARRAYLIST_VERSION = 2;
	public static final int STRINGMAP_VERSION = 1;

	public static final String BOX_PREFIX = "..box.";
	public static final String BOX_INTEGER = "..box.FocInteger";
	public static final String BOX_FLOAT = "..box.FocFloat";
	public static final String BOX_DATETIME = "..box.FocDate";
	public static final String BOX_STRING = "";
}
