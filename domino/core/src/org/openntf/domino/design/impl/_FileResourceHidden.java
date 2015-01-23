/*
 * Copyright 2013
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

package org.openntf.domino.design.impl;

import org.openntf.domino.Document;

/**
 * @author Roland Praml
 * 
 */
public class _FileResourceHidden extends AbstractDesignFileResource/* implements TODO */{
	private static final long serialVersionUID = 1L;

	/**
	 * @param document
	 */
	protected _FileResourceHidden(final Document document) {
		super(document);
	}

	@Override
	public String getOnDiskFolder() {
		return "";
	}

	@Override
	public String getOnDiskExtension() {
		return "";
	}

	@Override
	protected boolean mustEncode(final String resName) {
		return false;
	}

	public void setDeployable(boolean deployable) {
		// TODO Auto-generated method stub
		
	}
}
