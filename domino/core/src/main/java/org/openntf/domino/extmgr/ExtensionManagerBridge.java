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
package org.openntf.domino.extmgr;

import java.lang.reflect.Constructor;
import java.util.EnumMap;
import java.util.Map;

import org.openntf.domino.extmgr.events.AbstractEMBridgeEvent;
import org.openntf.domino.extmgr.events.AdminPProcessRequestEvent;
import org.openntf.domino.extmgr.events.AgentOpenEvent;
import org.openntf.domino.extmgr.events.ClearPasswordEvent;
import org.openntf.domino.extmgr.events.EMEventIds;
import org.openntf.domino.extmgr.events.IEMBridgeEvent;
import org.openntf.domino.extmgr.events.MailSendNoteEvent;
import org.openntf.domino.extmgr.events.MediaRecoveryEvent;
import org.openntf.domino.extmgr.events.OpenViewEvent;
import org.openntf.domino.extmgr.events.RouterJournalMessageEvent;
import org.openntf.domino.extmgr.events.SECAuthenticationEvent;
import org.openntf.domino.extmgr.events.SMTPCommandEvent;
import org.openntf.domino.extmgr.events.SMTPConnectEvent;
import org.openntf.domino.extmgr.events.SMTPDisconnectEvent;
import org.openntf.domino.extmgr.events.SMTPMessageAcceptEvent;
import org.openntf.domino.extmgr.events.TerminateNSFEvent;
import org.openntf.domino.extmgr.events.UnknownEMEvent;

public class ExtensionManagerBridge {

	private static final String EM_EVENT_PREFIX = "[[event:"; //$NON-NLS-1$
	private static final String EM_NSFHOOKEVENT_PREFIX = "[[dbhookevent:"; //$NON-NLS-1$
	private static final String EM_EVENT_POSTFIX = "]];"; //$NON-NLS-1$

	private static final Map<EMEventIds, Class<?>> eventsMap = new EnumMap<EMEventIds, Class<?>>(EMEventIds.class);

