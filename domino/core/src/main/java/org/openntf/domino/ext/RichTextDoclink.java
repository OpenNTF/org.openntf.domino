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

/**
 * @author withersp
 * 
 *         OpenNTF extensions to RichTextDoclink class
 */
public interface RichTextDoclink {
	/**
	 * Gets the Database ob ject referenced by this doclink
	 * 
	 * @return Database referenced by this doclink
	 * @since org.openntf.domino 4.5.0
	 */
	public org.openntf.domino.Database getDatabase();

	/**
	 * Gets the Document referenced by this doclink, if applicable; null otherwise
	 * 
	 * @return Document or null
	 * @since org.openntf.domino 4.5.0
	 */
	public org.openntf.domino.Document getDocument();

	/**
	 * Gets the View referenced by this doclink, if applicable; null otherwise
	 * 
	 * @return View or null
	 * @since org.openntf.domino 5.0.0
	 */
	public org.openntf.domino.View getView();
}
