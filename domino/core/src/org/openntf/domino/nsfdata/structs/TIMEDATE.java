package org.openntf.domino.nsfdata.structs;


/**
 * This is the Domino binary time/date format. This structure only should be interpreted using the C API time/date calls -- it cannot easily
 * be parsed directly. (global.h)
 *
 */
public class TIMEDATE extends AbstractStruct {
	public final Unsigned32[] Innards = array(new Unsigned32[2]);

}
