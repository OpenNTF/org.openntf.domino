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
package org.openntf.domino.graph2.builtin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openntf.domino.ACL;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.graph2.annotations.Shardable;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.impl.DVertex;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerClass;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;
import com.tinkerpop.frames.modules.typedgraph.TypeField;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@Shardable
@TypeField("foo")
@TypeValue("org.openntf.domino.graph2.builtin")
@JavaHandlerClass(DbInfoVertex.DbInfoVertexImpl.class)
@SuppressWarnings({ "rawtypes", "unchecked", "nls" })
public interface DbInfoVertex extends VertexFrame {

	//	public static interface HasView extends EdgeFrame {
	//		public static final String LABEL = "HasView";
	//
	//		@InVertex
	//		public DbInfoVertex getDbInfo();
	//
	//		@OutVertex
	//		public ViewVertex getView();
	//	}

	@JavaHandler
	@TypedProperty("Title")
	public String getTitle();

	@JavaHandler
	@TypedProperty("ApiPath")
	public String getApiPath();

	@JavaHandler
	@TypedProperty("@viewInfo")
	public List<Object> getViewInfo();

	@JavaHandler
	@TypedProperty("@userInfo")
	public Map<String, Object> getUserInfo();

	@JavaHandler
	public Document asDocument();

	@JavaHandler
	public Map<CharSequence, Object> asMap();

	public static abstract class DbInfoVertexImpl implements DbInfoVertex, JavaHandlerContext<Vertex> {

		@Override
		public Document asDocument() {
			Object raw = asVertex();
			if (raw instanceof DVertex) {
				Object delegate = ((DVertex) raw).getDelegate();
				if (delegate instanceof Document) {
					return (Document) delegate;
				}
				throw new RuntimeException("VertexFrame not backed by org.openntf.domino.Document. Instead it's a "
						+ (delegate == null ? "null" : delegate.getClass().getName()));
			}
			throw new RuntimeException("VertexFrame not backed by org.openntf.domino.graph2.DVertex. Instead it's a "
					+ (raw == null ? "null" : raw.getClass().getName()));
		}

		@Override
		public Map<CharSequence, Object> asMap() {
			Object raw = asVertex();
			if (raw instanceof DVertex) {
				Object delegate = ((DVertex) raw).getDelegate();
				if (delegate instanceof Map) {
					return (Map<CharSequence, Object>) delegate;
				}
				throw new RuntimeException(
						"VertexFrame not backed by a Map. Instead it's a " + (delegate == null ? "null" : delegate.getClass().getName()));
			}
			throw new RuntimeException("VertexFrame not backed by org.openntf.domino.graph2.DVertex. Instead it's a "
					+ (raw == null ? "null" : raw.getClass().getName()));
		}

		@Override
		public String getTitle() {
			Document doc = asDocument();
			Database db = doc.getAncestorDatabase();
			return db.getTitle();
		}

		@Override
		public String getApiPath() {
			Document doc = asDocument();
			Database db = doc.getAncestorDatabase();
			return db.getApiPath();
		}

		@Override
		public List getViewInfo() {
			List<Map<String, String>> result = new ArrayList<Map<String, String>>();
			Document doc = asDocument();
			Database db = doc.getAncestorDatabase();
			String prefix = db.getReplicaID().toLowerCase();
			for (View view : db.getViews()) {
				Map<String, String> cur = new HashMap<String, String>();
				cur.put(view.getName(), prefix + view.getUniversalID().toLowerCase());
				result.add(cur);
			}
			return result;
		}

		@Override
		public Map<String, Object> getUserInfo() {
			Map<String, Object> result = new LinkedHashMap<String, Object>();
			Document doc = asDocument();
			Database db = doc.getAncestorDatabase();
			Session s = db.getAncestorSession();
			String name = s.getEffectiveUserName();
			result.put("username", name);
			result.put("accesslevel", ACL.Level.getLevel(db.getCurrentAccessLevel()));
			result.put("roles", db.getCurrentRoles());
			result.put("privileges", db.getCurrentPrivileges());
			return result;
		}

	}
}
