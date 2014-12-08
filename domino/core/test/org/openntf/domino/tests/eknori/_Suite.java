package org.openntf.domino.tests.eknori;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

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
		MassCopyDocumentsScratchTest.class, // copies doc by doc from OneMillion.nsf to target.nsf
		// MassCreateDocumentsScratchTest.class,// This test does nearly the same as CreateOneMillion
})
public class _Suite {
	static int DOC_COUNT = 2000;

}
