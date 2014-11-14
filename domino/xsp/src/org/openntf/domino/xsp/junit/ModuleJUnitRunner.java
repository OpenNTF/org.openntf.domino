package org.openntf.domino.xsp.junit;

import static org.junit.Assert.assertNotNull;

import javax.servlet.ServletException;

import org.junit.runners.model.InitializationError;
import org.openntf.domino.Session;
import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.helpers.ModuleLoader;
import org.openntf.domino.xsp.helpers.XPageSessionFactory;
import org.openntf.domino.xsp.xots.FakeHttpRequest;

import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;

/**
 * A Testrunner to run JUnit tests with proper set up of ODA.
 * 
 * Use <code>{@literal @}RunWith(DominoJUnitRunner.class)</code> in your test class.
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class ModuleJUnitRunner extends XspJUnitRunner {
	private String db;
	private ClassLoader oldCl;

	public ModuleJUnitRunner(final Class<?> testClass) throws InitializationError {
		super(testClass);
		System.out.println(testClass);
		db = testClass.getAnnotation(Module.class).value();
	}

	@Override
	protected void startUp() {
		// TODO Auto-generated method stub
		super.startUp();
		assertNotNull(db);
		NSFComponentModule module = null;
		try {
			module = ModuleLoader.loadModule(db, false);
		} catch (ServletException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		assertNotNull(module);
		NotesContext ctx = new NotesContext(module);
		NotesContext.initThread(ctx);
		Session s = Factory.getSession();
		System.out.println(s);
		FakeHttpRequest req = new FakeHttpRequest("Roland Praml");
		try {
			ctx.initRequest(req);
		} catch (java.lang.NoSuchFieldError nfe) {
			System.err.println("Could not init context completely: " + nfe);
			nfe.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		oldCl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(module.getModuleClassLoader());

		ISessionFactory sf = new XPageSessionFactory(ctx.getCurrentSession(), false);
		ISessionFactory sfFull = new XPageSessionFactory(ctx.getSessionAsSignerFullAdmin(), true);
		Factory.setSessionFactory(sf, SessionType.CURRENT);
		Factory.setSessionFactory(sfFull, SessionType.CURRENT_FULL_ACCESS);
	}

	@Override
	protected void tearDown() {
		Thread.currentThread().setContextClassLoader(oldCl);
		NotesContext.termThread();
		super.tearDown();
	}

}
