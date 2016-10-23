package org.openntf.domino.logging;

import org.openntf.domino.thread.AbstractDominoRunnable;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@SuppressWarnings("serial")
public class LogTaskletOpenLog extends AbstractDominoRunnable {

	public LogTaskletOpenLog() {
		super();
	}

	@Override
	public void run() {
		// TODO RPr Review this!
		while (!shouldStop()) {
			LogGeneratorOpenLog.OL_EntryToWrite oletw;
			try {
				oletw = LogGeneratorOpenLog._olQueue.take();
				if (oletw != null)
					oletw._logGenerator._olWriter.writeLogRecToDB(Factory.getSession(SessionType.CURRENT), oletw._logRec,
							oletw._logGenerator._startTime);
			} catch (InterruptedException e) {
				System.out.println("LogTaskletOpenLog: Caught an InterruptedException; finishing ...");
				break;
			} catch (Throwable t) {
				System.err.println("LogTaskletOpenLog: Caught an unexpected exception " + t.getClass().getName() + ":");
				t.printStackTrace();
				System.err.println("LogTaskletOpenLog: Aborting.");
				break;
			}
		}
		LogGeneratorOpenLog._olQueue = null;
	}

}
