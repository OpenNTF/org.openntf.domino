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
package org.openntf.domino.ext;

public enum NoteClass {
	UNKNOWN,  			//	Hex value (from nsfnote.h) 
	DOCUMENT, 			//	0x0001 - ocument note
	HELPABOUTDOCUMENT, 	//	0x0002 - notefile info (help-about) note 
	FORM, 				//	0x0004 - form note
	VIEW, 				//	0x0008 - view note
	ICON, 				//	0x0010 - icon note
	DESIGN_COLLECTION,	//  0x0020 - design note collection - not exportable!
	ACL,				//  0x0040 - acl note - not exportable - but fixed by ODA
	HELPINDEX, 			//	0x0080 - Notes product help index note
	HELPUSINGDOCUMENT, 	//	0x0100 - designer's help note
	FILTER, 			//  0x0200 - filter note
	SHAREDFIELD, 		//  0x0400 - field note
	REPLICATIONFORMULA  //  0x0800 - replication formula
}
