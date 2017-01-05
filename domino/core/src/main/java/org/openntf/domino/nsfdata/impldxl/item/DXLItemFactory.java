package org.openntf.domino.nsfdata.impldxl.item;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openntf.domino.nsfdata.NSFDateTime;
import org.openntf.domino.nsfdata.NSFItem;
import org.openntf.domino.nsfdata.NSFItem.Type;
import org.openntf.domino.utils.xml.XMLNode;

public enum DXLItemFactory {
	;

	// TODO determine how Domino handles fractional offsets
	private static final Pattern DATE_TIME = Pattern
			.compile("^(\\d\\d\\d\\d)(\\d\\d)(\\d\\d)T(\\d\\d)(\\d\\d)(\\d\\d),(\\d\\d)(\\+|-)(\\d\\d)$");
	private static final Pattern DATE_ONLY = Pattern.compile("^(\\d\\d\\d\\d)(\\d\\d)(\\d\\d)$");
	private static final Pattern TIME_ONLY = Pattern.compile("^T(\\d\\d)(\\d\\d)(\\d\\d),(\\d\\d)$");

	public static NSFItem create(final XMLNode node, final int dupItemId) {
		if (node.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE || !"item".equals(node.getNodeName())) {
			throw new IllegalArgumentException("Node is not an item node");
		}

		XMLNode dataNode = node.getFirstChildElement();
		if (dataNode == null) {
			// If this happens, the DXL is invalid
			throw new IllegalArgumentException("Item node does not have a data element");
		}

		String dataNodeName = dataNode.getNodeName();
		if ("rawitemdata".equals(dataNodeName)) {
			return DXLItemRaw.create(node, dupItemId);
		} else if ("text".equals(dataNodeName)) {
			return new DXLItemText(node, dupItemId);
		} else if ("textlist".equals(dataNodeName)) {
			return new DXLItemTextList(node, dupItemId);
		} else if ("number".equals(dataNodeName)) {
			// the value may be @Error
			XMLNode numberNode = node.selectSingleNode("./number");
			if (numberNode.getText().equals("@ERROR")) {
				return new DXLItemError(node, dupItemId, Type.NUMBER);
			} else {
				return new DXLItemNumber(node, dupItemId);
			}
		} else if ("numberlist".equals(dataNodeName)) {
			return new DXLItemNumberRange(node, dupItemId);
		} else if ("datetime".equals(dataNodeName)) {
			return new DXLItemTime(node, dupItemId);
		} else if ("datetimelist".equals(dataNodeName)) {
			return new DXLItemTimeRange(node, dupItemId);
		} else if ("formula".equals(dataNodeName)) {
			return new DXLItemFormula(node, dupItemId);
		} else if ("object".equals(dataNodeName)) {
			return DXLItemObject.create(node, dupItemId);
		} else {
			throw new UnsupportedOperationException("Unknown data type " + dataNodeName);
		}
	}

	protected static NSFDateTime createDateTime(final XMLNode node) {
		boolean dst = "true".equals(node.getAttribute("dst"));
		String text = node.getText();

		Matcher matcher = DATE_TIME.matcher(text);
		if (matcher.matches()) {
			int year = Integer.parseInt(matcher.group(1), 10);
			int month = Integer.parseInt(matcher.group(2), 10);
			int day = Integer.parseInt(matcher.group(3), 10);
			int hours = Integer.parseInt(matcher.group(4), 10);
			int minutes = Integer.parseInt(matcher.group(5), 10);
			int seconds = Integer.parseInt(matcher.group(6), 10);
			int millis = Integer.parseInt(matcher.group(7), 10) * 10;

			String plusMinus = matcher.group(8);
			int offset = Integer.parseInt(matcher.group(9), 10) * ("+".equals(plusMinus) ? 1 : -1);

			Calendar cal = Calendar.getInstance();
			cal.set(year, month, day, hours, minutes, seconds);
			cal.add(Calendar.HOUR, offset);
			cal.set(Calendar.MILLISECOND, millis);

			return new NSFDateTime(cal.getTime(), 0, dst);
		}
		matcher = DATE_ONLY.matcher(text);
		if (matcher.matches()) {
			int year = Integer.parseInt(matcher.group(1), 10);
			int month = Integer.parseInt(matcher.group(2), 10);
			int day = Integer.parseInt(matcher.group(3), 10);

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(0);
			cal.set(year, month, day);
			java.sql.Date date = new java.sql.Date(cal.getTimeInMillis());
			return new NSFDateTime(date);
		}
		matcher = TIME_ONLY.matcher(text);
		if (matcher.matches()) {
			int hours = Integer.parseInt(matcher.group(1), 10);
			int minutes = Integer.parseInt(matcher.group(2), 10);
			int seconds = Integer.parseInt(matcher.group(3), 10);
			int millis = Integer.parseInt(matcher.group(4), 10) * 10;

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(0);
			cal.set(Calendar.HOUR_OF_DAY, hours);
			cal.set(Calendar.MINUTE, minutes);
			cal.set(Calendar.SECOND, seconds);
			cal.set(Calendar.MILLISECOND, millis);
			java.sql.Time time = new java.sql.Time(cal.getTimeInMillis());
			return new NSFDateTime(time);
		}
		throw new IllegalArgumentException("Unable to parse datetime: " + text);
	}
}
