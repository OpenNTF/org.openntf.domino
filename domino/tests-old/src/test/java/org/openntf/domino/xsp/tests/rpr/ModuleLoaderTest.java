package org.openntf.domino.xsp.tests.rpr;

import javax.servlet.ServletException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.xsp.junit.ModuleJUnitRunner;

@RunWith(ModuleJUnitRunner.class)
@Module("entwicklung/JFOF4/proglibJFOF.nsf")
public class ModuleLoaderTest {

	@Test
	public void testModuleLoad() throws ServletException {

		System.out.println(Thread.currentThread().getContextClassLoader());
	}
}
