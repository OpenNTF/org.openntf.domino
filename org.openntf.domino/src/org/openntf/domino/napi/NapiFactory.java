/**
 * 
 */
package org.openntf.domino.napi;


/**
 * @author praml
 * 
 */
public interface NapiFactory {
	NapiDocument getNapiDocument(lotus.domino.Document doc);
}
