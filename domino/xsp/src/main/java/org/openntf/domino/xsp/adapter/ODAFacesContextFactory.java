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
package org.openntf.domino.xsp.adapter;

import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;

import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.ODAPlatform;
import org.openntf.domino.xsp.session.XPageCurrentSessionFactory;
import org.openntf.domino.xsp.session.XPageSignerSessionFactory;

import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.context.FacesContextFactoryImpl;
import com.ibm.xsp.domino.context.DominoFacesContext;
import com.ibm.xsp.domino.context.DominoFacesContextFactoryImpl;
import com.ibm.xsp.event.FacesContextListener;

public class ODAFacesContextFactory extends FacesContextFactory {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(ODAFacesContextFactory.class.getName());
	private final FacesContextFactory _delegate;
	private ContextListener _contextListener;

	/**
	 * The contextListener terminates the factory on context-release.
	 */
	public static class ContextListener implements FacesContextListener {
		@Override
		public void beforeContextReleased(final FacesContext arg0) {
			Factory.termThread();
		}

		@Override
		public void beforeRenderingPhase(final FacesContext arg0) {

		}
	}

	public ODAFacesContextFactory() {
		Object inst;
		try {
			@SuppressWarnings("rawtypes")
			Class delegateClass = DominoFacesContextFactoryImpl.class;
			inst = delegateClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FacesExceptionEx(e);
		}
		if (inst instanceof FacesContextFactory) {
			_delegate = (FacesContextFactory) inst;
		} else {
			_delegate = null;
		}
	}

	public ODAFacesContextFactory(final FacesContextFactory delegate) {
		if (delegate instanceof FacesContextFactoryImpl) {
			_delegate = ((FacesContextFactoryImpl) delegate).getDelegate();
		} else {
			_delegate = delegate;
		}
	}

	@Override
	public FacesContext getFacesContext(final Object context, final Object request, final Object response, final Lifecycle lifecycle)
			throws FacesException {
		FacesContext ctx = _delegate.getFacesContext(context, request, response, lifecycle);

		Factory.initThread(ODAPlatform.getAppThreadConfig(null));
		Factory.setSessionFactory(new XPageCurrentSessionFactory(), SessionType.CURRENT);
		Factory.setSessionFactory(new XPageSignerSessionFactory(false), SessionType.SIGNER);
		Factory.setSessionFactory(new XPageSignerSessionFactory(true), SessionType.SIGNER_FULL_ACCESS);

		// TODO RPr: This is probably the wrong locale. See ViewHandler.calculateLocale
		Factory.setUserLocale(ctx.getExternalContext().getRequestLocale());
		Factory.setClassLoader(ctx.getContextClassLoader());
		//		NotesContext ntx = NotesContext.getCurrent();

		if (ODAPlatform.isAppGodMode(null)) {
			ODAFacesContext localContext = new ODAFacesContext(ctx);
			attachListener(localContext);
			return localContext;
		} else {
			if (ctx instanceof FacesContextEx) {
				attachListener((FacesContextEx) ctx);
				return ctx;
			} else {
				DominoFacesContext localContext = new DominoFacesContext(ctx);
				attachListener(localContext);
				return localContext;
			}
		}
	}

	private void attachListener(final FacesContextEx ctx) {
		if (_contextListener == null)
			_contextListener = new ContextListener();
		ctx.addRequestListener(_contextListener);
	}

}
