/*
 * � Copyright IBM Corp. 2009,2010
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
package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public class NSFDbCreateEvent extends AbstractEMBridgeEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Dbclass,
			EMBridgeEventParams.Forcecreate };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public static final int DBCLASS_NSFTESTFILE = 0xff00;
	public static final int DBCLASS_NOTEFILE = 0xff01;
	public static final int DBCLASS_DESKTOP = 0xff02;
	public static final int DBCLASS_NOTECLIPBOARD = 0xff03;
	public static final int DBCLASS_TEMPLATEFILE = 0xff04;
	public static final int DBCLASS_GIANTNOTEFILE = 0xff05;
	public static final int DBCLASS_HUGENOTEFILE = 0xff06;
	public static final int DBCLASS_ONEDOCFILE = 0xff07; /* Not a mail message */
	public static final int DBCLASS_V2NOTEFILE = 0xff08;
	public static final int DBCLASS_ENCAPSMAILFILE = 0xff09; /* Specifically used by alt mail */
	public static final int DBCLASS_LRGENCAPSMAILFILE = 0xff0a; /* Specifically used by alt mail */
	public static final int DBCLASS_V3NOTEFILE = 0xff0b;
	public static final int DBCLASS_OBJSTORE = 0xff0c; /* Object store */
	public static final int DBCLASS_V3ONEDOCFILE = 0xff0d;
	public static final int DBCLASS_V4NOTEFILE = 0xff0e;
	public static final int DBCLASS_V5NOTEFILE = 0xff0f;
	public static final int DBCLASS_V6NOTEFILE = 0xff10;
	public static final int DBCLASS_V8NOTEFILE = 0xff11;
	public static final int DBCLASS_V85NOTEFILE = 0xff12;

	private boolean bForceCreation;
	private int wDbClass;

	/**
	 * @param eventId
	 */
	public NSFDbCreateEvent(final int eventId) {
		super(eventId);
	}

	public NSFDbCreateEvent() {
		super(IEMBridgeEvent.EM_NSFDBCREATE);
	}

	/**
	 * @param sBool
	 */
	private void setForceCreation(final String sBool) {
		this.bForceCreation = Boolean.parseBoolean(sBool);
	}

	/**
	 * @return
	 */
	public boolean isForceCreation() {
		return bForceCreation;
	}

	/**
	 * @param dbClass
	 */
	private void setDbClass(final String dbClass) {
		this.wDbClass = parseInt(dbClass);
	}

	/**
	 * @return
	 */
	public int getDbClass() {
		return wDbClass;
	}
}
