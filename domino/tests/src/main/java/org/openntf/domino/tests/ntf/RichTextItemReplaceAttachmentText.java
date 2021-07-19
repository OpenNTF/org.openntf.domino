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
package org.openntf.domino.tests.ntf;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.RichTextItem;
import org.openntf.domino.Session;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class RichTextItemReplaceAttachmentText extends org.openntf.domino.thread.AbstractDominoRunnable {

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(RichTextItemReplaceAttachmentText.class, TestRunnerUtil.NATIVE_SESSION, 1);
	}

	public RichTextItemReplaceAttachmentText() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		Session session = Factory.getSession(SessionType.NATIVE);
		Database db = session.getDatabase("", "sarmy/USSBulletins.nsf");
		Document doc = db.getDocumentByUNID("5F96214CFE92013D86258027007687FA");
		RichTextItem rtItem = (RichTextItem) doc.getFirstItem("BulletinText");
		rtItem.replaceAttachment("Feature Requests 2008.ods", "c:/data/Feature Requests 2008.ods");
		rtItem.compact();
		doc.save(true, true);
	}
}
