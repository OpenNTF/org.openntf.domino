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

package org.openntf.domino.design.impl;

import org.openntf.domino.Database;
import org.openntf.domino.design.DesignBase;

/**
 * 
 * @author jgallagher
 * 
 */
public class ProtectedDatabaseDesign extends DatabaseDesign {

	public ProtectedDatabaseDesign(final Database database) {
		super(database);
		// TODO Auto-generated constructor stub
	}

	@Override
	public DesignCollection<DesignBase> searchDesignElements(final String formula) {
		return new org.openntf.domino.design.impl.DesignCollection<DesignBase>(null, DesignBase.class);
	}

	@Override
	public <T extends DesignBase> DesignCollection<T> searchDesignElements(final Class<T> type, final String search) {
		return new org.openntf.domino.design.impl.DesignCollection<T>(null, type);
	}

	@Override
	public <T extends DesignBase> DesignCollection<T> getDesignElements(final Class<T> type, final String name) {
		return new org.openntf.domino.design.impl.DesignCollection<T>(null, type);
	}

	@Override
	public <T extends DesignBase> T getDesignElement(final Class<T> type, final String name) {
		return null;
	}
}
