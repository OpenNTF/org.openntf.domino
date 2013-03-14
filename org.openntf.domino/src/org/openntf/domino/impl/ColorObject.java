package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;

public class ColorObject extends Base<org.openntf.domino.ColorObject, lotus.domino.ColorObject> implements org.openntf.domino.ColorObject {

	protected ColorObject(lotus.domino.ColorObject delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public int getBlue() {
		try {
			return getDelegate().getBlue();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getGreen() {
		try {
			return getDelegate().getGreen();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getHue() {
		try {
			return getDelegate().getHue();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getLuminance() {
		try {
			return getDelegate().getLuminance();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

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
	public int getRed() {
		try {
			return getDelegate().getRed();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getSaturation() {
		try {
			return getDelegate().getSaturation();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int setHSL(int hue, int saturation, int luminance) {
		try {
			return getDelegate().setHSL(hue, saturation, luminance);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public void setNotesColor(int notesColor) {
		try {
			getDelegate().setNotesColor(notesColor);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

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
