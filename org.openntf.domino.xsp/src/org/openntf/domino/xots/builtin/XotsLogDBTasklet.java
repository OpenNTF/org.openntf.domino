package org.openntf.domino.xots.builtin;

import org.openntf.domino.logging.LogGeneratorOpenLog;

@SuppressWarnings("serial")
public class XotsLogDBTasklet extends XotsAbstractNativeTasklet {

	public XotsLogDBTasklet() {
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
				System.out.println("XotsLogDBTasklet: Caught an InterruptedException; finishing ...");
				break;
			} catch (Throwable t) {
				System.err.println("XotsLogDBTasklet: Caught an unexpected exception " + t.getClass().getName() + ":");
				t.printStackTrace();
				System.err.println("XotsLogDBTasklet: Aborting.");
				break;
			}
		}
		LogGeneratorOpenLog._olQueue = null;
	}

}
