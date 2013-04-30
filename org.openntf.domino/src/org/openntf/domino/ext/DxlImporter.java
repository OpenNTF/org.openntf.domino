/**
 * 
 */
package org.openntf.domino.ext;

import org.openntf.domino.DxlImporter.AclImportOption;
import org.openntf.domino.DxlImporter.DesignImportOption;
import org.openntf.domino.DxlImporter.DocumentImportOption;
import org.openntf.domino.DxlImporter.InputValidationOption;

/**
 * @author withersp
 * 
 */
public interface DxlImporter {
	public void setAclImportOption(AclImportOption option);

	public void setDesignImportOption(DesignImportOption option);

	public void setDocumentImportOption(DocumentImportOption option);

	public void setInputValidationOption(InputValidationOption option);
}
