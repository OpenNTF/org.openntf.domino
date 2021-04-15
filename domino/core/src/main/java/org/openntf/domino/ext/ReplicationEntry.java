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
/**
 * 
 */
package org.openntf.domino.ext;

import java.util.Collection;

import org.openntf.domino.Replication;

/**
 * @author withersp
 * 
 *         OpenNTF extensions to ReplicationEntry class
 */
public interface ReplicationEntry {

	/**
	 * Gets the parent Replication object of the entry
	 * 
	 * @return Replication parent
	 * @since org.openntf.domino 1.0.0
	 */
	public Replication getParent();

	/**
	 * Loads a collection of Views to be applied to the ReplicationEntry
	 * 
	 * @param views
	 *            Collection<String> of view names or aliases
	 * @since org.openntf.domino 1.0.0
	 */
	public void setViews(final Collection<String> views);

}
