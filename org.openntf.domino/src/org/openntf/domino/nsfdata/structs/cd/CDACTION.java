package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * The designer of a form or view may define custom actions associated with that form or view. Actions may be presented to the user as
 * buttons on the action button bar or as options on the "Actions" menu. (actods.h)
 * 
 * @author jgallagher
 * @since Lotus Notes 4.5
 *
 */
public class CDACTION extends CDRecord {

	protected CDACTION(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Type of action (formula, script, etc.)
	 */
	public short getType() {
		// TODO create enum
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return Index into array of icons
	 */
	public short getIconIndex() {
		return getData().getShort(getData().position() + 2);
	}

	/**
	 * @return Action flags
	 */
	public int getFlags() {
		// TODO create enum
		return getData().getInt(getData().position() + 4);
	}

	/**
	 * @return Length (in bytes) of action's title
	 */
	public short getTitleLen() {
		return getData().getShort(getData().position() + 8);
	}

	/**
	 * @return Length (in bytes) of "hide when" formula
	 */
	public short getFormulaLen() {
		return getData().getShort(getData().position() + 10);
	}

	/**
	 * @return Share ID of the Shared Action
	 */
	public int getShareId() {
		return getData().getInt(getData().position() + 12);
	}

	public String getTitle() {
		int titleLen = getTitleLen() & 0xFFFF;

		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 16);
		data.limit(data.position() + titleLen);
		return ODSUtils.fromLMBCS(data);
	}

	public ByteBuffer getActionData() {
		int titleLen = getTitleLen() & 0xFFFF;
		int formulaLen = getFormulaLen() & 0xFFF;

		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 16 + titleLen);
		data.limit(data.position() + (getSignature().getLength() - 16 - titleLen - formulaLen));
		return data;
	}

	public String getFormula() {
		// TODO determine whether this is compiled or decompiled formula
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.limit() - getFormulaLen());
		return ODSUtils.fromLMBCS(data);
	}

	@Override
	public int getExtraLength() {
		// TODO Determine if this is true
		return getDataLength() % 2;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Type: " + getType() + ", IconIndex: " + getIconIndex() + ", Flags: " + getFlags()
				+ ", TitleLen: " + getTitleLen() + ", FormulaLen: " + getFormulaLen() + ", ShareId: " + getShareId() + "]";
	}
}
