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
package org.openntf.domino.xsp.xots;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.openntf.domino.xots.Tasklet;
import org.openntf.domino.xots.XotsUtil;
import org.openntf.domino.xsp.IXspHttpServletResponseCallback;

@Tasklet(session = Tasklet.Session.CLONE, context = Tasklet.Context.XSPSCOPED)
public class BasicXotsXspCallbackRunnable extends AbstractXotsXspRunnable {

	private IXspHttpServletResponseCallback callback;
	private HttpServletRequest request;

	public BasicXotsXspCallbackRunnable(final IXspHttpServletResponseCallback callback, final HttpServletRequest request) {
		this.callback = callback;
		this.request = request;
	}

	@Override
	public void run() {
		try {
			callback.process(request, null);
		} catch (IOException e) {
			XotsUtil.handleException(e, getContext());
		}
	}

}
