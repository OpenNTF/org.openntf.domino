/**
 * 
 */
package org.openntf.domino.ext;

import org.openntf.domino.DxlExporter.MIMEOption;
import org.openntf.domino.DxlExporter.RichTextOption;

/**
 * @author withersp
 * 
 */
public interface DxlExporter {

	public void setMIMEOption(MIMEOption option);

	public void setRichTextOption(RichTextOption option);

}
