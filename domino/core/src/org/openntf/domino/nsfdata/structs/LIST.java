package org.openntf.domino.nsfdata.structs;


/**
 * This datatype is used by several data structures to specify that a field contains a list of one or more values. A list is made up of one
 * or more items of a datatype, separated by a delimiter. (global.h)
 *
 */
public class LIST extends AbstractStruct {
	public final Unsigned16 ListEntries = new Unsigned16();

}
