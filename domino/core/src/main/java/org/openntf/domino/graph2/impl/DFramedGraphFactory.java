package org.openntf.domino.graph2.impl;

import org.openntf.domino.graph2.annotations.AdjacencyHandler;
import org.openntf.domino.graph2.annotations.AdjacencyUniqueHandler;
import org.openntf.domino.graph2.annotations.IncidenceHandler;
import org.openntf.domino.graph2.annotations.IncidenceUniqueHandler;
import org.openntf.domino.graph2.annotations.PropertyHandler;
import org.openntf.domino.graph2.annotations.TypedPropertyHandler;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.FramedGraphConfiguration;
import com.tinkerpop.frames.FramedTransactionalGraph;
import com.tinkerpop.frames.annotations.DomainAnnotationHandler;
import com.tinkerpop.frames.annotations.InVertexAnnotationHandler;
import com.tinkerpop.frames.annotations.OutVertexAnnotationHandler;
import com.tinkerpop.frames.annotations.RangeAnnotationHandler;
import com.tinkerpop.frames.modules.Module;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerModule;

public class DFramedGraphFactory {
	protected Module[] modules;
	protected DConfiguration configuration_;

	public DFramedGraphFactory(final Module... modules) {
		this.modules = modules;
	}

	public DFramedGraphFactory(final DConfiguration configuration) {
		configuration_ = configuration;
		initConfiguration(configuration_);
	}

	/**
	 * Create a new {@link FramedGraph}.
	 * 
	 * @param baseGraph
	 *            The graph whose elements to frame.
	 * @return The {@link FramedGraph}
	 */
	public <T extends TransactionalGraph> FramedTransactionalGraph<T> create(final T baseGraph) {
		FramedGraphConfiguration config = getConfiguration(TransactionalGraph.class, baseGraph);
		FramedTransactionalGraph<T> framedGraph = new DFramedTransactionalGraph<T>(baseGraph, config);
		return framedGraph;
	}

	/**
	 * Returns a configuration that can be used when constructing a framed graph.
	 * 
	 * @param requiredType
	 *            The type of graph required after configuration e.g. {@link TransactionalGraph}
	 * @param baseGraph
	 *            The base graph to get a configuration for.
	 * @return The configuration.
	 */
	protected <T extends Graph> FramedGraphConfiguration getConfiguration(final Class<T> requiredType, final T baseGraph) {
		Graph configuredGraph = baseGraph;
		DConfiguration config;
		if (baseGraph instanceof DGraph) {
			config = (DConfiguration) ((DGraph) baseGraph).getConfiguration();
		} else {
			config = getBaseConfig();
		}
		if (modules == null) {
			Module module = configuration_.getModule();
			configuredGraph = module.configure(configuredGraph, config);
			configuredGraph = new JavaHandlerModule().configure(configuredGraph, config);
		} else {
			boolean hasJHM = false;
			for (Module module : modules) {
				if (module instanceof JavaHandlerModule)
					hasJHM = true;
				configuredGraph = module.configure(configuredGraph, config);
				if (!(requiredType.isInstance(configuredGraph))) {
					throw new UnsupportedOperationException("Module '" + module.getClass() + "' returned a '"
							+ baseGraph.getClass().getName() + "' but factory requires '" + requiredType.getName() + "'");
				}
			}
			if (!hasJHM) {
				configuredGraph = new JavaHandlerModule().configure(configuredGraph, config);
			}
		}
		config.setConfiguredGraph(configuredGraph);
		return config;
	}

	private DConfiguration getBaseConfig() {
		if (configuration_ == null) {
			DConfiguration configuration_ = new DConfiguration();
			initConfiguration(configuration_);
		}
		return configuration_;
	}

	private void initConfiguration(final DConfiguration config) {
		config.addMethodHandler(new PropertyHandler());
		config.addMethodHandler(new TypedPropertyHandler());
		config.addAnnotationHandler(new AdjacencyHandler());
		config.addAnnotationHandler(new AdjacencyUniqueHandler());
		config.addAnnotationHandler(new IncidenceHandler());
		config.addAnnotationHandler(new IncidenceUniqueHandler());
		config.addAnnotationHandler(new DomainAnnotationHandler());
		config.addAnnotationHandler(new RangeAnnotationHandler());
		config.addAnnotationHandler(new InVertexAnnotationHandler());
		config.addAnnotationHandler(new OutVertexAnnotationHandler());
	}

}
