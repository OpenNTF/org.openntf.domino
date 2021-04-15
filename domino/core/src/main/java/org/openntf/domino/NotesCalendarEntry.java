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
package org.openntf.domino;

import java.util.Vector;

import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * Provides access to one entry of the calendar and scheduling services in a Domino mail application in standard iCalendar format.
 * <p>
 * See Internet Calendaring and Scheduling Core Object Specification (iCalendar) at
 * <a href="http://tools.ietf.org/html/rfc5545">http://tools.ietf.org/html/rfc5545</a> for the format. {@link NotesCalendar} provides
 * methods for getting and creating calendar entries. Entries include meetings, appointments, reminders, and other events that the owner
 * places on the calendar, and notices from other users after they are processed. Unprocessed notices are handled by
 * {@link NotesCalendarNotice}.
 * </p>
 */
public interface NotesCalendarEntry extends Base<lotus.domino.NotesCalendarEntry>, lotus.domino.NotesCalendarEntry,
		org.openntf.domino.ext.NotesCalendarEntry, SessionDescendant {

	public static class Schema extends FactorySchema<NotesCalendarEntry, lotus.domino.NotesCalendarEntry, NotesCalendar> {
		@Override
		public Class<NotesCalendarEntry> typeClass() {
			return NotesCalendarEntry.class;
		}

		@Override
		public Class<lotus.domino.NotesCalendarEntry> delegateClass() {
			return lotus.domino.NotesCalendarEntry.class;
		}

		@Override
		public Class<NotesCalendar> parentClass() {
			return NotesCalendar.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Accepts a meeting entry or entries.
	 * <p>
	 * This method deals with meeting entries, not notices.
	 * </p>
	 *
	 * @param comments
	 *            Comments regarding a meeting change.
	 */
	@Override
	public void accept(final String comments);

	/**
	 * Accepts a meeting entry or entries.
	 * <p>
	 * This method deals with meeting entries, not notices.
	 * </p>
	 *
	 * @param comments
	 *            Comments regarding a meeting change.
	 * @param scope
	 *            The scope of a recurring operation:
	 *            <ul>
	 *            <li>CS_RANGE_REPEAT_ALL (1)</li>
	 *            <li>CS_RANGE_REPEAT_CURRENT (0)</li>
	 *            <li>CS_RANGE_REPEAT_FUTURE (3), inclusive</li>
	 *            <li>CS_RANGE_REPEAT_PREV (2), inclusive</li>
	 * @param recurrenceId
	 *            The recurrence identifier (RECURRENCE-ID item) for a recurring calendar event. The format of a recurrence identifier is a
	 *            time in UTC format, for example, 20120913T160000Z.
	 * @throws NotesError.NOTES_ERR_INVALIDID
	 *             when the identifier for the NotesCalendarEntry object is not valid.
	 * @throws NotesError.NOTES_ERR_RECURID_NOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object is not valid.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object does not identify an entry in the calendar, or the scope
	 *             and recurid are missing for a recurring entry.
	 */
	@Override
	public void accept(final String comments, final int scope, final String recurrenceId);

	/**
	 * Cancels a meeting entry or entries.
	 * <p>
	 * This method deals with meeting entries, not notices.
	 * </p>
	 *
	 * @param comments
	 *            Comments regarding a meeting change.
	 *
	 */
	@Override
	public void cancel(final String comments);

	/**
	 * Cancels a meeting entry or entries.
	 * <p>
	 * This method deals with meeting entries, not notices.
	 * </p>
	 *
	 * @param comments
	 *            Comments regarding a meeting change.
	 * @param scope
	 *            The scope of a recurring operation:
	 *            <ul>
	 *            <li>CS_RANGE_REPEAT_ALL (1)</li>
	 *            <li>CS_RANGE_REPEAT_CURRENT (0)</li>
	 *            <li>CS_RANGE_REPEAT_FUTURE (3), inclusive</li>
	 *            <li>CS_RANGE_REPEAT_PREV (2), inclusive</li>
	 * @param recurrenceId
	 *            The recurrence identifier (RECURRENCE-ID item) for a recurring calendar event. The format of a recurrence identifier is a
	 *            time in UTC format, for example, 20120913T160000Z.
	 * @throws NotesError.NOTES_ERR_INVALIDID
	 *             when the identifier for the NotesCalendarEntry object is not valid.
	 * @throws NotesError.NOTES_ERR_RECURID_NOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object is not valid.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object does not identify an entry in the calendar, or the scope
	 *             and recurid are missing for a recurring entry.
	 */
	@Override
	public void cancel(final String comments, final int scope, final String recurrenceId);

	/**
	 * Counters a meeting entry.
	 * <p>
	 * This method deals with meeting entries, not notices.
	 * </p>
	 *
	 * @param comments
	 *            Comments regarding a meeting change.
	 * @param start
	 *            The start time of the counter proposal.
	 * @param end
	 *            The end time of the counter proposal. An exception occurs if the end time is not greater than the start time.
	 */
	@Override
	public void counter(final String comments, final lotus.domino.DateTime start, final lotus.domino.DateTime end);

	/**
	 * Counters a meeting entry.
	 * <p>
	 * This method deals with meeting entries, not notices.
	 * </p>
	 *
	 * @param comments
	 *            Comments regarding a meeting change.
	 * @param start
	 *            The start time of the counter proposal.
	 * @param end
	 *            The end time of the counter proposal. An exception occurs if the end time is not greater than the start time.
	 * @param keepPlaceholder
	 *            Keeps a placeholder for the meeting.
	 */
	@Override
	public void counter(final String comments, final lotus.domino.DateTime start, final lotus.domino.DateTime end,
			final boolean keepPlaceholder);

	/**
	 * Counters a meeting entry or entries.
	 * <p>
	 * This method deals with meeting entries, not notices.
	 * </p>
	 *
	 * @param comments
	 *            Comments regarding a meeting change.
	 * @param start
	 *            The start time of the counter proposal.
	 * @param end
	 *            The end time of the counter proposal. An exception occurs if the end time is not greater than the start time.
	 * @param keepPlaceholder
	 *            Keeps a placeholder for the meeting.
	 * @param scope
	 *            The scope of a recurring operation:
	 *            <ul>
	 *            <li>CS_RANGE_REPEAT_ALL (1)</li>
	 *            <li>CS_RANGE_REPEAT_CURRENT (0)</li>
	 *            <li>CS_RANGE_REPEAT_FUTURE (3), inclusive</li>
	 *            <li>CS_RANGE_REPEAT_PREV (2), inclusive</li>
	 * @param recurrenceId
	 *            The recurrence identifier (RECURRENCE-ID item) for a recurring calendar event. The format of a recurrence identifier is a
	 *            time in UTC format, for example, 20120913T160000Z.
	 * @throws NotesError.NOTES_ERR_INVALIDID
	 *             when the identifier for the NotesCalendarEntry object is not valid.
	 * @throws NotesError.NOTES_ERR_RECURID_NOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object is not valid.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object does not identify an entry in the calendar, or the scope
	 *             and recurid are missing for a recurring entry.
	 */
	@Override
	public void counter(final String comments, final lotus.domino.DateTime start, final lotus.domino.DateTime end,
			final boolean keepPlaceholder, final int scope, final String recurrenceId);

	/**
	 * Counters a meeting entry or entries.
	 * <p>
	 * This method deals with meeting entries, not notices.
	 * </p>
	 *
	 * @param comments
	 *            Comments regarding a meeting change.
	 * @param start
	 *            The start time of the counter proposal.
	 * @param end
	 *            The end time of the counter proposal. An exception occurs if the end time is not greater than the start time.
	 * @param scope
	 *            The scope of a recurring operation:
	 *            <ul>
	 *            <li>CS_RANGE_REPEAT_ALL (1)</li>
	 *            <li>CS_RANGE_REPEAT_CURRENT (0)</li>
	 *            <li>CS_RANGE_REPEAT_FUTURE (3), inclusive</li>
	 *            <li>CS_RANGE_REPEAT_PREV (2), inclusive</li>
	 * @param recurrenceId
	 *            The recurrence identifier (RECURRENCE-ID item) for a recurring calendar event. The format of a recurrence identifier is a
	 *            time in UTC format, for example, 20120913T160000Z.
	 * @throws NotesError.NOTES_ERR_INVALIDID
	 *             when the identifier for the NotesCalendarEntry object is not valid.
	 * @throws NotesError.NOTES_ERR_RECURID_NOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object is not valid.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object does not identify an entry in the calendar, or the scope
	 *             and recurid are missing for a recurring entry.
	 */
	@Override
	public void counter(final String comments, final lotus.domino.DateTime start, final lotus.domino.DateTime end, final int scope,
			final String recurrenceId);

	/**
	 * Declines a meeting entry or entries.
	 * <p>
	 * This method deals with meeting entries, not notices.
	 * </p>
	 *
	 * @param comments
	 *            Comments regarding a meeting change.
	 */
	@Override
	public void decline(final String comments);

	/**
	 * Declines a meeting entry or entries.
	 * <p>
	 * This method deals with meeting entries, not notices.
	 * </p>
	 *
	 * @param comments
	 *            Comments regarding a meeting change.
	 * @param keepInformed
	 *            Specify true to continue to receive notices about the meeting.
	 */
	@Override
	public void decline(final String comments, final boolean keepInformed);

	/**
	 * Declines a meeting entry or entries.
	 * <p>
	 * This method deals with meeting entries, not notices.
	 * </p>
	 *
	 * @param comments
	 *            Comments regarding a meeting change.
	 * @param keepInformed
	 *            Specify true to continue to receive notices about the meeting.
	 * @param scope
	 *            The scope of a recurring operation:
	 *            <ul>
	 *            <li>CS_RANGE_REPEAT_ALL (1)</li>
	 *            <li>CS_RANGE_REPEAT_CURRENT (0)</li>
	 *            <li>CS_RANGE_REPEAT_FUTURE (3), inclusive</li>
	 *            <li>CS_RANGE_REPEAT_PREV (2), inclusive</li>
	 * @param recurrenceId
	 *            The recurrence identifier (RECURRENCE-ID item) for a recurring calendar event. The format of a recurrence identifier is a
	 *            time in UTC format, for example, 20120913T160000Z.
	 * @throws NotesError.NOTES_ERR_INVALIDID
	 *             when the identifier for the NotesCalendarEntry object is not valid.
	 * @throws NotesError.NOTES_ERR_RECURID_NOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object is not valid.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object does not identify an entry in the calendar, or the scope
	 *             and recurid are missing for a recurring entry.
	 */
	@Override
	public void decline(final String comments, final boolean keepInformed, final int scope, final String recurrenceId);

	/**
	 * Delegates a meeting entry or entries to a substitute attendee.
	 * <p>
	 * This method deals with meeting entries, not notices.
	 * </p>
	 *
	 * @param commentsToOrganizer
	 *            Comments regarding a meeting change.
	 * @param delegateTo
	 *            Mail address of new meeting attendee.
	 */
	@Override
	public void delegate(final String commentsToOrganizer, final String delegateTo);

	/**
	 * Delegates a meeting entry or entries to a substitute attendee.
	 * <p>
	 * This method deals with meeting entries, not notices.
	 * </p>
	 *
	 * @param commentsToOrganizer
	 *            Comments regarding a meeting change.
	 * @param delegateTo
	 *            Mail address of new meeting attendee.
	 * @param keepInformed
	 *            Specify true to continue to receive notices about the meeting.
	 */
	@Override
	public void delegate(final String commentsToOrganizer, final String delegateTo, final boolean keepInformed);

	/**
	 * Delegates a meeting entry or entries to a substitute attendee.
	 * <p>
	 * This method deals with meeting entries, not notices.
	 * </p>
	 *
	 * @param commentsToOrganizer
	 *            Comments regarding a meeting change.
	 * @param delegateTo
	 *            Mail address of new meeting attendee.
	 * @param keepInformed
	 *            Specify true to continue to receive notices about the meeting.
	 * @param scope
	 *            The scope of a recurring operation:
	 *            <ul>
	 *            <li>CS_RANGE_REPEAT_ALL (1)</li>
	 *            <li>CS_RANGE_REPEAT_CURRENT (0)</li>
	 *            <li>CS_RANGE_REPEAT_FUTURE (3), inclusive</li>
	 *            <li>CS_RANGE_REPEAT_PREV (2), inclusive</li>
	 * @param recurrenceId
	 *            The recurrence identifier (RECURRENCE-ID item) for a recurring calendar event. The format of a recurrence identifier is a
	 *            time in UTC format, for example, 20120913T160000Z.
	 * @throws NotesError.NOTES_ERR_INVALIDID
	 *             when the identifier for the NotesCalendarEntry object is not valid.
	 * @throws NotesError.NOTES_ERR_RECURID_NOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object is not valid.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object does not identify an entry in the calendar, or the scope
	 *             and recurid are missing for a recurring entry.
	 */
	@Override
	public void delegate(final String commentsToOrganizer, final String delegateTo, final boolean keepInformed, final int scope,
			final String recurrenceId);

	/**
	 * Delegates a meeting entry or entries to a substitute attendee.
	 * <p>
	 * This method deals with meeting entries, not notices.
	 * </p>
	 *
	 * @param commentsToOrganizer
	 *            Comments regarding a meeting change.
	 * @param delegateTo
	 *            Mail address of new meeting attendee.
	 * @param scope
	 *            The scope of a recurring operation:
	 *            <ul>
	 *            <li>CS_RANGE_REPEAT_ALL (1)</li>
	 *            <li>CS_RANGE_REPEAT_CURRENT (0)</li>
	 *            <li>CS_RANGE_REPEAT_FUTURE (3), inclusive</li>
	 *            <li>CS_RANGE_REPEAT_PREV (2), inclusive</li>
	 * @param recurrenceId
	 *            The recurrence identifier (RECURRENCE-ID item) for a recurring calendar event. The format of a recurrence identifier is a
	 *            time in UTC format, for example, 20120913T160000Z.
	 * @throws NotesError.NOTES_ERR_INVALIDID
	 *             when the identifier for the NotesCalendarEntry object is not valid.
	 * @throws NotesError.NOTES_ERR_RECURID_NOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object is not valid.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object does not identify an entry in the calendar, or the scope
	 *             and recurid are missing for a recurring entry.
	 */
	@Override
	public void delegate(final String commentsToOrganizer, final String delegateTo, final int scope, final String recurrenceId);

	/**
	 * Gets the document that contains a calendar entry.
	 */
	@Override
	public Document getAsDocument();

	/**
	 * Gets the document that contains a calendar entry.
	 *
	 * @param flags
	 *            One of the following:
	 *            <ul>
	 *            <li>CS_DOCUMENT_NOSPLIT (1)</li>
	 *            <li>0</li>
	 *            </ul>
	 */
	@Override
	public Document getAsDocument(final int flags);

	/**
	 * Gets the document that contains a calendar entry.
	 *
	 * @param recurrenceId
	 *            The recurrence identifier (RECURRENCE-ID item) for a recurring calendar event. The format of a recurrence identifier is a
	 *            time in UTC format, for example, 20120913T160000Z.
	 * @throws NotesError.NOTES_ERR_INVALIDID
	 *             when the identifier for the NotesCalendarEntry object is not valid.
	 * @throws NotesError.NOTES_ERR_RECURID_NOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object is not valid.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object does not identify an entry in the calendar, or the scope
	 *             and recurid are missing for a recurring entry.
	 */
	@Override
	public Document getAsDocument(final int flags, final String recurrenceId);

	/**
	 * Gets the calendar this entry belongs to.
	 */
	public NotesCalendar getParent();

	/**
	 * Gets calendar notices associated with an entry.
	 *
	 * @return The notices, or null for no notices.
	 * @throws NotesError.NOTES_ERR_NOTACCEPTED
	 *             You should locate the notice and accept, then try again.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             The recurrence identifier for the NotesCalendarEntry object does not identify an entry in the calendar, or the identifier
	 *             for the NotesCalendarNotice object does not identify a notice in the calendar.
	 */
	@Override
	public Vector<NotesCalendarNotice> getNotices();

	/**
	 * Globally unique identifier of a calendar entry in the iCalendar format.
	 */
	@Override
	public String getUID();

	/**
	 * Renders a calendar entry in the iCalendar format.
	 * <p>
	 * This method returns complete iCalendar data for the entry. For recurring entries, the data may contain multiple VEVENT entries.
	 * </p>
	 *
	 * @return The calendar entry in iCalendar format.
	 */
	@Override
	public String read();

	/**
	 * Renders a calendar entry in the iCalendar format.
	 * <p>
	 * This method returns complete iCalendar data for the entry. For recurring entries, the data may contain multiple VEVENT entries.
	 * </p>
	 *
	 * @param recurrenceId
	 *            The recurrence identifier (RECURRENCE-ID item) for a recurring calendar event. The format of a recurrence identifier is a
	 *            time in UTC format, for example, 20120913T160000Z.
	 * @return The calendar entry in iCalendar format.
	 * @throws NotesError.NOTES_ERR_INVALIDID
	 *             when the identifier for the NotesCalendarEntry object is not valid.
	 * @throws NotesError.NOTES_ERR_RECURID_NOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object is not valid.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object does not identify an entry in the calendar, or the scope
	 *             and recurid are missing for a recurring entry.
	 */
	@Override
	public String read(final String recurrenceId);

	/**
	 * Removes a meeting entry.
	 */
	@Override
	public void remove();

	/**
	 * Removes a meeting entry.
	 *
	 * @param scope
	 *            The scope of a recurring operation:
	 *            <ul>
	 *            <li>CS_RANGE_REPEAT_ALL (1)</li>
	 *            <li>CS_RANGE_REPEAT_CURRENT (0)</li>
	 *            <li>CS_RANGE_REPEAT_FUTURE (3), inclusive</li>
	 *            <li>CS_RANGE_REPEAT_PREV (2), inclusive</li>
	 * @param recurrenceId
	 *            The recurrence identifier (RECURRENCE-ID item) for a recurring calendar event. The format of a recurrence identifier is a
	 *            time in UTC format, for example, 20120913T160000Z.
	 * @throws NotesError.NOTES_ERR_INVALIDID
	 *             when the identifier for the NotesCalendarEntry object is not valid.
	 * @throws NotesError.NOTES_ERR_RECURID_NOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object is not valid.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object does not identify an entry in the calendar, or the scope
	 *             and recurid are missing for a recurring entry.
	 */
	@Override
	public void remove(final int scope, final String recurrenceId);

	/**
	 * Requests information for a meeting entry.
	 * <p>
	 * This method deals with meeting entries, not notices.
	 * </p>
	 *
	 * @param comments
	 *            Comments regarding a request.
	 */
	@Override
	public void requestInfo(final String comments);

	/**
	 * Tentatively accepts a meeting entry or entries.
	 *
	 * @param comments
	 *            Comments regarding a meeting change.
	 */
	@Override
	public void tentativelyAccept(final String comments);

	/**
	 * Tentatively accepts a meeting entry or entries.
	 *
	 * @param comments
	 *            Comments regarding a meeting change.
	 * @param scope
	 *            The scope of a recurring operation:
	 *            <ul>
	 *            <li>CS_RANGE_REPEAT_ALL (1)</li>
	 *            <li>CS_RANGE_REPEAT_CURRENT (0)</li>
	 *            <li>CS_RANGE_REPEAT_FUTURE (3), inclusive</li>
	 *            <li>CS_RANGE_REPEAT_PREV (2), inclusive</li>
	 * @param recurrenceId
	 *            The recurrence identifier (RECURRENCE-ID item) for a recurring calendar event. The format of a recurrence identifier is a
	 *            time in UTC format, for example, 20120913T160000Z.
	 * @throws NotesError.NOTES_ERR_RECURID_NOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object is not valid.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object does not identify an entry in the calendar, or the scope
	 *             and recurid are missing for a recurring entry.
	 */
	@Override
	public void tentativelyAccept(final String comments, final int scope, final String recurrenceId);

	/**
	 * Updates a calendar entry.
	 * <p>
	 * The entry value must contain one VEVENT. For a recurring entry, you must specify recurid. The iCalendar input must contain a single
	 * VEVENT and a UID.
	 * </p>
	 *
	 * @param iCalEntry
	 *            The new value of the entry in iCalendar format.
	 * @throws NotesError.NOTES_ERR_ERRSENDINGNOTICES
	 *             A problem occurred sending out notices for a meeting. You may want to update the meeting again.
	 * @throws NotesError.NOTES_ERR_NEWERVERSIONEXISTS
	 *             The icalentry data is not valid according to sequence. You should revise it or retrieve new data, and try again.
	 * @throws NotesError.NOTES_ERR_UNSUPPORTEDACTION
	 *             The method is attempting to apply an action that is not valid for the entry, for example, attempting to cancel a meeting
	 *             when you are not the chair.
	 */
	@Override
	public void update(final String iCalEntry);

	/**
	 * Updates a calendar entry.
	 * <p>
	 * The entry value must contain one VEVENT. For a recurring entry, you must specify recurid. The iCalendar input must contain a single
	 * VEVENT and a UID.
	 * </p>
	 *
	 * @param iCalEntry
	 *            The new value of the entry in iCalendar format.
	 * @param comments
	 *            Comments regarding a meeting change.
	 * @throws NotesError.NOTES_ERR_ERRSENDINGNOTICES
	 *             A problem occurred sending out notices for a meeting. You may want to update the meeting again.
	 * @throws NotesError.NOTES_ERR_NEWERVERSIONEXISTS
	 *             The icalentry data is not valid according to sequence. You should revise it or retrieve new data, and try again.
	 * @throws NotesError.NOTES_ERR_UNSUPPORTEDACTION
	 *             The method is attempting to apply an action that is not valid for the entry, for example, attempting to cancel a meeting
	 *             when you are not the chair.
	 */
	@Override
	public void update(final String iCalEntry, final String comments);

	/**
	 * Updates a calendar entry.
	 * <p>
	 * The entry value must contain one VEVENT. For a recurring entry, you must specify recurid. The iCalendar input must contain a single
	 * VEVENT and a UID.
	 * </p>
	 *
	 * @param iCalEntry
	 *            The new value of the entry in iCalendar format.
	 * @param comments
	 *            Comments regarding a meeting change.
	 * @param flags
	 *            Write flags. Combine values by adding them.
	 *            <ul>
	 *            <li>NotesCalendar.CS_WRITE_DISABLE_IMPLICIT_SCHEDULING (2) disables the automatic sending of notices to participants.
	 *            Setting this flag is the same as setting AutoSendNotices to false before calling this method.</li>
	 *            <li>NotesCalendar.CS_WRITE_MODIFY_LITERAL (1 completely overwrites the original entry and using only the icalentry input.
	 *            By default, the update preserves body attachments if they are not supplied with the input and preserves custom fields not
	 *            present on the icalentry input.</li>
	 *            </ul>
	 * @throws NotesError.NOTES_ERR_ERRSENDINGNOTICES
	 *             A problem occurred sending out notices for a meeting. You may want to update the meeting again.
	 * @throws NotesError.NOTES_ERR_NEWERVERSIONEXISTS
	 *             The icalentry data is not valid according to sequence. You should revise it or retrieve new data, and try again.
	 * @throws NotesError.NOTES_ERR_UNSUPPORTEDACTION
	 *             The method is attempting to apply an action that is not valid for the entry, for example, attempting to cancel a meeting
	 *             when you are not the chair.
	 */
	@Override
	public void update(final String iCalEntry, final String comments, final long flags);

	/**
	 * Updates a calendar entry.
	 * <p>
	 * The entry value must contain one VEVENT. For a recurring entry, you must specify recurid. The iCalendar input must contain a single
	 * VEVENT and a UID.
	 * </p>
	 *
	 * @param iCalEntry
	 *            The new value of the entry in iCalendar format.
	 * @param comments
	 *            Comments regarding a meeting change.
	 * @param flags
	 *            Write flags. Combine values by adding them.
	 *            <ul>
	 *            <li>NotesCalendar.CS_WRITE_DISABLE_IMPLICIT_SCHEDULING (2) disables the automatic sending of notices to participants.
	 *            Setting this flag is the same as setting AutoSendNotices to false before calling this method.</li>
	 *            <li>NotesCalendar.CS_WRITE_MODIFY_LITERAL (1 completely overwrites the original entry and using only the icalentry input.
	 *            By default, the update preserves body attachments if they are not supplied with the input and preserves custom fields not
	 *            present on the icalentry input.</li>
	 *            </ul>
	 * @param recurrenceId
	 *            The recurrence identifier (RECURRENCE-ID item) for a recurring calendar event. The format of a recurrence identifier is a
	 *            time in UTC format, for example, 20120913T160000Z.
	 * @throws NotesError.NOTES_ERR_RECURID_NOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object is not valid.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             when the recurrence identifier for the NotesCalendarEntry object does not identify an entry in the calendar, or the scope
	 *             and recurid are missing for a recurring entry.
	 * @throws NotesError.NOTES_ERR_ERRSENDINGNOTICES
	 *             A problem occurred sending out notices for a meeting. You may want to update the meeting again.
	 * @throws NotesError.NOTES_ERR_NEWERVERSIONEXISTS
	 *             The icalentry data is not valid according to sequence. You should revise it or retrieve new data, and try again.
	 * @throws NotesError.NOTES_ERR_UNSUPPORTEDACTION
	 *             The method is attempting to apply an action that is not valid for the entry, for example, attempting to cancel a meeting
	 *             when you are not the chair.
	 */
	@Override
	public void update(final String iCalEntry, final String comments, final long flags, final String recurrenceId);

}
