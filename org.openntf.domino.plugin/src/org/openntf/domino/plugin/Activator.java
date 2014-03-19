package org.openntf.domino.plugin;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(final BundleContext context) throws Exception {

		System.out.println("Starting OpenNTF Domino API");
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
