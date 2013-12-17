/*
 * © Copyright IBM Corp. 2012-2013
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

package com.ibm.commons.util;

/**
 * Thread lock used by the ThreadLock manager.
 * @ibm-api
 */
public interface ThreadLock {
	
	/**
	 * Acquire a lock.
	 * @ibm-api
	 */
	public boolean acquire() throws InterruptedException;

	/**
	 * Try to acquire a lock.
	 * @ibm-api
	 */
	public boolean tryAcquire() throws InterruptedException;
	
	/**
	 * Check if the lock is already in use.
	 * @ibm-api
	 */
	public boolean isLocked();

	/**
	 * Release the lock.
	 * @ibm-api
	 */
	public void release();
}
