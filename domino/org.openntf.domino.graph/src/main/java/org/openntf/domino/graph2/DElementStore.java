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
package org.openntf.domino.graph2;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;

/*
 * Interface definition to register relationship between a Frame type and an NSF
 */
public interface DElementStore extends Externalizable {

	public static interface CustomProxyResolver {
		public void setProxiedElementStore(DElementStore store);

		public Map<String, Object> getOriginalDelegate(Serializable key);

		public Map<String, Object> getProxyDelegate(Serializable key);
	}

	public void setCustomProxyResolver(CustomProxyResolver resolver);

	public void addType(Class<?> type);

	public void removeType(Class<?> type);

	public List<Class<?>> getTypes();

	public Object getStoreDelegate();

	public void setStoreDelegate(Object store);

	public DConfiguration getConfiguration();

	public void setConfiguration(DConfiguration config);

	public DIdentityFactory getIdentityFactory();

	public Object getIdentity(Class<?> type, Object context, Object... args);

	public void setIdentityFactory(DIdentityFactory identFactory);

	public Long getStoreKey();

	public void setStoreKey(Long key);

	public void setStoreKey(CharSequence key);

	public Long getProxyStoreKey();

	public Object getProxyStoreDelegate();

	public void setProxyStoreKey(Long key);

	public void setProxyStoreKey(CharSequence key);

	public void setProxyStoreDelegate(Object proxyStore);

	public Vertex addVertex(Object id);

	public Vertex addVertex(Object id, boolean temporary);

	//	public Vertex getVertex(Object id);

	public void removeVertex(Vertex vertex);

	public Edge addEdge(Object id);

	//	public Edge getEdge(Object id);

	public void removeEdge(Edge edge);

	public void removeEdge(final Edge edge, final Vertex removingVertex);

	public Object findElementDelegate(Object delegateKey/*, Class<? extends Element> type*/);

	public void removeElementDelegate(Element element);

	public Element getElement(final Object id) throws IllegalStateException;

	//	public Set<Vertex> getCachedVertices();
	//
	//	public Set<Edge> getCachedEdges();

	public DVertexIterable getVertices();

	public DEdgeIterable getEdges();

	public DVertexIterable getVertices(String formulaFilter);

	public DEdgeIterable getEdges(String formulaFilter);

	public DElementIterable getElements(String formulaFilter);

	public DVertexIterable getVertices(String key, Object value);

	public DEdgeIterable getEdges(String key, Object value);

	public DElementIterable getElements(String key, Object value);

	public void uncache(Element element);

	public boolean isProxied();

	public void flushCache();

	public CustomProxyResolver getCustomProxyResolver();

	Long getReverseProxyStoreKey();

	Object getReverseProxyStoreDelegate();

	boolean isReverseProxied();

	void setCustomReverseProxyResolver(CustomProxyResolver resolver);

	CustomProxyResolver getCustomReverseProxyResolver();

	void setReverseProxyStoreDelegate(Object store);

	void setReverseProxyStoreKey(Long storeKey);

	void setReverseProxyStoreKey(CharSequence storeKey);

	public void flushCache(final String id);

}
