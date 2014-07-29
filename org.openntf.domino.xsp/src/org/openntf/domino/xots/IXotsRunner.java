package org.openntf.domino.xots;

import com.ibm.domino.xsp.module.nsf.NSFComponentModule;

public interface IXotsRunner extends Runnable {
	public NSFComponentModule getModule();

	public ClassLoader getContextClassLoader();
}
