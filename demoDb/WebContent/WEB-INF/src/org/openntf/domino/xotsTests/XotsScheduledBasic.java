package org.openntf.domino.xotsTests;

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
