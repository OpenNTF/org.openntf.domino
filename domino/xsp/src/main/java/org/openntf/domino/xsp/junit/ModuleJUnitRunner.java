package org.openntf.domino.xsp.junit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.servlet.ServletException;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.session.XPageCurrentSessionFactory;
import org.openntf.domino.xsp.xots.FakeHttpRequest;
import org.openntf.domino.xsp.xots.ModuleLoader;

import com.ibm.commons.util.StringUtil;
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
public class ModuleJUnitRunner extends DominoJUnitRunner {

	public ModuleJUnitRunner(final Class<?> testClass) throws InitializationError {
		super(testClass);
	}

	private ClassLoader oldCl;

	@Override
	protected void startUp() {
		if (!Factory.isStarted()) {
			fail("Factory is not initialized. Probalby you do not run this test with @OsgiTest");
		}
		super.startUp();
	}

	@Override
	protected void beforeTest(final FrameworkMethod method) {
		String db = getDatabase(method);

		if (StringUtil.isEmpty(db)) {
			super.beforeTest(method);
			return;
		}

		assertNotNull(db);
		NSFComponentModule module = null;
		try {
			module = ModuleLoader.loadModule(db, false);
		} catch (ServletException e1) {
			e1.printStackTrace();
		}
		assertNotNull("Module " + db + " does not exist or is locked by server process", module);
		NotesContext ctx = new NotesContext(module);
		NotesContext.initThread(ctx); // Request is initialized later

		Factory.initThread(Factory.STRICT_THREAD_CONFIG);

		String runAs = getRunAs(method);
		if (StringUtil.isEmpty(runAs)) {
			runAs = Factory.getLocalServerName();
		}
		FakeHttpRequest req = new FakeHttpRequest(runAs);
		try {
			ctx.initRequest(req);
		} catch (java.lang.NoSuchFieldError nfe) {
			System.err.println("Could not init context completely: " + nfe);
			nfe.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}

		oldCl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(module.getModuleClassLoader());

		ISessionFactory sf = new XPageCurrentSessionFactory();
		//ISessionFactory sfFull = new XPageSessionFactory(ctx.getSessionAsSignerFullAdmin(), true);
		Factory.setSessionFactory(sf, SessionType.CURRENT);
		//Factory.setSessionFactory(sfFull, SessionType.CURRENT_FULL_ACCESS);
	}

	@Override
	protected void afterTest(final FrameworkMethod method) {
		if (oldCl == null) {
			super.tearDown();
			return;
		}
		Thread.currentThread().setContextClassLoader(oldCl);
		oldCl = null;
		Factory.termThread();
		NotesContext.termThread();

	}

}
