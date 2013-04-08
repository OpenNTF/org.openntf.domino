package org.openntf.domino.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;

import com.tinkerpop.blueprints.Element;

public abstract class DominoElement implements Element, Serializable {
	private static final long serialVersionUID = 1L;
	public static final String TYPE_FIELD = "_RPD_GRAPHTYPE";
	transient org.openntf.domino.Document doc_;
	// private String filepath_;
	private String key_;
	transient DominoGraph parent_;
	// private String server_;
	private String unid_;

	public DominoElement(DominoGraph parent, Document doc) {
		doc_ = doc;
		parent_ = parent;
		unid_ = doc.getUniversalID();
	}

	public void addProperty(String propertyName, Object value) {
		setProperty(propertyName, value);
	}

	private Database getDatabase() {
		return getParent().getRawDatabase();
	}

	public Document getRawDocument() {
		if (doc_ == null) {
			doc_ = getDocument();
		}
		return doc_;
	}

	public int incrementProperty(String propertyName) {
		int result = getProperty(propertyName, Integer.class);
		setProperty(propertyName, ++result);
		return result;
	}

	public int decrementProperty(String propertyName) {
		int result = getProperty(propertyName, Integer.class);
		setProperty(propertyName, --result);
		return result;
	}

	private Document getDocument() {
		// TODO NTF - use createOnFail for database
		return getDatabase().getDocumentByUNID(unid_);
	}

	@Override
	public String getId() {
		if (key_ == null) {
			key_ = unid_;
		}
		return key_;
	}

	public DominoGraph getParent() {
		return parent_;
	}

	public boolean hasProperty(String key) {
		return getPropertyKeys().contains(key);
	}

	@Override
	public <T> T getProperty(String key) {
		return getProperty(key, Object.class);
	}

	@SuppressWarnings("unchecked")
	public <T> T getProperty(String propertyName, Class<?> T) {
		T result = null;
		Item i = null;

		Session session = getParent().getRawSession();
		boolean convertMime = session.isConvertMIME();
		session.setConvertMIME(false);
		if (getRawDocument().hasItem(propertyName)) {
			// TODO - NTF deal with MIMEBean scenario. Should delegate as much as possible to the implementation...
			if (T.isAssignableFrom(String.class)) {
				result = (T) getRawDocument().getItemValue(propertyName).get(0);
			} else if (T.isAssignableFrom(Boolean.class)) {
				result = (T) Boolean.valueOf("1".equals(i.getValueString()));
			} else if (T.isAssignableFrom(Integer.class)) {
				result = (T) Integer.valueOf(i.getValueInteger());
			} else if (T.isAssignableFrom(Double.class)) {
				result = (T) Double.valueOf(i.getValueDouble());
			} else if (T.isAssignableFrom(Date.class)) {
				result = (T) DominoUtils.itemToDate(i);
			} else if (T.isAssignableFrom(Calendar.class)) {
				result = (T) DominoUtils.itemToCalendar(i);
			} else if (T.isAssignableFrom(java.util.Collection.class)) {
				if (i.getValues() != null && !i.getValues().isEmpty()) {
					result = (T) new ArrayList<java.lang.Object>(i.getValues());
				} else {
					result = (T) new ArrayList<java.lang.Object>();
				}
			} else if (T == Object.class) {
				result = (T) i.getValues();
			}
		} else {
			if (T.isAssignableFrom(String.class)) {
				result = (T) "";
			} else if (T.isAssignableFrom(Boolean.class)) {
				result = (T) Boolean.FALSE;
			} else if (T.isAssignableFrom(Integer.class)) {
				result = (T) Integer.valueOf(0);
			} else if (T.isAssignableFrom(Double.class)) {
				result = (T) Double.valueOf(0);
			} else if (T.isAssignableFrom(Date.class)) {
				result = (T) new Date(0);
			}
		}
		session.setConvertMIME(convertMime);
		return result;
	}

	@Override
	public Set<String> getPropertyKeys() {
		Set<String> result = new HashSet<String>();
		for (Item i : getRawDocument().getItems()) {
			result.add(i.getName());
		}
		return result;
	}

	@Override
	public void remove() {
		getRawDocument().removePermanently(true);
	}

	@Override
	public <T> T removeProperty(String key) {
		T result = getProperty(key);
		getRawDocument().removeItem(key);
		return result;
	}

	public void save() {
		getRawDocument().save();
	}

	public void setRawDocument(org.openntf.domino.Document doc) {
		doc_ = doc;
	}

	@Override
	public void setProperty(String propertyName, java.lang.Object value) {
		getRawDocument().replaceItemValue(propertyName, value);
	}

}
