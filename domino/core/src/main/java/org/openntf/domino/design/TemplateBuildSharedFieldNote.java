/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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
package org.openntf.domino.design;

import java.util.Date;

/**
 * Used as Design-based access to shared field $TemplateBuild. This can be used to define template version settings, which are then
 * displayed
 *
 * @author Paul Withers
 * @since 4.1.0
 *
 */
public interface TemplateBuildSharedFieldNote extends DesignBase {

	/**
	 * @return the template name stored in the Design note's $TemplateBuildName Item
	 */
	public String getTemplateBuildName();

	/**
	 * @param templateBuildName
	 *            to be stored in the Design note's $TemplateBuildName Item
	 */
	public void setTemplateBuildName(String templateBuildName);

	/**
	 * @return template build version stored in the Design note's $TemplateBuild Item
	 */
	public String getTemplateBuildVersion();

	/**
	 * @param templateBuildVersion
	 *            to be stored in the Design note's $TemplateBuild Item
	 */
	public void setTemplateBuildVersion(String templateBuildVersion);

	/**
	 * @return template build date stored (as DateTime object) in the Design note's $TemplateBuildDate Item
	 */
	public Date getTemplateBuildDate();

	/**
	 * @param date
	 *            to be stored (as DateTime object) in the Design note's $TemplateBuildDate Item
	 */
	public void setTemplateBuildDate(Date date);

}
