package org.openntf.domino.xsp.tests.rpr;

import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.junit.SessionDb;
import org.openntf.domino.xsp.junit.ModuleJUnitRunner;
import org.openntf.junit4xpages.OsgiTest;

@OsgiTest
@SessionDb("entwicklung/jfof4/proglibjfof.nsf")
@RunWith(ModuleJUnitRunner.class)
public class ASMTest {

	@Test
	public void testASM() throws InterruptedException, ExecutionException {
		lotus.notes.AgentSecurityManager asm = (lotus.notes.AgentSecurityManager) System.getSecurityManager();
		System.out.println(asm);
	}
}
