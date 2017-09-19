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
package org.openntf.domino;

import java.util.Vector;

import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * Represents a Domino calendar notice.
 * <h5>Usage</h5>
 * <p>
 * This object provides access to one notice of the calendar and scheduling services in a Domino mail application in standard iCalendar
 * format. See Internet Calendaring and Scheduling Core Object Specification (iCalendar) at
 * <a href="http://tools.ietf.org/html/rfc5545">http://tools.ietf.org/html/rfc5545</a> for the format.
 * </p>
 * <p>
 * {@link NotesCalendar} and {@link NotesCalendarEntry} provide methods for getting and creating calendar notices.
 * </p>
 * <p>
 * Notices include invitations, reschedules, information updates, confirmations, cancellations, counter proposals, requests for information,
 * acceptances, declines, and tentative acceptances received from another user and not yet processed. You can treat a notice as a
 * NotesCalendarEntry object to apply the following methods: accept, cancel, counter, decline, delegate, remove, requestInfo, and
 * tentativelyAccept. The NotesCalendarEntry methods have scope and recurid parameters, which the corresponding NotesCalendarNotice methods
 * do not. Application of other NotesCalendarEntry methods to a notice causes an exception.
 * </p>
 */
public interface NotesCalendarNotice extends Base<lotus.domino.NotesCalendarNotice>, lotus.domino.NotesCalendarNotice,
		org.openntf.domino.ext.NotesCalendarNotice, SessionDescendant {

	public static class Schema extends FactorySchema<NotesCalendarNotice, lotus.domino.NotesCalendarNotice, NotesCalendar> {
		@Override
		public Class<NotesCalendarNotice> typeClass() {
			return NotesCalendarNotice.class;
		}

		@Override
		public Class<lotus.domino.NotesCalendarNotice> delegateClass() {
			return lotus.domino.NotesCalendarNotice.class;
		}

		@Override
		public Class<NotesCalendar> parentClass() {
			return NotesCalendar.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Accepts a calendar notice.
	 *
	 * @param comments
	 *            Comments regarding a meeting change.
	 * @throws NotesError.NOTES_ERR_UNSUPPORTEDACTION
	 *             The method is attempting to apply an action that is not valid for the entry.
	 * @throws NotesError.NOTES_ERR_OVERWRITEDISALLOWED
	 *             The action should be verified then reissued with the overwrite flag set.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             The identifier for the NotesCalendarNotice object does not identify a notice in the calendar.
	 */
	@Override
	public void accept(final String comments);

	/**
	 * Accepts but counters a calendar notice.
	 *
	 * @param comments
	 *            Comments regarding a meeting change.
	 * @throws NotesError.NOTES_ERR_UNSUPPORTEDACTION
	 *             The method is attempting to apply an action that is not valid for the entry.
	 * @throws NotesError.NOTES_ERR_OVERWRITEDISALLOWED
	 *             The action should be verified then reissued with the overwrite flag set.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             The identifier for the NotesCalendarNotice object does not identify a notice in the calendar.
	 */
	@Override
	public void acceptCounter(final String comments);

	/**
	 * Counters a meeting notice.
	 *
	 * @param comments
	 *            Comments regarding a meeting change.
	 * @param start
	 *            The start time of the counter proposal.
	 * @param end
	 *            The end time of the counter proposal. An exception occurs if the end time is not greater than the start time.
	 * @throws NotesError.NOTES_ERR_UNSUPPORTEDACTION
	 *             The method is attempting to apply an action that is not valid for the entry.
	 * @throws NotesError.NOTES_ERR_OVERWRITEDISALLOWED
	 *             The action should be verified then reissued with the overwrite flag set.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             The identifier for the NotesCalendarNotice object does not identify a notice in the calendar.
	 */
	@Override
	public void counter(final String comments, final lotus.domino.DateTime start, final lotus.domino.DateTime end);

	/**
	 * Counters a meeting notice.
	 *
	 * @param comments
	 *            Comments regarding a meeting change.
	 * @param start
	 *            The start time of the counter proposal.
	 * @param end
	 *            The end time of the counter proposal. An exception occurs if the end time is not greater than the start time.
	 * @param keepPlaceholder
	 *            Keeps a placeholder for the meeting.
	 * @throws NotesError.NOTES_ERR_UNSUPPORTEDACTION
	 *             The method is attempting to apply an action that is not valid for the entry.
	 * @throws NotesError.NOTES_ERR_OVERWRITEDISALLOWED
	 *             The action should be verified then reissued with the overwrite flag set.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             The identifier for the NotesCalendarNotice object does not identify a notice in the calendar.
	 */
	@Override
	public void counter(final String comments, final lotus.domino.DateTime start, final lotus.domino.DateTime end,
			final boolean keepPlaceholder);

	/**
	 * Declines a meeting.
	 *
	 * @param comments
	 *            Comments regarding a meeting change.
	 * @throws NotesError.NOTES_ERR_UNSUPPORTEDACTION
	 *             The method is attempting to apply an action that is not valid for the entry.
	 * @throws NotesError.NOTES_ERR_OVERWRITEDISALLOWED
	 *             The action should be verified then reissued with the overwrite flag set.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             The identifier for the NotesCalendarNotice object does not identify a notice in the calendar.
	 */
	@Override
	public void decline(final String comments);

	/**
	 * Declines a meeting.
	 *
	 * @param comments
	 *            Comments regarding a meeting change.
	 * @param keepInformed
	 *            Specify true to continue to receive notices about the meeting.
	 * @throws NotesError.NOTES_ERR_UNSUPPORTEDACTION
	 *             The method is attempting to apply an action that is not valid for the entry.
	 * @throws NotesError.NOTES_ERR_OVERWRITEDISALLOWED
	 *             The action should be verified then reissued with the overwrite flag set.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             The identifier for the NotesCalendarNotice object does not identify a notice in the calendar.
	 */
	@Override
	public void decline(final String comments, final boolean keepInformed);

	/**
	 * Declines but counters a calendar notice.
	 *
	 * @param comments
	 *            Comments regarding a meeting change.
	 * @throws NotesError.NOTES_ERR_UNSUPPORTEDACTION
	 *             The method is attempting to apply an action that is not valid for the entry.
	 * @throws NotesError.NOTES_ERR_OVERWRITEDISALLOWED
	 *             The action should be verified then reissued with the overwrite flag set.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             The identifier for the NotesCalendarNotice object does not identify a notice in the calendar.
	 */
	@Override
	public void declineCounter(final String comments);

	/**
	 * Delegates a meeting notice to a new attendee.
	 *
	 * @param commentsToOrganizer
	 *            Comments regarding a meeting change.
	 * @param delegateTo
	 *            Mail address of new meeting attendee.
	 */
	@Override
	public void delegate(final String commentsToOrganizer, final String delegateTo);

	/**
	 * Delegates a meeting notice to a new attendee.
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
	 * Gets the document that contains a calendar notice.
	 */
	@Override
	public Document getAsDocument();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.NotesCalendarNotice#getNoteID()
	 */
	@Override
	public String getNoteID();

	/**
	 * Gets calendar notices that are outstanding invitations.
	 * <p>
	 * This method returns unprocessed notices for a meeting, such as reschedules, informational updates, cancellations, and confirmations.
	 * The meeting invitation must be responded to. For recurring meetings, notices that apply to any instance are returned.
	 * </p>
	 *
	 * @return the notices or null for no notices
	 */
	@Override
	public Vector<NotesCalendarNotice> getOutstandingInvitations();

	/**
	 * Gets the parent.
	 *
	 * @return the parent
	 */
	public NotesCalendar getParent();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.NotesCalendarNotice#getUNID()
	 */
	@Override
	public String getUNID();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.NotesCalendarNotice#read()
	 */
	@Override
	public boolean isOverwriteCheckEnabled();

	/**
	 * Renders a calendar notice in iCalendar format.
	 *
	 * @return The calendar notice in iCalendar format.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             The identifier for the NotesCalendarNotice object does not identify a notice in the calendar.
	 */
	@Override
	public String read();

	/**
	 * Removes and cancels a calendar notice.
	 * <p>
	 * If AutoSendNotices is true, this method works as follows:
	 * <ul>
	 * <li>Deletes (hard delete) a non-meeting entry such as an appointment.</li>
	 * <li>Cancels a meeting for which you are the organizer and removes it from your calendar.</li>
	 * <li>Declines a meeting for which you are an invitee and removes it from your calendar.</li>
	 * </ul>
	 * If AutoSendNotices is false, this method deletes (hard delete) an entry.
	 * </p>
	 *
	 * @throws NotesError.NOTES_ERR_UNSUPPORTEDACTION
	 *             The method is attempting to apply an action that is not valid for the entry.
	 * @throws NotesError.NOTES_ERR_OVERWRITEDISALLOWED
	 *             The action should be verified then reissued with the overwrite flag set.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             The identifier for the NotesCalendarNotice object does not identify a notice in the calendar.
	 */
	@Override
	public void removeCancelled();

	/**
	 * Requests information for a calendar notice.
	 *
	 * @param comments
	 *            Comments regarding a request.
	 * @throws NotesError.NOTES_ERR_UNSUPPORTEDACTION
	 *             The method is attempting to apply an action that is not valid for the entry.
	 * @throws NotesError.NOTES_ERR_OVERWRITEDISALLOWED
	 *             The action should be verified then reissued with the overwrite flag set.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             The identifier for the NotesCalendarNotice object does not identify a notice in the calendar.
	 */
	@Override
	public void requestInfo(final String comments);

	/**
	 * Sends updated information for a calendar notice.
	 *
	 * @param comments
	 *            Comments regarding a request.
	 * @throws NotesError.NOTES_ERR_UNSUPPORTEDACTION
	 *             The method is attempting to apply an action that is not valid for the entry.
	 * @throws NotesError.NOTES_ERR_OVERWRITEDISALLOWED
	 *             The action should be verified then reissued with the overwrite flag set.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             The identifier for the NotesCalendarNotice object does not identify a notice in the calendar.
	 */
	@Override
	public void sendUpdatedInfo(final String comments);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.NotesCalendarNotice#tentativelyAccept(java.lang.String)
	 */
	@Override
	public void setOverwriteCheckEnabled(final boolean flag);

	/**
	 * Tentatively accepts a meeting notice.
	 *
	 * @param comments
	 *            Comments regarding a meeting change.
	 * @throws NotesError.NOTES_ERR_UNSUPPORTEDACTION
	 *             The method is attempting to apply an action that is not valid for the entry.
	 * @throws NotesError.NOTES_ERR_OVERWRITEDISALLOWED
	 *             The action should be verified then reissued with the overwrite flag set.
	 * @throws NotesError.NOTES_ERR_IDNOTFOUND
	 *             The identifier for the NotesCalendarNotice object does not identify a notice in the calendar.
	 */
	@Override
	public void tentativelyAccept(final String comments);
}
