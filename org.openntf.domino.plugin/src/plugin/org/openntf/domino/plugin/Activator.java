package org.openntf.domino.plugin;

import org.eclipse.core.runtime.Plugin;
import org.openntf.domino.utils.Factory;
import org.osgi.framework.BundleContext;

public class Activator extends Plugin { // TODO: Warum nicht bundleActivator

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		System.out.println("Loading OpenNTF Domino API");
		Factory.setClassLoader(Thread.currentThread().getContextClassLoader());
		System.out.println("... done");
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		System.out.println("Stopping OpenNTF Domino API");
	}

}
