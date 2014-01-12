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
	 * @return
	 */
	Session fromLotusSession(lotus.domino.Session lotus);

	/**
	 * @param lotus
	 * @param parent
	 * @return
	 */
	Document fromLotusDocument(lotus.domino.Document lotus, Database parent);

	/**
	 * @param lotus
	 * @param parent
	 * @return
	 */
	Base<?> fromLotusObject(lotus.domino.Base lotus, Base<?> parent);

	/**
	 * @param lotus
	 * @param t
	 * @param parent
	 * @return
	 */
	<T> T fromLotus(lotus.domino.Base lotus, Class<? extends Base<?>> T, Base<?> parent);

	/**
	 * Method to unwrap a object
	 * 
	 * @param
	 * @return
	 */
	<T extends lotus.domino.Base> T toLotus(T base);

	/**
	 * 
	 */
	void terminate();

}
