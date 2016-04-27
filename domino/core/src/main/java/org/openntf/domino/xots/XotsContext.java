package org.openntf.domino.xots;

/**
 * @author Paul Withers
 * @since 2.5.0
 *
 */
public class XotsContext {
	private String contextApiPath;
	private String openLogApiPath;

	/**
	 * Getter for contextApiPath
	 * 
	 * @return String ApiPath of database to act upon
	 */
	public String getContextApiPath() {
		return contextApiPath;
	}

	/**
	 * Setter for contextApiPath
	 * 
	 * @param contextApiPath
	 *            String ApiPath of database to act upon
	 */
	public void setContextApiPath(final String contextApiPath) {
		this.contextApiPath = contextApiPath;
	}

	/**
	 * Getter for openLogApiPath
	 * 
	 * @return String ApiPath of OpenLog database to log to
	 */
	public String getOpenLogApiPath() {
		return openLogApiPath;
	}

	/**
	 * Setter for openLogApiPath
	 * 
	 * @param openLogApiPath
	 *            String ApiPath of OpenLog database to log to
	 */
	public void setOpenLogApiPath(final String openLogApiPath) {
		this.openLogApiPath = openLogApiPath;
	}

}
