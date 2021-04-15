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
package org.openntf.domino.xsp.model;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
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

	/**
	 * Constuctor
	 * 
	 * @param delegate
	 *            the {@link DominoDocument}
	 */
	public DominoDocumentMapAdapter(final DominoDocument delegate) {
		this.delegate = delegate;
	}

	/**
	 * Returns the delegate
	 * 
	 * @return a {@link DominoDocument}
	 */
	public DominoDocument getDelegate() {
		return delegate;
	}

	/**
	 * TODO RPr Not supported. Does it make sense to clear a DominoDocument?
	 */
	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	/**
	 * checks if the item (=key) is present in the delegate
	 * 
	 * @return true it the item exists
	 */
	@Override
	public boolean containsKey(final Object key) {
		try {
			return delegate.hasItem(key.toString());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/**
	 * TODO RPr this is not yet implemented. Does it make sense?
	 */
	@Override
	public boolean containsValue(final Object value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns a entrySet
	 */
	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		// ok this implementation with 3 cascaded inner classes is not nice... but should work for now
		return new AbstractSet<Map.Entry<String, Object>>() {

			@Override
			public Iterator<java.util.Map.Entry<String, Object>> iterator() {
				final Iterator<String> keyIt = keySet().iterator();

				return new Iterator<Map.Entry<String, Object>>() {

					private String currentKey;

					@Override
					public boolean hasNext() {
						return keyIt.hasNext();
					}

					@Override
					public java.util.Map.Entry<String, Object> next() {
						currentKey = keyIt.next();
						final String key = currentKey;
						return new java.util.Map.Entry<String, Object>() {

							@Override
							public String getKey() {
								return key;
							}

							@Override
							public Object getValue() {
								return get(key);
							}

							@Override
							public Object setValue(final Object value) {
								return put(key, value);
							}

						};
					}

					@Override
					public void remove() {
						DominoDocumentMapAdapter.this.remove(currentKey);
					}
				};
			}

			@Override
			public int size() {
				return keySet().size();
			}

		};
	}

	/**
	 * Returns the value of field "key"
	 */
	@Override
	public Object get(final Object key) {
		return delegate.getValue(key);
	}

	/**
	 * TODO RPr document is never empty?
	 */
	@Override
	public boolean isEmpty() {
		return false;
	}

	/**
	 * Returns a keySet (all keys in the document)
	 * 
	 * TODO RPr: this may be a little bit expensive.
	 */
	@Override
	public Set<String> keySet() {
		try {
			org.openntf.domino.Document doc = (Document) delegate.getDocument(true);
			return doc.keySet();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/**
	 * Sets a field
	 */
	@Override
	public Object put(final String paramK, final Object paramV) {
		Object old = delegate.getValue(paramK);
		delegate.setValue(paramK, paramV);
		return old;
	}

	/**
	 * Copies a complete map
	 */
	@Override
	public void putAll(final Map<? extends String, ? extends Object> otherMap) {
		for (java.util.Map.Entry<? extends String, ? extends Object> entry : otherMap.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}

	}

	/**
	 * removes a key/field
	 */
	@Override
	public Object remove(final Object key) {
		Object old = delegate.getValue(key);
		try {
			delegate.removeItem(key.toString());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return old;
	}

	/**
	 * returns the size (=count of the fields)
	 */
	@Override
	public int size() {
		return keySet().size();
	}

	/**
	 * returns all field values in an array
	 */
	@Override
	public Collection<Object> values() {

		return new AbstractCollection<Object>() {

			@Override
			public Iterator<Object> iterator() {
				final Iterator<String> keyIt = keySet().iterator();
				return new Iterator<Object>() {

					private String currentKey;

					@Override
					public boolean hasNext() {
						return keyIt.hasNext();
					}

					@Override
					public Object next() {
						currentKey = keyIt.next();
						return get(currentKey);
					}

					@Override
					public void remove() {
						DominoDocumentMapAdapter.this.remove(currentKey);
					}
				};
			}

			@Override
			public int size() {
				return keySet().size();
			}
		};

	}
}
