package org.openntf.domino.logging;

import org.openntf.domino.thread.tasklet.AbstractNativeTasklet;

@SuppressWarnings("serial")
public class LogTaskletOpenLog extends AbstractNativeTasklet {

	public LogTaskletOpenLog() {
		super();
	}

	@Override
	public void run() {
		for (;;) {
			LogGeneratorOpenLog.OL_EntryToWrite oletw;
			try {
				oletw = LogGeneratorOpenLog._olQueue.take();
				if (oletw != null)
					oletw._logGenerator._olWriter.writeLogRecToDB(this.getSession(), oletw._logRec, oletw._logGenerator._startTime);
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
