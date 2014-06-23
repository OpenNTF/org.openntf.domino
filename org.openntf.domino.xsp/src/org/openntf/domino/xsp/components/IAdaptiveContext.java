/**
 * 
 */
package org.openntf.domino.xsp.components;

/**
 * @author Nathan T. Freeman
 * 
 * Interface for managing functionality adapting to client type, not implemented yet
 */
/**
 * @author withersp
 * 
 */
public interface IAdaptiveContext {
	/**
	 * Enum to allow capturing client type
	 * 
	 * @since org.openntf.domino 4.5.0
	 */
	public static enum ClientType {
		BROWSER, MOBILE, TABLET, XPINC, API
	}

	/**
	 * Retrieves the relevant controller to use
	 * 
	 * @return IAdaptiveController instance
	 * @since org.openntf.domino 4.5.0
	 */
	public IAdaptiveController getController();

	/**
	 * Sets the controller to use for the relevant client type
	 * 
	 * @param controller
	 *            IAdaptiveController instance
	 * @since org.openntf.domino 4.5.0
	 */
	public void setController(IAdaptiveController controller);

	/**
	 * Gets the relevant client type in use
	 * 
	 * @return ClientType enum instance
	 * @since org.openntf.domino 4.5.0
	 */
	public ClientType getClientType();

	/**
	 * Setys the client type to use
	 * 
	 * @param type
	 *            ClientType enum instance
	 * @since org.openntf.domino 4.5.0
	 */
	public void setClientType(ClientType type);

}
