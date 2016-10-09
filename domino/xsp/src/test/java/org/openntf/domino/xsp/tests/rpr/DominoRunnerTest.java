package org.openntf.domino.xsp.tests.rpr;

import lotus.domino.NotesException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.exceptions.UserAccessException;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.junit.SessionUser;

@RunWith(DominoJUnitRunner.class)
public class DominoRunnerTest extends NotesRunnerTest {

	@Override
	@SessionUser("CN=Tony Stark/O=Avengers")
	@Test(expected = UserAccessException.class)
	public void testSessionNamedEx2() throws NotesException {
		super.testSessionNamedEx2();
	}
}
