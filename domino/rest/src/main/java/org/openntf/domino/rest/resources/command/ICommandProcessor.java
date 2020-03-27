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
package org.openntf.domino.rest.resources.command;

import com.tinkerpop.frames.FramedGraph;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

public interface ICommandProcessor extends Runnable {
	public Object processCommand(@SuppressWarnings("rawtypes") FramedGraph graph, String command,
			MultivaluedMap<String, String> params);

	public List<String> getNamespaces();

	public List<String> getCommands();
}
