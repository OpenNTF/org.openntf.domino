/**
 * 
 */
package org.openntf.domino.ext;

/**
 * @author withersp
 * 
 *         OpenNTF extensions to NotesCalendar class
 */
public interface NotesCalendar {
	//NTF to get around mixed version problems...
	public String getApptunidFromUID(final String arg0, final boolean arg1);
}
