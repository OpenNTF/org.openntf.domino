/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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
/**
 *
 */
package org.openntf.domino.ext;

import org.openntf.domino.DxlImporter.AclImportOption;
import org.openntf.domino.DxlImporter.DesignImportOption;
import org.openntf.domino.DxlImporter.DocumentImportOption;
import org.openntf.domino.DxlImporter.InputValidationOption;

/**
 * OpenNTF extensions to DxlImporter class
 * 
 * @author withersp
 *
 *
 */
public interface DxlImporter {

	/**
	 * Sets the ACL import options, using {@link org.openntf.domino.DxlImporter.AclImportOption}
	 *
	 * <p>
	 * E.g. ignore them, replace else create etc
	 * </p>
	 *
	 * @param option
	 *            AclImportOption to use when importing ACL entries
	 * @since org.openntf.domino 2.5.0
	 */
	public void setAclImportOption(final AclImportOption option);

	/**
	 * Sets the Design element import options, using {@link org.openntf.domino.DxlImporter.DesignImportOption}
	 *
	 * <p>
	 * E.g. ignore them, create, replace else create
	 * </p>
	 *
	 * @param option
	 *            DesignImportOption to use when importing design elements
	 * @since org.openntf.domino 2.5.0
	 */
	public void setDesignImportOption(final DesignImportOption option);

	/**
	 * Sets the Document element import options, using {@link org.openntf.domino.DxlImporter.DocumentImportOption}
	 *
	 * <p>
	 * E.g. ignore them, create, replace else create
	 * </p>
	 *
	 * @param option
	 *            DocumentImportOption to use when importing documents
	 * @since org.openntf.domino 2.5.0
	 */
	public void setDocumentImportOption(final DocumentImportOption option);

	/**
	 * Sets the options for how Input Validation formulas should be used when importing documents, using
	 * {@link org.openntf.domino.DxlImporter.InputValidationOption}
	 *
	 * <p>
	 * E.g. never validate, auto validate, always validate
	 * </p>
	 *
	 * @param option
	 *            InputValidationOption to use when importing documents
	 * @since org.openntf.domino 2.5.0
	 */
	public void setInputValidationOption(final InputValidationOption option);
}
