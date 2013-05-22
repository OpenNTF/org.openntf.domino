package org.openntf.domino.graph;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Item;

import com.tinkerpop.blueprints.Element;

public abstract class DominoElement implements Element, Serializable {
	private static final Logger log_ = Logger.getLogger(DominoElement.class.getName());
	private static final long serialVersionUID = 1L;
	public static final String TYPE_FIELD = "_OPEN_GRAPHTYPE";
	transient org.openntf.domino.Document doc_;
	private String key_;
	transient DominoGraph parent_;
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

	public String getRawId() {
		String prefix = getDatabase().getServer() + "!!" + getDatabase().getFilePath();
		return prefix + ": " + getRawDocument().getNoteID();
	}

	public int incrementProperty(String propertyName) {
		Integer result = getProperty(propertyName, Integer.class);
		if (result == null)
			result = 0;
		setProperty(propertyName, ++result);
		return result;
	}

	public int decrementProperty(String propertyName) {
		Integer result = getProperty(propertyName, Integer.class);
		if (result == null)
			result = 0;
		setProperty(propertyName, --result);
		return result;
	}

	private Document getDocument() {
		return getDatabase().getDocumentByKey(unid_, true);
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

	public <T> T getProperty(String propertyName, Class<?> T) {
		return getRawDocument().getItemValue(propertyName, T);
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
		getParent().startTransaction();
		getRawDocument().removePermanently(true);
	}

	@Override
	public <T> T removeProperty(String key) {
		getParent().startTransaction();
		T result = getProperty(key);
		getRawDocument().removeItem(key);
		return result;
	}

	// public void save() {
	// getRawDocument().save();
	// }

	public void setRawDocument(org.openntf.domino.Document doc) {
		doc_ = doc;
	}

	@Override
	public void setProperty(String propertyName, java.lang.Object value) {
		getParent().startTransaction();
		getRawDocument().replaceItemValue(propertyName, value);
	}

}
