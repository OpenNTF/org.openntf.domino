/**
 * 
 */
package org.openntf.domino.design;

import java.util.Map;

/**
 * @author jgallagher
 * 
 */
public interface JavaResource extends FileResource {
	public Map<String, byte[]> getClassData();
}
