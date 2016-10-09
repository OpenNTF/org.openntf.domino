/**
 * 
 */
package org.openntf.domino.xsp;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;

import org.openntf.domino.utils.Factory;

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
	 * Constructor
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

	/**
	 * Overloaded constructor
	 * 
	 * @param delegate
	 *            FacesContextFactory, delegate of OpenntfFacesContextFactory
	 */
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
			Class<?> vnClass = Class.forName("org.openntf.domino.xsp.helpers.OpenntfViewNavigatorEx");
		} catch (ClassNotFoundException e) {
			System.out.println("OpenntfFacesContextFactory unable to resolve ViewNavigatorEx either!");
		}
		try {
			Factory.setClassLoader(Thread.currentThread().getContextClassLoader());
			if (ctx instanceof com.ibm.xsp.context.FacesContextEx) {
				((com.ibm.xsp.context.FacesContextEx) ctx).addRequestListener(this);
			}
			// System.out.println("Created OpenntfFacesContext and setup factory");
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return ctx;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.event.FacesContextListener#beforeContextReleased(javax.faces.context.FacesContext)
	 */
	@Override
	public void beforeContextReleased(final FacesContext paramFacesContext) {
		try {
			System.out.println("Terminating the factory in " + getClass().getName());
			Factory.terminate();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.event.FacesContextListener#beforeRenderingPhase(javax.faces.context.FacesContext)
	 */
	@Override
	public void beforeRenderingPhase(final FacesContext paramFacesContext) {
		// TODO NOOP
	}
}
