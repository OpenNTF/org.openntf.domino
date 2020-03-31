/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
