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
package org.openntf.domino.nsfdata.structs;

import java.awt.Color;

/**
 * This data structure defines the three components of an RGB color which consist of a red, green, and blue color value. (colorods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class COLOR_VALUE extends AbstractStruct {

	public final Unsigned16 Flags = new Unsigned16();
	public final Unsigned8 Component1 = new Unsigned8();
	public final Unsigned8 Component2 = new Unsigned8();
	public final Unsigned8 Component3 = new Unsigned8();

	public int getFlags() {
		// TODO make enum
		return Flags.get();
	}

	public short getRed() {
		return Component1.get();
	}

	public void setRed(final short red) {
		Component1.set(red);
	}

	public short getBlue() {
		return Component3.get();
	}

	public void setBlue(final short blue) {
		Component3.set(blue);
	}

	public short getGreen() {
		return Component2.get();
	}

	public void setGreen(final short green) {
		Component2.set(green);
	}

	public Color getColor() {
		return new Color(getRed(), getGreen(), getBlue());
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Red=" + getRed() + ", Green=" + getGreen() + ", Blue=" + getBlue() + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}
}
