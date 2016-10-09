package com.tinkerpop.frames;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.frames.annotations.AnnotationHandler;
import com.tinkerpop.frames.modules.MethodHandler;
import com.tinkerpop.frames.modules.Module;
import com.tinkerpop.frames.modules.TypeResolver;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A configuration for a {@link FramedGraph}. These are supplied to
 * {@link Module}s for each {@link FramedGraph} being create by a
 * {@link FramedGraphFactory}.
 * 
 * Allows registration of {@link AnnotationHandler}s, {@link FrameInitializer}s
 * and {@link TypeResolver}s.
 * 
 * @author Bryn Cooke
 * 
 */
public class FramedGraphConfiguration {
	private Map<Class<? extends Annotation>, AnnotationHandler<?>> annotationHandlers = new HashMap<Class<? extends Annotation>, AnnotationHandler<?>>();
	private Map<Class<? extends Annotation>, MethodHandler<?>> methodHandlers = new HashMap<Class<? extends Annotation>, MethodHandler<?>>();
	private List<FrameInitializer> frameInitializers = new ArrayList<FrameInitializer>();
	private List<TypeResolver> typeResolvers = new ArrayList<TypeResolver>();
	private Graph configuredGraph;

	/**
	 * @param annotationHandler
	 *            The {@link AnnotationHandler} to add to the
	 *            {@link FramedGraph}.
	 */
	public void addAnnotationHandler(AnnotationHandler<?> annotationHandler) {
		annotationHandlers.put(annotationHandler.getAnnotationType(), annotationHandler);
	}

	/**
	 * @param methodHandler
	 *            The {@link MethodHandler} to add to the {@link FramedGraph}.
	 */
	public void addMethodHandler(MethodHandler<?> methodHandler) {
		methodHandlers.put(methodHandler.getAnnotationType(), methodHandler);
	}

	/**
	 * @param frameInitializer
	 *            The {@link FrameInitializer} to add to the {@link FramedGraph}
	 *            .
	 */
	public void addFrameInitializer(FrameInitializer frameInitializer) {
		frameInitializers.add(frameInitializer);
	}

	/**
	 * @param typeResolver
	 *            The {@link TypeResolver} to add to the {@link FramedGraph}.
	 */
	public void addTypeResolver(TypeResolver typeResolver) {
		typeResolvers.add(typeResolver);
	}

	public List<FrameInitializer> getFrameInitializers() {
		return frameInitializers;
	}

	public Map<Class<? extends Annotation>, AnnotationHandler<?>> getAnnotationHandlers() {
		return annotationHandlers;
	}

	public List<TypeResolver> getTypeResolvers() {
		return typeResolvers;
	}

	public void setConfiguredGraph(Graph configuredGraph) {
		this.configuredGraph = configuredGraph;
	}

	public Graph getConfiguredGraph() {
		return configuredGraph;
	}

	public Map<Class<? extends Annotation>, MethodHandler<?>> getMethodHandlers() {
		return methodHandlers;
	}
}
