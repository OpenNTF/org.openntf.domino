package org.openntf.domino.xots;

import org.openntf.domino.config.Configuration;
import org.openntf.domino.config.XotsConfiguration;

public class ScheduleDataNSF extends ScheduleData {

	private String dbPath_;
	private String taskletName_;
	private transient XotsConfiguration config_;

	/**
	 * 
	 * @param dbPath
	 *            the local DB-path
	 * @param taskletName
	 * @param schedules
	 * @param onAllServers
	 */
	public ScheduleDataNSF(final String dbPath, final String taskletName, final String[] schedules, final boolean onAllServers) {
		dbPath_ = dbPath;
		taskletName_ = taskletName;
		init(schedules, onAllServers);
	}

	@Override
	public XotsConfiguration getConfiguration() {
		if (config_ == null) {
			config_ = Configuration.getXotsNSFConfiguration(dbPath_, taskletName_);
		}
		return config_;
	}
}
