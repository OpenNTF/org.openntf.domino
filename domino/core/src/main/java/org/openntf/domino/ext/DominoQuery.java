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
package org.openntf.domino.ext;

import java.util.List;

/**
 * OpenNTF Domino API extensions for the {@code DominoQuery} class.
 * 
 * @author Jesse Gallagher
 * @since 12.0.1
 */
public interface DominoQuery {

	/**
	 * Creates an index view optimized for DQL query terms.
	 * 
	 * @param indexName the name of the index to create
	 * @param itemNames the names of the items to index
	 * @param visible whether the view should be visible in the Notes client (e.g. created
	 *                without parentheses in the name)
	 * @param nobuild whether to skip building the view immediately
	 * @since V12.0.1
	 */
	void createIndex(String indexName, List<String> itemNames, boolean visible, boolean nobuild);

	/**
	 * Creates an index view optimized for DQL query terms.
	 * 
	 * @param indexName the name of the index to create
	 * @param itemName the names of the items to index
	 * @since V12.0.1
	 */
	void createIndex(String indexName, List<String> itemNames);
}
