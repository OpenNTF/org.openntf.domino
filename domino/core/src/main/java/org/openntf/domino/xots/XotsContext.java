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

/**
 * @author Paul Withers
 * @since 2.5.0
 *
 */
public class XotsContext {
	private String contextApiPath;
	private String openLogApiPath;
	private String taskletClass;

	/**
	 * Getter for contextApiPath
	 * 
	 * @return String ApiPath of database to act upon
	 */
	public String getContextApiPath() {
		return contextApiPath;
	}

	/**
	 * Setter for contextApiPath
	 * 
	 * @param contextApiPath
	 *            String ApiPath of database to act upon
	 */
	public void setContextApiPath(final String contextApiPath) {
		this.contextApiPath = contextApiPath;
	}

	/**
	 * Getter for openLogApiPath
	 * 
	 * @return String ApiPath of OpenLog database to log to
	 */
	public String getOpenLogApiPath() {
		return openLogApiPath;
	}

	/**
	 * Setter for openLogApiPath
	 * 
	 * @param openLogApiPath
	 *            String ApiPath of OpenLog database to log to
	 */
	public void setOpenLogApiPath(final String openLogApiPath) {
		this.openLogApiPath = openLogApiPath;
	}

	/**
	 * Getter for taskletClass
	 * 
	 * @return String tasklet class to log for in OpenLog
	 */
	public String getTaskletClass() {
		return taskletClass;
	}

	/**
	 * Setter for taskletClass
	 * 
	 * @param taskletClass
	 *            String tasklet class to log for in OpenLog
	 */
	public void setTaskletClass(final String taskletClass) {
		this.taskletClass = taskletClass;
	}

}
