/**
 * 
 */
package org.openntf.domino;

/**
 * @author Roland Praml, Foconis AG
 * 
 */
public interface WrapperFactory {

	/**
	 * @param lotus
	 * @param parent
	 * @return
	 */
	Session fromLotusSession(lotus.domino.Session lotus, Base parent);

	/**
	 * @param lotus
	 * @param parent
	 * @return
	 */
	Document fromLotusDocument(lotus.domino.Document lotus, Base parent);

	/**
	 * @param lotus
	 * @param parent
	 * @return
	 */
	Base fromLotusObject(lotus.domino.Base lotus, Base parent);

	/**
	 * @param lotus
	 * @param t
	 * @param parent
	 * @return
	 */
	<T> T fromLotus(lotus.domino.Base lotus, Class<? extends Base> T, Base parent);

	/**
	 * 
	 */
	void terminate();

	/**
	 * @param
	 * @return
	 */
	lotus.domino.Base toLotus(lotus.domino.Base base);

}
