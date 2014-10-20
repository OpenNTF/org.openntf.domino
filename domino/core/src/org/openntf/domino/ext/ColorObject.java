/**
 * 
 */
package org.openntf.domino.ext;

/**
 * @author withersp
 * 
 *         OpenNTF Domino extensions to ColorObject
 * 
 */
public interface ColorObject {

	/**
	 * Gets the ColorObject's color as a hex value, standard for web development.
	 * 
	 * @return String corresponding to the hex value for the color, following RGB format, e.g.
	 *         <ul>
	 *         <li>000000 for black</li>
	 *         <li>FFFFFF for white
	 *         <li>
	 *         <li>FF0000 for red</li>
	 *         <li>0000FF for blue</li>
	 *         </ul>
	 * @since org.openntf.domino 1.0.0
	 */
	public String getHex();

	/**
	 * Sets a ColorObject to a specific color using hex format, standard for web development.
	 * 
	 * @param hex
	 *            String corresponding to the hex value for the color, following RGB format, e.g.
	 *            <ul>
	 *            <li>000000 for black</li>
	 *            <li>FFFFFF for white
	 *            <li>
	 *            <li>FF0000 for red</li>
	 *            <li>0000FF for blue</li>
	 *            </ul>
	 * @since org.openntf.domino 1.0.0
	 */
	public void setHex(final String hex);
}
