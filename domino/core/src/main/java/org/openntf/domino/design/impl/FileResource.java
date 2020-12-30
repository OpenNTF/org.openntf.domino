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
package org.openntf.domino.design.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.utils.DominoUtils;

public final class FileResource extends AbstractDesignFileResource implements org.openntf.domino.design.FileResource, HasMetadata {
	private static final long serialVersionUID = 1L;

	//	private static final long serialVersionUID = 1L;
	//	@SuppressWarnings("unused")
	//	private static final Logger log_ = Logger.getLogger(FileResource.class.getName());
	//
	//	private static final char DESIGN_FLAGEXT_FILE_DEPLOYABLE = 'D';
	//	private static final char DESIGN_FLAG_HIDEFROMDESIGNLIST = '~';
	//	private static final char DESIGN_FLAG_READONLY = '&';
	//
	//	private static final String DEFAULT_FILEDATA_FIELD = "$FileData";
	//	private static final String MIMETYPE_FIELD = "$MimeType";
	//
	protected FileResource(final Document document) {
		super(document);
	}

	@Override
	protected boolean enforceRawFormat() {
		return false;
	}

	protected FileResource(final Database database) {
		super(database);

		try {
			InputStream is = DesignView.class.getResourceAsStream("/org/openntf/domino/design/impl/dxl_fileresource.xml"); //$NON-NLS-1$
			loadDxl(is);
			is.close();
		} catch (IOException e) {
			DominoUtils.handleException(e);
		}
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
		// TODO Implement this
		return null;
	}

	@Override
	public void setDeployable(final boolean deployable) {
		// TODO Implement this

	}

}