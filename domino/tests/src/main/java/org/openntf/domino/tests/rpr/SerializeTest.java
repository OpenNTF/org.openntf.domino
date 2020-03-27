/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.tests.rpr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
import org.openntf.domino.Agent;
import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.utils.Factory.ThreadConfig;

@RunWith(DominoJUnitRunner.class)
public class SerializeTest {

	@SuppressWarnings("unchecked")
	protected <T> T test(final T obj, final boolean reboot) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(bos);
		out.writeObject(obj);

		if (reboot)
			doReboot();
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		ObjectInput in = null;
		in = new ObjectInputStream(bis);
		T ret = (T) in.readObject();
		assertEquals(obj, ret);
		return ret;
	}

	private void doReboot() {
		ThreadConfig tc = Factory.getThreadConfig();
		ISessionFactory csf1 = Factory.getSessionFactory(SessionType.CURRENT);
		ISessionFactory csf2 = Factory.getSessionFactory(SessionType.CURRENT_FULL_ACCESS);
		ISessionFactory csf3 = Factory.getSessionFactory(SessionType.FULL_ACCESS);
		ISessionFactory csf4 = Factory.getSessionFactory(SessionType.NATIVE);
		ISessionFactory csf5 = Factory.getSessionFactory(SessionType.SIGNER);
		ISessionFactory csf6 = Factory.getSessionFactory(SessionType.SIGNER_FULL_ACCESS);
		ISessionFactory csf7 = Factory.getSessionFactory(SessionType.TRUSTED);
		Factory.termThread();

		Factory.initThread(tc);
		Factory.setSessionFactory(csf1, SessionType.CURRENT);
		Factory.setSessionFactory(csf2, SessionType.CURRENT_FULL_ACCESS);
		Factory.setSessionFactory(csf3, SessionType.FULL_ACCESS);
		Factory.setSessionFactory(csf4, SessionType.NATIVE);
		Factory.setSessionFactory(csf5, SessionType.SIGNER);
		Factory.setSessionFactory(csf6, SessionType.SIGNER_FULL_ACCESS);
		Factory.setSessionFactory(csf7, SessionType.TRUSTED);

	}

	@Test
	public void testSession() throws IOException, ClassNotFoundException {
		Session sess = Factory.getSession(SessionType.CURRENT);
		Session ret = test(sess, false);
		assertTrue(sess == ret);

		sess = Factory.getSession(SessionType.CURRENT_FULL_ACCESS);
		ret = test(sess, false);
		assertTrue(sess == ret);

		sess = Factory.getSession(SessionType.FULL_ACCESS);
		ret = test(sess, false);
		assertTrue(sess == ret);

	}

	@Test
	public void testSessionReboot() throws IOException, ClassNotFoundException {
		Session sess = Factory.getSession(SessionType.CURRENT);
		Session ret = test(sess, true);
		assertFalse(sess == ret);

		sess = Factory.getSession(SessionType.CURRENT_FULL_ACCESS);
		ret = test(sess, true);
		assertFalse(sess == ret);

		sess = Factory.getSession(SessionType.FULL_ACCESS);
		ret = test(sess, true);
		assertFalse(sess == ret);

	}

	@Test
	public void testDatabase() throws IOException, ClassNotFoundException {
		Session sess = Factory.getSession(SessionType.CURRENT);
		Database db = sess.getDatabase("log.nsf");
		Database ret = test(db, false);
		assertTrue(db == ret);
	}

	@Test
	public void testDatabaseReboot() throws IOException, ClassNotFoundException {
		Session sess = Factory.getSession(SessionType.CURRENT);
		Database db = sess.getDatabase("log.nsf");
		Database ret = test(db, true);
		assertFalse(db == ret);
	}

	@Test
	public void testView() throws IOException, ClassNotFoundException {
		Session sess = Factory.getSession(SessionType.CURRENT);
		View vw = sess.getDatabase("log.nsf").getViews().get(0);
		View ret = test(vw, false);
		assertTrue(vw == ret);
	}

	@Test
	public void testViewReboot() throws IOException, ClassNotFoundException {
		Session sess = Factory.getSession(SessionType.CURRENT);
		View vw = sess.getDatabase("log.nsf").getViews().get(0);
		View ret = test(vw, true);
		assertFalse(vw == ret);
	}

	@Test
	public void testViewNameConflict() throws IOException, ClassNotFoundException {
		Session sess = Factory.getSession(SessionType.CURRENT);
		Database db = sess.getDatabase("log.nsf");

		View vw = db.getView("name1");
		assertNotNull(
				"you need 4 views for this test in your log.nsf: 'name1|sameAlias','name2|sameAlias', 'sameName|alias1', 'sameName|alias2'",
				vw);
		View ret = test(vw, true);
		assertFalse(vw == ret);

		vw = db.getView("name2");
		ret = test(vw, true);
		assertFalse(vw == ret);

		vw = db.getView("alias1");
		ret = test(vw, true);
		assertFalse(vw == ret);

		vw = db.getView("alias2");
		ret = test(vw, true);
		assertFalse(vw == ret);

	}

	@Test
	public void testAgentNameConflict() throws IOException, ClassNotFoundException {
		Session sess = Factory.getSession(SessionType.CURRENT);
		Database db = sess.getDatabase("log.nsf");

		Agent ag = db.getAgent("name1");
		assertNotNull(
				"you need 4 agents for this test in your log.nsf: 'name1|sameAlias','name2|sameAlias', 'sameName|alias1', 'sameName|alias2'",
				ag);
		Agent ret = test(ag, true);
		assertFalse(ag == ret);

		ag = db.getAgent("name2");
		ret = test(ag, true);
		assertFalse(ag == ret);

		ag = db.getAgent("alias1");
		ret = test(ag, true);
		assertFalse(ag == ret);

		ag = db.getAgent("alias2");
		ret = test(ag, true);
		assertFalse(ag == ret);

	}
}
