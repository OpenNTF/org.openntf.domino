/*
 * Copyright OpenNTF 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.domino;

import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface ColorObject.
 */
public interface ColorObject extends Base<lotus.domino.ColorObject>, lotus.domino.ColorObject, org.openntf.domino.ext.ColorObject,
		SessionDescendant {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ColorObject#getBlue()
	 */
	@Override
	public int getBlue();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ColorObject#getGreen()
	 */
	public int getGreen();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ColorObject#getHue()
	 */
	@Override
	public int getHue();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ColorObject#getLuminance()
	 */
	@Override
	public int getLuminance();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ColorObject#getNotesColor()
	 */
	@Override
	public int getNotesColor();

	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	public Session getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ColorObject#getRed()
	 */
	@Override
	public int getRed();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ColorObject#getSaturation()
	 */
	@Override
	public int getSaturation();

	/**
	 * Sets the color.
	 * 
	 * @param color
	 *            the new color
	 */
	public void setColor(java.awt.Color color);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ColorObject#setHSL(int, int, int)
	 */
	@Override
	public int setHSL(int hue, int saturation, int luminance);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ColorObject#setNotesColor(int)
	 */
	@Override
	public void setNotesColor(int notesColor);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ColorObject#setRGB(int, int, int)
	 */
	@Override
	public int setRGB(int red, int green, int blue);

}
