/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
package org.openntf.domino.schema.exceptions;

import java.util.logging.Logger;

import org.openntf.domino.schema.impl.ItemDefinition;

/**
 * @author nfreeman
 * 
 */
public abstract class ItemException extends SchemaException {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(ItemException.class.getName());
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private Object value_;
	@SuppressWarnings("unused")
	private ItemDefinition itemdef_;

	public ItemException() {
	}

	/**
	 * @param message
	 *            the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 */
	public ItemException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 *            the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and
	 *            indicates that the cause is nonexistent or unknown.)
	 */
	public ItemException(final Throwable cause) {
		super(cause);
	}
}
