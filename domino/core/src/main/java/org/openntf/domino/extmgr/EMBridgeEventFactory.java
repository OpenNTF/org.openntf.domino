/**
 * 
 */
package org.openntf.domino.extmgr;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.openntf.domino.extmgr.events.AdminPProcessRequestEvent;
import org.openntf.domino.extmgr.events.AgentOpenEvent;
import org.openntf.domino.extmgr.events.ClearPasswordEvent;
import org.openntf.domino.extmgr.events.EMEventIds;
import org.openntf.domino.extmgr.events.FTDeleteIndexEvent;
import org.openntf.domino.extmgr.events.FTIndexEvent;
import org.openntf.domino.extmgr.events.FTSearchEvent;
import org.openntf.domino.extmgr.events.IEMBridgeEvent;
import org.openntf.domino.extmgr.events.MailSendNoteEvent;
import org.openntf.domino.extmgr.events.MediaRecoveryEvent;
import org.openntf.domino.extmgr.events.NIFOpenCollectionEvent;
import org.openntf.domino.extmgr.events.NSFAddToFolderEvent;
import org.openntf.domino.extmgr.events.NSFConflictHandlerEvent;
import org.openntf.domino.extmgr.events.NSFDbCloseEvent;
import org.openntf.domino.extmgr.events.NSFDbCompactEvent;
import org.openntf.domino.extmgr.events.NSFDbCopyACLEvent;
import org.openntf.domino.extmgr.events.NSFDbCopyEvent;
import org.openntf.domino.extmgr.events.NSFDbCopyNoteEvent;
import org.openntf.domino.extmgr.events.NSFDbCopyTemplateACLEvent;
import org.openntf.domino.extmgr.events.NSFDbCreateACLFromTemplateEvent;
import org.openntf.domino.extmgr.events.NSFDbCreateAndCopyEvent;
import org.openntf.domino.extmgr.events.NSFDbCreateEvent;
import org.openntf.domino.extmgr.events.NSFDbDeleteEvent;
import org.openntf.domino.extmgr.events.NSFDbDeleteNotesEvent;
import org.openntf.domino.extmgr.events.NSFDbNoteLockEvent;
import org.openntf.domino.extmgr.events.NSFDbNoteUnlockEvent;
import org.openntf.domino.extmgr.events.NSFDbOpenExtendedEvent;
import org.openntf.domino.extmgr.events.NSFDbRenameEvent;
import org.openntf.domino.extmgr.events.NSFDbReopenEvent;
import org.openntf.domino.extmgr.events.NSFNoteAttachFileEvent;
import org.openntf.domino.extmgr.events.NSFNoteCipherDecryptEvent;
import org.openntf.domino.extmgr.events.NSFNoteCipherExtractFileEvent;
import org.openntf.domino.extmgr.events.NSFNoteCloseEvent;
import org.openntf.domino.extmgr.events.NSFNoteCopyEvent;
import org.openntf.domino.extmgr.events.NSFNoteCreateEvent;
import org.openntf.domino.extmgr.events.NSFNoteDecryptEvent;
import org.openntf.domino.extmgr.events.NSFNoteDeleteEvent;
import org.openntf.domino.extmgr.events.NSFNoteDetachFileEvent;
import org.openntf.domino.extmgr.events.NSFNoteExtractFileEvent;
import org.openntf.domino.extmgr.events.NSFNoteOpenByUNIDEvent;
import org.openntf.domino.extmgr.events.NSFNoteOpenEvent;
import org.openntf.domino.extmgr.events.NSFNoteOpenExtendedEvent;
import org.openntf.domino.extmgr.events.NSFNoteUpdateExtendedEvent;
import org.openntf.domino.extmgr.events.NSFNoteUpdateMailBoxEvent;
import org.openntf.domino.extmgr.events.RouterJournalMessageEvent;
import org.openntf.domino.extmgr.events.SECAuthenticationEvent;
import org.openntf.domino.extmgr.events.SMTPCommandEvent;
import org.openntf.domino.extmgr.events.SMTPConnectEvent;
import org.openntf.domino.extmgr.events.SMTPDisconnectEvent;
import org.openntf.domino.extmgr.events.SMTPMessageAcceptEvent;
import org.openntf.domino.extmgr.events.TerminateNSFEvent;

/**
 * @author nfreeman
 * 
 */
public enum EMBridgeEventFactory {
	INSTANCE;

