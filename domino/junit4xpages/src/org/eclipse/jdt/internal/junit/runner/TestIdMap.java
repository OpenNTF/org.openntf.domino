/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     David Saff (saff@mit.edu) - initial API and implementation
 *             (bug 102632: [JUnit] Support for JUnit 4.)
 *******************************************************************************/

package org.eclipse.jdt.internal.junit.runner;

import java.util.HashMap;

public class TestIdMap {
	private HashMap fIdMap= new HashMap();

	private int fNextId= 1;

	public String getTestId(ITestIdentifier identifier) {
		Object id= fIdMap.get(identifier);
		if (id != null)
			return (String) id;
		String newId= Integer.toString(fNextId++);
		fIdMap.put(identifier, newId);
		return newId;
	}

	public String getTestId(ITestReference ref) {
		return getTestId(ref.getIdentifier());
	}
}
