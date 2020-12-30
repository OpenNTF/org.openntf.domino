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
package org.openntf.domino.logging;

import org.openntf.domino.thread.AbstractDominoRunnable;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@SuppressWarnings({ "serial", "nls" })
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
