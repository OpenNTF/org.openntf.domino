package org.openntf.domino.xsp;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;

import org.openntf.domino.View;
import org.openntf.domino.exceptions.BackendBridgeSanityCheckException;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xots.XotsDaemon;
import org.openntf.domino.xsp.helpers.XPageSessionFactory;
import org.openntf.domino.xsp.xots.XotsDominoExecutor;
import org.openntf.service.IServiceLocator;
import org.openntf.service.IServiceLocatorFactory;
import org.openntf.service.ServiceLocatorFinder;

import com.ibm.commons.extension.ExtensionManager;
import com.ibm.domino.napi.c.BackendBridge;
import com.ibm.xsp.application.ApplicationEx;

public enum ODAPlatform {
	;

	public static class OsgiServiceLocatorFactory implements IServiceLocatorFactory {

		@Override
		public IServiceLocator createServiceLocator() {
			return AccessController.doPrivileged(new PrivilegedAction<IServiceLocator>() {
				@Override
				public IServiceLocator run() {
					ApplicationFactory aFactory = (ApplicationFactory) FactoryFinder
							.getFactory("javax.faces.application.ApplicationFactory");
					final ApplicationEx app_ = aFactory == null ? null : (ApplicationEx) aFactory.getApplication();

					if (app_ == null) {
						return new IServiceLocator() {
							final Map<Class<?>, List<?>> cache = new HashMap<Class<?>, List<?>>();

							@Override
							public <T> List<T> findApplicationServices(final Class<T> serviceClazz) {
								List<T> ret = (List<T>) cache.get(serviceClazz);

								if (ret == null) {
									ret = AccessController.doPrivileged(new PrivilegedAction<List<T>>() {
										@Override
										public List<T> run() {
											return (List<T>) ExtensionManager.findApplicationServices(null, Thread.currentThread()
													.getContextClassLoader(), serviceClazz.getName());
										}
									});
									if (Comparable.class.isAssignableFrom(serviceClazz)) {
										Collections.sort((List<? extends Comparable>) ret);
									}
									cache.put(serviceClazz, ret);
								}
								return ret;
							}
						};

					} else {

						return new IServiceLocator() {
							final Map<Class<?>, List<?>> cache = new HashMap<Class<?>, List<?>>();

							@Override
							public <T> List<T> findApplicationServices(final Class<T> serviceClazz) {
								List<T> ret = (List<T>) cache.get(serviceClazz);

								if (ret == null) {
									ret = AccessController.doPrivileged(new PrivilegedAction<List<T>>() {
										@Override
										public List<T> run() {
											return app_.findServices(serviceClazz.getName());
										}
									});
									if (Comparable.class.isAssignableFrom(serviceClazz)) {
										Collections.sort((List<? extends Comparable>) ret);
									}
									cache.put(serviceClazz, ret);
								}
								return ret;
							}
						};
					}

				}
			});
		}

	}

	public static void start() {
		// Here is all the init/term stuff done
		ServiceLocatorFinder.setServiceLocatorFactory(new OsgiServiceLocatorFactory());
		Factory.startup();

		// Setup the named factories 4 XPages
		Factory.setNamedFactories4XPages(new XPageSessionFactory(false), new XPageSessionFactory(true));
		verifyIGetEntryByKey();

		DominoExecutor executor = new XotsDominoExecutor(50);
		XotsDaemon.start(executor);

	}

	public static void stop() {
		XotsDaemon.stop(15);
		Factory.shutdown();

	}

	/**
	 * there is one weird thing in getViewEntryByKeyWithOptions. IBM messed up something in the JNI calls.
	 * 
	 * a correct call would look like this:
	 * 
	 * <pre>
	 * jclass activityClass = env -&gt; GetObjectClass(dummyView);
	 * jmethodID mID = env -&gt; GetMethodID(activityClass, &quot;iGetEntryByKey&quot;, &quot;...&quot;);
	 * entry = env -&gt; CallIntMethod(obj, mID);
	 * </pre>
	 * 
	 * IBM's code probably looks like this:
	 * 
	 * <pre>
	 * jclass activityClass = env->GetObjectClass(lotus.domino.local.View); <font color=red>&lt;--- This is wrong!</font>
	 * jmethodID mID = env->GetMethodID(activityClass, "iGetEntryByKey", "..."); 
	 * entry = env->CallIntMethod(obj, mID);
	 * </pre>
	 * 
	 * so we get the method-pointer mID for the "lotus.domino.local.View" and we call this method on an "org.openntf.domino.impl.View".
	 * 
	 * This is something that normally wouldn't work. But C/C++ does no sanity checks if it operates on the correct class and will call a
	 * (more or less) random method that is on position "mID". (compare to a 'goto 666')
	 * 
	 * To get that working, we must reorder the methods in the View class, so that "iGetEntryByKey" is on the correct place. Every time you
	 * add or remove methods to the View class (and maybe also to the Base class) the position must be checked again. This is done in the
	 * this method:
	 * <ol>
	 * <li>
	 * We call getViewEntryByKeyWithOptions with the "key parameters" dummyView, null, 42.</li>
	 * <li>This will result in a call to dummyView.iGetEntryByKey(null, false, 42);</li>
	 * <li>If iGetEntryByKey is called with a "null" vector and 42 as int, it will throw a "BackendBridgeSanityCheckException" (which we
	 * expect)</li>
	 * <li>If any other mehtod is called it will throw a different exception. (Most likely a NPE, because our view has no delegate)</li>
	 * </ol>
	 * I hope the server would not crash then. I assume this because:
	 * <ul>
	 * <li>null as parameter is less problematic than a Vector that was forced in a String variable</li>
	 * <li>Throwing an exception does not generate a return value that will be forced in a ViewEntry</li>
	 * </ul>
	 */
	private static void verifyIGetEntryByKey() {
		@SuppressWarnings("deprecation")
		View dummyView = new org.openntf.domino.impl.View();
		try {
			BackendBridge.getViewEntryByKeyWithOptions(dummyView, null, 42);
		} catch (BackendBridgeSanityCheckException allGood) {
			Factory.println("Operation of BackendBridge.getViewEntryByKeyWithOptions verified");
			return;
		} catch (Exception e) {
			e.printStackTrace();
			// if you get here, analyze the stack trace and rearrange the "iGetEntryByKey" method in
			// the view to the position that is listed in the stack trace above "getViewEntryByKeyWithOptions"
		}
		// if you do not get an exception, you will have to debug it with "step into"
		Factory.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		Factory.println("Operation of BackendBridge.getViewEntryByKeyWithOptions FAILED");
		Factory.println("Please read the comments in " + ODAPlatform.class.getName());
		Factory.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}
}
