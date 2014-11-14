package org.openntf.domino.xsp.helpers;

import javax.faces.context.FacesContext;

import org.openntf.domino.session.AbstractSessionFactory;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.Activator;

import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.context.RequestCustomizerFactory;
import com.ibm.xsp.context.RequestParameters;
import com.ibm.xsp.event.FacesContextListener;

public class OpenntfFactoryInitializer extends RequestCustomizerFactory {

	@Override
	public void initializeParameters(final FacesContext ctx, final RequestParameters req) {
		if (!Activator.isAPIEnabled(ctx)) {
			return;
		}

		final NotesContext notesContext = NotesContext.getCurrent();

		Factory.initThread();
		Factory.setSession(notesContext.getCurrentSession(), SessionType.CURRENT);

		// Technically, there exist only ONE sessionAsSigner. This is a bug in NotesContext
		// So we ask first for the Full-Access session
		// We can't set the sessionAsSigner here, because it is too early!

		//Factory.setSession(notesContext.getSessionAsSigner(true), SessionType.SIGNER_FULL_ACCESS);
		//Factory.setSession(notesContext.getSessionAsSigner(false), SessionType.SIGNER);

		AbstractSessionFactory sessionAsSigner1 = new XPageSignerSessionFactory(notesContext, false);
		AbstractSessionFactory sessionAsSigner2 = new XPageSignerSessionFactory(notesContext, true);
		Factory.setSessionFactory(sessionAsSigner1, SessionType.SIGNER);
		Factory.setSessionFactory(sessionAsSigner2, SessionType.SIGNER_FULL_ACCESS);

		// TODO RPr: This is probably the wrong locale. See ViewHandler.calculateLocale
		Factory.setUserLocale(ctx.getExternalContext().getRequestLocale());
		Factory.setClassLoader(ctx.getContextClassLoader());

		FacesContextEx ctxEx = (FacesContextEx) ctx;
		ctxEx.addRequestListener(new FacesContextListener() {

			@Override
			public void beforeRenderingPhase(final FacesContext paramFacesContext) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeContextReleased(final FacesContext paramFacesContext) {
				Factory.termThread();
			}
		});

	}
}
