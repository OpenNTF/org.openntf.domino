package org.openntf.domino.xotsTests;

/*
 	Copyright 2014 OpenNTF Domino API Team Licensed under the Apache License, Version 2.0
	(the "License"); you may not use this file except in compliance with the
	License. You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
	or agreed to in writing, software distributed under the License is distributed
	on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
	express or implied. See the License for the specific language governing
	permissions and limitations under the License
	
*/

import java.util.Map;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.thread.DominoSessionType;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xots.XotsBaseTasklet;
import org.openntf.domino.xots.XotsDaemon;
import org.openntf.domino.xots.annotations.Persistent;
import org.openntf.domino.xots.annotations.Persistent.Scope;

import com.ibm.xsp.extlib.util.ExtLibUtil;

@Persistent(scope = Scope.USER)
public class XotsBasic extends XotsBaseTasklet {
	private static final long serialVersionUID = 1L;
	private String apiPath;
	private Map<String, Object> applicationScope;

	public XotsBasic() {
		Database db = Factory.getSession().getCurrentDatabase();
		setApiPath(db.getApiPath());

		ExtLibUtil.getApplicationScope().put("MessageFromXots",
				"Please refresh page to see message (set from constructor)");
		ExtLibUtil.getApplicationScope().put("MessageFromXotsConstructor", "Nothing yet (set from constructor)");
		setApplicationScope(ExtLibUtil.getApplicationScope());
		this.setRunAs("Admin");
		this.setSessionType(DominoSessionType.NAMED);
	}

	public void queue() {
		XotsDaemon.addToQueue(this);
	}

	@Override
	public void run() {
		try {
			Session sess = this.getSession();
			Database db = sess.getDatabase(getApiPath());

			String msg = "User is " + sess.getEffectiveUserName();
			msg = msg + " access is " + Integer.toString(db.getCurrentAccessLevel());
			System.out.println(msg);
			getApplicationScope().put("MessageFromXots", msg + "(set from Xots method)");
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void setApiPath(String apiPath) {
		this.apiPath = apiPath;
	}

	public String getApiPath() {
		return apiPath;
	}

	public void setApplicationScope(Map<String, Object> applicationScope) {
		this.applicationScope = applicationScope;
	}

	public Map<String, Object> getApplicationScope() {
		return applicationScope;
	}

}
