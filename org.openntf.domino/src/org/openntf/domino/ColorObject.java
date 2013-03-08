package org.openntf.domino;

public interface ColorObject extends Base<lotus.domino.ColorObject>, lotus.domino.ColorObject {

	@Override
	public int getBlue();

	public int getGreen();

	@Override
	public int getHue();

	@Override
	public int getLuminance();

	@Override
	public int getNotesColor();

	@Override
	public int getRed();

	@Override
	public int getSaturation();

	@Override
	public int setHSL(int hue, int saturation, int luminance);

	@Override
	public void setNotesColor(int notesColor);

	@Override
	public int setRGB(int red, int green, int blue);

}
