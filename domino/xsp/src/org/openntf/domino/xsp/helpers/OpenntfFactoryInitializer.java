package org.openntf.domino.xsp.helpers;

import javax.faces.context.FacesContext;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.ODAPlatform;
import org.openntf.domino.xsp.session.XPageCurrentSessionFactory;
import org.openntf.domino.xsp.session.XPageSignerSessionFactory;

import com.ibm.xsp.DominoXspContributor;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.context.RequestCustomizerFactory;
import com.ibm.xsp.context.RequestParameters;
import com.ibm.xsp.domino.el.DominoImplicitObjectFactory;
import com.ibm.xsp.el.ImplicitObjectFactory;
import com.ibm.xsp.event.FacesContextListener;
import com.ibm.xsp.factory.FactoryLookup;

public class OpenntfFactoryInitializer extends RequestCustomizerFactory {

	/**
	 * Shared code, needed also for XOTS
	 * 
	 * @param ctx
	 *            the FacesContext - may be null
	 * @param ctxClassLoader
	 */
	public static void initializeFromContext(final FacesContext ctx, final ClassLoader ctxClassLoader) {

		Factory.initThread();
		//Factory.setSession(notesContext.getCurrentSession(), SessionType.CURRENT);
		// set up the sessionFactories for three XPage-types

		Factory.setSessionFactory(new XPageCurrentSessionFactory(), SessionType.CURRENT);
		Factory.setSessionFactory(new XPageSignerSessionFactory(false), SessionType.SIGNER);
		Factory.setSessionFactory(new XPageSignerSessionFactory(true), SessionType.SIGNER_FULL_ACCESS);

		if (ODAPlatform.isAppFlagSet("BUBBLEEXCEPTIONS")) {
			DominoUtils.setBubbleExceptions(true);
		}

		Factory.setClassLoader(ctxClassLoader);

		if (ctx != null) {

			// TODO RPr: This is probably the wrong locale. See ViewHandler.calculateLocale
			Factory.setUserLocale(ctx.getExternalContext().getRequestLocale());

			// set up  ObjectFactory in Chain mode - (if godMode is enabled)
			if (ODAPlatform.isAppGodMode(null)) {
				@SuppressWarnings("deprecation")
				FactoryLookup lookup = ((FacesContextEx) ctx).getApplicationEx().getFactoryLookup();

				ImplicitObjectFactory iof = (ImplicitObjectFactory) lookup.getFactory(DominoXspContributor.DOMINO_IMPLICITOBJECTS_FACTORY);

				if (iof instanceof DominoImplicitObjectFactory) {
					if (ODAPlatform.isAppDebug(null)) {
						System.out.println("Changing the DominoImplicitObjectFactory");
					}
					iof = new OpenntfGodModeImplicitObjectFactory(iof);
					lookup.setFactory(DominoXspContributor.DOMINO_IMPLICITOBJECTS_FACTORY, iof);
				}
			}
		}
	}

	@Override
	public void initializeParameters(final FacesContext ctx, final RequestParameters req) {
		if (!ODAPlatform.isAPIEnabled()) {
			return;
		}
		initializeFromContext(ctx, ctx.getContextClassLoader());
		((FacesContextEx) ctx).addRequestListener(new FacesContextListener() {
			@Override
			public void beforeRenderingPhase(final FacesContext paramFacesContext) {
			}

			@Override
			public void beforeContextReleased(final FacesContext paramFacesContext) {
				Factory.termThread();
			}
		});
		// In XPages, convertMime should always be false
		//		Session session = Factory.getSession(SessionType.CURRENT);
		//		if (session != null) {
		//			if (Activator.isAppMimeFriendly(ctx))
		//				session.setConvertMIME(false); // this should be always the case in XPages env
		//		}

	}
}
