/**
 *
 */
package org.openntf.domino.ext;

import org.openntf.domino.DxlExporter.MIMEOption;
import org.openntf.domino.DxlExporter.RichTextOption;

/**
 * OpenNTF extension to DxlExporter class
 * 
 * @author withersp
 *
 *
 */
public interface DxlExporter {

	/**
	 * Sets how MIME should be exported, either as DXL or as raw content, using {@link org.openntf.domino.DxlExporter.MIMEOption}
	 *
	 * @param option
	 *            MIMEOption to apply
	 * @since org.openntf.domino 4.5.0
	 */
	public void setMIMEOption(MIMEOption option);

	// PW TODO: Add getters to return MIME options

	/**
	 * Sets how Rich Text should be exported, either as DXL or as raw content, using {@link org.openntf.domino.DxlExporter.RichTextOption}
	 *
	 * @param option
	 *            RichTextOption to apply
	 * @since org.openntf.domino 4.5.0
	 */
	public void setRichTextOption(RichTextOption option);

}
