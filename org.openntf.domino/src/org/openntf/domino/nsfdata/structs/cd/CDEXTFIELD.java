package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * New field attributes have been added in Release 4.0 of Notes. To preserve compatibility with existing applications, the new attributes
 * have been placed in this extension to the CDFIELD record. This record is optional, and may not be present in the $Body item of the form
 * note. (editods.h)
 * 
 * @author jgallagher
 * @since Lotus Notes 4.0
 */
public class CDEXTFIELD extends CDRecord {

	public CDEXTFIELD(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Field Flags (see FEXT_xxx)
	 */
	public int getFlags1() {
		// TODO make enum
		return getData().getInt(getData().position() + 0);
	}

	public int getFlags2() {
		return getData().getInt(getData().position() + 4);
	}

	/**
	 * @return Field entry helper type (see FIELD_HELPER_xxx)
	 */
	public short getEntryHelper() {
		// TODO make enum
		return getData().getShort(getData().position() + 8);
	}

	/**
	 * @return Entry helper DB name length
	 */
	public short getEntryDBNameLen() {
		return getData().getShort(getData().position() + 10);
	}

	/**
	 * @return Entry helper View name length
	 */
	public short getEntryViewNameLen() {
		return getData().getShort(getData().position() + 12);
	}

	/**
	 * @return Entry helper column number
	 */
	public short getEntryColumnNumber() {
		return getData().getShort(getData().position() + 14);
	}

	public String getEntryHelperDBName() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 16);
		data.limit(data.position() + getEntryDBNameLen());
		return ODSUtils.fromLMBCS(data);
	}

	public String getEntryHelperViewName() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 16 + getEntryDBNameLen());
		data.limit(data.position() + getEntryViewNameLen());
		return ODSUtils.fromLMBCS(data);
	}
}
