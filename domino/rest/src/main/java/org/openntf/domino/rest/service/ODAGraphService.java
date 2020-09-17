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
package org.openntf.domino.rest.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.spi.RegistryContributor;
import org.openntf.domino.AutoMime;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.graph2.impl.DGraph;
import org.openntf.domino.rest.resources.AbstractResource;
import org.openntf.domino.rest.resources.ReferenceResource;
import org.openntf.domino.rest.resources.command.CommandResource;
import org.openntf.domino.rest.resources.frames.FramedCollectionResource;
import org.openntf.domino.rest.resources.frames.FramedResource;
import org.openntf.domino.rest.resources.info.InfoResource;
import org.openntf.domino.rest.resources.search.SearchResource;
import org.openntf.domino.rest.resources.search.TermsResource;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.utils.Factory.ThreadConfig;
import org.openntf.domino.xsp.ODAPlatform;
import org.openntf.domino.xsp.session.DasCurrentSessionFactory;

import com.ibm.domino.das.service.IRestServiceExt;
import com.ibm.domino.das.service.RestService;
import com.tinkerpop.frames.FramedGraph;

@SuppressWarnings({ "rawtypes", "nls", "unused" })
public class ODAGraphService extends RestService implements IRestServiceExt {
	private static ThreadLocal<HttpServletRequest> REQUEST_CTX = new ThreadLocal<HttpServletRequest>();

	public static HttpServletRequest getCurrentRequest() {
		return REQUEST_CTX.get();
	}

	private Map<String, FramedGraph<?>> graphMap_;
	private Map<String, IGraphFactory> factoryMap_;
	private List<IResourceProvider> resourceProviders_;
	private Set<AbstractResource> externalSingletons_ = new HashSet<AbstractResource>();
	private Set<Class<?>> externalClasses_ = new HashSet<Class<?>>();
	public static final String PREFIX = "ODA Graph Service: ";

	static void report(final String message) {
		System.out.println(PREFIX + message);
	}

	public ODAGraphService() {
		report("Starting...");
		init();
	}

	protected void initDynamicGraphs() {
		// Get a list of all registered extensions
		report("Searching for extensions...");
		IExtension extensions[] = null;
		final IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		if (extensionRegistry != null) {
			final IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint("org.openntf.domino.rest.graph"); // $NON-NLS-1$
			if (extensionPoint != null) {
				extensions = extensionPoint.getExtensions();
			}
		}

		if (extensions == null) {
			return;
		}

		// Walk through each extension in the list

		for (final IExtension extension : extensions) {
			final IConfigurationElement configElements[] = extension.getConfigurationElements();
			if (configElements == null) {
				continue;
			}

			for (final IConfigurationElement configElement : configElements) {
				if ("graphFactory".equalsIgnoreCase(configElement.getName())) {
					try {
						beforeDoService(null);
						final Object object = configElement.createExecutableExtension("class"); // $NON-NLS-1$
						IContributor contributor = configElement.getContributor();
						String contribSource = "";
						if (contributor instanceof RegistryContributor) {
							contribSource = contributor.getName() + " (" + ((RegistryContributor)contributor).getActualName() + ")";
						}
						report("Found a Graph Factory extension point: " + object.getClass().getName() + " from contributor " + contribSource);
						if (object instanceof IGraphFactory) {
							IGraphFactory factory = (IGraphFactory) object;
							Map<String, FramedGraph<?>> registry = factory.getRegisteredGraphs();
							for (String key : registry.keySet()) {
								FramedGraph<?> graph = registry.get(key);
								report("Adding graph called: " + key);
								addGraph(key, graph);
								addFactory(key, factory);
							}
						}
					} catch (final CoreException e) {
						e.printStackTrace();
					} finally {
						afterDoService(null);
					}
				} else if ("resourceProvider".equalsIgnoreCase(configElement.getName())) {
					try {
						beforeDoService(null);
						final Object object = configElement.createExecutableExtension("class"); // $NON-NLS-1$
						report("Found a Resource Provider extension point: " + object.getClass().getName());
						if (object instanceof IResourceProvider) {
							IResourceProvider provider = (IResourceProvider) object;
							report("Adding a Resource Provider: " + provider.getClass().getName());
							externalClasses_.addAll(provider.getClasses());
							externalSingletons_.addAll(provider.getSingletons(this));
						}
					} catch (final CoreException e) {
						e.printStackTrace();
					} finally {
						afterDoService(null);
					}
				}
			}
		}
		report("Extensions complete.");
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> result = new HashSet<Class<?>>();
		result.addAll(super.getClasses());
		result.addAll(externalClasses_);
		return result;
	}

