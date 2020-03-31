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
package org.openntf.domino.xsp.tests.paul;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.design.DatabaseDesign.DbProperties;
import org.openntf.domino.design.DatabaseDesign.UnreadReplicationSetting;
import org.openntf.domino.design.impl.DatabaseDesign;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class DbPropTest implements Runnable {

	public DbPropTest() {

	}

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new DbPropTest(), TestRunnerUtil.NATIVE_SESSION);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			Session sess = Factory.getSession(SessionType.NATIVE);
			Database db = sess.getDatabase("PrivateTest.nsf");
			StringBuilder sb = new StringBuilder();
			setDbInfo(db);
			getDbInfo(db, sb);
			System.out.println(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getDbInfo(final Database db, final StringBuilder sb) {
		DatabaseDesign dbDesign = (DatabaseDesign) db.getDesign();
		List<DbProperties> props = dbDesign.getDatabaseProperties();
		for (DbProperties prop : props) {
			sb.append(prop);
			addNewLine(sb);
		}
		sb.append(dbDesign.getTemplateName());
		addNewLine(sb);
		sb.append(dbDesign.getNameIfTemplate());
		addNewLine(sb);
		sb.append("DAS Setting = " + dbDesign.getDasMode().name());
		addNewLine(sb);
		sb.append("Replicate Unread = " + dbDesign.getReplicateUnreadSetting().name());
		addNewLine(sb);
		sb.append("Max Updated = " + dbDesign.getMaxUpdatedBy());
		addNewLine(sb);
		sb.append("Max Revisions = " + dbDesign.getMaxRevisions());
		addNewLine(sb);
		sb.append("Soft Deletes = " + dbDesign.getSoftDeletionsExpireIn());
	}

	private void setDbInfo(final Database db) throws IOException {
		DatabaseDesign dbDesign = (DatabaseDesign) db.getDesign();
		HashMap<DbProperties, Boolean> props = new HashMap<>();
		props.put(DbProperties.USE_JS, false);
		props.put(DbProperties.REQUIRE_SSL, true);
		props.put(DbProperties.NO_URL_OPEN, false);
		props.put(DbProperties.ENHANCED_HTML, true);
		dbDesign.setReplicateUnreadSetting(UnreadReplicationSetting.CLUSTER);
		dbDesign.setDatabaseProperties(props);
		dbDesign.setMaxRevisions(30);
		dbDesign.save();
	}

	private StringBuilder addNewLine(final StringBuilder sb) {
		sb.append("\r\n");
		return sb;
	}

}
