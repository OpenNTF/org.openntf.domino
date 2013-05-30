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
package lotus.domino.local;

import lotus.domino.NotesException;

// TODO: Auto-generated Javadoc
//NTF - I should have known this wouldn't work at runtime. The compiler was okay with it, but the JVM said it was an IllegalAccess :)

/**
 * The Class BaseUtils.
 */
public class BaseUtils extends NotesBase {
	// private static Method getCppMethod;
	// private static Method isInvalidMethod;
	// static {
	// try {
	// getCppMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("GetCppObj", (Class<?>[]) null);
	// getCppMethod.setAccessible(true);
	// isInvalidMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("isInvalid", (Class<?>[]) null);
	// isInvalidMethod.setAccessible(true);
	// } catch (Exception e) {
	// DominoUtils.handleException(e);
	// }
	//
	// }

	/**
	 * Checks if is recycled.
	 * 
	 * @param base
	 *            the base
	 * @return true, if is recycled
	 */
	public static boolean isRecycled(lotus.domino.local.NotesBase base) {
		return base.isInvalid();
	}

	/**
	 * Gets the cpp id.
	 * 
	 * @param base
	 *            the base
	 * @return the cpp id
	 */
	public static long getCppId(lotus.domino.local.NotesBase base) {
		return base.GetCppObj();
	}

	/**
	 * Instantiates a new base utils.
	 * 
	 * @throws NotesException
	 *             the notes exception
	 */
	public BaseUtils() throws NotesException {

	}
}
