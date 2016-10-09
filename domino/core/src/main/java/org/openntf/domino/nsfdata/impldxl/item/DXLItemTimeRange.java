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

		XMLNodeList dateTimeNodes = node.selectNodes("./datetimelist/datetime");
		dateTimeValues_ = new NSFDateTime[dateTimeNodes.size()];
		for(int i = 0; i < dateTimeNodes.size(); i++) {
			dateTimeValues_[i] = DXLItemFactory.createDateTime(dateTimeNodes.get(i));
		}

		XMLNodeList dateRangeNodes = node.selectNodes("./datetimelist/datetimepair");
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
