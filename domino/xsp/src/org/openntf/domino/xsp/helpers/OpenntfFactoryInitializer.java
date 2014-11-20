package org.openntf.domino.xsp.helpers;

import javax.faces.context.FacesContext;

import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.session.AbstractSessionFactory;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.Activator;

import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.xsp.DominoXspContributor;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.context.RequestCustomizerFactory;
import com.ibm.xsp.context.RequestParameters;
import com.ibm.xsp.domino.el.DominoImplicitObjectFactory;
import com.ibm.xsp.el.ImplicitObjectFactory;
import com.ibm.xsp.event.FacesContextListener;
import com.ibm.xsp.factory.FactoryLookup;

public class OpenntfFactoryInitializer extends RequestCustomizerFactory {

	@Override
	public void initializeParameters(final FacesContext ctx, final RequestParameters req) {
		if (!Activator.isAPIEnabled(ctx)) {
			return;
		}

		final NotesContext notesContext = NotesContext.getCurrent();

		Factory.initThread();
		Fixes[] fixes = Activator.isAppAllFix(ctx) ? Fixes.values() : null;

		//Factory.setSession(notesContext.getCurrentSession(), SessionType.CURRENT);
		Factory.setSessionFactory(new XPageCurrentSessionFactory(fixes, Activator.getAppAutoMime(ctx)), SessionType.CURRENT);

		// In XPages, convertMime should always be false.
		//		Session session = Factory.getSession(SessionType.CURRENT);
		//		if (session != null) {
		//			if (Activator.isAppMimeFriendly(ctx))
		//				session.setConvertMIME(false); // this should be always the case in XPages env
		//		}

		if (Activator.isAppFlagSet(ctx, "BUBBLEEXCEPTIONS"))
			DominoUtils.setBubbleExceptions(true);

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
			}

			@Override
			public void beforeContextReleased(final FacesContext paramFacesContext) {
				Factory.termThread();
			}
		});

		// set up  ObjectFactoriy if godMode is enabled
		if (Activator.isAppGodMode(ctx)) {
			@SuppressWarnings("deprecation")
			FactoryLookup lookup = ctxEx.getApplicationEx().getFactoryLookup();

			ImplicitObjectFactory iof = (ImplicitObjectFactory) lookup.getFactory(DominoXspContributor.DOMINO_IMPLICITOBJECTS_FACTORY);

			if (iof instanceof DominoImplicitObjectFactory) {
				if (Activator.isAppDebug(ctx)) {
					System.out.println("Changing the DominoImplicitObjectFactory");
				}
				iof = new OpenntfGodModeImplicitObjectFactory(iof);
				lookup.setFactory(DominoXspContributor.DOMINO_IMPLICITOBJECTS_FACTORY, iof);
			}
		}

	}
}
