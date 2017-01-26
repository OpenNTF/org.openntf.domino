package org.openntf.domino.extmgr;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import lotus.notes.internal.MessageQueue;

import org.openntf.domino.extmgr.events.EMEventIds;
import org.openntf.domino.xots.Xots;

public enum EMBridgeMessageQueue {
	INSTANCE;

	public static final String QUEUE_NAME = "MQ$DOTS";
	private static AtomicBoolean isStarted = new AtomicBoolean(false);
	public static final int MQ_TIMEOUT = 1119;
	public static final int MESSAGE_SIZE = 256;
	public static final int MESSAGE_WAIT_TIME = 1000;
	public static final int MQ_SIZE = 10000;

	private QueueListener listener_;
	private QueueDispatcher dispatcher_;
	private MessageQueue queue_;
	private Map<EMEventIds, List<IEMBridgeSubscriber>> subscribers_ = new EnumMap<EMEventIds, List<IEMBridgeSubscriber>>(EMEventIds.class);
	private boolean stopOrdered_;
	private boolean statReport_ = false;
	long priorMaxEventCount_ = 0;
	long lastTimeoutTime_ = new Date().getTime();

	private static class QueueDispatcher implements Runnable {
		private EMBridgeMessageQueue bridge_;
		private BlockingQueue<String> queue_ = new ArrayBlockingQueue<String>(MQ_SIZE);

		QueueDispatcher(final EMBridgeMessageQueue bridge) {
			bridge_ = bridge;
		}

		@Override
		public void run() {
			try {
				while (!bridge_.isStopOrdered()) {
					if (Thread.interrupted()) {
						Thread.currentThread().interrupt();
						break;
					}
					String event = queue_.poll(500, TimeUnit.MILLISECONDS);
					if (event != null) {
						int eventCode = EMBridgeEventFactory.getEventId(event);
						EMEventIds id = EMEventIds.getEMEventFromId(eventCode);
						//						System.out.println("TEMP DEBUG: Dispatcher thread dispatched an event " + id.toString() + ": " + event);
						List<IEMBridgeSubscriber> subscribers = bridge_.getSubscriberList(id);
						if (subscribers != null && !subscribers.isEmpty()) {
							for (IEMBridgeSubscriber subscriber : subscribers) {
								subscriber.handleMessage(id, event);
								//								System.out.println("TEMP DEBUG: Dispatcher thread dispatched an event " + id.toString() + ": " + event);
							}
						}
					}
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}

		public void queueEvent(final String event) {
			queue_.offer(event);
		}
	}

	private static class QueueListener implements Runnable {
		private EMBridgeMessageQueue bridge_;

		QueueListener(final EMBridgeMessageQueue bridge) {
			bridge_ = bridge;
		}

		@Override
		public void run() {
			QueueDispatcher dispatch = bridge_.getDispatcher();
			int getStatus = 0;
			StringBuffer sb = new StringBuffer(MESSAGE_SIZE);
			long eventCount = 0;
			try {
				while (getStatus == 0) {
					if (Thread.interrupted()) {
						break;
					}
					getStatus = bridge_.getQueue().get(sb, MESSAGE_SIZE, 1, MESSAGE_WAIT_TIME);
					if (getStatus == 0) {
						dispatch.queueEvent(sb.toString());
						sb.delete(0, MESSAGE_SIZE);
						eventCount++;
					} else if (getStatus == MQ_TIMEOUT) {
						if (bridge_.isStopOrdered()) {
							break;
						}
						getStatus = 0;
						bridge_.reportStats(eventCount);
						eventCount = 0;
					}
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} catch (Throwable t) {
				t.printStackTrace();
			} finally {
				bridge_.getQueue().close(0);
			}
		}

	}

	protected MessageQueue getQueue() {
		if (queue_ == null) {
			queue_ = new MessageQueue();
			int c = queue_.create(QUEUE_NAME, MQ_SIZE, 0);
			int i = queue_.open(QUEUE_NAME, 0);
			if (i != 0) {
				//TODO report error
			}
		}
		return queue_;
	}

	protected QueueListener getListener() {
		return listener_;
	}

	protected QueueDispatcher getDispatcher() {
		return dispatcher_;
	}

	public boolean isStopOrdered() {
		return stopOrdered_;
	}

	public static void start() {
		if (!isStarted.get()) {
			INSTANCE.dispatcher_ = new QueueDispatcher(INSTANCE);
			Xots.getService().execute(INSTANCE.dispatcher_);
			INSTANCE.listener_ = new QueueListener(INSTANCE);
			Xots.getService().execute(INSTANCE.listener_);
			isStarted = new AtomicBoolean(true);
		}
	}

	public static void stop() {
		INSTANCE.stopOrdered_ = true;
	}

	public void reportStats(final long eventCount) throws InterruptedException {
		if (statReport_) {
			if (eventCount > priorMaxEventCount_) {
				priorMaxEventCount_ = eventCount;
			}
			long msSinceLastTimeout = (new Date().getTime() - lastTimeoutTime_);
			System.out.println("STAT: EMBridgeMessageQueue has processed " + eventCount + " events since starting and "
					+ (eventCount - priorMaxEventCount_) + " events since last queue timeout " + msSinceLastTimeout + "ms ago.");
			priorMaxEventCount_ = eventCount;
			lastTimeoutTime_ = new Date().getTime();
		}
	}

	public static void addSubscriber(final IEMBridgeSubscriber subscriber) {
		for (EMEventIds id : subscriber.getSubscribedEventIds()) {
			List<IEMBridgeSubscriber> list = INSTANCE.subscribers_.get(id);
			if (list == null) {
				list = new ArrayList<IEMBridgeSubscriber>();
				INSTANCE.subscribers_.put(id, list);
			}
			list.add(subscriber);
			System.out.println("TEMP DEBUG registered a new subscriber for event " + id.toString());
		}
	}

	public static void removeSubscriber(final IEMBridgeSubscriber subscriber) {
		for (EMEventIds id : subscriber.getSubscribedEventIds()) {
			List<IEMBridgeSubscriber> list = INSTANCE.subscribers_.get(id);
			if (list == null) {
				//not subscribed
			} else {
				list.remove(subscriber);
			}
		}
	}

	protected List<IEMBridgeSubscriber> getSubscriberList(final EMEventIds eventid) {
		return subscribers_.get(eventid);
	}

}
