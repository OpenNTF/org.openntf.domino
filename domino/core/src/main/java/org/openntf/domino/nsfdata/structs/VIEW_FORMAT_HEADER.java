package org.openntf.domino.nsfdata.structs;

/**
 * This defines the structure of the header member of the VIEW_TABLE_FORMAT structure. The VIEW_TABLE_FORMAT structure is part of a
 * $VIEWFORMAT item (also known as a "View Table Format" item), which is an item in all view notes. (viewfmt.h)
 * 
 * @since forever
 */
public class VIEW_FORMAT_HEADER extends AbstractStruct {
	/**
	 * VIEW_STYLE_xxx. The ViewStyle property jumps from 0 to 16 in order to allow a quick check of table vs. calendar by masking against
	 * VIEW_CLASS_MASK
	 */
	public static enum Style {
		TABLE, UNUSED1, UNUSED2, UNUSED3, UNUSED4, UNUSED5, UNUSED6, UNUSED7, UNUSED8, UNUSED9, UNUSED10, UNUSED11, UNUSED12, UNUSED13,
		UNUSED14, UNUSED15, CALENDAR, CALENDAR_DAY, CALENDAR_WEEK, CALENDAR_MONTH
	}

	public static final byte VIEW_FORMAT_VERSION = 1;
	public static final byte VIEW_CLASS_MASK = (byte) 0xF0;

	public final Unsigned8 Version = new Unsigned8();
	public final Enum8<Style> ViewStyle = new Enum8<Style>(Style.values());

	public boolean isCalendar() {
		Style style = ViewStyle.get();
		switch (style) {
		case CALENDAR:
		case CALENDAR_DAY:
		case CALENDAR_WEEK:
		case CALENDAR_MONTH:
			return true;
		default:
			return false;
		}
	}
}
