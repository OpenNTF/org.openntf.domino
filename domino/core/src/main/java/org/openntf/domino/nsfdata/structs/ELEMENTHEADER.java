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
package org.openntf.domino.nsfdata.structs;


/**
 * This structure contains the common fields for the graphical elements in a layout region of a form.
 * 
 * @since Lotus Notes 4.1
 *
 */
public class ELEMENTHEADER extends AbstractStruct {

	/**
	 * Location of the left edge of the element in twips
	 */
	public final Unsigned16 wLeft = new Unsigned16();
	/**
	 * Location of the top of the element in twips
	 */
	public final Unsigned16 wTop = new Unsigned16();
	/**
	 * Width of the element in twips
	 */
	public final Unsigned16 wWidth = new Unsigned16();
	/**
	 * Height of the element in twips
	 */
	public final Unsigned16 wHeight = new Unsigned16();
	/**
	 * Font used to display text in the element
	 */
	public final FONTID FontID = inner(new FONTID());
	/**
	 * Background color for the element
	 */
	public final Unsigned8 byBackColor = new Unsigned8();
	public final Unsigned8 bSpare = new Unsigned8();
	public final COLOR_VALUE BackgroundColor = inner(new COLOR_VALUE());

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Left=" + wLeft.get() + ", Top=" + wTop.get() + ", Width=" + wWidth.get() + ", Height="
				+ wHeight.get() + ", FontID=" + FontID + ", BackColor=" + byBackColor.get() + ", BackgroundColor=" + BackgroundColor + "]";
	}
}
