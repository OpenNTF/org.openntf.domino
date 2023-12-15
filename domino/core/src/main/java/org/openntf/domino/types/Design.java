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
package org.openntf.domino.types;

// TODO: Auto-generated Javadoc
/**
 * The Interface Design.
 */
public interface Design {

	/**
	 * Gets the note id.
	 * 
	 * @return the note id
	 */
	public String getNoteID();

	/**
	 * Gets the universal id.
	 * 
	 * @return the universal id
	 */
	public String getUniversalID();

	/**
	 * Gets the document.
	 * 
	 * @return the document
	 */
	public org.openntf.domino.Document getDocument();
}