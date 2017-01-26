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
		String tmpBuffer = eventBuffer.substring(eventBuffer.indexOf(";") + 1);
		String[] vals = tmpBuffer.split(",");
		if (vals.length == params.length) {
			for (int i = 0; i < params.length; i++) {
				event.setEventValue(params[i], vals[i]);
			}
		} else {
			System.out.println("Incorrect elements in eventBuffer for " + event.getClass().getName() + ", expected " + params.length
					+ ", found " + vals.length);
		}
		return event;
	}

	static {
		eventsMap.put(EMEventIds.EM_NSFDBCLOSE, new org.openntf.domino.extmgr.events.database.CloseEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTEUPDATE, new org.openntf.domino.extmgr.events.document.UpdateExtendedEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTEUPDATEXTENDED, new org.openntf.domino.extmgr.events.document.UpdateExtendedEvent());
		eventsMap.put(EMEventIds.EM_NSFDBCREATE, new org.openntf.domino.extmgr.events.database.CreateEvent());
		eventsMap.put(EMEventIds.EM_NSFDBDELETE, new org.openntf.domino.extmgr.events.database.DeleteEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTECREATE, new org.openntf.domino.extmgr.events.document.CreateEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTEDELETE, new org.openntf.domino.extmgr.events.document.DeleteEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTEOPEN, new org.openntf.domino.extmgr.events.document.OpenEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTECLOSE, new org.openntf.domino.extmgr.events.document.CloseEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTEOPENBYUNID, new org.openntf.domino.extmgr.events.document.OpenByUNIDEvent());
		eventsMap.put(EMEventIds.EM_FTINDEX, new org.openntf.domino.extmgr.events.database.FTIndexEvent());
		eventsMap.put(EMEventIds.EM_FTSEARCH, new org.openntf.domino.extmgr.events.database.FTSearchEvent());
		eventsMap.put(EMEventIds.EM_NSFDBCOMPACT, new org.openntf.domino.extmgr.events.database.CompactEvent());
		eventsMap.put(EMEventIds.EM_NSFDBDELETENOTES, new org.openntf.domino.extmgr.events.database.DeleteNotesEvent());
		eventsMap.put(EMEventIds.EM_NIFOPENCOLLECTION, new OpenViewEvent());
		eventsMap.put(EMEventIds.EM_NSFDBCOMPACTEXTENDED, new org.openntf.domino.extmgr.events.database.CompactEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTEOPENEXTENDED, new org.openntf.domino.extmgr.events.document.OpenExtendedEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTEDECRYPT, new org.openntf.domino.extmgr.events.document.DecryptEvent());
		eventsMap.put(EMEventIds.EM_NSFDBNOTELOCK, new org.openntf.domino.extmgr.events.document.LockEvent());
		eventsMap.put(EMEventIds.EM_NSFDBNOTEUNLOCK, new org.openntf.domino.extmgr.events.document.UnlockEvent());
		eventsMap.put(EMEventIds.EM_NSFDBCOPYNOTE, new org.openntf.domino.extmgr.events.document.CopyToDatabaseEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTECOPY, new org.openntf.domino.extmgr.events.document.CopyEvent());
		eventsMap.put(EMEventIds.EM_NSFCONFLICTHANDLER, new org.openntf.domino.extmgr.events.database.ConflictHandlerEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTEATTACHFILE, new org.openntf.domino.extmgr.events.document.AttachFileEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTEDETACHFILE, new org.openntf.domino.extmgr.events.document.DetachFileEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTEEXTRACTFILE, new org.openntf.domino.extmgr.events.document.ExtractFileEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTECIPHERDECRYPT, new org.openntf.domino.extmgr.events.document.CipherDecryptEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTECIPHEREXTRACTFILE, new org.openntf.domino.extmgr.events.document.CipherExtractFileEvent());
		eventsMap.put(EMEventIds.EM_ADMINPPROCESSREQUEST, new AdminPProcessRequestEvent());
		eventsMap.put(EMEventIds.EM_CLEARPASSWORD, new ClearPasswordEvent());
		eventsMap.put(EMEventIds.EM_MEDIARECOVERY_NOTE, new MediaRecoveryEvent());
		eventsMap.put(EMEventIds.EM_FTDELETEINDEX, new org.openntf.domino.extmgr.events.database.FTDeleteIndexEvent());
		eventsMap.put(EMEventIds.EM_AGENTOPEN, new AgentOpenEvent());
		eventsMap.put(EMEventIds.EM_NSFADDTOFOLDER, new org.openntf.domino.extmgr.events.document.AddToFolderEvent());
		eventsMap.put(EMEventIds.EM_MAILSENDNOTE, new MailSendNoteEvent());

		eventsMap.put(EMEventIds.EM_NSFDBRENAME, new org.openntf.domino.extmgr.events.database.RenameEvent());
		eventsMap.put(EMEventIds.EM_NSFDBREOPEN, new org.openntf.domino.extmgr.events.database.ReopenEvent());
		eventsMap.put(EMEventIds.EM_NSFDBOPENEXTENDED, new org.openntf.domino.extmgr.events.database.OpenExtendedEvent());
		eventsMap.put(EMEventIds.EM_TERMINATENSF, new TerminateNSFEvent());
		eventsMap.put(EMEventIds.EM_NSFDBCOPY, new org.openntf.domino.extmgr.events.database.CopyEvent());
		eventsMap.put(EMEventIds.EM_NSFDBCREATEANDCOPY, new org.openntf.domino.extmgr.events.database.CreateAndCopyEvent());
		eventsMap.put(EMEventIds.EM_NSFDBCOPYACL, new org.openntf.domino.extmgr.events.database.CopyACLEvent());
		eventsMap.put(EMEventIds.EM_NSFDBCOPYTEMPLATEACL, new org.openntf.domino.extmgr.events.database.CopyTemplateACLEvent());
		eventsMap.put(EMEventIds.EM_NSFDBCREATEACLFROMTEMPLATE, new org.openntf.domino.extmgr.events.database.CreateACLFromTemplateEvent());
		eventsMap.put(EMEventIds.EM_NSFNOTEUPDATEMAILBOX, new org.openntf.domino.extmgr.events.document.UpdateMailBoxEvent());
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

	public static int toInt(final char c) {
		return Character.digit(c, 10);
	}

	public static int getEventId(final String commandBuffer) {
		char[] charBuffer = commandBuffer.toCharArray();
		char d1 = charBuffer[EM_EVENT_PREFIX_LEN + 0];
		char d2 = charBuffer[EM_EVENT_PREFIX_LEN + 1];
		char d3 = charBuffer[EM_EVENT_PREFIX_LEN + 2];
		if (Character.isDigit(d3)) {
			return (100 * toInt(d1)) + (10 * toInt(d2)) + toInt(d3);
		} else if (Character.isDigit(d2)) {
			return (10 * toInt(d1)) + toInt(d2);
		} else {
			return toInt(d1);
		}
		//			int iIndex = commandBuffer.indexOf(EM_EVENT_POSTFIX);
		//		if (iIndex < 0)
		//			return -1;
		//		int eventId = Integer.parseInt(commandBuffer.substring(EM_EVENT_PREFIX_LEN, iIndex).trim());
		//		return eventId;
	}

	public static String getEventParams(final String commandBuffer) {
		int iIndex = commandBuffer.indexOf(EM_EVENT_POSTFIX);
		String params = commandBuffer.substring(iIndex + EM_EVENT_POSTFIX.length());
		return params;
	}

}
