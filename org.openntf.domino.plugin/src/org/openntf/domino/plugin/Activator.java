package org.openntf.domino.plugin;

import org.openntf.domino.View;
import org.openntf.domino.exceptions.BackendBridgeSanityCheckException;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.ibm.domino.napi.c.BackendBridge;

public class Activator implements BundleActivator {

	/**
	 * there is one weird thing in getViewEntryByKeyWithOptions. IBM messed up something in the JNI calls.
	 * 
	 * a correct call would look like this:
	 * 
	 * <pre>
	 * jclass activityClass = env->GetObjectClass(dummyView); 
	 * jmethodID mID = env->GetMethodID(activityClass, "iGetEntryByKey", "..."); 
	 * entry = env->CallIntMethod(obj, mID);
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
	 * <li>If any other method is called it will throw a different exception. (Most likely a NPE, because our view has no delegate)</li>
	 * </ol>
	 * I hope the server would not crash then. I assume this because:
	 * <ul>
	 * <li>null as parameter is less problematic than a Vector that was forced in a String variable</li>
	 * <li>Throwing an exception does not generate a return value that will be forced in a ViewEntry</li>
	 * </ul>
	 */
	private void verifyIGetEntryByKey() {
		View dummyView = new org.openntf.domino.impl.View();
		try {
			BackendBridge.getViewEntryByKeyWithOptions(dummyView, null, 42);
		} catch (BackendBridgeSanityCheckException allGood) {
			System.out.println("Operation of BackendBridge.getViewEntryByKeyWithOptions verified");
			return;
		} catch (Exception e) {
			e.printStackTrace();
			// if you get here, analyze the stack trace and rearrange the "iGetEntryByKey" method in
			// the view to the position that is listed in the stack trace above "getViewEntryByKeyWithOptions"
		}
		// if you do not get an exception, you will have to debug it with "step into"
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println("Operation of BackendBridge.getViewEntryByKeyWithOptions FAILED");
		System.out.println("Please read the comments in " + getClass().getName());
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(final BundleContext context) throws Exception {

		System.out.println("Starting OpenNTF Domino API");
		verifyIGetEntryByKey();
		//org.openntf.domino.impl.Base.setNapiFactory(new NapiFactory());
		//System.out.println("Using NAPI");
		//		Factory.setClassLoader(Thread.currentThread().getContextClassLoader());

	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(final BundleContext context) throws Exception {
		System.out.println("Stopping OpenNTF Domino API");
	}

}
