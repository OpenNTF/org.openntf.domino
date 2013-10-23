/**
 * 
 */
package org.openntf.domino.xsp.components;

/**
 * @author Nathan T. Freeman
 * 
 */
public interface IAdaptiveController {
	public org.openntf.domino.schema.types.IDominoType getDataType(String id);

	public void setDataType(String id, org.openntf.domino.schema.types.IDominoType type);
}
