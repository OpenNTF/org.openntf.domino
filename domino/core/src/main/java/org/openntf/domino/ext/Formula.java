/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
/**
 * 
 */
package org.openntf.domino.ext;

import java.io.Externalizable;
import java.util.Vector;

/**
 * @author nfreeman
 * 
 */
public interface Formula extends Externalizable {
	public void setSession(final org.openntf.domino.Session session);

	public void setExpression(final String expression);

	public String getExpression();

	public Vector<Object> getValue();

	public <T> T getValue(final Class<T> type);

	public Vector<Object> getValue(final org.openntf.domino.Session session);

	public <T> T getValue(final org.openntf.domino.Session session, final Class<T> type);

	public Vector<Object> getValue(final org.openntf.domino.Document document);

	public <T> T getValue(final org.openntf.domino.Document document, final Class<T> type);

}
