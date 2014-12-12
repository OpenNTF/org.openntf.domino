package org.openntf.domino.tests.rpr;

import static org.junit.Assert.assertEquals;

import lotus.domino.NotesException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.Session;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@RunWith(DominoJUnitRunner.class)
public class NameTest {

	public enum NotesBug {
		O, OUs;
		boolean isPresent(final NotesBug... notesbug) {
			if (notesbug == null)
				return false;
			for (NotesBug bug : notesbug) {
				if (bug == this)
					return true;
			}
			return false;
		}
	}

	protected void compareNames(final String message, final lotus.domino.Name l, final org.openntf.domino.Name o,
			final NotesBug... notesbug) throws NotesException {

		assertEquals(message + ": getAddr821()", l.getAddr821(), o.getAddr821());
		assertEquals(message + ": getAddr822Comment1()", l.getAddr822Comment1(), o.getAddr822Comment1());
		assertEquals(message + ": getAddr822Comment2()", l.getAddr822Comment2(), o.getAddr822Comment2());
		assertEquals(message + ": getAddr822Comment3()", l.getAddr822Comment3(), o.getAddr822Comment3());

		assertEquals(message + ": getAddr822LocalPart()", l.getAddr822LocalPart(), o.getAddr822LocalPart());
		assertEquals(message + ": getAddr822Phrase()", l.getAddr822Phrase(), o.getAddr822Phrase());
		assertEquals(message + ": getADMD()", l.getADMD(), o.getADMD());

		assertEquals(message + ": getCommon()", l.getCommon(), o.getCommon());

		assertEquals(message + ": getCountry()", l.getCountry(), o.getCountry());
		assertEquals(message + ": getGeneration()", l.getGeneration(), o.getGeneration());
		assertEquals(message + ": getGiven()", l.getGiven(), o.getGiven());
		assertEquals(message + ": getInitials()", l.getInitials(), o.getInitials());
		assertEquals(message + ": isHierarchical()", l.isHierarchical(), o.isHierarchical());

		//assertEquals(message + ": getKeyword()", l.getKeyword(), o.getKeyword());
		assertEquals(message + ": getLanguage()", l.getLanguage(), o.getLanguage());

		if (!NotesBug.O.isPresent(notesbug))
			assertEquals(message + ": getOrganization()", l.getOrganization(), o.getOrganization());
		if (!NotesBug.OUs.isPresent(notesbug)) {
			assertEquals(message + ": getOrgUnit1()", l.getOrgUnit1(), o.getOrgUnit1());
			assertEquals(message + ": getOrgUnit2()", l.getOrgUnit2(), o.getOrgUnit2());

			assertEquals(message + ": getOrgUnit3()", l.getOrgUnit3(), o.getOrgUnit3());
			assertEquals(message + ": getOrgUnit4()", l.getOrgUnit4(), o.getOrgUnit4());
		}
		assertEquals(message + ": getPRMD()", l.getPRMD(), o.getPRMD());
		assertEquals(message + ": getSurname()", l.getSurname(), o.getSurname());

		//TODO: it seems that getCanonical + getAbbreviated returns the original string, if the name is invalid
		assertEquals(message + ": getCanonical()", l.getCanonical(), o.getCanonical());
		assertEquals(message + ": getAbbreviated()", l.getAbbreviated(), o.getAbbreviated());
	}

	protected org.openntf.domino.Name testName(final String what, final NotesBug... notesbug) throws NotesException {
		Session s = Factory.getSession(SessionType.CURRENT);
		lotus.domino.Session ls = s.getFactory().toLotus(s);
		org.openntf.domino.Name o = s.createName(what);
		lotus.domino.Name n = ls.createName(what);
		compareNames(n.getCanonical(), n, o, notesbug);
		return o;
	}

	@Test
	public void testWildCard() throws NotesException {
		testName("*/FOCONIS");
		testName("*/DE");
		testName("*/FOCONIS/DE");
		testName("*/DE/int");
		testName("*/OU=01/OU=int/O=FOCONIS");
		testName("*/I=1", NotesBug.O, NotesBug.OUs);
	}

	@Test
	public void testCanonical() throws NotesException {
		testName("CN=Roland Praml/OU=01/OU=int/O=FOCONIS");
		testName("CN=Roland Praml/O=FOCONIS");
		testName("CN=Roland Praml/C=DE");
		testName("CN=Roland Praml/C=FOCONIS");
		testName("CN=Roland Praml/OU=01/OU=int/OU=ger/OU=bay/O=FOCONIS");

		//it seems, if the name is incorrect, that Notes just returns the original string
		//testName("CN=Roland Praml/OU=01/OU=int/OU=ger/OU=bay/OU=dev/O=FOCONIS"); 

		testName("CN=Roland Praml/OU=01/OU=int/OU=ger/OU=bay/O=FOCONIS/C=DE");
		testName("C=DE/CN=Roland Praml/OU=01/OU=int/OU=ger/OU=bay/O=FOCONIS");

		//testName("CN=Roland Praml/P=pd");
		testName("CN=Roland Praml/H=8/I=9");
		testName("CN=Roland Praml/I=9/H=8");

		testName("CN=Roland Praml/A=1");
		testName("CN=Roland Praml/B=2");
		testName("CN=Roland Praml/C=3");
		testName("CN=Roland Praml/D=4");
		testName("CN=Roland Praml/E=5");
		testName("CN=Roland Praml/F=6");

		// Canonical is = G=7/CN=Roland Praml/OU=srv/O=FOCONIS, but notes.getOrganizaztion() returns nothing, we do
		testName("CN=Roland Praml/G=7", NotesBug.O, NotesBug.OUs);

		testName("CN=Roland Praml/H=8");
		testName("CN=Roland Praml/I=9", NotesBug.O, NotesBug.OUs);
		testName("CN=Roland Praml/J=10");
		testName("CN=Roland Praml/K=11");
		testName("CN=Roland Praml/L=12");
		testName("CN=Roland Praml/M=13");
		testName("CN=Roland Praml/N=14");
		testName("CN=Roland Praml/O=15");
		testName("CN=Roland Praml/P=16");
		testName("CN=Roland Praml/Q=17");
		testName("CN=Roland Praml/R=18");
		testName("CN=Roland Praml/S=19");
		testName("CN=Roland Praml/T=20");
		testName("CN=Roland Praml/U=21");
		testName("CN=Roland Praml/V=22");
		testName("CN=Roland Praml/W=23");
		testName("CN=Roland Praml/X=24");
		testName("CN=Roland Praml/Y=25");
		testName("CN=Roland Praml/Z=26");
		//seems to be a invalid name
		//testName("C=DE/A=Foc admd/Q=first generation/S=sure i have one/G=pram/I=RPr/CN=Roland Praml/OU=01/P=Private Domain/OU=int/OU=ger/O=FOCONIS");

	}

	//@Test
	public void test1() throws NotesException {
		testName("CN=Roland Praml");
		testName("Roland Praml");
	}

	@Test
	public void testCountryCode() throws NotesException {
		testName("CN=Roland Praml/C=DE");
		testName("Roland Praml/DE");  // TODO: Country code not yet recognized
	}

	//@Test
	public void test4() throws NotesException {
		testName("CN=Roland Praml/OU=int/O=FOCONIS");
		testName("CN=Roland Praml/OU=int");
	}
}
