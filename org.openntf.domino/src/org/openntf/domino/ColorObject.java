package org.openntf.domino;

import java.util.Vector;

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
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public int setHSL(int arg0, int arg1, int arg2);

	@Override
	public void setNotesColor(int arg0);

	@Override
	public int setRGB(int arg0, int arg1, int arg2);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