	private static final String EM_EVENT_PREFIX = "[[event:"; // $NON-NLS-1$
	private static final int EM_EVENT_PREFIX_LEN = EM_EVENT_PREFIX.length();
	private static final String EM_NSFHOOKEVENT_PREFIX = "[[dbhookevent:"; // $NON-NLS-1$
	private static final String EM_EVENT_POSTFIX = "]];";

	private static final Map<EMEventIds, IEMBridgeEvent> eventsMap = new EnumMap<EMEventIds, IEMBridgeEvent>(EMEventIds.class);

	public static IEMBridgeEvent parseEventBuffer(final String eventBuffer, final IEMBridgeEvent event) {
		EMBridgeEventParams[] params = event.getParams();

		return event;
	}

	static {
		eventsMap.put(EMEventIds.EM_NSFDBCLOSE, new NSFDbCloseEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTEUPDATE, new NSFNoteUpdateExtendedEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTEUPDATEXTENDED, new NSFNoteUpdateExtendedEvent());
		eventsMap.put(EMEventIds.EM_NSFDBCREATE, new NSFDbCreateEvent());
		eventsMap.put(EMEventIds.EM_NSFDBDELETE, new NSFDbDeleteEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTECREATE, new NSFNoteCreateEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTEDELETE, new NSFNoteDeleteEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTEOPEN, new NSFNoteOpenEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTECLOSE, new NSFNoteCloseEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTEOPENBYUNID, new NSFNoteOpenByUNIDEvent());
		eventsMap.put(EMEventIds.EM_FTINDEX, new FTIndexEvent());
		eventsMap.put(EMEventIds.EM_FTSEARCH, new FTSearchEvent());
		eventsMap.put(EMEventIds.EM_NSFDBCOMPACT, new NSFDbCompactEvent());
		eventsMap.put(EMEventIds.EM_NSFDBDELETENOTES, new NSFDbDeleteNotesEvent());
		eventsMap.put(EMEventIds.EM_NIFOPENCOLLECTION, new NIFOpenCollectionEvent());
		eventsMap.put(EMEventIds.EM_NSFDBCOMPACTEXTENDED, new NSFDbCompactEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTEOPENEXTENDED, new NSFNoteOpenExtendedEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTEDECRYPT, new NSFNoteDecryptEvent());
		eventsMap.put(EMEventIds.EM_NSFDBNOTELOCK, new NSFDbNoteLockEvent());
		eventsMap.put(EMEventIds.EM_NSFDBNOTEUNLOCK, new NSFDbNoteUnlockEvent());
		eventsMap.put(EMEventIds.EM_NSFDBCOPYNOTE, new NSFDbCopyNoteEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTECOPY, new NSFNoteCopyEvent());
		eventsMap.put(EMEventIds.EM_NSFCONFLICTHANDLER, new NSFConflictHandlerEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTEATTACHFILE, new NSFNoteAttachFileEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTEDETACHFILE, new NSFNoteDetachFileEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTEEXTRACTFILE, new NSFNoteExtractFileEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTECIPHERDECRYPT, new NSFNoteCipherDecryptEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTECIPHEREXTRACTFILE, new NSFNoteCipherExtractFileEvent());
		eventsMap.put(EMEventIds.EM_ADMINPPROCESSREQUEST, new AdminPProcessRequestEvent());
		eventsMap.put(EMEventIds.EM_CLEARPASSWORD, new ClearPasswordEvent());
		eventsMap.put(EMEventIds.EM_MEDIARECOVERY_NOTE, new MediaRecoveryEvent());
		eventsMap.put(EMEventIds.EM_FTDELETEINDEX, new FTDeleteIndexEvent());
		eventsMap.put(EMEventIds.EM_AGENTOPEN, new AgentOpenEvent());
		eventsMap.put(EMEventIds.EM_NSFADDTOFOLDER, new NSFAddToFolderEvent());
		eventsMap.put(EMEventIds.EM_MAILSENDNOTE, new MailSendNoteEvent());

		eventsMap.put(EMEventIds.EM_NSFDBRENAME, new NSFDbRenameEvent());
		eventsMap.put(EMEventIds.EM_NSFDBREOPEN, new NSFDbReopenEvent());
		eventsMap.put(EMEventIds.EM_NSFDBOPENEXTENDED, new NSFDbOpenExtendedEvent());
		eventsMap.put(EMEventIds.EM_TERMINATENSF, new TerminateNSFEvent());
		eventsMap.put(EMEventIds.EM_NSFDBCOPY, new NSFDbCopyEvent());
		eventsMap.put(EMEventIds.EM_NSFDBCREATEANDCOPY, new NSFDbCreateAndCopyEvent());
		eventsMap.put(EMEventIds.EM_NSFDBCOPYACL, new NSFDbCopyACLEvent());
		eventsMap.put(EMEventIds.EM_NSFDBCOPYTEMPLATEACL, new NSFDbCopyTemplateACLEvent());
		eventsMap.put(EMEventIds.EM_NSFDBCREATEACLFROMTEMPLATE, new NSFDbCreateACLFromTemplateEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTEUPDATEMAILBOX, new NSFNoteUpdateMailBoxEvent());
		eventsMap.put(EMEventIds.EM_SECAUTHENTICATION, new SECAuthenticationEvent());
		eventsMap.put(EMEventIds.EM_ROUTERJOURNALMESSAGE, new RouterJournalMessageEvent());
		eventsMap.put(EMEventIds.EM_SMTPCONNECT, new SMTPConnectEvent());
		eventsMap.put(EMEventIds.EM_SMTPCOMMAND, new SMTPCommandEvent());
		eventsMap.put(EMEventIds.EM_SMTPMESSAGEACCEPT, new SMTPMessageAcceptEvent());
		eventsMap.put(EMEventIds.EM_SMTPDISCONNECT, new SMTPDisconnectEvent());

		// Not yet implemented...
		// eventsMap.put( IExtensionManagerEvent.EM_NSFDBWRITEOBJECT , NSFDbWriteObjectEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NIFCLOSECOLLECTION , NIFCloseCollectionEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NIFFINDBYKEY , NIFFindByKeyEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NIFFINDBYNAME , NIFFindByNameEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NIFOPENNOTE , NIFOpenNoteEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NIFREADENTRIES , NIFReadEntriesEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NIFUPDATECOLLECTION , NIFUpdateCollectionEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_SCHFREETIMESEARCH, SCHFreeTimeSearchEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_SCHRETRIEVE, SCHRetrieveEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_SCHSRVRETRIEVE, SCHSrvRetrieveEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEATTACHOLE2OBJECT, NSFNoteAttachOLE2ObjectEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEDELETEOLE2OBJECT, NSFNoteDeleteOLE2ObjectEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEEXTRACTOLE2OBJECT, NSFNoteExtractOLE2ObjectEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_FTSEARCHEXT, FTSearchExtEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NAMELOOKUP, NameLookupEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_AGENTRUN, AgentRunEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_AGENTCLOSE, AgentCloseEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NAMELOOKUP2, NameLookup2Event());
		// eventsMap.put( IExtensionManagerEvent.EM_NSFARCHIVECOPYNOTES, NSFArchiveCopyNotesEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NSFARCHIVEDELETENOTES, NSFArchiveDeleteNotesEvent());

	}

