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
package org.openntf.domino.graph2.builtin;

import java.util.List;
import java.util.Map;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.View;
import org.openntf.domino.graph2.annotations.Shardable;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.impl.DVertex;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.Incidence;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerClass;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;
import com.tinkerpop.frames.modules.typedgraph.TypeField;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@Shardable
@TypeField("$FormulaClass")
@TypeValue("1")
@JavaHandlerClass(ViewVertex.ViewVertexImpl.class)
public interface ViewVertex extends VertexFrame {

	@JavaHandler
	public Document asDocument();

	@JavaHandler
	public Map<CharSequence, Object> asMap();

	@JavaHandler
	public View asView();

	@JavaHandler
	@TypedProperty("entrycount")
	public int getDocCount();

	@TypedProperty("$Title")
	public String getTitle();

	@Incidence(label = "contents", direction = Direction.OUT)
	public List<Contains> getContents();

	@Incidence(label = "doccontents", direction = Direction.OUT)
	public List<Contains> getDocContents();

	public abstract static class ViewVertexImpl implements ViewVertex, JavaHandlerContext<Vertex> {
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
		public View asView() {
			Document doc = asDocument();
			Database db = doc.getParentDatabase();
			View result = db.getView(doc);
			if (result == null) {
				System.out.println("TEMP DEBUG Can't find a view for a document with title " + doc.getItemValueString("$Title")
						+ " in database " + db.getApiPath());
			}
			return result;
		}

		@Override
		public int getDocCount() {
			View view = asView();
			if (view != null) {
				return view.createViewNav().getCount();
			} else {
				return 0;
			}
		}

		@SuppressWarnings("unchecked")
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
	}

	public static interface Contains extends EdgeFrame {
		public static final String LABEL = "Contains";

		@InVertex
		public Vertex getIn();

		@OutVertex
		public Vertex getOut();

	}

}
