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

package org.openntf.domino.design;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 * @author jgallagher
 * 
 */
public interface AnyFileResource extends DesignBaseNamed, org.openntf.domino.types.DatabaseDescendant {

	/**
	 * @return the file resource's data as a byte array
	 */
	public void getFileData(OutputStream os) throws IOException;

	/**
	 * Get the fileData as Byte-Array.Internally it reads the data with {@link #getFileData(OutputStream)} in a
	 * {@link ByteArrayOutputStream}
	 * 
	 * @return the fileData as Byte-Array
	 */
	public byte[] getFileData();

	/**
	 * @param fileData
	 *            The new data for the file resource, as a byte array
	 */
	public void setFileData(byte[] fileData) throws IOException;

	/**
	 * @return mime type
	 */
	public String getMimeType();

	/**
	 * @param mimeType
	 *            The new MIME type for the file resource
	 */
	public void setMimeType(final String mimeType);

	/**
	 * @return whether the file resource is marked as read-only
	 */
	public boolean isReadOnly();

	/**
	 * @return whether the file resource is marked as "deployable", whatever that means <br/>
	 *         RPr: This means, if it is a WebResource. So a setter makes no sense
	 */
	public boolean isDeployable();

	//	/**
	//	 * @return whether the file resource is hidden from the design list in Designer (e.g. WebContent files)
	//	 */
	//	public boolean isHideFromDesignList();

	public void setReadOnly(final boolean readOnly);

	public void setDeployable(final boolean deployable);

	//	public void setHideFromDesignList(final boolean hideFromDesignList);
}
