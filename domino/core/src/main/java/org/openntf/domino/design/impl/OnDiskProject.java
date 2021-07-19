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
package org.openntf.domino.design.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.ibm.commons.util.StringUtil;

public class OnDiskProject {
	private Path root_;

	public OnDiskProject(final Path root) {
		root_ = root;
	}

	public void export(final org.openntf.domino.design.DesignBase elem_) throws IOException {
		//elem.getDxlString(null)
		AbstractDesignBase elem = (AbstractDesignBase) elem_;
		String odp = elem.getOnDiskPath();
		if (StringUtil.isEmpty(odp)) {
			odp = elem.getNoteID() + ".note"; //$NON-NLS-1$
		}
		//elem.getLastModified();
		Path odsFile = root_.resolve(odp);
		Files.createDirectories(odsFile.getParent());
		elem.writeOnDiskFile(odsFile);
		if (elem instanceof HasMetadata) {
			Path meta = Paths.get(odsFile.toString() + ".metadata"); //$NON-NLS-1$
			((HasMetadata) elem).writeOnDiskMeta(meta);
		}

	}

}
