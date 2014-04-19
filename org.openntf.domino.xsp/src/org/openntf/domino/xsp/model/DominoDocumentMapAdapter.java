package org.openntf.domino.xsp.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import lotus.domino.NotesException;

import org.openntf.domino.Document;
import org.openntf.domino.utils.DominoUtils;

import com.ibm.xsp.model.domino.wrapped.DominoDocument;

/**
 * A adapter to convert a DominoDocument to a map.
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class DominoDocumentMapAdapter implements Map<String, Object>, Serializable {

	private static final long serialVersionUID = 1L;

	protected DominoDocument delegate;

	public DominoDocumentMapAdapter(final DominoDocument delegate) {
		this.delegate = delegate;
	}

	@Override
	public void clear() {
		// TODO RPr is this needed?
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsKey(final Object key) {
		try {
			return delegate.hasItem(key.toString());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean containsValue(final Object paramObject) {
		// TODO RPr is this needed?
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		// TODO RPr is this needed?
		throw new UnsupportedOperationException();
	}

	@Override
	public Object get(final Object key) {
		return delegate.getValue(key);
	}

	@Override
	public boolean isEmpty() {
		// TODO RPr document is never empty?
		return false;
	}

	@Override
	public Set<String> keySet() {
		// TODO RPr this is a bit expensive!
		try {
			org.openntf.domino.Document doc = (Document) delegate.getDocument(true);
			return doc.keySet();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public Object put(final String paramK, final Object paramV) {
		Object old = delegate.getValue(paramK);
		delegate.setValue(paramK, paramV);
		return old;
	}

	@Override
	public void putAll(final Map<? extends String, ? extends Object> paramMap) {
		// TODO Auto-generated method stub
		for (java.util.Map.Entry<? extends String, ? extends Object> entry : paramMap.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}

	}

	@Override
	public Object remove(final Object paramObject) {
		Object old = delegate.getValue(paramObject);
		try {
			delegate.removeItem(paramObject.toString());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return old;
	}

	@Override
	public int size() {
		return keySet().size();
	}

	@Override
	public Collection<Object> values() {
		// TODO RPr is this needed?
		throw new UnsupportedOperationException();
	}

}