	@Override
	public Set<Object> getSingletons() {
		Set<Object> result = new HashSet<Object>();
		result.addAll(super.getSingletons());
		result.addAll(externalSingletons_);
		// TODO NTF commented out resources are ones that aren't yet
		// implemented.
		// existing classes are just stubs to be filled out later.
		// result.add(new DataResource(this));
		// result.add(new DataCollectionResource(this));
		// result.add(new ViewResource(this));
		// result.add(new ViewCollectionResource(this));
		// result.add(new DocumentResource(this));
		// result.add(new DocumentCollectionResource(this));
		result.add(new FramedResource(this));
		result.add(new FramedCollectionResource(this));
		result.add(new InfoResource(this));
		result.add(new ReferenceResource(this));
		result.add(new SearchResource(this));
		result.add(new TermsResource(this));
		// result.add(new VertexFrameResource(this));
		// result.add(new VertexFrameCollectionResource(this));
		// result.add(new EdgeFrameResource(this));
		// result.add(new EdgeFrameCollectionResource(this));
		// result.add(new GraphResource(this));
		// result.add(new GraphCollectionResource(this));
		// result.add(new EdgeResource(this));
		// result.add(new EdgeCollectionResource(this));
		// result.add(new VertexResource(this));
		// result.add(new VertexCollectionResource(this));
		result.add(new CommandResource(this));
		return result;
	}

	protected Map<String, FramedGraph<?>> getGraphMap() {
		if (graphMap_ == null) {
			graphMap_ = new HashMap<String, FramedGraph<?>>();
		}
		return graphMap_;
	}

	protected Map<String, IGraphFactory> getFactoryMap() {
		if (factoryMap_ == null) {
			factoryMap_ = new HashMap<String, IGraphFactory>();
		}
		return factoryMap_;
	}

	public void addFactory(final String name, final IGraphFactory factory) {
		getFactoryMap().put(name, factory);
	}

	public IGraphFactory getFactory(final String name) {
		return getFactoryMap().get(name);
	}

	public DFramedTransactionalGraph getGraph(final String name) {
		return (DFramedTransactionalGraph) getGraphMap().get(name);
	}

	public void addGraph(final String name, final FramedGraph<?> graph) {
		Map<String, FramedGraph<?>> map = getGraphMap();
		if (map.containsKey(name)) {
			Class<?> graphClass = graph.getClass();
			Package graphPackage = graphClass.getPackage();
			String version = graphPackage.getImplementationVersion();
			report ("A graph called " + name + " is already registered with the ODA Graph Service. You cannot add it again. The graph object was a " + graphClass.getName() + " with package implementation version of " + version);
		}
		getGraphMap().put(name, graph);
	}

	public FramedGraph<?> removeGraph(final String name) {
		return getGraphMap().remove(name);
	}

	public void init() {
		try {
			ODAPlatform.start();
			initDynamicGraphs();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		ODAPlatform.stop();
	}

	protected ThreadConfig getDataServiceConfig() {
		Fixes[] fixes = Fixes.values();
		AutoMime autoMime = AutoMime.WRAP_32K;
		boolean bubbleExceptions = false;
		return new ThreadConfig(fixes, autoMime, bubbleExceptions);
	}

	@Override
	public boolean beforeDoService(final HttpServletRequest request) {
		try {
			Factory.initThread(getDataServiceConfig());
			REQUEST_CTX.set(request);
			Factory.setSessionFactory(new DasCurrentSessionFactory(request), SessionType.CURRENT);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return true;
	}

	@Override
	public void afterDoService(final HttpServletRequest request) {
		Map<String, FramedGraph<?>> graphs = getGraphMap();
		for (FramedGraph graph : graphs.values()) {
			if (graph instanceof DFramedTransactionalGraph) {
				Object raw = ((DFramedTransactionalGraph) graph).getBaseGraph();
				if (raw instanceof DGraph) {
					((DGraph) raw).clearTransaction();
				}
			}
		}
		REQUEST_CTX.set(null);
		Factory.termThread();
	}

	//	@Override
	public void onError(final HttpServletRequest request, final Throwable t) {
		Factory.termThread();
		t.printStackTrace();
	}

	@Override
	public void onUnknownError(final HttpServletRequest request, final Throwable t) {
		Factory.termThread();
		t.printStackTrace();
	}

}
