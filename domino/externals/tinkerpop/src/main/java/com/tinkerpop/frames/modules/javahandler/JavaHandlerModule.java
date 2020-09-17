package com.tinkerpop.frames.modules.javahandler;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.FramedGraphConfiguration;
import com.tinkerpop.frames.modules.Module;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.ExecutionException;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.Proxy;
import javassist.util.proxy.ProxyFactory;

/**
 * <p>
 * Adds support for calling Java methods to handle a frames call. A call to a
 * frames method annotated with {@link JavaHandler} will cause the supplied
 * factory to instantiate an instance of the handler class and call the
 * appropriate method on it.
 * </p>
 * <p>
 * The default implementation is an inner class of the frame interface named
 * 'Impl'. For example:
 * </p>
 * 
 * <pre>
 * interface Person {
 * 
 * 	&#064;JavaHandler
 * 	public String doSomething();
 * 
 * 	abstract class Impl implements Person, JavaHandlerContext&lt;Vertex&gt; {
 * 		public String doSomething() {
 * 			return &quot;Use Frames!&quot;;
 * 		}
 * 	}
 * 
 * }
 * 
 * </pre>
 * <p>
 * The implementation class can be overridden by using the
 * {@link JavaHandlerClass} annotation.
 * </p>
 * 
 * 
 * @author Bryn Cooke
 */
@SuppressWarnings({ "unchecked", "nls" })
public class JavaHandlerModule implements Module {

	// We don't want to use the global class cache. Instead we cache the classes
	// at the module level.
	// Maps from frameClass -> proxy class.
	private LoadingCache<Class<?>, Class<?>> classCache = CacheBuilder.newBuilder().build(
			new CacheLoader<Class<?>, Class<?>>() {

				@Override
				public Class<?> load(final Class<?> frameClass) throws Exception {
					final Class<?> handlerClass = getHandlerClass(frameClass);
					ProxyFactory proxyFactory = new ProxyFactory() {
						@Override
						protected ClassLoader getClassLoader() {

							return new URLClassLoader(new URL[0], handlerClass.getClassLoader());
						}
					};
					proxyFactory.setUseCache(false);
					proxyFactory.setSuperclass(handlerClass);
					return proxyFactory.createClass();
				}
			});

	private JavaHandlerFactory factory = new JavaHandlerFactory() {

		@Override
		public <T> T create(Class<T> handlerClass) throws InstantiationException, IllegalAccessException {
			return handlerClass.newInstance();
		}

	};

	/**
	 * Provide an alternative factory for creating objects that handle frames
	 * calls.
	 * 
	 * @param factory
	 *            The factory to use.
	 * @return The module.
	 */
	public JavaHandlerModule withFactory(JavaHandlerFactory factory) {
		this.factory = factory;
		return this;
	}

	@Override
	public Graph configure(Graph baseGraph, FramedGraphConfiguration config) {

		config.addMethodHandler(new JavaMethodHandler(this));
		config.addFrameInitializer(new JavaFrameInitializer(this));
		return baseGraph;
	}

	Class<?> getHandlerClass(Class<?> frameClass) throws ClassNotFoundException {
		JavaHandlerClass handlerClass = frameClass.getAnnotation(JavaHandlerClass.class);
		if (handlerClass != null) {
			return handlerClass.value();
		}
		return frameClass.getClassLoader().loadClass(frameClass.getName() + "$Impl");
	}

	<T> T createHandler(final Object framedElement, final FramedGraph<?> graph, final Element element,
			Class<?> frameClass, final Method method) {
		// System.out.println("TEMP DEBUG creating new Handler for method " +
		// method.getName() + " on object "
		// + System.identityHashCode(framedElement));
		try {
			Class<T> implClass = (Class<T>) classCache.get(frameClass);
			T handler = factory.create(implClass);
			((Proxy) handler).setHandler(new MethodHandler() {
				private JavaHandlerContextImpl<Element> defaultJavahandlerImpl = new JavaHandlerContextImpl<Element>(
						graph, method, element);

				@Override
				public Object invoke(Object o, Method m, Method proceed, Object[] args) throws Throwable {
					if (!Modifier.isAbstract(m.getModifiers())) {
						try {
							return proceed.invoke(o, args);
						} catch (Throwable t) {
							System.out.println("Exception processing a method called " + m.getName()
									+ " with an object of type " + o.getClass().getName());
							t.printStackTrace();
							return null;
						}
					} else {
						if (m.getAnnotation(JavaHandler.class) != null) {
							throw new JavaHandlerException("Method " + m
									+ " is marked with @JavaHandler but is not implemented");
						}
						if (m.getDeclaringClass() == JavaHandlerContext.class) {
							// System.out.println("TEMP DEBUG using JavaHandlerImpl strategy...");
							return m.invoke(defaultJavahandlerImpl, args);
						}
						// System.out.println("TEMP DEBUG using direct invocation strategy for method "
						// + m.getName()
						// + " from " + m.getDeclaringClass().getName() +
						// " and object is "
						// + System.identityHashCode(framedElement));
						return m.invoke(framedElement, args);
					}
				}

			});
			return handler;
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			if (cause instanceof ClassNotFoundException) {
				throw new JavaHandlerException("Problem locating handler class for " + frameClass, e);
			} else {
				throw new JavaHandlerException("Cannot create class for handling framed method", cause);
			}
		} catch (InstantiationException e) {
			throw new JavaHandlerException("Problem instantiating handler class", e);
		} catch (IllegalAccessException e) {
			throw new JavaHandlerException("Problem instantiating handler class", e);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new JavaHandlerException("Problem instantiating handler class", t);
		}

	}

}
