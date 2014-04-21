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
public class OpenntfDominoDocument extends DominoDocument implements Map<String, Object> {
	// private static final Logger log_ = Logger.getLogger(FocDominoDocument.class.getName());
	private static final long serialVersionUID = 1L;

	// private static Constructor<DominoDocument> documentConstructorExisting;
	// private static Constructor<DominoDocument> documentConstructorNew;
	private static List<Field> parentFields = new ArrayList<Field>();

	private Map<String, Object> mapAdapter = new DominoDocumentMapAdapter(this);

	static {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					// System.out.println("Init Constructor");
					// some tricks to "inherit" from DominoDocument

					// documentConstructorExisting = DominoDocument.class.getDeclaredConstructor(String.class, lotus.domino.Document.class,
					// int.class, int.class, boolean.class, String.class, String.class);
					// documentConstructorExisting.setAccessible(true);
					//
					// documentConstructorNew = DominoDocument.class.getDeclaredConstructor(String.class, lotus.domino.Database.class,
					// String.class, String.class, int.class, int.class, boolean.class, String.class, String.class);
					// documentConstructorNew.setAccessible(true);

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

	public OpenntfDominoDocument() {
		// default constructor
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
			// trick: first create a "DominoDocument" with the accessible constructor
			// then create a blank "FocDominoDocument" and copy all fields in the blank document. This is similar to a
			// serialize/deserialize action.
			// This is more stable than the former FocDominoDelegate approach

			for (Field field : parentFields) {
				field.set(this, field.get(delegate));
			}

		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
	}

	public void clear() {
		mapAdapter.clear();
	}

	public boolean containsKey(final Object arg0) {
		return mapAdapter.containsKey(arg0);
	}

	public boolean containsValue(final Object arg0) {
		return mapAdapter.containsValue(arg0);
	}

	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return mapAdapter.entrySet();
	}

	@Override
	public boolean equals(final Object arg0) {
		return mapAdapter.equals(arg0);
	}

	public Object get(final Object arg0) {
		return mapAdapter.get(arg0);
	}

	@Override
	public int hashCode() {
		return mapAdapter.hashCode();
	}

	public boolean isEmpty() {
		return mapAdapter.isEmpty();
	}

	public Set<String> keySet() {
		return mapAdapter.keySet();
	}

	public Object put(final String arg0, final Object arg1) {
		return mapAdapter.put(arg0, arg1);
	}

	public void putAll(final Map<? extends String, ? extends Object> arg0) {
		mapAdapter.putAll(arg0);
	}

	public Object remove(final Object arg0) {
		return mapAdapter.remove(arg0);
	}

	public int size() {
		return mapAdapter.size();
	}

	public Collection<Object> values() {
		return mapAdapter.values();
	}

}
