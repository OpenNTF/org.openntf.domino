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

import org.openntf.domino.graph2.impl.DVertex;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;

public interface Eventable extends VertexFrame {

	@JavaHandler
	public boolean create();

	@JavaHandler
	public boolean read();

	@JavaHandler
	public boolean update();

	@JavaHandler
	public boolean delete();

	@JavaHandler
	public boolean isNew();

	@JavaHandler
	public boolean onCreate() throws NoSuchMethodException;

	@JavaHandler
	public boolean onRead() throws NoSuchMethodException;

	@JavaHandler
	public boolean onUpdate() throws NoSuchMethodException;

	@JavaHandler
	public boolean onDelete() throws NoSuchMethodException;

	public static abstract class Impl implements JavaHandlerContext<Vertex>, Eventable {

		@Override
		public boolean create() {
			try {
				return onCreate();
			} catch (NoSuchMethodException nsme) {
				return true; //ignore
			} catch (Throwable t) {
				throw new RuntimeException(t);
			}
		}

		@Override
		public boolean read() {
			try {
				return onRead();
			} catch (NoSuchMethodException nsme) {
				return true; //ignore
			} catch (Throwable t) {
				throw new RuntimeException(t);
			}
		}

		@Override
		public boolean update() {
			try {
				return onUpdate();
			} catch (NoSuchMethodException nsme) {
				return true; //ignore
			} catch (Throwable t) {
				throw new RuntimeException(t);
			}
		}

		@Override
		public boolean delete() {
			try {
				return onDelete();
			} catch (NoSuchMethodException nsme) {
				return true; //ignore
			} catch (Throwable t) {
				throw new RuntimeException(t);
			}
		}

		@Override
		public boolean isNew() {
			Vertex v = this.asVertex();
			return ((DVertex) v).asDocument().isNewNote();
		}

	}
}
