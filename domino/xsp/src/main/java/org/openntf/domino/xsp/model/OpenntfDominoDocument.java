/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openntf.domino.AsDocMap;
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
 * A Wrapper for DominoDocument (Currently only needed for foconis, but I am sure that we have to fix the one or other method in
 * "DominoDocument") The DominoDocument must not extend Map, this has unpredictable side effects. So that's why there is an "asMap()"
 * method.
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class OpenntfDominoDocument extends DominoDocument implements AsDocMap {
	// private static final Logger log_ = Logger.getLogger(FocDominoDocument.class.getName());
	private static final long serialVersionUID = 1L;

	private static List<Field> parentFields = new ArrayList<Field>();

	private Map<String, Object> mapAdapter;

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

	/*
	 * retruns generic class now 
	 */
	@Override
	public Class<?> getType(final Object arg0) {
		return super.getType(arg0);
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

	@Override
	public Map<String, Object> asDocMap() {
		if (mapAdapter == null) {
			mapAdapter = new DominoDocumentMapAdapter(this);
		}
		return mapAdapter;
	}

	/**
	 * Fix the ability to read MIME beans properly.
	 * 
	 * The problem is that, DominoDocument.getValue(...) tries to wrap EVERY MIME-Item into a DominoRichtextItem. A quick hack was, not to
	 * return Type 25 for MIME-Beans
	 */
	//	@Override
	//	public Object getValue(final Object paramObject) {
	//
	//		Document doc = this.getDocument();
	//		org.openntf.domino.impl.Document openDoc = null;
	//		if (doc instanceof org.openntf.domino.impl.Document) {
	//			openDoc = (org.openntf.domino.impl.Document) doc;
	//			openDoc.beginXspRead(paramObject);
	//		}
	//		try {
	//			return super.getValue(paramObject);
	//		} finally {
	//			if (openDoc != null) {
	//				openDoc.endXspRead(paramObject);
	//			}
	//		}
	//	}

}
