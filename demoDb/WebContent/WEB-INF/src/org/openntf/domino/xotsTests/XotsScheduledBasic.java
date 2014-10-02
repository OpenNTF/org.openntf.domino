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

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.thread.DominoSessionType;
import org.openntf.domino.xots.XotsAbstractScheduledTasklet;
import org.openntf.domino.xots.XotsDaemon;
import org.openntf.domino.xots.annotations.Schedule;
import org.openntf.domino.xsp.XspOpenLogUtil;

import com.ibm.xsp.extlib.util.ExtLibUtil;

// NOT YET WORKING

@Schedule(frequency = 1, timeunit = TimeUnit.MINUTES, weekdays = { Calendar.MONDAY, Calendar.FRIDAY }, starthour = 8, endhour = 16)
public class XotsScheduledBasic extends XotsAbstractScheduledTasklet {
	private static final long serialVersionUID = 1L;

	public XotsScheduledBasic() {
		this.setSessionType(DominoSessionType.SIGNER);
		XotsDaemon.addToQueue(this);
	}

	@Override
	public void run() {
		try {
			Session sess = getSession();
			Date now = new Date();
			String msg = "Running Xots " + now.toString();
			msg = msg + "\n " + sess.getEffectiveUserName() + " is on server " + String.valueOf(sess.isOnServer());
			Database db = sess.getDatabase("log.nsf");
			msg = msg + "\n" + "Log at " + db.getApiPath() + " is " + db.getSize();
			System.out.println(msg);
			ExtLibUtil.getApplicationScope().put("MessageFromXotsSchedule", msg);
			XspOpenLogUtil.logEvent(new Throwable(msg), msg, Level.FINE, null);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
