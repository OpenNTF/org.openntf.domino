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
