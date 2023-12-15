/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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
package org.openntf.domino;

import org.openntf.domino.types.DocumentDescendant;
import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.Resurrectable;

/**
 * The Interface MIMEHeader.
 */
public interface MIMEHeader extends Base<lotus.domino.MIMEHeader>, lotus.domino.MIMEHeader, org.openntf.domino.ext.MIMEHeader,
		DocumentDescendant, Resurrectable {

	public static class Schema extends FactorySchema<MIMEHeader, lotus.domino.MIMEHeader, MIMEEntity> {
		@Override
		public Class<MIMEHeader> typeClass() {
			return MIMEHeader.class;
		}

		@Override
		public Class<lotus.domino.MIMEHeader> delegateClass() {
			return lotus.domino.MIMEHeader.class;
		}

		@Override
		public Class<MIMEEntity> parentClass() {
			return MIMEEntity.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEHeader#addValText(java.lang.String)
	 */
	@Override
	public boolean addValText(final String valueText);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEHeader#addValText(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addValText(final String valueText, final String charSet);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEHeader#getHeaderName()
	 */
	@Override
	public String getHeaderName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEHeader#getHeaderVal()
	 */
	@Override
	public String getHeaderVal();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEHeader#getHeaderVal(boolean)
	 */
	@Override
	public String getHeaderVal(final boolean folded);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEHeader#getHeaderVal(boolean, boolean)
	 */
	@Override
	public String getHeaderVal(final boolean folded, final boolean decoded);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEHeader#getHeaderValAndParams()
	 */
	@Override
	public String getHeaderValAndParams();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEHeader#getHeaderValAndParams(boolean)
	 */
	@Override
	public String getHeaderValAndParams(final boolean folded);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEHeader#getHeaderValAndParams(boolean, boolean)
	 */
	@Override
	public String getHeaderValAndParams(final boolean folded, final boolean decoded);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEHeader#getParamVal(java.lang.String)
	 */
	@Override
	public String getParamVal(final String paramName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEHeader#getParamVal(java.lang.String, boolean)
	 */
	@Override
	public String getParamVal(final String paramName, final boolean folded);

	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	@Override
	public MIMEEntity getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEHeader#remove()
	 */
	@Override
	public void remove();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEHeader#setHeaderVal(java.lang.String)
	 */
	@Override
	public boolean setHeaderVal(final String headerValue);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEHeader#setHeaderValAndParams(java.lang.String)
	 */
	@Override
	public boolean setHeaderValAndParams(final String headerParamValue);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEHeader#setParamVal(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean setParamVal(final String parameterName, final String parameterValue);

}
