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

import java.util.Date;
import java.util.Map;

import org.openntf.domino.Document;
import org.openntf.domino.graph2.DEdge;
import org.openntf.domino.graph2.annotations.TypedProperty;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerClass;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;
import com.tinkerpop.frames.modules.typedgraph.TypeField;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeField("form")
@TypeValue("edgeFrame")
@JavaHandlerClass(DEdgeFrame.DEdgeFrameImpl.class)
@SuppressWarnings("nls")
public interface DEdgeFrame extends EdgeFrame {
	@TypedProperty(value = "@CreatedDate", derived = true)
	public Date getCreated();

	@TypedProperty(value = "@ModifiedDate", derived = true)
	public Date getModified();

	@JavaHandler
	public Map<CharSequence, Object> asMap();

	@JavaHandler
	public Document asDocument();

	@JavaHandler
	public void updateReadOnlyProperties();

	public abstract static class DEdgeFrameImpl implements DEdgeFrame, JavaHandlerContext<Vertex> {
		@Override
		public Document asDocument() {
			Object raw = asEdge();
			if (raw instanceof DEdge) {
				Object delegate = ((DEdge) raw).getDelegate();
				if (delegate instanceof Document) {
					return (Document) delegate;
				}
				throw new RuntimeException("EdgeFrame not backed by org.openntf.domino.Document. Instead it's a "
						+ (delegate == null ? "null" : delegate.getClass().getName()));
			}
			throw new RuntimeException("EdgeFrame not backed by org.openntf.domino.graph2.DEdge. Instead it's a "
					+ (raw == null ? "null" : raw.getClass().getName()));
		}

		@SuppressWarnings("unchecked")
		@Override
		public Map<CharSequence, Object> asMap() {
			Object raw = asEdge();
			if (raw instanceof DEdge) {
				Object delegate = ((DEdge) raw).getDelegate();
				if (delegate instanceof Map) {
					return (Map<CharSequence, Object>) delegate;
				}
				throw new RuntimeException(
						"EdgeFrame not backed by a Map. Instead it's a " + (delegate == null ? "null" : delegate.getClass().getName()));
			}
			throw new RuntimeException("EdgeFrame not backed by org.openntf.domino.graph2.DEdge. Instead it's a "
					+ (raw == null ? "null" : raw.getClass().getName()));
		}

		@Override
		public void updateReadOnlyProperties() {

		}
	}

}
