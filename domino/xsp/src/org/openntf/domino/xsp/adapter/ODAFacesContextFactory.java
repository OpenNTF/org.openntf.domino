package org.openntf.domino.xsp.adapter;

import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;

import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.helpers.XPageCurrentSessionFactory;

import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.context.FacesContextFactoryImpl;
import com.ibm.xsp.domino.context.DominoFacesContextFactoryImpl;
import com.ibm.xsp.event.FacesContextListener;

public class ODAFacesContextFactory extends FacesContextFactory {
	private static final Logger log_ = Logger.getLogger(ODAFacesContextFactory.class.getName());
	public static boolean useODAFacesContext_ = false;	//TODO NTF make this figure it out from some property setting
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
		Factory.setUserLocale(ctx.getExternalContext().getRequestLocale());
		Factory.setClassLoader(ctx.getContextClassLoader());
		//		NotesContext ntx = NotesContext.getCurrent();
		Factory.setSessionFactory(new XPageCurrentSessionFactory(), SessionType.CURRENT);
		if (useODAFacesContext_) {
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
