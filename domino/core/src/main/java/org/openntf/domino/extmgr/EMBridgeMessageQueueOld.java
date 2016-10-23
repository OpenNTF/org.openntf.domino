package org.openntf.domino.extmgr;

import java.util.Date;

import lotus.notes.internal.MessageQueue;

public class EMBridgeMessageQueueOld implements Runnable {
	protected MessageQueue queue_;
	protected boolean stopOrdered_ = false;

	public EMBridgeMessageQueueOld() {

	}

	public static final String QUEUE_NAME = "MQ$DOTS";
	public static final int MQ_TIMEOUT = 1119;
	public static final int MESSAGE_SIZE = 256;
	public static final int MESSAGE_WAIT_TIME = 1000;
	public static final int MQ_SIZE = 1000;
	public static final boolean STAT_REPORT = false;

	@Override
	public void run() {
		queue_ = new MessageQueue();
		int c = queue_.create(QUEUE_NAME, MQ_SIZE, 0);
		int i = queue_.open(QUEUE_NAME, 0);
		//		System.out.println("XOTS Message Queue listener created with status " + c + " and started with status of " + i + ". Discovered "
		//				+ queue_.getCount() + " messages in queue.");
		if (i == 0) {
			int getStatus = 0;
			StringBuffer sb = new StringBuffer(MESSAGE_SIZE);
			long eventCount = 0;
			long priorReportedEventCount = 0;
			long lastTimeoutTime = new Date().getTime();
			while (getStatus == 0) {
				if (Thread.interrupted() || stopOrdered_) {
					break;
				}
				try {
					getStatus = queue_.get(sb, MESSAGE_SIZE, 1, MESSAGE_WAIT_TIME);
					if (getStatus == 0) {
						eventCount++;
					} else if (getStatus == MQ_TIMEOUT) {
						getStatus = 0;
						if (STAT_REPORT && eventCount > priorReportedEventCount) {
							long msSinceLastTimeout = (new Date().getTime() - lastTimeoutTime);
							System.out.println("STAT: EMBridgeMessageQueue has processed " + eventCount + " events since starting and "
									+ (eventCount - priorReportedEventCount) + " events since last queue timeout " + msSinceLastTimeout
									+ "ms ago.");
							priorReportedEventCount = eventCount;
							lastTimeoutTime = new Date().getTime();
						}
					}
				} catch (Throwable t) {
					t.printStackTrace();
				} finally {
					sb.delete(0, MESSAGE_SIZE);
				}
			}
			queue_.close(0);
		} else {
			//			System.out.println("TEMP DEBUG MessageQueue listener failed to open queue with status of " + i);
		}
	}

	public void stop() {
		stopOrdered_ = true;
	}

}
