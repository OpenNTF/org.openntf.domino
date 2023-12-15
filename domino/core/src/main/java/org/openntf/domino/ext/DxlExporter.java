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
