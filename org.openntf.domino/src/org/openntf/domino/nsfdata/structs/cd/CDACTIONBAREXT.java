package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.LENGTH_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record defines the Action Bar attributes. It is an extension of the CDACTIONBAR record. It is found within a $V5ACTIONS item and
 * is preceded by a CDACTIONBAR record. (actods.h)
 * 
 * @author jgallagher
 * @since Lotus Notes/Domino 5.0
 */
public class CDACTIONBAREXT extends CDRecord {

	public CDACTIONBAREXT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public COLOR_VALUE getBackColor() {
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 0);
		data.limit(data.position() + 6);
		return new COLOR_VALUE(data);
	}

	public COLOR_VALUE getLineColor() {
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 6);
		data.limit(data.position() + 6);
		return new COLOR_VALUE(data);
	}

	public COLOR_VALUE getFontColor() {
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 12);
		data.limit(data.position() + 6);
		return new COLOR_VALUE(data);
	}

	public COLOR_VALUE getButtonColor() {
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 18);
		data.limit(data.position() + 6);
		return new COLOR_VALUE(data);
	}

	public short getBtnBorderDisplay() {
		return getData().getShort(getData().position() + 24);
	}

	/**
	 * This is always recalculated on save
	 */
	public short getAppletHeight() {
		return getData().getShort(getData().position() + 26);
	}

	public short getBarBackgroundRepeat() {
		// TODO make enum
		return getData().getShort(getData().position() + 28);
	}

	public byte getBtnWidthStyle() {
		// TODO make enum
		return getData().get(getData().position() + 30);
	}

	public byte getBtnTextJustify() {
		// TODO make enum
		return getData().get(getData().position() + 31);
	}

	/**
	 * Valid only if BtnWidthStyle is ACTIONBAR_BUTTON_WIDTH_ABSOLUTE
	 */
	public short getBtnWidthAbsolute() {
		return getData().getShort(getData().position() + 32);
	}

	/**
	 * @return Extra margin on the inside right and left edges of a button to space image and text away from the right and left edges
	 */
	public short getBtnInternalMargin() {
		return getData().getShort(getData().position() + 34);
	}

	/**
	 * @return See ACTIONBAREXT_xxx flags
	 */
	public int getFlags() {
		// TODO make enum
		return getData().getInt(getData().position() + 36);
	}

	/**
	 * @return Used in conjunction with barHeight
	 */
	public int getBarFontId() {
		// TODO map to font
		return getData().getInt(getData().position() + 40);
	}

	public LENGTH_VALUE getBarHeight() {
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 44);
		data.limit(data.position() + 12);
		return new LENGTH_VALUE(data);
	}

	/**
	 * @return Leaving many spares for future mouse down/ mouse over colors and whatever else we want
	 */
	public int[] getSpare() {
		int[] result = new int[12];
		for (int i = 0; i < 12; i++) {
			result[i] = getData().getInt(getData().position() + 56 + (i * 4));
		}
		return result;
	}
}
