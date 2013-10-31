/**
 * 
 */
package org.openntf.domino.xsp.components;

/**
 * @author Nathan T. Freeman
 * 
 */
public interface IAdaptiveContext {
	public static enum ClientType {
		BROWSER, MOBILE, TABLET, XPINC, API
	}

	public IAdaptiveController getController();

	public void setController(IAdaptiveController controller);

	public ClientType getClientType();

	public void setClientType(ClientType type);

}
