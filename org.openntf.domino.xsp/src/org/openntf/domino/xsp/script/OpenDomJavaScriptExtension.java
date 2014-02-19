package org.openntf.domino.xsp.script;

import org.openntf.domino.xsp.helpers.OpenntfNABNamePickerData;

import com.ibm.designer.runtime.extensions.JavaScriptProvider;
import com.ibm.jscript.JSContext;
import com.ibm.xsp.extlib.component.picker.data.DominoNABNamePickerData;

public class OpenDomJavaScriptExtension implements JavaScriptProvider {

	public OpenDomJavaScriptExtension() {
	}

	@Override
	public void registerWrappers(final JSContext context) {
		WrapperOpenDomino.register(context);

		try {
			DominoNABNamePickerData dd = new DominoNABNamePickerData();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		try {
			OpenntfNABNamePickerData d = new OpenntfNABNamePickerData();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
