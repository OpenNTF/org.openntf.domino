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

/**
 * @author withersp
 * 
 *         OpenNTF Domino extensions to ColorObject
 * 
 */
public interface ColorObject {

	/**
	 * Gets the ColorObject's color as a hex value, standard for web development.
	 * 
	 * @return String corresponding to the hex value for the color, following RGB format, e.g.
	 *         <ul>
	 *         <li>000000 for black</li>
	 *         <li>FFFFFF for white
	 *         <li>
	 *         <li>FF0000 for red</li>
	 *         <li>0000FF for blue</li>
	 *         </ul>
	 * @since org.openntf.domino 1.0.0
	 */
	public String getHex();

	/**
	 * Sets a ColorObject to a specific color using hex format, standard for web development.
	 * 
	 * @param hex
	 *            String corresponding to the hex value for the color, following RGB format, e.g.
	 *            <ul>
	 *            <li>000000 for black</li>
	 *            <li>FFFFFF for white
	 *            <li>
	 *            <li>FF0000 for red</li>
	 *            <li>0000FF for blue</li>
	 *            </ul>
	 * @since org.openntf.domino 1.0.0
	 */
	public void setHex(final String hex);
}
