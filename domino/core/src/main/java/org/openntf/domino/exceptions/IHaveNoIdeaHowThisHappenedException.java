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
package org.openntf.domino.exceptions;

import java.util.logging.Logger;

/**
 * @author Nathan T. Freeman
 * 
 */
public class IHaveNoIdeaHowThisHappenedException extends RuntimeException {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(IHaveNoIdeaHowThisHappenedException.class.getName());
	private static final long serialVersionUID = 1L;

	public IHaveNoIdeaHowThisHappenedException() {
	}

	/**
	 * @param message
	 *            the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 */
	public IHaveNoIdeaHowThisHappenedException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 *            the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and
	 *            indicates that the cause is nonexistent or unknown.)
	 */
	public IHaveNoIdeaHowThisHappenedException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 *            the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 * @param cause
	 *            the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and
	 *            indicates that the cause is nonexistent or unknown.)
	 */
	public IHaveNoIdeaHowThisHappenedException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
