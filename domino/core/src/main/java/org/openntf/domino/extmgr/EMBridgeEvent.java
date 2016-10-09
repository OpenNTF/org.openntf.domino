/**
 * 
 */
package org.openntf.domino.extmgr;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.EnumMap;
import java.util.Set;

import org.openntf.domino.extmgr.events.IEMBridgeEvent;

/**
 * @author nfreeman
 * 
 */
public class EMBridgeEvent implements Runnable {
	public enum TYPE {
		TRIGGERED, SCHEDULED, MANUAL, STARTUP
	}

	private final TYPE type_;
	private final Date when_;
	private IEMBridgeEvent refEvent_;
	private final EnumMap<EMBridgeEventParams, Object> params_ = new EnumMap<EMBridgeEventParams, Object>(EMBridgeEventParams.class);
	private Object tasklet_;
	private Method method_;

	public TYPE getType() {
		return type_;
	}

	public EMBridgeEvent(final TYPE type) {
		type_ = type;
		when_ = new Date();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// System.out.println("Running DotsEvent!");
		if (method_ != null && tasklet_ != null) {
			try {

				method_.invoke(tasklet_, this, null); // TODO: enable progress monitoring
			} catch (Throwable t) {
				t.printStackTrace();
			}
		} else {
			System.out.println("No task or method defined!!!");
		}

		EMBridgeEventFactory.recycleEvent(this); // return event object to pool for future runs
	}

	public void loadEvent(final IEMBridgeEvent event, final String buffer) {
		refEvent_ = event;
		EMBridgeEventParams.populateParamMap(params_, event.getParams(), buffer);
	}

	public void loadMethod(final Method method, final Object tasklet) {
		tasklet_ = tasklet;
		method_ = method;
	}

	public Object getEventParam(final EMBridgeEventParams param) {
		if (params_.containsKey(param)) {
			return params_.get(param);
		}
		return null;
	}

	public Set<EMBridgeEventParams> getAvailableParams() {
		return params_.keySet();
	}

	public void recycle() {
		when_.setTime(0);
		params_.clear();
		tasklet_ = null;
		method_ = null;
		refEvent_ = null;
	}

}
