package org.openntf.domino.xsp.tests.rpr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.junit.SessionDb;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xsp.junit.ModuleJUnitRunner;
import org.openntf.junit4xpages.OsgiTest;

import com.ibm.domino.xsp.module.nsf.NotesContext;

@OsgiTest
@SessionDb("entwicklung/jfof4/proglibjfof.nsf")
@RunWith(ModuleJUnitRunner.class)
public class OSGiRunnerTest2 {

	@Test
	public void testModule() {
		NotesContext.getCurrent().getModule();
		System.out.println(Factory.findApplicationServices(WrapperFactory.class));
	}

}
