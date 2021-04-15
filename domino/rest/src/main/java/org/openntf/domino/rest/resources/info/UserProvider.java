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
package org.openntf.domino.rest.resources.info;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.openntf.domino.Name;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.tinkerpop.frames.FramedGraph;

@SuppressWarnings({ "rawtypes", "nls" })
public class UserProvider implements IInfoProvider {
	private final List<String> namespaces_ = new ArrayList<String>();

	@Override
	public Object processRequest(FramedGraph graph, String item, MultivaluedMap<String, String> params) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		try {
			Session session = Factory.getSession(SessionType.CURRENT);
			String userName = session.getEffectiveUserName();
			result.put("username", userName);
			Name name = session.createName(userName);
			Collection<String> groups = name.getGroups("");
			result.put("groups", groups);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return result;
	}

	@Override
	public List<String> getItems() {
		List<String> result = new ArrayList<String>();
		result.add("user");
		return result;
	}

	@Override
	public List<String> getNamespaces() {
		if (namespaces_.isEmpty()) {
			namespaces_.add("core");
		}
		return namespaces_;
	}

	public void addNamespace(String namespace) {
		namespaces_.add(namespace);
	}

}
