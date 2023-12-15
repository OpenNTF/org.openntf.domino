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
package org.openntf.domino.xsp.helpers;

import lotus.domino.NotesException;

import org.eclipse.core.runtime.Status;
import org.openntf.domino.ExceptionDetails;
import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;

import com.ibm.domino.xsp.module.nsf.ThreadSessionExecutor;

public class OpenntfThreadSessionExecutor<IStatus> extends ThreadSessionExecutor<IStatus> {

	public OpenntfThreadSessionExecutor() {
	}

	@SuppressWarnings("unchecked")
	protected IStatus run(final Session session) throws Exception {
		try {
			return run(session.getFactory().toLotus(session));
		} catch (NotesException e) {
			DominoUtils.handleException(e, (ExceptionDetails) this);
			return (IStatus) Status.CANCEL_STATUS;
		}
	}

	@Override
	protected IStatus run(final lotus.domino.Session arg0) throws Exception {
		return super.run();
	}

}
