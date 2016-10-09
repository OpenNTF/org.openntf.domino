package org.openntf.domino.xsp.script;

import org.openntf.domino.xsp.helpers.OpenntfNABNamePickerData;

import com.ibm.designer.runtime.extensions.JavaScriptProvider;
import com.ibm.jscript.JSContext;
import com.ibm.xsp.extlib.component.picker.data.DominoNABNamePickerData;

public class OpenDomJavaScriptExtension implements JavaScriptProvider {

	/**
	 * Constructor
	 */
	public OpenDomJavaScriptExtension() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.designer.runtime.extensions.JavaScriptProvider#registerWrappers(com.ibm.jscript.JSContext)
	 */
	@Override
	public void registerWrappers(final JSContext context) {
		WrapperOpenDomino.register(context);

		// RPr: Don't know exactly why we do that
		try {
			new DominoNABNamePickerData();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		try {
			new OpenntfNABNamePickerData();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
