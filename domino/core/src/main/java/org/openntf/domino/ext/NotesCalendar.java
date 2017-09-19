/**
 *
 */
package org.openntf.domino.ext;

/**
 * OpenNTF extensions to NotesCalendar class
 * 
 * @author withersp
 *
 */
public interface NotesCalendar {
	//NTF to get around mixed version problems...
	public String getApptunidFromUID(final String arg0, final boolean arg1);
}
