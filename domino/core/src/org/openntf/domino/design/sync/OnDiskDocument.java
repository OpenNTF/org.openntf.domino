/*
 * Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.openntf.domino.design.sync;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.openntf.domino.Document;
import org.openntf.domino.utils.DominoUtils;

/**
 * 
 * @author Alexander Wagner, FOCONIS AG
 * 
 */
public class OnDiskDocument extends OnDiskAbstract<Document> {

	private static final long serialVersionUID = -3298261314433290242L;

	private transient String unid_;

	public OnDiskDocument(final File parent, final File file) {
		super(parent, file);

	}

	@Override
	public String getKey() {
		if (unid_ == null) {
			//			try {
			//				XMLDocument dxl = new XMLDocument();
			//				FileInputStream is = new FileInputStream(getFile());
			//				try {
			//					dxl.loadInputStream(is);
			//				} finally {
			//					is.close();
			//				}
			//				XMLNode noteinfo = dxl.getDocumentElement().selectSingleNode("//noteinfo");
			//				unid_ = noteinfo.getAttribute("unid");
			//			} catch (IOException e) {
			//				DominoUtils.handleException(e, getFile().getAbsolutePath());
			//			} catch (SAXException e) {
			//				DominoUtils.handleException(e, getFile().getAbsolutePath());
			//			} catch (ParserConfigurationException e) {
			//				DominoUtils.handleException(e, getFile().getAbsolutePath());
			//			}
			try {
				// this is x times faster than the code above!
				Scanner scanner = new Scanner(getFile());
				try {
					int i = 0;
					while (scanner.hasNextLine() && i++ < 10) {
						String line = scanner.nextLine();
						if (line.contains("<noteinfo")) {
							int pos = line.indexOf("unid=");
							unid_ = line.substring(pos + 6, pos + 6 + 32);
							break;
						}
					}
				} finally {
					scanner.close();
				}
			} catch (IOException e) {
				DominoUtils.handleException(e, getFile().getAbsolutePath());
			}
		}
		return unid_;

	}

	@Override
	public void setDbTimeStamp(final Document dbElem) {
		setDbTimeStamp(dbElem.getLastModifiedDate().getTime());

	}

	@Override
	public long getDbTimeStampDelta(final Document dbElem) {
		return dbElem.getLastModifiedDate().getTime() - getDbTimeStamp();
	}

}