	static {
		eventsMap.put(EMEventIds.EM_NSFDBCLOSE, org.openntf.domino.extmgr.events.database.CloseEvent.class);
		eventsMap.put(EMEventIds.EM_NSFNOTEUPDATE, org.openntf.domino.extmgr.events.document.UpdateExtendedEvent.class);
		eventsMap.put(EMEventIds.EM_NSFNOTEUPDATEXTENDED, org.openntf.domino.extmgr.events.document.UpdateExtendedEvent.class);
		eventsMap.put(EMEventIds.EM_NSFDBCREATE, org.openntf.domino.extmgr.events.database.CreateEvent.class);
		eventsMap.put(EMEventIds.EM_NSFDBDELETE, org.openntf.domino.extmgr.events.database.DeleteEvent.class);
		eventsMap.put(EMEventIds.EM_NSFNOTECREATE, org.openntf.domino.extmgr.events.document.CreateEvent.class);
		eventsMap.put(EMEventIds.EM_NSFNOTEDELETE, org.openntf.domino.extmgr.events.document.DeleteEvent.class);
		eventsMap.put(EMEventIds.EM_ADMINPPROCESSREQUEST, AdminPProcessRequestEvent.class);
		eventsMap.put(EMEventIds.EM_NSFNOTEOPEN, org.openntf.domino.extmgr.events.document.OpenEvent.class);
		eventsMap.put(EMEventIds.EM_NSFNOTECLOSE, org.openntf.domino.extmgr.events.document.CloseEvent.class);
		eventsMap.put(EMEventIds.EM_NSFNOTEOPENBYUNID, org.openntf.domino.extmgr.events.document.OpenByUNIDEvent.class);
		eventsMap.put(EMEventIds.EM_FTINDEX, org.openntf.domino.extmgr.events.database.FTIndexEvent.class);
		eventsMap.put(EMEventIds.EM_FTSEARCH, org.openntf.domino.extmgr.events.database.FTSearchEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NIFFINDBYKEY , NIFFindByKeyEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NIFFINDBYNAME , NIFFindByNameEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NIFOPENNOTE , NIFOpenNoteEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NIFREADENTRIES , NIFReadEntriesEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NIFUPDATECOLLECTION , NIFUpdateCollectionEvent.class);
		eventsMap.put(EMEventIds.EM_NSFDBCOMPACT, org.openntf.domino.extmgr.events.database.CompactEvent.class);
		eventsMap.put(EMEventIds.EM_NSFDBDELETENOTES, org.openntf.domino.extmgr.events.database.DeleteNotesEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NSFDBWRITEOBJECT , NSFDbWriteObjectEvent.class);
		eventsMap.put(EMEventIds.EM_NIFOPENCOLLECTION, OpenViewEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NIFCLOSECOLLECTION , NIFCloseCollectionEvent.class);
		eventsMap.put(EMEventIds.EM_NSFDBRENAME, org.openntf.domino.extmgr.events.database.RenameEvent.class);
		eventsMap.put(EMEventIds.EM_NSFDBREOPEN, org.openntf.domino.extmgr.events.database.ReopenEvent.class);
		eventsMap.put(EMEventIds.EM_NSFDBOPENEXTENDED, org.openntf.domino.extmgr.events.database.OpenExtendedEvent.class);
		eventsMap.put(EMEventIds.EM_NSFNOTEOPENEXTENDED, org.openntf.domino.extmgr.events.document.OpenExtendedEvent.class);
		eventsMap.put(EMEventIds.EM_TERMINATENSF, TerminateNSFEvent.class);
		eventsMap.put(EMEventIds.EM_NSFNOTEDECRYPT, org.openntf.domino.extmgr.events.document.DecryptEvent.class);
		eventsMap.put(EMEventIds.EM_NSFCONFLICTHANDLER, org.openntf.domino.extmgr.events.database.ConflictHandlerEvent.class);
		eventsMap.put(EMEventIds.EM_MAILSENDNOTE, MailSendNoteEvent.class);
		eventsMap.put(EMEventIds.EM_CLEARPASSWORD, ClearPasswordEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_SCHFREETIMESEARCH, SCHFreeTimeSearchEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_SCHRETRIEVE, SCHRetrieveEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_SCHSRVRETRIEVE, SCHSrvRetrieveEvent.class);
		eventsMap.put(EMEventIds.EM_NSFDBCOMPACTEXTENDED, org.openntf.domino.extmgr.events.database.CompactEvent.class);
		eventsMap.put(EMEventIds.EM_NSFDBCOPYNOTE, org.openntf.domino.extmgr.events.document.CopyToDatabaseEvent.class);
		eventsMap.put(EMEventIds.EM_NSFNOTECOPY, org.openntf.domino.extmgr.events.document.CopyEvent.class);
		eventsMap.put(EMEventIds.EM_NSFNOTEATTACHFILE, org.openntf.domino.extmgr.events.document.AttachFileEvent.class);
		eventsMap.put(EMEventIds.EM_NSFNOTEDETACHFILE, org.openntf.domino.extmgr.events.document.DetachFileEvent.class);
		eventsMap.put(EMEventIds.EM_NSFNOTEEXTRACTFILE, org.openntf.domino.extmgr.events.document.ExtractFileEvent.class);
		//      eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEATTACHOLE2OBJECT, NSFNoteAttachOLE2ObjectEvent.class);
		//      eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEDELETEOLE2OBJECT, NSFNoteDeleteOLE2ObjectEvent.class);
		//      eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEEXTRACTOLE2OBJECT, NSFNoteExtractOLE2ObjectEvent.class);
		eventsMap.put(EMEventIds.EM_NSFDBCOPY, org.openntf.domino.extmgr.events.database.CopyEvent.class);
		eventsMap.put(EMEventIds.EM_NSFDBCREATEANDCOPY, org.openntf.domino.extmgr.events.database.CreateAndCopyEvent.class);
		eventsMap.put(EMEventIds.EM_NSFDBCOPYACL, org.openntf.domino.extmgr.events.database.CopyACLEvent.class);
		eventsMap.put(EMEventIds.EM_NSFDBCOPYTEMPLATEACL, org.openntf.domino.extmgr.events.database.CopyTemplateACLEvent.class);
		eventsMap.put(EMEventIds.EM_NSFDBCREATEACLFROMTEMPLATE, org.openntf.domino.extmgr.events.database.CreateACLFromTemplateEvent.class);
		eventsMap.put(EMEventIds.EM_FTDELETEINDEX, org.openntf.domino.extmgr.events.database.FTDeleteIndexEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_FTSEARCHEXT, FTSearchExtEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NAMELOOKUP, NameLookupEvent.class);
		eventsMap.put(EMEventIds.EM_NSFNOTEUPDATEMAILBOX, org.openntf.domino.extmgr.events.document.UpdateMailBoxEvent.class);
		eventsMap.put(EMEventIds.EM_AGENTOPEN, AgentOpenEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_AGENTRUN, AgentRunEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_AGENTCLOSE, AgentCloseEvent.class);
		eventsMap.put(EMEventIds.EM_SECAUTHENTICATION, SECAuthenticationEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NAMELOOKUP2, NameLookup2Event.class);
		eventsMap.put(EMEventIds.EM_NSFADDTOFOLDER, org.openntf.domino.extmgr.events.document.AddToFolderEvent.class);
		eventsMap.put(EMEventIds.EM_ROUTERJOURNALMESSAGE, RouterJournalMessageEvent.class);
		eventsMap.put(EMEventIds.EM_SMTPCONNECT, SMTPConnectEvent.class);
		eventsMap.put(EMEventIds.EM_SMTPCOMMAND, SMTPCommandEvent.class);
		eventsMap.put(EMEventIds.EM_SMTPMESSAGEACCEPT, SMTPMessageAcceptEvent.class);
		eventsMap.put(EMEventIds.EM_SMTPDISCONNECT, SMTPDisconnectEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NSFARCHIVECOPYNOTES, NSFArchiveCopyNotesEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NSFARCHIVEDELETENOTES, NSFArchiveDeleteNotesEvent.class);
		eventsMap.put(EMEventIds.EM_MEDIARECOVERY_NOTE, MediaRecoveryEvent.class);
		eventsMap.put(EMEventIds.EM_NSFNOTECIPHERDECRYPT, org.openntf.domino.extmgr.events.document.CipherDecryptEvent.class);
		eventsMap.put(EMEventIds.EM_NSFNOTECIPHEREXTRACTFILE, org.openntf.domino.extmgr.events.document.CipherExtractFileEvent.class);
		eventsMap.put(EMEventIds.EM_NSFDBNOTELOCK, org.openntf.domino.extmgr.events.document.LockEvent.class);
		eventsMap.put(EMEventIds.EM_NSFDBNOTEUNLOCK, org.openntf.domino.extmgr.events.document.UnlockEvent.class);
	}

