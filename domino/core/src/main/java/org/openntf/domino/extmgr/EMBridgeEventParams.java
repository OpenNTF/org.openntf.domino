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
/**
 * 
 */
package org.openntf.domino.extmgr;

import java.util.EnumMap;

/**
 * @author nfreeman
 * 
 */
public enum EMBridgeEventParams implements IEMBridgeEventParams {
	SourceDbpath, DestDbpath, DataDbpath, Noteid, Flag(Long.class), Username, Dbclass(Integer.class), Forcecreate(Boolean.class), RequestNoteid, ResponseNoteid, Unid, Stopfile, DocsAdded(
			Long.class), DocsUpdated(Long.class), DocsDelete(Long.class), BytesIndexed(Long.class), Query, NoteArray(String.class), Options(
			Long.class), Limit(Long.class), DocsReturned(Long.class), SizeBefore(Long.class), SizeAfter(Long.class), DocsCount(
			Integer.class), FromName, ToName, SinceSeqNum(Long.class), Filename, OriginalFilename, EncodingType(Integer.class), Itemname, From, Insert(
			Boolean.class), Update(Boolean.class), Delete(Boolean.class), FolderNoteid, AddOperation(Boolean.class), SinceTimeDate, NoteClass(
			Integer.class), Manager, DefaultAccess(Integer.class), RemoteName, NetProtocol(Integer.class), NetAddress, WEvent(Integer.class), SessionId(
			Long.class), Command, MaxCommandLen(Long.class), SMTPReply, SMTPReplyLength(Long.class), RemoteIP, RemoteHost, PossibileRelay(
			Boolean.class), SMTPGreeting, SMTPMaxGreetingLen(Long.class);

	final Class<?> type_;

	private EMBridgeEventParams() {
		type_ = String.class;
	}

	private EMBridgeEventParams(Class<?> c) {
		type_ = c;
	}

	@Override
	public Class<?> getType() {
		return type_;
	}

	public static void populateParamMap(EnumMap<EMBridgeEventParams, Object> map, EMBridgeEventParams[] params, String buffer) {
		String[] values = null;
		if (buffer == null || buffer.length() == 0) {
			values = new String[0];
		} else {
			if (buffer.endsWith(",")) {
				buffer += " ";
			}
			values = buffer.split(",");
		}
		for (int i = 0; i < values.length; i++) {
			if (params.length > i) {
				EMBridgeEventParams param = params[i];
				if (param.equals(EMBridgeEventParams.Noteid)) {
					int nid = Integer.parseInt(values[i], 10);
					map.put(param, Integer.toHexString(nid));
				} else if (String.class.equals(param.getType())) {
					map.put(param, values[i]);
				} else if (Integer.class.equals(param.getType())) {
					map.put(param, Integer.parseInt(values[i], 16));
				} else if (Boolean.class.equals(param.getType())) {
					map.put(param, ("0".equals(values[i]) || "false".equals(values[i])));
				} else if (Long.class.equals(param.getType())) {
					map.put(param, Long.parseLong(values[i], 16));
				}
			} else {
				if (!map.containsKey(EMBridgeEventParams.Username)) {
					map.put(EMBridgeEventParams.Username, values[i]);
				}
			}
		}

	}

}
