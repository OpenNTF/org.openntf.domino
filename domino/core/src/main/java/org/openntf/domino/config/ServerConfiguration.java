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
package org.openntf.domino.config;

import org.openntf.domino.Database;
import org.openntf.domino.Document;

public class ServerConfiguration extends ConfigurationObject {

	private String serverName_;

	public ServerConfiguration(final String serverName) {
		serverName_ = serverName;
	}

	@Override
	protected Object[] schema() {
		// @formatter:off
		return new Object[] {
				"ServerName", 		String.class,  //$NON-NLS-1$
				"ServerAlias", 		String.class, //$NON-NLS-1$
				"XotsTasks", 		Integer.class, //$NON-NLS-1$
				"XotsStopDelay", 	Integer.class //$NON-NLS-1$
		};
		// @formatter:on
	}

	public String getServerName() {
		return get("ServerName"); //$NON-NLS-1$
	}

	public String getServerAlias() {
		return get("ServerAlias"); //$NON-NLS-1$
	}

	public int getXotsTasks() {
		return get("XotsTasks", 10); //$NON-NLS-1$
	}

	public int getXotsStopDelay() {
		return get("XotsStopDelay", 15); //$NON-NLS-1$
	}

	/**
	 * Returns the document for this server
	 * 
	 * @return
	 */
	@Override
	protected Document getDocument(final boolean create) {
		Database odaDb = Configuration.getOdaDb();
		if (odaDb == null)
			return null;

		String unid = Configuration.computeUNID("ServerConfig:".concat(serverName_), odaDb); //$NON-NLS-1$

		Document currentConfig_ = odaDb.getDocumentByUNID(unid);
		if (currentConfig_ == null) {
			if (!create)
				return null;
			currentConfig_ = odaDb.createDocument();
			currentConfig_.setUniversalID(unid);
			currentConfig_.replaceItemValue("Form", "Configuration"); //$NON-NLS-1$ //$NON-NLS-2$
			currentConfig_.replaceItemValue("ServerName", serverName_).setNames(true); //$NON-NLS-1$
			currentConfig_.replaceItemValue("$ConflictAction", "3"); // merge - no conflicts //$NON-NLS-1$ //$NON-NLS-2$
			currentConfig_.save();
		}
		return currentConfig_;
	}

}
