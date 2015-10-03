/*
 * � Copyright IBM Corp. 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.openntf.domino.xsp.config;

import com.ibm.xsp.extlib.config.ExtlibPluginConfig;

/**
 * @author Paul Withers
 * 
 *         Loads Domino Picker configurations
 */
public class DominoConfig extends ExtlibPluginConfig {
	public DominoConfig() {
	}

	// ===============================================================
	// Compose the lists of extra config files
	// ===============================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.extlib.config.ExtlibPluginConfig#getXspConfigFiles(java.lang.String[])
	 */
	@Override
	// TODO: Remove before 3.0 - all functionality introduced in ExtLib 14
	public String[] getXspConfigFiles(final String[] files) {
		return concat(files, new String[] { "org/openntf/domino/xsp/config/openntf-domino-picker.xsp-config", // $NON-NLS-1$
				"org/openntf/domino/xsp/config/openntf-dominodocument.xsp-config",// $NON-NLS-1$
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.extlib.config.ExtlibPluginConfig#getFacesConfigFiles(java.lang.String[])
	 */
	@Override
	public String[] getFacesConfigFiles(final String[] files) {
		return concat(files, new String[] {});
	}
}
