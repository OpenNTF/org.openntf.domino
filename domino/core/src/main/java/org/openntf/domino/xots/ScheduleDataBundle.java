package org.openntf.domino.xots;

import org.openntf.domino.config.Configuration;
import org.openntf.domino.config.XotsConfiguration;

public class ScheduleDataBundle extends ScheduleData {

	private String bundle_;
	private String taskletName_;

	private transient XotsConfiguration config_;

	public ScheduleDataBundle(final String bundle, final String taskletName, final String[] schedules, final boolean onAllServers) {
		bundle_ = bundle;
		taskletName_ = taskletName;
		init(schedules, onAllServers);

	}

	@Override
	public XotsConfiguration getConfiguration() {
		if (config_ == null) {
			config_ = Configuration.getXotsBundleConfiguration(bundle_, taskletName_);
		}
		return config_;
	}
}
