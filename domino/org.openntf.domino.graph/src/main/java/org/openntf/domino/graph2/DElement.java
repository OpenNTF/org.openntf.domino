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
/**
 *
 */
package org.openntf.domino.graph2;

import java.util.Map;
import java.util.Set;

import org.openntf.domino.Document;

/**
 * @author nfreeman
 *
 */
@SuppressWarnings("nls")
public interface DElement extends com.tinkerpop.blueprints.Element {
	public static final String TYPE_FIELD = "_ODA_GRAPHTYPE";
	public static final String FORMULA_FILTER = DElement.TYPE_FIELD + "=\"" + DVertex.GRAPH_TYPE_VALUE + "\" | " + DElement.TYPE_FIELD
			+ "=\"" + DEdge.GRAPH_TYPE_VALUE + "\"";

	public boolean hasProperty(String key);

	public <T> T getProperty(String key, Class<T> type);

	public <T> T getProperty(String key, Class<T> type, boolean allowNull);

	public int incrementProperty(String key);

	public int decrementProperty(String key);

	public Map<String, Object> getDelegate();

	public Class<?> getDelegateType();

	public void setDelegate(Map<String, Object> delegate);

	//	public Set<String> getPropertyKeys(boolean includeEdgeFields);

	public Map<String, Object> toMap(String[] props);

	public Map<String, Object> toMap(Set<String> props);

	public void fromMap(Map<String, Object> map);

	public void rollback();

	public void commit();

	public Document asDocument();

	public void uncache();

	public boolean isEditable();

}
