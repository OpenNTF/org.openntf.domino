/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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
package org.openntf.domino.xots;

import org.openntf.domino.config.XotsConfiguration;

public abstract class ScheduleData {

	//	private String replicaID;
	//	private String className;
	//	private String apiPath;
	//	private String[] schedules;
	//
	//	public ScheduleData(final String replicaID, final String className, final String apiPath, final String[] schedules) {
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
