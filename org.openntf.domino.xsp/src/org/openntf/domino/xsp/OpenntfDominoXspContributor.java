package org.openntf.domino.xsp;

import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

import javax.faces.FactoryFinder;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

import com.ibm.xsp.library.XspContributor;

public class OpenntfDominoXspContributor extends XspContributor {

	@Override
	public Object[][] getFactories() {
		Object[][] result = new Object[][] { { "org.openntf.domino.xsp.helpers.DOMINO_IMPLICIT_OBJECT_FACTORY",
				org.openntf.domino.xsp.helpers.OpenntfDominoImplicitObjectFactory2.class } };
		try {
			// Adding a phase listener is a restricted operation
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					System.out.println("Loading OpenLog PhaseListener");
					LifecycleFactory factory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
					Lifecycle lifecycle = factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
					lifecycle.addPhaseListener(new XspOpenLogPhaseListener());
					System.out.println("Loaded OpenLog PhaseListener");
					return null;
				}
			});
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return result;
	}
}
