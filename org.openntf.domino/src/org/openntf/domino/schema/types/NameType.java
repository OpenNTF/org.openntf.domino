/**
 * 
 */
package org.openntf.domino.schema.types;
import java.util.logging.Logger;

import org.openntf.domino.Item;
/**
 * @author nfreeman
 *
 */
public class NameType extends AbstractDominoType {
	private static final Logger log_ = Logger.getLogger(NameType.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public NameType() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.schema.types.IDominoType#getUITypeName()
	 */
	@Override
	public String getUITypeName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.schema.types.IDominoType#setItemToDefault(org.openntf.domino.Item)
	 */
	@Override
	public void setItemToDefault(Item item) {
		// TODO Auto-generated method stub

	}
}
