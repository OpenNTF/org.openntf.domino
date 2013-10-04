/**
 * 
 */
package org.openntf.domino.xsp;

import java.util.List;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;

import org.openntf.domino.utils.Factory;

import com.ibm.commons.extension.ExtensionManager;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.context.FacesContextFactoryImpl;
import com.ibm.xsp.domino.context.DominoFacesContextFactoryImpl;

/**
 * @author Nathan T. Freeman
 * 
 */
public class OpenntfFacesContextFactory extends FacesContextFactory implements com.ibm.xsp.event.FacesContextListener {
	// private static final Logger log_ = Logger.getLogger(OpenntfFacesContextFactory.class.getName());
	private final FacesContextFactory _delegate;

	/**
	 * 
	 */
	public OpenntfFacesContextFactory() {
		// System.out.println("Creating new OpenntfFacesContextFactory");
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
			System.out.println("WARNING: Delegate for OpenntfFacesContextFactory is null. Many things will probably break.");
			_delegate = null;
		}
	}

	public OpenntfFacesContextFactory(final FacesContextFactory delegate) {
		// System.out.println("Creating new OpenntfFacesContextFactory from delegate");

		if (delegate instanceof FacesContextFactoryImpl) {
			_delegate = ((FacesContextFactoryImpl) delegate).getDelegate();
		} else {
			_delegate = delegate;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.context.FacesContextFactory#getFacesContext(java.lang.Object, java.lang.Object, java.lang.Object,
	 * javax.faces.lifecycle.Lifecycle)
	 */
	@Override
	public FacesContext getFacesContext(final Object context, final Object request, final Object response, final Lifecycle lifecycle)
			throws FacesException {
		FacesContext ctx = _delegate.getFacesContext(context, request, response, lifecycle);

		try {
			ClassLoader cl = ctx.getContextClassLoader();
			List<Object> mapperList = ExtensionManager.findApplicationServices(cl, "org.openntf.domino.mapper");
			System.out.println("Setting up factory. Mapperservices: " + mapperList.size());
			// TODO FOC: Maybe we can cache this in sessionScope (maybe not, for GC reasons!)
			Factory.setMapperList(mapperList);
			Factory.setClassLoader(cl);
			if (ctx instanceof com.ibm.xsp.context.FacesContextEx) {
				((com.ibm.xsp.context.FacesContextEx) ctx).addRequestListener(this);
			}
			// System.out.println("Created OpenntfFacesContext and setup factory");
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return ctx;
	}

	@Override
	public void beforeContextReleased(final FacesContext paramFacesContext) {
		try {
			Factory.terminate();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public void beforeRenderingPhase(final FacesContext paramFacesContext) {
		// TODO NOOP
	}
}
