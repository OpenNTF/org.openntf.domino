/*
 * © Copyright Foconis AG, 2013
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
package org.openntf.junit4xpages;

import com.ibm.xsp.library.AbstractXspLibrary;

/**
 * 
 * @author Alexander Wagner, FOCONIS AG
 * 
 */
public class Library extends AbstractXspLibrary {

	@Override
	public String[] getDependencies() {
		return new String[] { "com.ibm.xsp.core.library", "com.ibm.xsp.domino.library", "com.ibm.xsp.designer.library" };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.library.XspLibrary#getLibraryId()
	 */
	@Override
	public String getLibraryId() {
		return Library.class.getName();
	}

	@Override
	public String getPluginId() {
		return Library.class.getPackage().getName();
	}

}