	private EMBridgeEventFactory() {

	}

	private ConcurrentLinkedQueue<EMBridgeEvent> recycleBin_ = new ConcurrentLinkedQueue<EMBridgeEvent>();

	private void _recycleEvent(final EMBridgeEvent event) {
		event.recycle();
		synchronized (recycleBin_) {
			recycleBin_.add(event);
		}
	}

	public static void recycleEvent(final EMBridgeEvent event) {
		INSTANCE._recycleEvent(event);
	}

	private EMBridgeEvent _getEvent() {
		EMBridgeEvent result = null;
		synchronized (recycleBin_) {
			if (recycleBin_.isEmpty()) {
				recycleBin_.add(new EMBridgeEvent(EMBridgeEvent.TYPE.TRIGGERED));
			}
			result = recycleBin_.poll();
		}

		return result;
	}

	public static int getEventId(final String commandBuffer) {
		int iIndex = commandBuffer.indexOf(EM_EVENT_POSTFIX);
		if (iIndex < 0)
			return -1;
		int eventId = Integer.parseInt(commandBuffer.substring(EM_EVENT_PREFIX_LEN, iIndex).trim());
		return eventId;
	}

	public static String getEventParams(final String commandBuffer) {
		int iIndex = commandBuffer.indexOf(EM_EVENT_POSTFIX);
		String params = commandBuffer.substring(iIndex + EM_EVENT_POSTFIX.length());
		return params;
	}

}
