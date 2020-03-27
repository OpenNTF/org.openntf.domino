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
package org.openntf.domino.utils;

/**
 * Utility enum as a carrier for Item-centric static properties and methods.
 * 
 */
public enum Items {
	;

	/** The Constant log_. */
	// private final static Logger log_ = Logger.getLogger("org.openntf.domino");

	/** The Constant logBackup_. */
	//	private final static Logger logBackup_ = Logger.getLogger("com.ibm.xsp.domino");

	public static enum Flags {
		IsAuthors, IsEncrypted, IsNames, IsProtected, IsReaders, IsSaveToDisk, IsSigned, IsSummary;

		@Override
		public String toString() {
			return this.getDeclaringClass() + "." + this.getClass() + ":" + this.name();
		}
	};

}
