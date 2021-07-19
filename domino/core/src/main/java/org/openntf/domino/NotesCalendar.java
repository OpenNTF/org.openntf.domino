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
 * Represents a Domino calendar.
 * <p>
 * This object provides access to calendar and scheduling services in a Domino mail application in standard iCalendar format. See Internet
 * Calendaring and Scheduling Core Object Specification (iCalendar) at
 * <a href="http://tools.ietf.org/html/rfc5545" target="_blank">http://tools.ietf.org/html/rfc5545</a> for the format.
 * </p>
 * <h3>Creation and access</h3>
 * <p>
 * To create a <code>NotesCalendar</code> object, call {@link Session#getCalendar(lotus.domino.Database)}.
 * </p>
 * <p>
 * You can access calendar entries as Document objects, but this interface should be used sparingly. In a standard Domino mail application,
 * calendar documents can be accessed through the ($Calendar) view. You can access calendar documents directly with
 * {@link NotesCalendarEntry#getAsDocument()} and {@link NotesCalendarNotice#getAsDocument()}. For a description of calendar documents, see
 * <a href="http://www-01.ibm.com/support/docview.wss?uid=swg21229486" target=
 * "_blank">http://www-01.ibm.com/support/docview.wss?uid=swg21229486</a>.
 * </p>
 *
 * @author Paul Withers
 *
 */
public interface NotesCalendar
		extends Base<lotus.domino.NotesCalendar>, lotus.domino.NotesCalendar, org.openntf.domino.ext.NotesCalendar, SessionDescendant {

	public static class Schema extends FactorySchema<NotesCalendar, lotus.domino.NotesCalendar, Session> {
		@Override
		public Class<NotesCalendar> typeClass() {
			return NotesCalendar.class;
		}

		@Override
		public Class<lotus.domino.NotesCalendar> delegateClass() {
			return lotus.domino.NotesCalendar.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Creates a calendar entry.
	 * <h5>Usage</h5>
	 * <p>
	 * An exception occurs if the input is invalid. See Internet Calendaring and Scheduling Core Object Specification (iCalendar) at
	 * <a href="http://tools.ietf.org/html/rfc5545" target="_blank">http://tools.ietf.org/html/rfc5545</a> for the format, or use the output
	 * from a read operation as a template. If problems persist, try running with the following notes.ini variable:
	 * <code>CSDebugAPI=1</code>. When the exception occurs, check the console log for details.
	 * </p>
	 * <p>
	 * For a meeting, if AutoSendNotices is not set to false, notices are automatically sent to participants.
	 * </p>
	 * <p>
	 * If ical entry contains a recurrence rule (RRULE item), this method creates a calendar event for each recurrence with its own
	 * identifier (RECURRENCE-ID item). The format of a recurrence identifier is the time of the event in UTC format, for example,
	 * 20120913T160000Z. However, if you later change the time of the event, the identifier does not change.
	 * </p>
	 *
	 * @param iCalEntry
	 *            Input for one calendar entry in iCalendar format.
	 * @return The new calendar entry.
	 * @throws NotesError.NOTES_ERR_ERRSENDINGNOTICES
	 *             when sending notices
	 * @throws NotesError.NOTES_ERR_ENTRYEXISTS
	 *             when the entry already exists
	 */
	@Override
	public NotesCalendarEntry createEntry(final String iCalEntry);

	/**
	 * Creates a calendar entry.
	 * <h5>Usage</h5>
	 * <p>
	 * An exception occurs if the input is invalid. See Internet Calendaring and Scheduling Core Object Specification (iCalendar) at
	 * <a href="http://tools.ietf.org/html/rfc5545" target="_blank">http://tools.ietf.org/html/rfc5545</a> for the format, or use the output
	 * from a read operation as a template. If problems persist, try running with the following notes.ini variable:
	 * <code>CSDebugAPI=1</code>. When the exception occurs, check the console log for details.
	 * </p>
	 * <p>
	 * For a meeting, if AutoSendNotices is not set to false and NotesCalendar.CS_WRITE_DISABLE_IMPLICIT_SCHEDULING is not set, notices are
	 * automatically sent to participants.
	 * </p>
	 * <p>
	 * If ical entry contains a recurrence rule (RRULE item), this method creates a calendar event for each recurrence with its own
	 * identifier (RECURRENCE-ID item). The format of a recurrence identifier is the time of the event in UTC format, for example,
	 * 20120913T160000Z. However, if you later change the time of the event, the identifier does not change.
	 * </p>
	 *
	 * @param iCalEntry
	 *            Input for one calendar entry in iCalendar format.
	 * @param flags
	 *            <code>NotesCalendar.CS_WRITE_DISABLE_IMPLICIT_SCHEDULING</code> disables the automatic sending of notices to participants.
	 *            Setting this flag is the same as setting {@link #setAutoSendNotices(boolean) AutoSendNotices} to false before calling this
	 *            method.
	 * @return The new calendar entry.
	 */
	@Override
	public NotesCalendarEntry createEntry(final String iCalEntry, final int flags);

	/**
	 * Indicates whether to automatically send information to participants when creating and updating meetings.
	 *
	 * @return true if information will be sent to participants
	 */
	@Override
	public boolean getAutoSendNotices();

	/**
	 * Gets calendar entries within a given time range.
	 *
	 * @param start
	 *            The start time of the entries.
	 * @param end
	 *            The end time of the entries.
	 * @return The calendar entries in the range, or an empty vector for no entries.
	 */
	@Override
	public Vector<NotesCalendarEntry> getEntries(final lotus.domino.DateTime start, final lotus.domino.DateTime end);

	/**
	 * Gets calendar entries within a given time range.
	 *
	 * @param start
	 *            The start time of the entries.
	 * @param end
	 *            The end time of the entries.
	 * @param skipCount
	 *            The number of entries to skip before starting the get operation.
	 * @param maxReturn
	 *            The maximum number of entries to return.
	 * @return The calendar entries in the range, or an empty vector for no entries.
	 */
	@Override
	public Vector<NotesCalendarEntry> getEntries(final lotus.domino.DateTime start, final lotus.domino.DateTime end, final int skipCount,
			final int maxReturn);

	/**
	 * Returns the number of entries processed by {@link #getEntries(lotus.domino.DateTime, lotus.domino.DateTime)} and
	 * {@link #readRange(lotus.domino.DateTime, lotus.domino.DateTime)} operations.
	 * <p>
	 * Use this property as the third parameter to {@link #getEntries(lotus.domino.DateTime, lotus.domino.DateTime, int, int)} and
	 * {@link #readRange(lotus.domino.DateTime, lotus.domino.DateTime, int, int)} to process entries in successive operations.
	 * </p>
	 */
	@Override
	public int getEntriesProcessed();

	/**
	 * Gets a calendar entry.
	 * <p>
	 * This method does not verify uid except for its existence. Validity checking and error reporting occur when you attempt to use the
	 * returned <code>NotesCalendarEntry</code> object.
	 * </p>
	 *
	 * @param uid
	 *            The iCalendar identifier (UID item) of the entry.
	 * @return The calendar entry. No exception occurs for this method if the identifier is invalid. However, when you attempt to use the
	 *         returned NotesCalendarEntry object, the following exception occurs: Entry not found in index.
	 */
	@Override
	public NotesCalendarEntry getEntry(final String uid);

	/**
	 * Gets a calendar entry given its note identifier.
	 *
	 * @param noteid
	 *            The note identifier of the Domino document containing the entry.
	 * @return The calendar entry.
	 */
	@Override
	public NotesCalendarEntry getEntryByNoteID(final String noteid);

	/**
	 * Gets a calendar entry given its universal identifier (UNID).
	 *
	 * @param unid
	 *            The universal identifier (UNID) of the Domino document containing the entry.
	 * @return The calendar entry.
	 * @throws NotesExcerption
	 *             if the identifier is invalid
	 */
	@Override
	public NotesCalendarEntry getEntryByUNID(final String unid);

	/**
	 * Gets calendar entries that are new invitations.
	 *
	 * @return The new invitations, or an empty vector for no invitations.
	 */
	@Override
	public Vector<NotesCalendarNotice> getNewInvitations();

	/**
	 * Gets calendar entries that are new invitations.
	 * <p>
	 * It is important to remember that:
	 * </p>
	 * <ul>
	 * <li>The first parameter applies to meetings and specifies the first date to be included in the search.</li>
	 * <li>The second parameter applies to invitations and specifies the last date to be excluded from the search.</li>
	 * </ul>
	 *
	 * @param start
	 *            The start time for meetings to which any new invitations apply.
	 * @param since
	 *            The since time for any new invitations. Use this parameter in conjunction with UntilTime to get invitations posted since
	 *            the last call.
	 * @return The new invitations, or an empty vector for no invitations.
	 */
	@Override
	public Vector<NotesCalendarNotice> getNewInvitations(final lotus.domino.DateTime start, final lotus.domino.DateTime since);

	/**
	 * Gets a calendar notice given its universal identifier (UNID).
	 *
	 * @param unid
	 *            The universal identifier (UNID) of the Domino document containing the notice.
	 * @return The calendar notice.
	 */
	@Override
	public NotesCalendarNotice getNoticeByUNID(final String unid);

	/**
	 * Gets the parent.
	 *
	 * @return the parent
	 */
	public Session getParent();

	/**
	 * Mask that controls the property display for a {@link #readRange(lotus.domino.DateTime, lotus.domino.DateTime, int, int)} operation.
	 * <p>
	 * Before calling <code>readRange</code>, set this mask to designate the properties that you want returned. By default, all properties
	 * are returned. The following table specifies the bit values. Combine values by adding them.
	 * </p>
	 * <table cellpadding="4" cellspacing="0" summary="" rules="all" frame="border" border="1">
	 * <thead align="left">
	 * <tr valign="bottom">
	 * <th valign="bottom">Constant name</th>
	 * <th valign="bottom">Numerical value</th>
	 * </tr>
	 * </thead> <tbody >
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_ALARM</samp></td>
	 * <td valign="top">131072</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_APPTTYPE</samp></td>
	 * <td valign="top">2048</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_CATEGORY</samp></td>
	 * <td valign="top">1024</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_CLASS</samp></td>
	 * <td valign="top">16</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_DTEND</samp></td>
	 * <td valign="top">2</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_DTSTAMP</samp></td>
	 * <td valign="top">4</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_DTSTART</samp></td>
	 * <td valign="top">1</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_LOCATION</samp></td>
	 * <td valign="top">256</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_NOTESORGANIZER</samp></td>
	 * <td valign="top">32768</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_NOTESROOM</samp></td>
	 * <td valign="top">65536</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_NOTICETYPE</samp></td>
	 * <td valign="top">4096</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_ONLINE_URL</samp></td>
	 * <td valign="top">16384</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_PRIORITY</samp></td>
	 * <td valign="top">32</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_RECURRENCE_ID</samp></td>
	 * <td valign="top">64</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_SEQUENCE</samp></td>
	 * <td valign="top">128</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_STATUS</samp></td>
	 * <td valign="top">8192</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_SUMMARY</samp></td>
	 * <td valign="top">8</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_TRANSP</samp></td>
	 * <td valign="top">512</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 *
	 * @return A combination of the values in the above table
	 */
	@Override
	public int getReadRangeMask1();

	/**
	 * Mask that controls the optional property display for a {@link #readRange(lotus.domino.DateTime, lotus.domino.DateTime, int, int)}
	 * operation.
	 * <p>
	 * Before calling <code>readRange</code>, set this mask to designate the properties that you want returned. By default, no properties
	 * are returned. The following table specifies the bit values. Combine values by adding them.
	 * </p>
	 * <table cellpadding="4" cellspacing="0" rules="all" frame="border" border="1">
	 * <thead align="left">
	 * <tr valign="bottom">
	 * <th valign="bottom" >Constant name</th>
	 * <th valign="bottom" >Numerical value</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr >
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_HASATTACH</samp></td>
	 * <td valign="top">1</td>
	 * </tr>
	 * <tr >
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_UNID</samp></td>
	 * <td valign="top">2</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 */
	@Override
	public int getReadRangeMask2();

	/**
	 * Controls the return of <code>X-LOTUS</code> properties when reading a calendar entry or notice.
	 * <p>
	 * Before reading an entry or notice, you can set this option or accept the default <code>NotesCalendar.CS_XLOTUS_READ_DEFAULT</code>.
	 * </p>
	 * <table cellpadding="4" cellspacing="0" summary="" rules="all" frame="border" border="1">
	 * <thead align="left">
	 * <tr valign="bottom">
	 * <th valign="bottom" >Constant name</th>
	 * <th valign="bottom" >Numerical value</th>
	 * </tr>
	 * </thead> <tbody >
	 * <tr >
	 * <td valign="top" ><samp >NotesCalendar.CS_XLOTUS_READ_DEFAULT</samp> (0)</td>
	 * <td valign="top" >Generates non-proprietary <samp>X-LOTUS</samp> properties. This is the default if this property is not set prior to
	 * reading.</td>
	 * </tr>
	 * <tr >
	 * <td valign="top" ><samp >NotesCalendar.CS_XLOTUS_READ_NONE</samp> (1)</td>
	 * <td valign="top" >Omits all <samp >X-LOTUS</samp> properties.</td>
	 * </tr>
	 * <tr >
	 * <td valign="top" ><samp >NotesCalendar.CS_XLOTUS_READ_ALL</samp> (2)</td>
	 * <td valign="top" >Generates proprietary <samp >X-LOTUS</samp> properties. The caller must know how to update these.</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 */
	@Override
	public int getReadXLotusPropsOutputLevel();

	/**
	 * Specifies the time of the last invitation processed by {@link #getNewInvitations()}.
	 * <p>
	 * This property is set by {@link #getNewInvitations()}. It can be used as the second parameter to <code>getNewInvitations</code> to get
	 * invitations posted since the last call.
	 * </p>
	 */
	@Override
	public DateTime getUntilTime();

	/**
	 * Gets a summary of calendar entries for a range of times.
	 * <p>
	 * For recurring entries, each entry in the range appears as a separate <code>VEVENT</code> with a unique recurrence ID.
	 * </p>
	 * <p>
	 * Each entry contains a <code>UID</code> item whose value can be used to get the corresponding <code>NotesCalendarEntry</code> object.
	 * </p>
	 * <p>
	 * Inclusion in a date range is determined by the start time of a meeting.
	 * </p>
	 * <p>
	 * The content of the return value is modified by any values set for {@link #setReadRangeMask1(int)} and
	 * {@link #setReadRangeMask2(int)}.
	 * </p>
	 *
	 *
	 * @param start
	 *            The start time of the range.
	 * @param end
	 *            The end time of the range. An exception occurs if the end time is not greater than the start time.
	 * @return A summary in iCalendar format of the entries from the start date to the end date, inclusive. An exception occurs if the range
	 *         contains no entries.
	 */
	@Override
	public String readRange(final lotus.domino.DateTime start, final lotus.domino.DateTime end);

	/**
	 * Gets a summary of calendar entries for a range of times.
	 * <p>
	 * For recurring entries, each entry in the range appears as a separate <code>VEVENT</code> with a unique recurrence ID.
	 * </p>
	 * <p>
	 * Each entry contains a <code>UID</code> item whose value can be used to get the corresponding <code>NotesCalendarEntry</code> object.
	 * </p>
	 * <p>
	 * Inclusion in a date range is determined by the start time of a meeting.
	 * </p>
	 * <p>
	 * The content of the return value is modified by any values set for {@link #setReadRangeMask1(int)} and
	 * {@link #setReadRangeMask2(int)}.
	 * </p>
	 * <p>
	 * Use the last two parameters in conjunction with {@link #getEntriesProcessed()} to process entries in successive operations.
	 * </p>
	 *
	 * @param start
	 *            The start time of the range.
	 * @param end
	 *            The end time of the range. An exception occurs if the end time is not greater than the start time.
	 * @param skipCount
	 *            The number of entries to skip from the beginning of the range. This parameter can be used in conjunction with
	 *            {@link #getEntriesProcessed()} to read the entries in a series of calls.
	 * @param maxRead
	 *            The maximum number of entries to read.
	 * @return A summary in iCalendar format of the entries from the start date to the end date, inclusive. An exception occurs if the range
	 *         contains no entries.
	 */
	@Override
	public String readRange(final lotus.domino.DateTime start, final lotus.domino.DateTime end, final int skipCount, final int maxRead);

	/**
	 * Sets whether to automatically send information to participants when creating and updating meetings.
	 *
	 * @param flag
	 *            specify true to automatically send information such as invitations and reschedules to participants
	 */
	@Override
	public void setAutoSendNotices(final boolean flag);

	/**
	 * Sets a mask that controls the property display for a readRange operation.
	 * <p>
	 * Before calling {@link #readRange}, set this mask to designate the properties that you want returned. By default, all properties are
	 * returned. The following table specifies the bit values. Combine values by adding them.
	 * </p>
	 * <table cellpadding="4" cellspacing="0" summary="" rules="all" frame="border" border="1">
	 * <thead align="left">
	 * <tr valign="bottom">
	 * <th valign="bottom">Constant name</th>
	 * <th valign="bottom">Numerical value</th>
	 * </tr>
	 * </thead> <tbody >
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_ALARM</samp></td>
	 * <td valign="top">131072</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_APPTTYPE</samp></td>
	 * <td valign="top">2048</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_CATEGORY</samp></td>
	 * <td valign="top">1024</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_CLASS</samp></td>
	 * <td valign="top">16</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_DTEND</samp></td>
	 * <td valign="top">2</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_DTSTAMP</samp></td>
	 * <td valign="top">4</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_DTSTART</samp></td>
	 * <td valign="top">1</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_LOCATION</samp></td>
	 * <td valign="top">256</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_NOTESORGANIZER</samp></td>
	 * <td valign="top">32768</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_NOTESROOM</samp></td>
	 * <td valign="top">65536</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_NOTICETYPE</samp></td>
	 * <td valign="top">4096</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_ONLINE_URL</samp></td>
	 * <td valign="top">16384</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_PRIORITY</samp></td>
	 * <td valign="top">32</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_RECURRENCE_ID</samp></td>
	 * <td valign="top">64</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_SEQUENCE</samp></td>
	 * <td valign="top">128</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_STATUS</samp></td>
	 * <td valign="top">8192</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_SUMMARY</samp></td>
	 * <td valign="top">8</td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_TRANSP</samp></td>
	 * <td valign="top">512</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 *
	 * @param mask
	 *            Mask as a combination of values in the table above
	 */
	@Override
	public void setReadRangeMask1(final int mask);

	/**
	 * Sets a mask that controls the optional property display for a
	 * {@link #readRange(lotus.domino.DateTime, lotus.domino.DateTime, int, int)} operation.
	 * <p>
	 * Before calling <code>readRange</code>, set this mask to designate the properties that you want returned. By default, no properties
	 * are returned. The following table specifies the bit values. Combine values by adding them.
	 * </p>
	 * <table cellpadding="4" cellspacing="0" rules="all" frame="border" border="1">
	 * <thead align="left">
	 * <tr valign="bottom">
	 * <th valign="bottom" >Constant name</th>
	 * <th valign="bottom" >Numerical value</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr >
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_HASATTACH</samp></td>
	 * <td valign="top">1</td>
	 * </tr>
	 * <tr >
	 * <td valign="top"><samp>NotesCalendar.CS_READ_RANGE_MASK_UNID</samp></td>
	 * <td valign="top">2</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 *
	 * @param mask
	 *            Mask as a combination of values in the table above
	 */
	@Override
	public void setReadRangeMask2(final int mask);

	/**
	 * Sets the the return of <code>X-LOTUS</code> properties when reading a calendar entry or notice.
	 * <p>
	 * Before reading an entry or notice, you can set this option or accept the default <code>NotesCalendar.CS_XLOTUS_READ_DEFAULT</code>.
	 * </p>
	 * <table cellpadding="4" cellspacing="0" summary="" rules="all" frame="border" border="1">
	 * <thead align="left">
	 * <tr valign="bottom">
	 * <th valign="bottom" >Constant name</th>
	 * <th valign="bottom" >Numerical value</th>
	 * </tr>
	 * </thead> <tbody >
	 * <tr >
	 * <td valign="top" ><samp >NotesCalendar.CS_XLOTUS_READ_DEFAULT</samp> (0)</td>
	 * <td valign="top" >Generates non-proprietary <samp>X-LOTUS</samp> properties. This is the default if this property is not set prior to
	 * reading.</td>
	 * </tr>
	 * <tr >
	 * <td valign="top" ><samp >NotesCalendar.CS_XLOTUS_READ_NONE</samp> (1)</td>
	 * <td valign="top" >Omits all <samp >X-LOTUS</samp> properties.</td>
	 * </tr>
	 * <tr >
	 * <td valign="top" ><samp >NotesCalendar.CS_XLOTUS_READ_ALL</samp> (2)</td>
	 * <td valign="top" >Generates proprietary <samp >X-LOTUS</samp> properties. The caller must know how to update these.</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 *
	 * @param level
	 *            One of the values in the table above
	 */
	@Override
	public void setReadXLotusPropsOutputLevel(final int level);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.NotesCalendar#getApptunidFromUID(java.lang.String, boolean)
	 */
	@Override
	public String getApptunidFromUID(String arg0, boolean arg1);

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendar#getActAsDbOwner()
	 * @since Domino 9.0.1 FP8
	 */
	@Override
	public boolean getActAsDbOwner();

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendar#getAutoRemoveProcessedNotices()
	 * @since Domino 9.0.1 FP8
	 */
	@Override
	public boolean getAutoRemoveProcessedNotices();

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendar#getRecurrenceID(lotus.domino.DateTime)
	 * @since Domino 9.0.1 FP8
	 */
	@Override
	public String getRecurrenceID(lotus.domino.DateTime arg0);

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendar#setActAsDbOwner(boolean)
	 * @since Domino 9.0.1 FP8
	 */
	@Override
	public void setActAsDbOwner(boolean arg0);

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendar#setAutoRemoveProcessedNotices(boolean)
	 * @since Domino 9.0.1 FP8
	 */
	@Override
	public void setAutoRemoveProcessedNotices(boolean arg0);

}
