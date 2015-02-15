package org.openntf.domino.xots;

import org.openntf.domino.config.XotsConfiguration;

public abstract class ScheduleData {

	//	private String replicaID;
	//	private String className;
	//	private String apiPath;
	//	private String[] schedules;
	//
	//	public ScheduleData(final String replicaID, final String className, final String apiPath, final String[] schedules) {
	//		// TODO Auto-generated constructor stub
	//		this.replicaID = replicaID;
	//		this.className = className;
	//		this.apiPath = apiPath;
	//		this.schedules = schedules;
	//	}
	//
	//	@Override
	//	public String toString() {
	//		return "ScheduleData [replicaID=" + replicaID + ", className=" + className + ", apiPath=" + apiPath + ", schedules="
	//				+ Arrays.toString(schedules) + "]";
	//	}
	public abstract XotsConfiguration getConfiguration();

	protected void init(final String[] schedules, final boolean onAllServers) {
		XotsConfiguration cfg = getConfiguration();
		cfg.setSchedulesDefault(schedules);
		cfg.setOnAllServers(onAllServers);

	}
}
