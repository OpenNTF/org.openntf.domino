/**
 * 
 */
package org.openntf.domino.xsp.components;

/**
 * @author Nathan T. Freeman
 * 
 *         Interface for adpative controller, not implemented yet<br/>
 *         TODO: Complete comments
 */
public interface IAdaptiveController {
	/**
	 * 
	 * @param id
	 *            String
	 */
	public org.openntf.domino.schema.IDominoType getDataType(String id);

	public void setDataType(String id, org.openntf.domino.schema.IDominoType type);
}
