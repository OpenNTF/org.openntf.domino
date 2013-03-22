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
package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class ColorObject.
 */
public class ColorObject extends Base<org.openntf.domino.ColorObject, lotus.domino.ColorObject> implements org.openntf.domino.ColorObject {

	/**
	 * Instantiates a new color object.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public ColorObject(lotus.domino.ColorObject delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, Factory.getSession(parent));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ColorObject#getBlue()
	 */
	@Override
	public int getBlue() {
		try {
			return getDelegate().getBlue();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ColorObject#getGreen()
	 */
	@Override
	public int getGreen() {
		try {
			return getDelegate().getGreen();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ColorObject#getHue()
	 */
	@Override
	public int getHue() {
		try {
			return getDelegate().getHue();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ColorObject#getLuminance()
	 */
	@Override
	public int getLuminance() {
		try {
			return getDelegate().getLuminance();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ColorObject#getNotesColor()
	 */
	@Override
	public int getNotesColor() {
		try {
			return getDelegate().getNotesColor();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public Session getParent() {
		return (Session) super.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ColorObject#getRed()
	 */
	@Override
	public int getRed() {
		try {
			return getDelegate().getRed();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ColorObject#getSaturation()
	 */
	@Override
	public int getSaturation() {
		try {
			return getDelegate().getSaturation();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ColorObject#setHSL(int, int, int)
	 */
	@Override
	public int setHSL(int hue, int saturation, int luminance) {
		try {
			return getDelegate().setHSL(hue, saturation, luminance);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ColorObject#setNotesColor(int)
	 */
	@Override
	public void setNotesColor(int notesColor) {
		try {
			getDelegate().setNotesColor(notesColor);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ColorObject#setRGB(int, int, int)
	 */
	@Override
	public int setRGB(int red, int green, int blue) {
		try {
			return getDelegate().setRGB(red, green, blue);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

}
