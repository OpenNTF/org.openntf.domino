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

import java.util.Collection;
import java.util.Map;

/**
 * @author Roland Praml
 * 
 */
public interface XspResource extends DesignBaseNamed {
	public Collection<String> getClassNames();

	public Map<String, byte[]> getClassData();

	public void setClassData(Map<String, byte[]> classData);
}
