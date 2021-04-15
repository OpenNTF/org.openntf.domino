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
package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public interface IEMBridgeEvent {

	//	public static final int EM_NSFDBCLOSESESSION = 1;
	//	public static final int EM_NSFDBCLOSE = 2;
	//	public static final int EM_NSFDBCREATE = 3;
	//	public static final int EM_NSFDBDELETE = 4;
	//	public static final int EM_NSFNOTEOPEN = 5;
	//	public static final int EM_NSFNOTECLOSE = 6;
	//	public static final int EM_NSFNOTECREATE = 7;
	//	public static final int EM_NSFNOTEDELETE = 8;
	//	public static final int EM_NSFNOTEOPENBYUNID = 10;
	//	public static final int EM_FTGETLASTINDEXTIME = 11;
	//	public static final int EM_FTINDEX = 12;
	//	public static final int EM_FTSEARCH = 13;
	//	public static final int EM_NIFFINDBYKEY = 14;
	//	public static final int EM_NIFFINDBYNAME = 15;
	//	public static final int EM_NIFOPENNOTE = 17;
	//	public static final int EM_NIFREADENTRIES = 18;
	//	public static final int EM_NIFUPDATECOLLECTION = 20;
	//	public static final int EM_NSFDBALLOCOBJECT = 22;
	//	public static final int EM_NSFDBCOMPACT = 23;
	//	public static final int EM_NSFDBDELETENOTES = 24;
	//	public static final int EM_NSFDBFREEOBJECT = 25;
	//	public static final int EM_NSFDBGETMODIFIEDNOTETABLE = 26;
	//	public static final int EM_NSFDBGETNOTEINFO = 29;
	//	public static final int EM_NSFDBGETNOTEINFOBYUNID = 30;
	//	public static final int EM_NSFDBGETOBJECTSIZE = 31;
	//	public static final int EM_NSFDBGETSPECIALNOTEID = 32;
	//	public static final int EM_NSFDBINFOGET = 33;
	//	public static final int EM_NSFDBINFOSET = 34;
	//	public static final int EM_NSFDBLOCATEBYREPLICAID = 35;
	//	public static final int EM_NSFDBMODIFIEDTIME = 36;
	//	public static final int EM_NSFDBREADOBJECT = 37;
	//	public static final int EM_NSFDBREALLOCOBJECT = 39;
	//	public static final int EM_NSFDBREPLICAINFOGET = 40;
	//	public static final int EM_NSFDBREPLICAINFOSET = 41;
	//	public static final int EM_NSFDBSPACEUSAGE = 42;
	//	public static final int EM_NSFDBSTAMPNOTES = 43;
	//	public static final int EM_NSFDBWRITEOBJECT = 45;
	//	public static final int EM_NSFNOTEUPDATE = 47;
	//	public static final int EM_NIFOPENCOLLECTION = 50;
	//	public static final int EM_NIFCLOSECOLLECTION = 51;
	//	public static final int EM_NSFDBGETBUILDVERSION = 52;
	//	public static final int EM_NSFDBRENAME = 54;
	//	public static final int EM_NSFDBITEMDEFTABLE = 56;
	//	public static final int EM_NSFDBREOPEN = 59;
	//	public static final int EM_NSFDBOPENEXTENDED = 63;
	//	public static final int EM_NSFNOTEOPENEXTENDED = 64;
	//	public static final int EM_TERMINATENSF = 69;
	//	public static final int EM_NSFNOTEDECRYPT = 70;
	//	public static final int EM_GETPASSWORD = 73;
	//	public static final int EM_SETPASSWORD = 74;
	//	public static final int EM_NSFCONFLICTHANDLER = 75;
	//	public static final int EM_MAILSENDNOTE = 83;
	//	public static final int EM_CLEARPASSWORD = 90;
	//	public static final int EM_NSFNOTEUPDATEXTENDED = 102;
	//	public static final int EM_SCHFREETIMESEARCH = 105;
	//	public static final int EM_SCHRETRIEVE = 106;
	//	public static final int EM_SCHSRVRETRIEVE = 107;
	//	public static final int EM_NSFDBCOMPACTEXTENDED = 121;
	//	public static final int EM_ADMINPPROCESSREQUEST = 124;
	//	public static final int EM_NIFGETCOLLECTIONDATA = 126;
	//	public static final int EM_NSFDBCOPYNOTE = 127;
	//	public static final int EM_NSFNOTECOPY = 128;
	//	public static final int EM_NSFNOTEATTACHFILE = 129;
	//	public static final int EM_NSFNOTEDETACHFILE = 130;
	//	public static final int EM_NSFNOTEEXTRACTFILE = 131;
	//	public static final int EM_NSFNOTEATTACHOLE2OBJECT = 132;
	//	public static final int EM_NSFNOTEDELETEOLE2OBJECT = 133;
	//	public static final int EM_NSFNOTEEXTRACTOLE2OBJECT = 134;
	//	public static final int EM_NSGETSERVERLIST = 135;
	//	public static final int EM_NSFDBCOPY = 136;
	//	public static final int EM_NSFDBCREATEANDCOPY = 137;
	//	public static final int EM_NSFDBCOPYACL = 138;
	//	public static final int EM_NSFDBCOPYTEMPLATEACL = 139;
	//	public static final int EM_NSFDBCREATEACLFROMTEMPLATE = 140;
	//	public static final int EM_NSFDBREADACL = 141;
	//	public static final int EM_NSFDBSTOREACL = 142;
	//	public static final int EM_NSFDBFILTER = 143;
	//	public static final int EM_FTDELETEINDEX = 144;
	//	public static final int EM_NSFNOTEGETINFO = 145;
	//	public static final int EM_NSFNOTESETINFO = 146;
	//	public static final int EM_NSFNOTECOMPUTEWITHFORM = 147;
	//	public static final int EM_NIFFINDDESIGNNOTE = 148;
	//	public static final int EM_NIFFINDPRIVATEDESIGNNOTE = 149;
	//	public static final int EM_NIFGETLASTMODIFIEDTIME = 150;
	//	public static final int EM_FTSEARCHEXT = 160;
	//	public static final int EM_NAMELOOKUP = 161;
	//	public static final int EM_NSFNOTEUPDATEMAILBOX = 164;
	//	public static final int EM_NIFFINDDESIGNNOTEEXT = 167;
	//	public static final int EM_AGENTOPEN = 170;
	//	public static final int EM_AGENTRUN = 171;
	//	public static final int EM_AGENTCLOSE = 172;
	//	public static final int EM_AGENTISENABLED = 173;
	//	public static final int EM_AGENTCREATERUNCONTEXT = 175;
	//	public static final int EM_AGENTDESTROYRUNCONTEXT = 176;
	//	public static final int EM_AGENTSETDOCUMENTCONTEXT = 177;
	//	public static final int EM_AGENTSETTIMEEXECUTIONLIMIT = 178;
	//	public static final int EM_AGENTQUERYSTDOUTBUFFER = 179;
	//	public static final int EM_AGENTREDIRECTSTDOUT = 180;
	//	public static final int EM_SECAUTHENTICATION = 184;
	//	public static final int EM_NAMELOOKUP2 = 185;
	//	public static final int EM_NSFDBHASPROFILENOTECHANGED = 198;
	//	public static final int EM_NSFMARKREAD = 208;
	//	public static final int EM_NSFADDTOFOLDER = 209;
	//	public static final int EM_NSFDBSPACEUSAGESCALED = 210; /* V6 */
	//	public static final int EM_NSFDBGETMAJMINVERSION = 222; /* V5.09 */
	//	public static final int EM_ROUTERJOURNALMESSAGE = 223; /* V6 */
	//
	//	/* V6 SMTP hooks */
	//	public static final int EM_SMTPCONNECT = 224;
	//	public static final int EM_SMTPCOMMAND = 225;
	//	public static final int EM_SMTPMESSAGEACCEPT = 226;
	//	public static final int EM_SMTPDISCONNECT = 227;
	//	public static final int EM_NSFARCHIVECOPYNOTES = 228;
	//	public static final int EM_NSFARCHIVEDELETENOTES = 229;
	//	public static final int EM_NSFNOTEEXTRACTWITHCALLBACK = 235;
	//	public static final int EM_NSFDBSTAMPNOTESMULTIITEM = 239;
	//	public static final int EM_MEDIARECOVERY_NOTE = 244;
	//	public static final int EM_NSFGETCHANGEDDBS = 246;
	//	public static final int EM_NSFDBMODIFIEDTIMEBYNAME = 247;
	//	public static final int EM_NSFGETDBCHANGES = 250;
	//	public static final int EM_NSFNOTECIPHERDECRYPT = 251;
	//	public static final int EM_NSFNOTECIPHEREXTRACTFILE = 252;
	//	public static final int EM_NSFNOTECIPHEREXTRACTWITHCALLBACK = 253;
	//	public static final int EM_NSFNOTECIPHEREXTRACTOLE2OBJECT = 254;
	//	public static final int EM_NSFNOTECIPHERDELETEOLE2OBJECT = 255;
	//	public static final int EM_NSFDBNOTELOCK = 256;
	//	public static final int EM_NSFDBNOTEUNLOCK = 257;
	//
	//	// NSFHook Events
	//	public static final int HOOK_EVENT_NOTE_UPDATE = 500;
	//	public static final int HOOK_EVENT_NOTE_OPEN = 501;

	public int getEventId();

	public void setEventValue(EMBridgeEventParams param, Object value);

	public EMBridgeEventParams[] getParams();

}
