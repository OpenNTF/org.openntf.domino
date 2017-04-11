/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     David Saff (saff@mit.edu) - bug 102632: [JUnit] Support for JUnit 4.
 *******************************************************************************/

package org.eclipse.jdt.internal.junit.runner;

/**
 * This is a copy of IElementComparer from org.eclipse.jface.viewers
 */
public /* package */ interface IElementComparer {

	/**
	 * Compares two elements for equality
	 *
	 * @param a the first element
	 * @param b the second element
	 * @return whether a is equal to b
	 */
	boolean equals(Object a, Object b);

	/**
	 * Returns the hash code for the given element.
	 *
	 * @param element the element
	 * @return the hash code for the given element
	 */
	int hashCode(Object element);
}
