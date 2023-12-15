/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.design.DesignColumn;
import org.openntf.domino.design.DesignColumn.ResortOrder;
import org.openntf.domino.design.DesignColumn.SortOrder;
import org.openntf.domino.design.DesignView;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class Engage17DesignView implements Runnable {

	public Engage17DesignView() {

	}

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new Engage17DesignView(), TestRunnerUtil.NATIVE_SESSION);
	}

	@Override
	public void run() {
		Session sess = Factory.getSession(SessionType.NATIVE);
		Database extLib = sess.getDatabase("oda_1.nsf");
		View contacts = extLib.getView("AllContactsProgrammatic");
		if (null != contacts) {
			contacts.remove();
		}
		DatabaseDesign dbDesign = extLib.getDesign();
		DesignView newView = dbDesign.createView();
		newView.setSelectionFormula("SELECT Form=\"Contact\"");
		newView.setName("AllContactsProgrammatic");
		DesignColumn col = newView.addColumn();
		col.setItemName("State");
		col.setSortOrder(SortOrder.ASCENDING);
		col.setTitle("STATE");
		col.setCategorized(true);
		DesignColumn name = newView.addColumn();
		name.setFormula("FirstName+\" \"+LastName");
		name.setSortOrder(SortOrder.ASCENDING);
		name.setTitle("NAME");
		DesignColumn name2 = newView.addColumn();
		name2.setFormula("LastName");
		name2.setSortOrder(SortOrder.ASCENDING);
		name2.setTitle("NAME");
		DesignColumn city = newView.addColumn();
		city.setItemName("City");
		city.setTitle("CITY");
		city.setResortOrder(ResortOrder.ASCENDING);
		city.setSecondarySortColumn(2);
		newView.save();
	}

}
