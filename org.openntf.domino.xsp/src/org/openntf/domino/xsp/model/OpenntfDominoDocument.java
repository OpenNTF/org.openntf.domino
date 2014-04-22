package org.openntf.domino.xsp.model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openntf.domino.utils.DominoUtils;

/*
 * © Copyright FOCONIS AG, 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 * 
 */
import com.ibm.xsp.model.domino.wrapped.DominoDocument;

/**
 * A Wrapper for DominoDocument that extends the Map Interface (Currently only needed for formula engine, but I am sure that we have to fix
 * the one or other method in "DominoDocument")
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
@SuppressWarnings("unchecked")
public class OpenntfDominoDocument extends DominoDocument implements Map<String, Object> {
	// private static final Logger log_ = Logger.getLogger(FocDominoDocument.class.getName());
	private static final long serialVersionUID = 1L;

	private static List<Field> parentFields = new ArrayList<Field>();

	private Map<String, Object> mapAdapter = new DominoDocumentMapAdapter(this);

	static {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					// get all fields that are not static and make them accessible
					for (Field field : DominoDocument.class.getDeclaredFields()) {
						if ((field.getModifiers() & Modifier.STATIC) == 0) {
							field.setAccessible(true);
							parentFields.add(field);
						}
					}
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Default constructor.
	 */
	public OpenntfDominoDocument() {
	}

	/**
	 * Wraps a DominoDocument in an OpenntfDominoDocument. It is important that "OpenntfDominoDocument instanceof DominoDocument". As IBM
	 * "forgot" to use an interface here, we must inherit from DominoDocument and do some tricks not to break everything. (Otherwise we can
	 * use a delegate pattern)
	 * 
	 * @param delegate
	 * @return
	 */
	public OpenntfDominoDocument(final DominoDocument delegate) {

		try {
			// trick: first create a "DominoDocument" with the wrap-method (done outside)
			// then create a blank "FocDominoDocument" and copy all fields in the blank object. This is similar to a
			// serialize/deserialize action.
			// This is more stable than the former DominoDelegate approach

			for (Field field : parentFields) {
				field.set(this, field.get(delegate));
			}

		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#clear()
	 */
	public void clear() {
		mapAdapter.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(final Object arg0) {
		return mapAdapter.containsKey(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(final Object arg0) {
		return mapAdapter.containsValue(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#entrySet()
	 */
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return mapAdapter.entrySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object arg0) {
		return mapAdapter.equals(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public Object get(final Object arg0) {
		return mapAdapter.get(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return mapAdapter.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty() {
		return mapAdapter.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#keySet()
	 */
	public Set<String> keySet() {
		return mapAdapter.keySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(final String arg0, final Object arg1) {
		return mapAdapter.put(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(final Map<? extends String, ? extends Object> arg0) {
		mapAdapter.putAll(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public Object remove(final Object arg0) {
		return mapAdapter.remove(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#size()
	 */
	public int size() {
		return mapAdapter.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#values()
	 */
	public Collection<Object> values() {
		return mapAdapter.values();
	}

}
