/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
package org.openntf.domino.helpers;

import java.util.List;

/*
 * @author Nathan T. Freeman
 * Parser for console.log contents that trace NRPC API calls in Notes client
 * 
 */
public class ClientClockParser {
	/*
	 * Example console.log
	 * 
	[1C2C:0002-1084] (264-5929 [264]) DB_MODIFIED_TIME(REP80257839:005DE04A): 94 ms. [18+82=100]
	[1C2C:0002-1084] (265-5929 [265]) OPEN_COLLECTION(REP80257839:005DE04A-NT0006C376,0000,0000): 105 ms. [46+38=84]
	[1C2C:0002-1084] (266-5929 [266]) FIND_BY_KEY_EXTENDED2(REP80257839:005DE04A): 123 ms. [418+34=452] (Unsupported return flag(s))
	[1C2C:0002-1084] (267-5929 [267]) FIND_BY_KEY(REP80257839:005DE04A): 168 ms. [416+42=458]
	[1C2C:0002-1084] (268-5929 [268]) READ_ENTRIES(REP80257839:005DE04A-NT0006C376): 125 ms. [72+142=214]
	[1C2C:0002-1084] (269-5929 [269]) OPEN_DB(CN=COMMONNAME/OU=OrgUnit/O=ACME CORP!!helpdesk\Knowledge Base.nsf): (Opened: REP80257839:005E0A6F) 138 ms. [126+182=308]
	[1C2C:0002-1084] (270-5929 [270]) DB_MODIFIED_TIME(REP80257839:005E0A6F): 147 ms. [18+80=98]
	[1C2C:0002-1084] (271-5930 [271]) ISDB2_RQST(REP80257839:005E0A6F): 176 ms. [18+20=38]
	[1C2C:0002-1084] (272-5930 [272]) OPEN_COLLECTION(REP80257839:005E0A6F-NTFFFF0020,0000,1000): 132 ms. [50+606=656]
	[1C2C:0002-1084] (273-5930 [273]) READ_ENTRIES(REP80257839:005E0A6F-NTFFFF0020,Since:06/25/2014 04:10:46 PM): 218 ms. [142+1418=1560]
	[1C2C:0002-1084] (274-5930 [274]) CLOSE_COLLECTION(REP80257839:005E0A6F-NTFFFF0020): 0 ms. [16+0=16]
	[1C2C:0002-1084] (275-5930 [275]) DB_MODIFIED_TIME(REP80257839:005E0A6F): 163 ms. [18+80=98]
	[1C2C:0002-1084] (276-5930 [276]) GET_MULT_NOTE_INFO_BY_UNID(REP80257839:005E0A6F): 155 ms. [192+348=540]
	[1C2C:0002-1084] (277-5930 [277]) DB_MODIFIED_TIME(REP80257839:005E0A6F): 80 ms. [18+80=98]
	[1C2C:0002-1084] (278-5930 [278]) GET_UNREAD_NOTE_TABLE(REP80257839:005E0A6F): 79 ms. [78+122=200]
	[1C2C:0002-1084] (279-5931 [279]) OPEN_NOTE(REP80257839:005E0A6F-NTFFFF0010,03000400): 74 ms. [52+2888=2940]
	 */

	public static enum Function {
		DB_MODIFIED_TIME, OPEN_COLLECTION, FIND_BY_KEY_EXTENDED2, FIND_BY_KEY, READ_ENTRIES, OPEN_DB, ISDB2_RQST, CLOSE_COLLECTION,
		GET_MULT_NOTE_INFO_BY_UNID, GET_UNREAD_NOTE_TABLE, OPEN_NOTE, GET_LAST_INDEX_TIME, GET_SPECIAL_NOTE_ID, GET_NAMED_OBJECT_ID,
		DB_REPLINFO_GET, GET_NOTE_INFO, SERVER_AVAILABLE_LITE, GET_MODIFIED_NOTES, READ_OBJECT, GETOBJECT_RQST, CLOSE_DB, OPEN_SESSION,
		POLL_DEL_SEQNUM
	}

	@SuppressWarnings("unused")
	public static class APIOp {
		private Function function_;
		private String replicaid_;
		private String noteid_;
		private String threadId_;
		private int txnId_;
		private int clockcount_;
		private int time_;
		private int bytesSent_;
		private int bytesReceived_;
		private int bytesTotal_;
		private String[] args_;
		private String message_;

		// [1C2C:0002-1084]  [1C2C:0002-1084] (273-5930 [273]) READ_ENTRIES(REP80257839:005E0A6F-NTFFFF0020,Since:06/25/2014 04:10:46 PM): 218 ms. [142+1418=1560]

		public APIOp(final String log) {

		}

	}

	@SuppressWarnings("unused")
	public static class APIOpGroup {
		private int startClock_;
		private int endClock_;
		private List<APIOp> operations_;
	}

	public ClientClockParser() {

	}

}
