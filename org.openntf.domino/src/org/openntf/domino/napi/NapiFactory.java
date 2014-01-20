/**
 * 
 */
package org.openntf.domino.napi;

import org.openntf.domino.Document;

/**
 * @author praml
 * 
 */
public interface NapiFactory {
	NapiDocument getNapiDocument(Document doc);
}
