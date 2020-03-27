/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
package org.openntf.domino.nsfdata.structs;

import java.util.EnumSet;
import java.util.Set;

public enum DesignFlagExt {
	FILE_DEPLOYABLE('D'), DONTREFRESH_ON_REDEPLOY('R'), NOTE_HAS_DAVPROPERTIES('P'), NOTE_HAS_MSPROPERTIES('M'), DAVLOCKNULL('N'), WEBDAV_HIDDEN(
			'H'), DAVCOMPUTEFORM('C'), DAVATTACH('A'), DAVGMTNORMAL('Z'), JAVADEBUG('D'), PROFILE('F'), JAVA_ERROR('E'), WEBSERVICELIB('W'), WEBCONTENTFILE(
			'W'), REBUILD_VIEW('X');

	private final char character_;

	private DesignFlagExt(final char character) {
		character_ = character;
	}

	public char getCharacter() {
		return character_;
	}

	public static Set<DesignFlagExt> valuesOf(final char character) {
		Set<DesignFlagExt> result = EnumSet.noneOf(DesignFlagExt.class);
		for (DesignFlagExt flag : values()) {
			if (character == flag.getCharacter()) {
				result.add(flag);
			}
		}
		if (result.isEmpty()) {
			throw new IllegalArgumentException("Unknown flag character '" + character + "'");
		} else {
			return result;
		}
	}

	public static Set<DesignFlagExt> valuesOf(final String pattern) {
		Set<DesignFlagExt> result = EnumSet.noneOf(DesignFlagExt.class);
		for (int i = 0; i < pattern.length(); i++) {
			result.addAll(valuesOf(pattern.charAt(i)));
		}
		return result;
	}
}