	/**
	 *
	 */
	private ExtensionManagerBridge() {
	}

	public static IEMBridgeEvent getEventFromCommand(String commandBuffer) {
		boolean bIsEvent = commandBuffer.startsWith(EM_EVENT_PREFIX);
		boolean bIsNSFHookEvent = commandBuffer.startsWith(EM_NSFHOOKEVENT_PREFIX);
		if (!bIsEvent && !bIsNSFHookEvent) {
			return null;
		}

		String prefix = bIsEvent ? EM_EVENT_PREFIX : EM_NSFHOOKEVENT_PREFIX;
		commandBuffer = commandBuffer.substring(prefix.length());
		int iIndex = commandBuffer.indexOf(EM_EVENT_POSTFIX);
		if (iIndex < 0) {
			return null;
		}

		try {
			int eventId = Integer.parseInt(commandBuffer.substring(0, iIndex).trim());
			return getEvent(eventId, commandBuffer.substring(iIndex + EM_EVENT_POSTFIX.length()));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private static IEMBridgeEvent getEvent(final int eventId, final String eventBuffer) {
		AbstractEMBridgeEvent retEvent = null;
		Class<?> eventClass = eventsMap.get(EMEventIds.getEMEventFromId(eventId));
		if (eventClass != null) {
			try {
				Constructor<?> constructor = null;
				try {
					constructor = eventClass.getConstructor();
					retEvent = (AbstractEMBridgeEvent) constructor.newInstance();
				} catch (Throwable t) {
					//Try with eventId
					constructor = eventClass.getConstructor(Integer.TYPE);
					retEvent = (AbstractEMBridgeEvent) constructor.newInstance(eventId);
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}

		if (retEvent != null && retEvent.parseEventBuffer(eventBuffer)) {
			return retEvent;
		}
		return new UnknownEMEvent();
	}

	public static Class<?> getEMEventClass(final IEMBridgeEvent emEvent) {
		return getEMEventClass(emEvent.getEventId());
	}

	public static Class<?> getEMEventClass(final int eventId) {
		return eventsMap.get(EMEventIds.getEMEventFromId(eventId));
	}

}
