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
import java.io.OutputStream;
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
	public void exportXspConfig(final DxlConverter converter, final OutputStream os) throws IOException {
		getConfigData(converter.writeXspConfigFile(os));
	}

	protected void getConfigData(final OutputStream os) throws IOException {
		throw new UnsupportedOperationException(); // TODO
		//getFileDataRaw(DEFAULT_CONFIGDATA_FIELD, os);
	}

	@Override
	public void importXspConfig(final DxlConverter converter, final InputStream is) throws IOException {
		setConfigData(converter.readXspConfigFile(is));
	}

	protected void setConfigData(final InputStream is) throws IOException {
		throw new UnsupportedOperationException(); // TODO
		//setFileDataRaw(DEFAULT_CONFIGDATA_FIELD, is);
	}

	@Override
	public void exportDesign(final DxlConverter converter, final OutputStream os) throws IOException {
		getFileData(converter.writeXspFile(os));
	}

	@Override
	public void importDesign(final DxlConverter converter, final InputStream is) throws IOException {
		setFileData(toBytes(converter.readXspFile(is)));
	}

	@Override
	protected String getDefaultFlags() {
		// TODO Auto-generated method stub
		return "gC~4;";
	}

	@Override
	protected String getDefaultFlagsExt() {
		// TODO Auto-generated method stub
		return "";
	}

}
