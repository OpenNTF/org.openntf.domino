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
package org.openntf.domino.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openntf.domino.tests.eknori.CreateOneMillion;
import org.openntf.domino.tests.eknori.MassCopyDocumentsScratchTest;

/**
 * To run this suite you may have to
 * <ul>
 * <li>adjust the ACL of billing.ntf if your server is not in the LocalDomainServers group</li>
 * </ul>
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ // The Test suite:
CreateOneMillion.class, // this test takse a quite long time and is currently limited to 100000 docs.  
		MassCopyDocumentsScratchTest.class // copies doc by doc from OneMillion.nsf to target.nsf
// MassCreateDocumentsScratchTest.class,// This test does nearly the same as CreateOneMillion
})
public class _Suite {
	public static int MASS_DOC_COUNT = 50000;

}
