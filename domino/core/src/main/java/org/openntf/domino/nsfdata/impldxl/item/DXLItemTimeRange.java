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
package org.openntf.domino.nsfdata.impldxl.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openntf.domino.nsfdata.NSFDateRange;
import org.openntf.domino.nsfdata.NSFDateTime;
import org.openntf.domino.nsfdata.NSFDateTimeValue;
import org.openntf.domino.utils.xml.XMLNode;
import org.openntf.domino.utils.xml.XMLNodeList;

public class DXLItemTimeRange extends AbstractDXLItem {
	private static final long serialVersionUID = 1L;

	private NSFDateTime[] dateTimeValues_;
	private NSFDateRange[] dateRangeValues_;

	protected DXLItemTimeRange(final XMLNode node, final int dupItemId) {
		super(node, dupItemId);

		XMLNodeList dateTimeNodes = node.selectNodes("./datetimelist/datetime"); //$NON-NLS-1$
		dateTimeValues_ = new NSFDateTime[dateTimeNodes.size()];
		for(int i = 0; i < dateTimeNodes.size(); i++) {
			dateTimeValues_[i] = DXLItemFactory.createDateTime(dateTimeNodes.get(i));
		}

		XMLNodeList dateRangeNodes = node.selectNodes("./datetimelist/datetimepair"); //$NON-NLS-1$
		dateRangeValues_ = new NSFDateRange[dateRangeNodes.size()];
		for(int i = 0; i < dateRangeNodes.size(); i++) {
			XMLNode startNode = dateRangeNodes.get(i).getFirstChildElement();
			NSFDateTime start = DXLItemFactory.createDateTime(startNode);
			XMLNode endNode = startNode.getNextSiblingElement();
			NSFDateTime end = DXLItemFactory.createDateTime(endNode);
			dateRangeValues_[i] = new NSFDateRange(start, end);
		}
	}

	@Override
	public List<NSFDateTimeValue> getValue() {
		List<NSFDateTimeValue> result = new ArrayList<NSFDateTimeValue>();
		Collections.addAll(result, dateTimeValues_);
		Collections.addAll(result, dateRangeValues_);
		return Collections.unmodifiableList(result);
	}

	@Override
	public Type getType() {
		return Type.TIME_RANGE;
	}

	@Override
	public byte[] getBytes() {
		throw new UnsupportedOperationException();
	}

}
