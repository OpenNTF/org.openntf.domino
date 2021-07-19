/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 
 */
package org.openntf.domino.exceptions;

import java.util.logging.Logger;

/**
 * @author Nathan T. Freeman
 * 
 */
public class TransactionAlreadySetException extends RuntimeException {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(TransactionAlreadySetException.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * @param dbpath
	 *            The path to the context database.
	 */
	@SuppressWarnings("nls")
	public TransactionAlreadySetException(final String dbpath) {
		super("Cannot set a transaction on database " + dbpath + " because it already has a different transaction.");
	}

}
