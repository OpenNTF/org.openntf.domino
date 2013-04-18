/**
 * 
 */
package org.openntf.domino.helpers;

import java.util.Comparator;
import java.util.logging.Logger;

import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.Item;

/**
 * @author nfreeman
 * 
 *         Copyright Michael Zischeck and licensed under Apache License 2.0 from http://in-mood.blogspot.com/
 * 
 */
public class DocumentComparator implements Comparator<Document> {
	private static final Logger log_ = Logger.getLogger(DocumentComparator.class.getName());
	private static final long serialVersionUID = 1L;

	String[] sortFields = null;

	public DocumentComparator(String... sortFields) {
		this.sortFields = sortFields;
	}

	public int compare(Document doc1, Document doc2) {

		int compared = 0;
		// loop all sortFields
		for (String field : sortFields) {
			Item item1 = doc1.getFirstItem(field);
			Item item2 = doc2.getFirstItem(field);
			switch (item1.getType()) {
			case Item.TEXT:
			case Item.AUTHORS:
			case Item.NAMES:
			case Item.READERS:
				String val1 = doc1.getItemValueString(field);
				String val2 = doc2.getItemValueString(field);
				compared = val1.compareTo(val2);
				if (0 != compared) {
					return compared;
				}
				break;
			case Item.NUMBERS:
				Double d1 = doc1.getItemValueDouble(field);
				Double d2 = doc2.getItemValueDouble(field);
				compared = d1.compareTo(d2);
				if (0 != compared) {
					return compared;
				}
				break;

			case Item.DATETIMES:

				DateTime dt1 = item1.getDateTimeValue();
				DateTime dt2 = item2.getDateTimeValue();
				compared = dt2.timeDifference(dt1);
				if (0 != compared) {
					return compared;
				}
				break;
			}

		}
		return 0;
	}
}
