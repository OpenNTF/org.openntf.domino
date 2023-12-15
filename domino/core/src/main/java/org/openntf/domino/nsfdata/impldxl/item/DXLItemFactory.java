/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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
package org.openntf.domino.nsfdata.impldxl.item;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openntf.domino.nsfdata.NSFDateTime;
import org.openntf.domino.nsfdata.NSFItem;
import org.openntf.domino.nsfdata.NSFItem.Type;
import org.openntf.domino.utils.xml.XMLNode;

@SuppressWarnings("nls")
public enum DXLItemFactory {
	;

	// TODO determine how Domino handles fractional offsets
	private static final Pattern DATE_TIME = Pattern
			.compile("^(\\d\\d\\d\\d)(\\d\\d)(\\d\\d)T(\\d\\d)(\\d\\d)(\\d\\d),(\\d\\d)(\\+|-)(\\d\\d)$"); //$NON-NLS-1$
	private static final Pattern DATE_ONLY = Pattern.compile("^(\\d\\d\\d\\d)(\\d\\d)(\\d\\d)$"); //$NON-NLS-1$
	private static final Pattern TIME_ONLY = Pattern.compile("^T(\\d\\d)(\\d\\d)(\\d\\d),(\\d\\d)$"); //$NON-NLS-1$

	public static NSFItem create(final XMLNode node, final int dupItemId) {
		if (node.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE || !"item".equals(node.getNodeName())) { //$NON-NLS-1$
			throw new IllegalArgumentException("Node is not an item node");
		}

		XMLNode dataNode = node.getFirstChildElement();
		if (dataNode == null) {
			// If this happens, the DXL is invalid
			throw new IllegalArgumentException("Item node does not have a data element");
		}

		String dataNodeName = dataNode.getNodeName();
		if ("rawitemdata".equals(dataNodeName)) { //$NON-NLS-1$
			return DXLItemRaw.create(node, dupItemId);
		} else if ("text".equals(dataNodeName)) { //$NON-NLS-1$
			return new DXLItemText(node, dupItemId);
		} else if ("textlist".equals(dataNodeName)) { //$NON-NLS-1$
			return new DXLItemTextList(node, dupItemId);
		} else if ("number".equals(dataNodeName)) { //$NON-NLS-1$
			// the value may be @Error
			XMLNode numberNode = node.selectSingleNode("./number"); //$NON-NLS-1$
			if (numberNode.getText().equals("@ERROR")) { //$NON-NLS-1$
				return new DXLItemError(node, dupItemId, Type.NUMBER);
			} else {
				return new DXLItemNumber(node, dupItemId);
			}
		} else if ("numberlist".equals(dataNodeName)) { //$NON-NLS-1$
			return new DXLItemNumberRange(node, dupItemId);
		} else if ("datetime".equals(dataNodeName)) { //$NON-NLS-1$
			return new DXLItemTime(node, dupItemId);
		} else if ("datetimelist".equals(dataNodeName)) { //$NON-NLS-1$
			return new DXLItemTimeRange(node, dupItemId);
		} else if ("formula".equals(dataNodeName)) { //$NON-NLS-1$
			return new DXLItemFormula(node, dupItemId);
		} else if ("object".equals(dataNodeName)) { //$NON-NLS-1$
			return DXLItemObject.create(node, dupItemId);
		} else {
			throw new UnsupportedOperationException("Unknown data type " + dataNodeName);
		}
	}

	protected static NSFDateTime createDateTime(final XMLNode node) {
		boolean dst = "true".equals(node.getAttribute("dst")); //$NON-NLS-1$ //$NON-NLS-2$
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
			int offset = Integer.parseInt(matcher.group(9), 10) * ("+".equals(plusMinus) ? 1 : -1); //$NON-NLS-1$

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
			LocalDate date = LocalDate.ofInstant(cal.toInstant(), ZoneId.systemDefault());
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
			LocalTime time = LocalTime.ofInstant(cal.toInstant(), ZoneId.systemDefault());
			return new NSFDateTime(time);
		}
		throw new IllegalArgumentException("Unable to parse datetime: " + text);
	}
}
