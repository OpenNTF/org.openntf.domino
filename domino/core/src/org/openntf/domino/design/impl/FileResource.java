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

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.utils.DominoUtils;

/**
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public final class FileResource extends AbstractDesignFileResource implements org.openntf.domino.design.FileResource, HasMetadata {
	private static final long serialVersionUID = 1L;

	protected FileResource(final Document document) {
		super(document);
	}

	@Override
	protected boolean enforceRawFormat() {
		return false;
	}

	protected FileResource(final Database database) {
		super(database);
	}

	protected FileResource(final Database database, final String dxlResource) {
		super(database);

		try {
			InputStream is = DesignView.class.getResourceAsStream(dxlResource);
			loadDxl(is);
			is.close();
		} catch (IOException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public Collection<String> getItemNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDeployable(final boolean deployable) {
		// TODO Auto-generated method stub

	}

}