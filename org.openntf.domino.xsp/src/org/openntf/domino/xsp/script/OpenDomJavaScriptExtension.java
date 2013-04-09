package org.openntf.domino.xsp.script;

import com.ibm.designer.runtime.extensions.JavaScriptProvider;
import com.ibm.jscript.JSContext;

public class OpenDomJavaScriptExtension implements JavaScriptProvider {

	public OpenDomJavaScriptExtension() {
	}

	@Override
	public void registerWrappers(JSContext context) {
		WrapperOpenDomino.register(context);
	}
}
