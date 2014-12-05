package org.openntf.domino.tests.rpr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.Session;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@RunWith(DominoJUnitRunner.class)
public class SerializeTest {

	protected <T> T test(final T obj) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(bos);
		out.writeObject(obj);

		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		ObjectInput in = null;
		in = new ObjectInputStream(bis);
		T ret = (T) in.readObject();
		assertEquals(obj, ret);
		return ret;
	}

	@Test
	public void testSession() throws IOException, ClassNotFoundException {
		Session sess = Factory.getSession(SessionType.CURRENT);
		Session ret = test(sess);
		assertTrue(sess == ret);

		sess = Factory.getSession(SessionType.CURRENT_FULL_ACCESS);
		ret = test(sess);
		assertTrue(sess == ret);

		sess = Factory.getSession(SessionType.FULL_ACCESS);
		ret = test(sess);
		assertTrue(sess == ret);

	}
}
