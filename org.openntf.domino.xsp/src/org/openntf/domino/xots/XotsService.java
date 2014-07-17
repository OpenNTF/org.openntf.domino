package org.openntf.domino.xots;

import java.io.IOException;

import javax.servlet.ServletException;

import com.ibm.designer.runtime.domino.adapter.ComponentModule;
import com.ibm.designer.runtime.domino.adapter.LCDEnvironment;
import com.ibm.designer.runtime.domino.bootstrap.adapter.HttpServletRequestAdapter;
import com.ibm.designer.runtime.domino.bootstrap.adapter.HttpServletResponseAdapter;
import com.ibm.designer.runtime.domino.bootstrap.adapter.HttpSessionAdapter;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NSFService;

public class XotsService extends NSFService {

	public XotsService(final LCDEnvironment arg0) {
		super(arg0);
	}

	@Override
	public int getPriority() {
		return 100;
	}

	@Override
	protected NSFComponentModule createNSFModule(final String arg0) throws ServletException {
		System.out.println("DEBUG: create NSF module for path " + arg0);
		return super.createNSFModule(arg0);
	}

	@Override
	public ComponentModule getComponentModule(final String arg0) throws ServletException {
		return super.createNSFModule(arg0);
	}

	@Override
	public boolean doService(final String arg0, final String arg1, final HttpSessionAdapter arg2, final HttpServletRequestAdapter arg3,
			final HttpServletResponseAdapter arg4) throws ServletException, IOException {
		System.out.println("DEBUG ALERT!! XotsService has been asked to service an HttpRequest!");
		return super.doService(arg0, arg1, arg2, arg3, arg4);
	}

}
