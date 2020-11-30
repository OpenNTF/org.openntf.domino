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

import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openntf.domino.Document;
import org.openntf.domino.exceptions.UserAccessException;
import org.openntf.domino.graph2.annotations.Action;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.graph2.impl.DVertex;
import org.openntf.domino.types.CaseInsensitiveString;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerClass;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;
import com.tinkerpop.frames.modules.typedgraph.TypeField;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeField("form")
@TypeValue("vertexFrame")
@JavaHandlerClass(DVertexFrame.DVertexFrameImpl.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
public interface DVertexFrame extends Editable {
	@TypedProperty(value = "@CreatedDate", derived = true)
	public Date getCreated();

	@TypedProperty(value = "@ModifiedDate", derived = true)
	public Date getModified();

	@TypedProperty(value = "$$Key")
	public String getKey();

	@JavaHandler
	public Document asDocument();

	@JavaHandler
	public Map<CharSequence, Object> asMap();

	@JavaHandler
	@TypedProperty("@editors")
	public String[] getEditors();

	@JavaHandler
	public void updateReadOnlyProperties(DVertexFrame frame);

	@JavaHandler
	@Action(name = "cleanEdges")
	public Boolean cleanEdges();

	//TODO NTF Future
	//	public String[] getOwners();

	//TODO NTF Future
	//	public String[] getReaders();

	@JavaHandler
	public Map<String, Object> getMetadata();

	public abstract static class DVertexFrameImpl implements DVertexFrame, JavaHandlerContext<Vertex> {

		@Override
		public Boolean cleanEdges() {
			Vertex raw = asVertex();
			if (raw instanceof DVertex) {
				boolean isValid = ((DVertex) raw).validateEdges();
				if (!isValid) {
					((DVertex) raw).applyChanges();
				}
			}
			return true;
		}

		@Override
		public Document asDocument() {
			Object raw = asVertex();
			if (raw instanceof DVertex) {
				Object delegate = ((DVertex) raw).getDelegate();
				if (delegate instanceof Document) {
					return (Document) delegate;
				}
				throw new RuntimeException(
						"VertexFrame " + ((DVertex) raw).getId() + " not backed by org.openntf.domino.Document. Instead it's a "
								+ (delegate == null ? "null" : delegate.getClass().getName()));
			}
			throw new IllegalStateException(
					"DVertexFrame is not backed by a DVertex. It's backed by a " + (raw == null ? "null" : raw.getClass().getName()));
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
		public Map<String, Object> getMetadata() throws UserAccessException {
			Map<String, Object> result = new LinkedHashMap<String, Object>();
			try {
				result.put("createdDate", getCreated());
				result.put("modifiedDate", getModified());
				String[] editors = getEditors();
				if (editors != null && editors.length > 0) {
					result.put("lastEditor", editors[editors.length - 1]);
				} else {
					result.put("lastEditor", null);
				}
			} catch (UserAccessException uae) {
				throw uae;
			} catch (Throwable t1) {
				//				t1.printStackTrace();
			}
			return result;
		}

		@Override
		public String[] getEditors() {
			//			System.out.println("TEMP DEBUG getting Editors...");
			String[] result = null;
			try {
				Object raw = asVertex().getProperty("$UpdatedBy");
				if (raw != null) {
					result = org.openntf.domino.utils.TypeUtils.toStrings(raw);
				} else {
					//					System.out.println("TEMP DEBUG $UpdatedBy was null for " + asVertex().getId().toString());
				}
				return result;
			} catch (UserAccessException uae) {
				return null;
			} catch (Throwable t) {
				//				t.printStackTrace();
				return null;
			}
		}

		@Override
		public void updateReadOnlyProperties(final DVertexFrame frame) {
			DFramedTransactionalGraph graph = (DFramedTransactionalGraph) g();
			Class<?> type = graph.getTypeManager().resolve(frame);
			Map<CaseInsensitiveString, Method> computeds = graph.getTypeRegistry().getComputeds(type);
			for (Map.Entry<CaseInsensitiveString, Method> entry : computeds.entrySet()) {
				CharSequence key = entry.getKey();
				Method crystal = entry.getValue();
				try {
					crystal.invoke(frame);
				} catch (Exception e) {
					System.err.println("ALERT Exception encountered attempting to update computed property: " + key
							+ " using a method called " + crystal.getName() + " in a frame of type " + type.getName()
							+ ". The exception is " + e.getClass().getName() + ": " + e.getMessage());
					Throwable cause = e;
					while (cause.getCause() != null) {
						cause = cause.getCause();
						System.err.println("Caused by: " + cause.getClass().getName() + " " + cause.getMessage());
					}

				}
			}
		}

		//		@Override
		//		public Date getCreated() {
		//			//			System.out.println("Impl.getCreated() called");
		//			Date result = null;
		//			Object raw = ((DVertex) asVertex()).getProperty("@CreatedDate", Date.class);
		//			if (raw instanceof Date) {
		//				return result;
		//			} else {
		//				System.out.println("Looked for a Date but got a " + (raw == null ? "null" : raw.getClass().getName()));
		//				return new Date(0);
		//			}
		//		}
	}

}
