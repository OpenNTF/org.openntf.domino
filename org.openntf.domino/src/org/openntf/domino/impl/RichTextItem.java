package org.openntf.domino.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.DateTime;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.xml.sax.InputSource;

public class RichTextItem extends Base<org.openntf.domino.RichTextItem, lotus.domino.RichTextItem> implements
		org.openntf.domino.RichTextItem {

	public RichTextItem(lotus.domino.RichTextItem delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public String abstractText(int maxLen, boolean dropVowels, boolean userDict) {
		try {
			return getDelegate().abstractText(maxLen, dropVowels, userDict);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void addNewLine() {
		try {
			getDelegate().addNewLine();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void addNewLine(int count) {
		try {
			getDelegate().addNewLine(count);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void addNewLine(int count, boolean newParagraph) {
		try {
			getDelegate().addNewLine(count, newParagraph);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void addPageBreak() {
		try {
			getDelegate().addPageBreak();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void addPageBreak(lotus.domino.RichTextParagraphStyle pstyle) {
		try {
			getDelegate().addPageBreak((lotus.domino.RichTextParagraphStyle) toLotus(pstyle));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void addTab() {
		try {
			getDelegate().addTab();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void addTab(int count) {
		try {
			getDelegate().addTab(count);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void appendDocLink(lotus.domino.Database db) {
		try {
			getDelegate().appendDocLink((lotus.domino.Database) toLotus(db));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void appendDocLink(lotus.domino.Database db, String comment) {
		try {
			getDelegate().appendDocLink((lotus.domino.Database) db, comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void appendDocLink(lotus.domino.Database db, String comment, String hotspotText) {
		try {
			getDelegate().appendDocLink((lotus.domino.Database) db, comment, hotspotText);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void appendDocLink(lotus.domino.Document doc) {
		try {
			getDelegate().appendDocLink((lotus.domino.Document) toLotus(doc));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void appendDocLink(lotus.domino.Document doc, String comment) {
		try {
			getDelegate().appendDocLink((lotus.domino.Document) toLotus(doc), comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void appendDocLink(lotus.domino.Document doc, String comment, String hotspotText) {
		try {
			getDelegate().appendDocLink((lotus.domino.Document) toLotus(doc), comment, hotspotText);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void appendDocLink(lotus.domino.View view) {
		try {
			getDelegate().appendDocLink((lotus.domino.View) toLotus(view));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void appendDocLink(lotus.domino.View view, String comment) {
		try {
			getDelegate().appendDocLink((lotus.domino.View) toLotus(view), comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void appendDocLink(lotus.domino.View view, String comment, String hotspotText) {
		try {
			getDelegate().appendDocLink((lotus.domino.View) toLotus(view), hotspotText);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void appendParagraphStyle(lotus.domino.RichTextParagraphStyle pstyle) {
		try {
			getDelegate().appendParagraphStyle((lotus.domino.RichTextParagraphStyle) toLotus(pstyle));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void appendRTItem(lotus.domino.RichTextItem rtitem) {
		try {
			getDelegate().appendRTItem((lotus.domino.RichTextItem) toLotus(rtitem));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void appendStyle(lotus.domino.RichTextStyle rstyle) {
		try {
			getDelegate().appendStyle((lotus.domino.RichTextStyle) toLotus(rstyle));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void appendTable(int rows, int columns) {
		try {
			getDelegate().appendTable(rows, columns);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void appendTable(int rows, int columns, Vector labels) {
		try {
			getDelegate().appendTable(rows, columns, labels);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void appendTable(int rows, int columns, Vector labels, int leftMargin, Vector pstyles) {
		try {
			getDelegate().appendTable(rows, columns, labels, leftMargin, toLotus(pstyles));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void appendText(String text) {
		try {
			getDelegate().appendText(text);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void appendToTextList(String value) {
		try {
			getDelegate().appendToTextList(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void appendToTextList(Vector values) {
		try {
			getDelegate().appendToTextList(values);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void beginInsert(lotus.domino.Base element) {
		try {
			getDelegate().beginInsert(toLotus(element));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void beginInsert(lotus.domino.Base element, boolean after) {
		try {
			getDelegate().beginInsert(toLotus(element), after);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void beginSection(String title) {
		try {
			getDelegate().beginSection(title);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void beginSection(String title, lotus.domino.RichTextStyle titleStyle) {
		try {
			getDelegate().beginSection(title, (lotus.domino.RichTextStyle) toLotus(titleStyle));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void beginSection(String title, lotus.domino.RichTextStyle titleStyle, lotus.domino.ColorObject barColor, boolean expand) {
		try {
			getDelegate().beginSection(title, (lotus.domino.RichTextStyle) toLotus(titleStyle),
					(lotus.domino.ColorObject) toLotus(barColor), expand);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void compact() {
		try {
			getDelegate().compact();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public boolean containsValue(Object value) {
		try {
			if (value instanceof lotus.domino.Base) {
				return getDelegate().containsValue(toLotus((lotus.domino.Base) value));
			}
			return getDelegate().containsValue(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public Item copyItemToDocument(lotus.domino.Document doc) {
		try {
			return Factory.fromLotus(getDelegate().copyItemToDocument((lotus.domino.Document) toLotus(doc)), Item.class,
					(org.openntf.domino.Document) doc);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Item copyItemToDocument(lotus.domino.Document doc, String newName) {
		try {
			return Factory.fromLotus(getDelegate().copyItemToDocument((lotus.domino.Document) toLotus(doc), newName), Item.class,
					(org.openntf.domino.Document) doc);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public RichTextNavigator createNavigator() {
		try {
			return Factory.fromLotus(getDelegate().createNavigator(), RichTextNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public org.openntf.domino.RichTextRange createRange() {
		try {
			return Factory.fromLotus(getDelegate().createRange(), org.openntf.domino.RichTextRange.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public EmbeddedObject embedObject(int type, String className, String source, String name) {
		try {
			return Factory.fromLotus(getDelegate().embedObject(type, className, source, name), EmbeddedObject.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void endInsert() {
		try {
			getDelegate().endInsert();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void endSection() {
		try {
			getDelegate().endSection();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public DateTime getDateTimeValue() {
		try {
			return Factory.fromLotus(getDelegate().getDateTimeValue(), DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public EmbeddedObject getEmbeddedObject(String name) {
		try {
			return Factory.fromLotus(getDelegate().getEmbeddedObject(name), EmbeddedObject.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Vector<org.openntf.domino.EmbeddedObject> getEmbeddedObjects() {
		try {
			return Factory.fromLotusAsVector(getDelegate().getEmbeddedObjects(), org.openntf.domino.EmbeddedObject.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getFormattedText(boolean tabStrip, int lineLen, int maxLen) {
		try {
			return getDelegate().getFormattedText(tabStrip, lineLen, maxLen);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public InputSource getInputSource() {
		try {
			return getDelegate().getInputSource();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public InputStream getInputStream() {
		try {
			return getDelegate().getInputStream();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public DateTime getLastModified() {
		try {
			return Factory.fromLotus(getDelegate().getLastModified(), DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public MIMEEntity getMIMEEntity() {
		try {
			return Factory.fromLotus(getDelegate().getMIMEEntity(), MIMEEntity.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getName() {
		try {
			return getDelegate().getName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getNotesFont(String faceName, boolean addOnFail) {
		try {
			return getDelegate().getNotesFont(faceName, addOnFail);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public Document getParent() {
		try {
			return Factory.fromLotus(getDelegate().getParent(), Document.class, this);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	@Override
	public Reader getReader() {
		try {
			return getDelegate().getReader();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getText() {
		try {
			return getDelegate().getText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getText(int maxLen) {
		try {
			return getDelegate().getText(maxLen);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getType() {
		try {
			return getDelegate().getType();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public String getUnformattedText() {
		try {
			return getDelegate().getUnformattedText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Object getValueCustomData() throws IOException, ClassNotFoundException {
		try {
			return getDelegate().getValueCustomData();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Object getValueCustomData(String dataTypeName) throws IOException, ClassNotFoundException {
		try {
			return getDelegate().getValueCustomData(dataTypeName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public byte[] getValueCustomDataBytes(String dataTypeName) throws IOException {
		try {
			return getDelegate().getValueCustomDataBytes(dataTypeName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Vector<org.openntf.domino.DateTime> getValueDateTimeArray() {
		try {
			return Factory.fromLotusAsVector(getDelegate().getValueDateTimeArray(), org.openntf.domino.DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public double getValueDouble() {
		try {
			return getDelegate().getValueDouble();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0d;
		}
	}

	@Override
	public int getValueInteger() {
		try {
			return getDelegate().getValueInteger();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getValueLength() {
		try {
			return getDelegate().getValueLength();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public String getValueString() {
		try {
			return getDelegate().getValueString();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Vector<Object> getValues() {
		try {
			Vector<Object> result = new Vector<Object>();
			for (Object value : getDelegate().getValues()) {
				if (value instanceof lotus.domino.DateTime) {
					result.add(Factory.fromLotus((lotus.domino.DateTime) value, DateTime.class, this));
				} else if (value instanceof lotus.domino.DateRange) {
					result.add(Factory.fromLotus((lotus.domino.DateRange) value, DateRange.class, this));
				} else {
					result.add(value);
				}
			}
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public boolean isAuthors() {
		try {
			return getDelegate().isAuthors();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isEncrypted() {
		try {
			return getDelegate().isEncrypted();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isNames() {
		try {
			return getDelegate().isNames();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isProtected() {
		try {
			return getDelegate().isProtected();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isReaders() {
		try {
			return getDelegate().isReaders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isSaveToDisk() {
		try {
			return getDelegate().isSaveToDisk();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isSigned() {
		try {
			return getDelegate().isSigned();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isSummary() {
		try {
			return getDelegate().isSummary();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public org.w3c.dom.Document parseXML(boolean validate) throws IOException {
		try {
			return getDelegate().parseXML(validate);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void remove() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setAuthors(boolean flag) {
		try {
			getDelegate().setAuthors(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setDateTimeValue(lotus.domino.DateTime dateTime) {
		try {
			getDelegate().setDateTimeValue((lotus.domino.DateTime) toLotus(dateTime));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setEncrypted(boolean flag) {
		try {
			getDelegate().setEncrypted(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setNames(boolean flag) {
		try {
			getDelegate().setNames(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setProtected(boolean flag) {
		try {
			getDelegate().setProtected(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setReaders(boolean flag) {
		try {
			getDelegate().setReaders(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setSaveToDisk(boolean flag) {
		try {
			getDelegate().setReaders(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setSigned(boolean flag) {
		try {
			getDelegate().setSigned(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setSummary(boolean flag) {
		try {
			getDelegate().setSummary(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setValueCustomData(Object userObj) throws IOException {
		try {
			getDelegate().setValueCustomData(userObj);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setValueCustomData(String dataTypeName, Object userObj) throws IOException {
		try {
			getDelegate().setValueCustomData(dataTypeName, userObj);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setValueCustomDataBytes(String dataTypeName, byte[] byteArray) throws IOException {
		try {
			getDelegate().setValueCustomData(dataTypeName, byteArray);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setValueDouble(double value) {
		try {
			getDelegate().setValueDouble(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setValueInteger(int value) {
		try {
			getDelegate().setValueInteger(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setValueString(String value) {
		try {
			getDelegate().setValueString(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValues(java.util.Vector values) {
		try {
			java.util.Vector<Object> result = new java.util.Vector<Object>(values.size());
			for (Object value : values) {
				if (value instanceof org.openntf.domino.DateTime) {
					result.add(toLotus((org.openntf.domino.DateTime) value));
				} else if (value instanceof org.openntf.domino.DateRange) {
					result.add(toLotus((org.openntf.domino.DateRange) value));
				} else {
					result.add(value);
				}
			}
			getDelegate().setValues(result);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void transformXML(Object style, lotus.domino.XSLTResultTarget result) {
		try {
			getDelegate().transformXML(style, result);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void update() {
		try {
			getDelegate().update();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
