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
package org.openntf.domino.contributor;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.openntf.domino.Database;
import org.openntf.domino.Document;

/**
 * Represents a contributed service that can provide access to time-based backups of documents.
 *
 * @author Jesse Gallagher
 * @since 10.0
 */
public interface DocumentBackupContributor {
	/**
	 * Creates a document in a "sidecar" database representing the state of the original document at the provided point in time, without
	 * modifying the origin DB.
	 *
	 * @param database
	 *            the Database housing the document whose version to restore
	 * @param universalId
	 *            the UNID of the document to restore
	 * @param revisionDate
	 *            the date to find a revision for
	 * @return the sidecar document if the document existed in the proxy DB, or an empty optional otherwise
	 */
	Optional<Document> createSidecarDocument(Database database, String universalId, Date date);

	/**
	 * @return a {@link List} of {@link Dates} for the document's revisions, or an empty optional if the document does not have a proxy
	 */
	Optional<List<Date>> getRevisionDates(Database database, String universalId);
}
