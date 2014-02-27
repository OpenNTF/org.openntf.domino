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

import org.openntf.domino.Document;

/**
 * @author jgallagher
 * 
 */
enum DesignFactory {
	INSTANCE;

	private DesignFactory() {
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromDocument(final Document doc, final Class<? extends org.openntf.domino.design.DesignBase> T) {
		if (T == ACLNote.class) {
			return (T) (new ACLNote(doc));
		} else if (T == AboutDocument.class) {
			return (T) (new AboutDocument(doc));
		} else if (T == FileResource.class) {
			return (T) (new FileResource(doc));
		} else if (T == DesignForm.class) {
			return (T) (new DesignForm(doc));
		} else if (T == IconNote.class) {
			return (T) (new IconNote(doc));
		} else if (T == ReplicationFormula.class) {
			return (T) (new ReplicationFormula(doc));
		} else if (T == UsingDocument.class) {
			return (T) (new UsingDocument(doc));
		} else if (T == DesignView.class) {
			return (T) (new DesignView(doc));
		} else if (T == JavaResource.class) {
			return (T) (new JavaResource(doc));
		} else if (T == JarResource.class) {
			return (T) (new JarResource(doc));
		} else if (T == XPage.class) {
			return (T) (new XPage(doc));
		} else if (T == JavaScriptLibrary.class) {
			return (T) (new JavaScriptLibrary(doc));
		}
		return null;
	}
}
