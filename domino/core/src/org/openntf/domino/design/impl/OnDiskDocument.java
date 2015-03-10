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

package org.openntf.domino.design.impl;

import java.io.File;
import java.io.Serializable;
import java.net.URI;

/**
 * 
 * @author Alexander Wagner, FOCONIS AG
 * 
 */
public class OnDiskDocument implements Serializable {

	private static final long serialVersionUID = -3298261314433290242L;

	//private static final String NOTEINFO_UNID = "noteinfo unid=\"";

	private String path_;
	private long dbTimeStamp_;
	private long diskTimeStamp_;

	private transient boolean processed;
	private transient File file_;

	private String md5_;

	public OnDiskDocument(final File parent, final File file) {
		file_ = file;
		setProcessed(false);

		// example:
		// parent 	= C:\documents\odp\
		// file 	= C:\documents\odp\Code\Scriptlibraries\lib.lss
		// odpFolder= C:\documents\odp\Code\Scriptlibraries
		// relUri 	= lib.lss

		URI relUri = parent.toURI().relativize(file.toURI());
		path_ = relUri.getPath();

	}

	public String getPath() {
		return path_;
	}

	public File getFile() {
		return file_;
	}

	public long getDbTimeStamp() {
		return dbTimeStamp_;
	}

	public void setDbTimeStamp(final long timeStamp) {
		this.dbTimeStamp_ = timeStamp;
	}

	public long getDiskTimeStamp() {
		return diskTimeStamp_;
	}

	public void setDiskTimeStamp(final long timeStamp) {
		this.diskTimeStamp_ = timeStamp;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(final boolean processed) {
		this.processed = processed;
	}

	public void setFile(final File file) {
		this.file_ = file;

	}

	public void setMD5(final String md5) {
		this.md5_ = md5;
	}

	public String getMD5() {
		return md5_;
	}

	@Override
	public String toString() {
		return "OnDiskDocument: " + path_;
	}

	//	public String getUniversalID() throws FileNotFoundException {
	//		Scanner scanner = new Scanner(file_);
	//		try {
	//
	//			for (int i = 0; i < 10 && scanner.hasNextLine(); i++) {
	//				String tmp = scanner.nextLine();
	//				if (tmp.contains(NOTEINFO_UNID)) {
	//					tmp = tmp.substring(tmp.indexOf(NOTEINFO_UNID) + NOTEINFO_UNID.length());
	//					return tmp.substring(0, tmp.indexOf("\""));
	//				}
	//			}
	//		} finally {
	//			scanner.close();
	//		}
	//		return "";
	//
	//	}

}
