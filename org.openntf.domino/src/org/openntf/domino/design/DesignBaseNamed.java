/**
 * 
 */
package org.openntf.domino.design;

import java.util.List;

/**
 * @author jgallagher
 * 
 */
public interface DesignBaseNamed extends DesignBase {
	/**
	 * @return the design element's name
	 */
	public String getName();

	/**
	 * @param name
	 *            The new name for the design element; any |-delimited values will be appended to the aliases
	 */
	public void setName(final String name);

	/**
	 * @return a List of the design element's aliases
	 */
	public List<String> getAliases();

	/**
	 * 
	 * @param alias
	 *            The new alias(es) for the design element, |-delimited
	 */
	public void setAlias(final String alias);

	/**
	 * 
	 * @param aliases
	 *            The new aliases for the design element; any |-delimited values will be exploded into the final list
	 */
	public void setAliases(final Iterable<String> aliases);
}
