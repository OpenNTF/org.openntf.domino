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
/**
 * 
 */
package org.openntf.domino.ext;

import org.openntf.domino.RichTextParagraphStyle.Align;
import org.openntf.domino.Session;

/**
 * @author withersp
 * 
 *         OpenNTF extensions for RichTextParagraphStyle class
 */
public interface RichTextParagraphStyle {

	/**
	 * Gets the parent Session of the RichTextParagraphStyle
	 * 
	 * @return parent Session
	 * @since org.openntf.domino 1.0.0
	 */
	public Session getParent();

	/**
	 * Sets the alignment using {@link org.openntf.domino.RichTextParagraphStyle.Align}
	 * 
	 * @param value
	 *            Align alignment
	 * @since org.openntf.domino 1.0.0
	 */
	public void setAlignment(final Align value);

}
