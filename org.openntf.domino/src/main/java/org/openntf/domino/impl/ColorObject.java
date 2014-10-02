/*
 * Copyright 2013
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

import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class ColorObject.
 */
public class ColorObject extends Base<org.openntf.domino.ColorObject, lotus.domino.ColorObject, Session> implements
		org.openntf.domino.ColorObject {

	/**
	 * Instantiates a new color object.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	@Deprecated
	public ColorObject(final lotus.domino.ColorObject delegate, final org.openntf.domino.Base<?> parent) {
		super(delegate, Factory.getSession(parent));
	}

	/**
	 * Instantiates a new outline.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 * @param wf
	 *            the wrapperfactory
	 * @param cppId
	 *            the cpp-id
	 */
	public ColorObject(final lotus.domino.ColorObject delegate, final Session parent, final WrapperFactory wf, final long cppId) {
		super(delegate, parent, wf, cppId, NOTES_COLOR);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#findParent(lotus.domino.Base)
	 */
	@Override
	protected Session findParent(final lotus.domino.ColorObject delegate) {
		return fromLotus(Base.getSession(delegate), Session.SCHEMA, null);
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public Session getParent() {
		return getAncestor();
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.ColorObject#setColor(java.awt.Color)
	 */
	public void setColor(final java.awt.Color color) {
		this.setRGB(color.getRed(), color.getGreen(), color.getBlue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ColorObject#setHSL(int, int, int)
	 */
	@Override
	public int setHSL(final int hue, final int saturation, final int luminance) {
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
	public void setNotesColor(final int notesColor) {
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
	public int setRGB(final int red, final int green, final int blue) {
		try {
			return getDelegate().setRGB(red, green, blue);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public Session getAncestorSession() {
		return this.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.ColorObject#getHex()
	 */
	@Override
	public String getHex() {
		String r = Integer.toHexString(this.getRed());
		String g = Integer.toHexString(this.getGreen());
		String b = Integer.toHexString(this.getBlue());
		return (r.length() < 2 ? "0" : "") + r + (g.length() < 2 ? "0" : "") + g + (b.length() < 2 ? "0" : "") + b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.ColorObject#setHex(java.lang.String)
	 */
	@Override
	public void setHex(String hex) {
		if (hex == null || hex.length() < 6 || hex.length() > 7) {
			throw new IllegalArgumentException();
		}

		if (hex.length() == 7) {
			hex = hex.substring(1);
		}
		String r = hex.substring(0, 2);
		String g = hex.substring(2, 4);
		String b = hex.substring(4, 6);

		this.setRGB(Integer.valueOf(r, 16), Integer.valueOf(g, 16), Integer.valueOf(b, 16));
	}

}
