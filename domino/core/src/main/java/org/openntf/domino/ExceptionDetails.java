/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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
package org.openntf.domino;

import java.util.List;

import org.openntf.domino.types.Null;

/**
 * The interface ExceptionDetails; useful to add further information to an OpenNTFNotesException. Of course, Document, Item, Database,
 * Session, ... implement this interface
 * 
 */
public interface ExceptionDetails {

	/**
	 * This method will provide additional exception details
	 * 
	 */
	public void fillExceptionDetails(List<Entry> result);

	/**
	 * A Entry to provide more Exception details.<br>
	 * <ul>
	 * <li>Session: userName</li>
	 * <li>Database: apiPath</li>
	 * <li>Document: ReplicaID:..., UNID:..., NoteId:..., Form:...</li>
	 * </ul>
	 * 
	 * @author Roland Praml, FOCONIS AG
	 * 
	 */
	public static class Entry {
		final Class<?> sourceCls;
		final String msg;

		public Entry(final Object source, final String msg) {
			sourceCls = source == null ? Null.class : source.getClass();
			this.msg = msg;
		}

		@Override
		public String toString() {
			return sourceCls.getName() + "=" + msg; //$NON-NLS-1$
		}

		public Class<?> getSource() {
			return sourceCls;
		}

		public String getMessage() {
			return msg;
		}
	}

}
