package org.openntf.domino.xsp.adapter;

import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.ODAPlatform;
import org.openntf.domino.xsp.helpers.OpenntfGodModeImplicitObjectFactory;
import org.openntf.domino.xsp.session.XPageCurrentSessionFactory;
import org.openntf.domino.xsp.session.XPageSignerSessionFactory;

import com.ibm.xsp.DominoXspContributor;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.context.FacesContextFactoryImpl;
import com.ibm.xsp.domino.context.DominoFacesContextFactoryImpl;
import com.ibm.xsp.domino.el.DominoImplicitObjectFactory;
import com.ibm.xsp.el.ImplicitObjectFactory;
import com.ibm.xsp.event.FacesContextListener;
import com.ibm.xsp.factory.FactoryLookup;

public class ODAFacesContextFactory extends FacesContextFactory {
	private static final Logger log_ = Logger.getLogger(ODAFacesContextFactory.class.getName());
	private final FacesContextFactory _delegate;
	private ContextListener _contextListener;

	public static class ContextListener implements FacesContextListener {
		@Override
		public void beforeContextReleased(final FacesContext arg0) {
			Factory.termThread();
		}

		@Override
		public void beforeRenderingPhase(final FacesContext arg0) {
			// TODO Auto-generated method stub
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

		Factory.initThread();
		Factory.setSessionFactory(new XPageCurrentSessionFactory(), SessionType.CURRENT);
		Factory.setSessionFactory(new XPageSignerSessionFactory(false), SessionType.SIGNER);
		Factory.setSessionFactory(new XPageSignerSessionFactory(true), SessionType.SIGNER_FULL_ACCESS);

		if (ODAPlatform.isAppFlagSet("BUBBLEEXCEPTIONS")) {
			DominoUtils.setBubbleExceptions(true);
		}

		// TODO RPr: This is probably the wrong locale. See ViewHandler.calculateLocale
		Factory.setUserLocale(ctx.getExternalContext().getRequestLocale());
		Factory.setClassLoader(ctx.getContextClassLoader());

		if (ODAPlatform.isAppGodMode(null)) {
			// set up  ObjectFactory in Chain mode - (if godMode is enabled)
			// TODO RPr: This is a real hack, but I found no way to guarantee that our object factory is the first one
			// (OFs are put in a HashMap where you can not predict the order)
			@SuppressWarnings("deprecation")
			FactoryLookup lookup = ((FacesContextEx) ctx).getApplicationEx().getFactoryLookup();

			ImplicitObjectFactory delegateIof = (ImplicitObjectFactory) lookup
					.getFactory(DominoXspContributor.DOMINO_IMPLICITOBJECTS_FACTORY);

			if (delegateIof instanceof DominoImplicitObjectFactory) {
				if (ODAPlatform.isAppDebug(null)) {
					System.out.println("Changing the DominoImplicitObjectFactory");
				}
				lookup.setFactory(DominoXspContributor.DOMINO_IMPLICITOBJECTS_FACTORY, new OpenntfGodModeImplicitObjectFactory(delegateIof));
			}
		}

		if (ODAPlatform.isAppFlagSet("ODAFACESCONTEXT")) {
			ODAFacesContext localContext = new ODAFacesContext(ctx);
			attachListener(localContext);
			return localContext;
		} else {
			attachListener((FacesContextEx) ctx);
			return ctx;
		}
	}

	private void attachListener(final FacesContextEx ctx) {
		if (_contextListener == null)
			_contextListener = new ContextListener();
		ctx.addRequestListener(_contextListener);
	}

}
