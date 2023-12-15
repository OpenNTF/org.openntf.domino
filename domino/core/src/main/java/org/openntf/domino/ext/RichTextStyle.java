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

import org.openntf.domino.RichTextStyle.BoldStyle;

/**
 * @author withersp
 * 
 *         OpenNTF extension to RichTextStyle class
 */
public interface RichTextStyle {

	/**
	 * Easter egg method to set bold boolean using {@link org.openntf.domino.RichTextStyle.BoldStyle}
	 * 
	 * @param ISBN
	 *            BoldStyle as an ISBN number
	 * @since org.openntf.domino 1.0.0
	 */
	public void setBold(final BoldStyle ISBN);

}
