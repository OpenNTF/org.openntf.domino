package org.openntf.domino.xsp.junit;

import lotus.notes.NotesThread;

import org.junit.runners.model.InitializationError;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xots.XotsDaemon;
import org.openntf.domino.xsp.xots.XotsDominoExecutor;

import com.ibm.designer.runtime.domino.adapter.LCDEnvironment;
import com.ibm.designer.runtime.domino.bootstrap.BootstrapEnvironment;

/**
 * A Testrunner to run JUnit tests with proper set up of ODA.
 * 
 * Use <code>{@literal @}RunWith(DominoJUnitRunner.class)</code> in your test class.
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class XspJUnitRunner extends DominoJUnitRunner {

	public XspJUnitRunner(final Class<?> testClass) throws InitializationError {
		super(testClass);

	}

	@Override
	protected void startUp() {
		System.out.println("Setting up the XSP test environment");
		System.setProperty("com.ibm.commons.platform", "com.ibm.domino.xsp.module.nsf.platform.DominoPlatform");
		BootstrapEnvironment.getInstance().setGlobalContextPath("/");
		new LCDEnvironment();

		Factory.startup();
		NotesThread.sinitThread();
		DominoExecutor executor = new XotsDominoExecutor(50);
		XotsDaemon.start(executor);
	}

	@Override
	protected void tearDown() {
		XotsDaemon.stop(600); // 10 minutes should be enough for tests
		Factory.shutdown();
		NotesThread.stermThread();
	}

}
