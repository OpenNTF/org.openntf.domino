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
package org.openntf.domino.tests.ntf;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.schema.impl.DatabaseSchema;
import org.openntf.domino.schema.impl.DocumentDefinition;
import org.openntf.domino.schema.impl.ItemDefinition;
import org.openntf.domino.schema.types.DateTimeType;
import org.openntf.domino.schema.types.IntegerType;
import org.openntf.domino.schema.types.StringType;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@SuppressWarnings("unused")
public class SchemaConstructionTest implements Runnable {
	public static void main(final String[] args) {
		DominoThread thread = new DominoThread(new SchemaConstructionTest(), "Schema Construction");
		thread.start();
	}

	public SchemaConstructionTest() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		Session session = Factory.getSession(SessionType.CURRENT);
		Database db = session.getDatabase("", "log.nsf");
		try {
			DatabaseSchema schema = new DatabaseSchema();
			DocumentDefinition eventDef = new DocumentDefinition();
			eventDef.setName("Events");

			ItemDefinition eventAddinNameDef = new ItemDefinition();
			eventAddinNameDef.setName("EventAddinName");
			eventAddinNameDef.setType(StringType.class);

			ItemDefinition eventListDef = new ItemDefinition();
			eventListDef.setName("EventList"); //text list
			eventListDef.setType(StringType.class);

			ItemDefinition eventSeverityDef = new ItemDefinition();
			eventSeverityDef.setName("EventSeverity"); //integer list
			eventSeverityDef.setType(IntegerType.class);

			ItemDefinition eventStatusDef = new ItemDefinition();
			eventStatusDef.setName("EventStatus"); //text list
			eventStatusDef.setType(StringType.class);

			ItemDefinition eventTimeDef = new ItemDefinition();
			eventTimeDef.setName("EventTime"); //Datetime list
			eventTimeDef.setType(DateTimeType.class);

			ItemDefinition eventTimeSizeDef = new ItemDefinition();
			eventTimeSizeDef.setName("EventTimeSize"); // integer list
			eventTimeSizeDef.setType(IntegerType.class);

			ItemDefinition eventTypeDef = new ItemDefinition();
			eventTypeDef.setName("EventType");	//integer list
			eventTypeDef.setType(IntegerType.class);

			ItemDefinition finishTimeDef = new ItemDefinition();
			finishTimeDef.setName("FinishTime");	//Datetime
			finishTimeDef.setType(DateTimeType.class);

			ItemDefinition serverDef = new ItemDefinition();
			serverDef.setName("Server");	//Name
			serverDef.setType(StringType.class);

			ItemDefinition startTimeDef = new ItemDefinition();
			startTimeDef.setName("StartTime");  //Datetime
			startTimeDef.setType(DateTimeType.class);

			ItemDefinition targetDbDef = new ItemDefinition();
			targetDbDef.setName("TargetDb");	//String list
			targetDbDef.setType(StringType.class);

			ItemDefinition targetExtraDef = new ItemDefinition();
			targetExtraDef.setName("TargetExtra");	//String list
			targetExtraDef.setType(StringType.class);

			ItemDefinition targetServerDef = new ItemDefinition();
			targetServerDef.setName("TargetServer");	//String list
			targetServerDef.setType(StringType.class);

			ItemDefinition targetUserDef = new ItemDefinition();
			targetUserDef.setName("TargetUser");	//String list
			targetUserDef.setType(StringType.class);

		} catch (Throwable t) {
			t.printStackTrace();
		}

	}
}
