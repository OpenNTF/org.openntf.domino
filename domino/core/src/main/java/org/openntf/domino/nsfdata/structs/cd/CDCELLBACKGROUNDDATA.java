package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.RepeatType;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD Record gives information pertaining to Background Data for a Table, specifically the 'Cell Image' repeat value. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDCELLBACKGROUNDDATA extends CDRecord {

	public final WSIG Header = inner(new WSIG());
	public final Enum8<RepeatType> Repeat = new Enum8<RepeatType>(RepeatType.values());

	@Override
	public SIG getHeader() {
		return Header;
	}
}
