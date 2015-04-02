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

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.openntf.domino.design.DxlConverter;

/**
 * @author jgallagher
 * 
 */
public final class CustomControl extends AbstractXspResource implements org.openntf.domino.design.CustomControl, HasMetadata, HasXspConfig {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(CustomControl.class.getName());

	@Override
	protected boolean enforceRawFormat() {
		// CustomControl is exported in RAW-format. There is no DXL representation
		return true;
	}

	@Override
	public void exportXspConfig(final DxlConverter converter, final File configFile) throws IOException {
		converter.writeXspConfigFile(configFile, getConfigData());
	}

	protected byte[] getConfigData() {
		return getFileDataRaw(DEFAULT_CONFIGDATA_FIELD);
	}

	@Override
	public void importXspConfig(final DxlConverter converter, final File configFile) throws IOException {
		setConfigData(converter.readXspConfigFile(configFile));
	}

	protected void setConfigData(final byte[] data) {
		setFileDataRaw(DEFAULT_CONFIGDATA_FIELD, data);
	}

	@Override
	public void exportDesign(final DxlConverter converter, final File file) throws IOException {
		converter.writeXspFile(file, getFileData());
	}

	@Override
	public void importDesign(final DxlConverter converter, final File file) throws IOException {
		setFileData(converter.readXspFile(file));
	}

}
