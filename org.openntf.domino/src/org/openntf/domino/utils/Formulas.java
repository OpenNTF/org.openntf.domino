package org.openntf.domino.utils;

import java.util.List;
import java.util.Vector;

import org.openntf.domino.exceptions.UnimplementedException;

//NTF This class should be marked deprecated until it has a real implementation!

@Deprecated
public enum Formulas {
	;

	/*
	 * ******BEGIN STRING METHODS******
	 */
	public static List<String> atLeft(final List<Object> strings, final int length) {
		List<String> result = new Vector<String>(strings.size());	//TODO change to ArrayList?
		for (Object o : strings) {
			if (o instanceof String) {
				result.add(atLeft((String) o, length));
			}
		}
		return result;
	}

	public static String atLeft(final String string, final int length) {
		throw new UnimplementedException("Not yet implemented");
	}

	public static List<String> atLeftBack(final List<Object> strings, final int length) {
		List<String> result = new Vector<String>(strings.size());	//TODO change to ArrayList?
		for (Object o : strings) {
			if (o instanceof String) {
				result.add(atLeftBack((String) o, length));
			}
		}
		return result;
	}

	public static String atLeftBack(final String string, final int length) {
		throw new UnimplementedException("Not yet implemented");
	}

	public static List<String> atRight(final List<Object> strings, final int length) {
		List<String> result = new Vector<String>(strings.size());	//TODO change to ArrayList?
		for (Object o : strings) {
			if (o instanceof String) {
				result.add(atRight((String) o, length));
			}
		}
		return result;
	}

	public static String atRight(final String string, final int length) {
		throw new UnimplementedException("Not yet implemented");
	}

	public static List<String> atRightBack(final List<Object> strings, final int length) {
		List<String> result = new Vector<String>(strings.size());	//TODO change to ArrayList?
		for (Object o : strings) {
			if (o instanceof String) {
				result.add(atRightBack((String) o, length));
			}
		}
		return result;
	}

	public static String atRightBack(final String string, final int length) {
		throw new UnimplementedException("Not yet implemented");
	}

	public static int atAbs(final int number) {
		return Math.abs(number);
	}

	public static double atAbs(final double number) {
		return Math.abs(number);
	}

	public static long atAbs(final long number) {
		return Math.abs(number);
	}

	public static float atAbs(final float number) {
		return Math.abs(number);
	}

	public static List<Number> atAbs(final List<Number> numbers) {
		List<Number> result = new Vector<Number>(numbers.size());
		for (Number number : numbers) {
			if (number instanceof Integer) {
				result.add(Math.abs((Integer) number));
			} else if (number instanceof Double) {
				result.add(Math.abs((Double) number));
			} else if (number instanceof Long) {
				result.add(Math.abs((Long) number));
			} else if (number instanceof Float) {
				result.add(Math.abs((Float) number));
			} else {
				//TODO what?
			}
		}
		return result;
	}

	public static Object atAbstract() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atAbstractSimple() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atAccessed() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atACos() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atAddToFolder() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atAdjust() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atAdminECLIsLocked() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atAll() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atAllChildren() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atAllDescendants() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atAscii() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atASin() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atATan() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atATan2() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atAttachmentLengths() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atAttachmentModifiedTimes() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atAttachmentNames() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atAttachments() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atAuthor() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atBegins() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atBrowserInfo() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atBusinessDays() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atCertificate() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atChar() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atCheckAlarms() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atClientType() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atCompare() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atConfigFile() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atContains() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atCos() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atCount() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atCreated() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDate() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDay() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDbColumn() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDbCommand() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDbExists() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDbLookup() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDbManager() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDbName() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDbTitle() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDeleteDocument() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDeleteField() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDialogBox() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDo() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDocChildren() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDocDescendants() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDocFields() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDocLength() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDocLevel() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDocLock() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDocMark() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDocNumber() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDocOmmittedLength() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDocParentNumber() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDocSiblings() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDocumentUniqueID() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDomain() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atDoWhile() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atElements() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atEnableAlarms() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atEnds() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atEnvironment() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atError() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atExp() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atExplode() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atFailure() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atFalse() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atFileDir() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atFloatEq() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atFontList() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atFor() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atFormLanguage() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atGetAddressBooks() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atGetCurrentTimeZone() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atGetDocField() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atGetField() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atGetFocusTable() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atGetHTTPHeader() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atGetIMContactListGroupNames() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atGetPortsList() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atGetProfileField() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atGetViewInfo() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atHardDeleteDocument() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atHashPassword() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atHour() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIf() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atImplode() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atInheritedDocumentUniqueID() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atInteger() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsAgentEnabled() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsAppInstalled() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsAvailable() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsCategory() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsDB2() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsDocBeingEdited() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsDocBeingLoaded() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsDocBeingMailed() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsDocBeingRecalculated() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsDocBeingSaved() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsDocTruncated() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsEmbeddedInsideWCT() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsError() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsExpandable() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsInCompositeApp() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsMember() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsModalHelp() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsNewDoc() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsNotMember() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsNull() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsNumber() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsResponseDoc() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsText() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsTime() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsUnavailable() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsUsingJavaElement() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsValid() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atIsVirtualizedDirectory() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atKeywords() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atLanguagePreference() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atLDAPServer() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atLength() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atLike() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atLn() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atLocale() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atLog() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atLowerCase() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atMailDbName() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atMailEncryptSavedPreference() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atMailEncryptSentPreference() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atMailSavePreference() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atMailSend() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atMailSignPreference() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atMatches() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atMax() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atMember() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atMiddle() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atMiddleBack() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atMin() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atMinute() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atModified() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atModulo() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atMonth() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atName() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atNameLookup() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atNarrow() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atNewLine() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atNo() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atNoteID() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atNothing() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atNow() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atOptimizeMailAddress() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atOrgDir() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atPassword() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atPasswordQuality() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atPi() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atPickList() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atPlatform() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atPolicyIsFieldLocked() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atPower() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atPrompt() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atProperCase() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atRandom() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atRefreshECL() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atRegQueryValue() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atRepeat() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atReplace() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atReplaceSubstring() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atReplicaID() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atResponses() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atReturn() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atRound() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atSecond() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atSelect() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atServerAccess() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atServerName() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atSet() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atSetDocField() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atSetEnvironment() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atSetField() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atSetHTTPHeader() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atSetProfileField() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atSetTargetFrame() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atSetViewInfo() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atShowParentPreview() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atSign() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atSin() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atSort() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atSoundex() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atSqrt() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atStatusBar() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atSubset() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atSuccess() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atSum() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atTan() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atTemplateVersion() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atText() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atTextToNumber() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atTextToTime() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atThisName() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atThisValue() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atTime() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atTimeMerge() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atTimeToTextInZone() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atToday() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atTomorrow() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atToNumber() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atToTime() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atTransform() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atTrim() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atTrue() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atUnavailable() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atUndeleteDocument() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atUnique() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atUpperCase() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atURLDecode() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atURLEncode() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atURLGetHeader() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atURLHistory() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atURLOpen() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atUrlQueryString() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atUserAccess() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atUserName() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atUserNameLanguage() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atUserNamesList() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atUserPrivileges() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atUserRoles() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atV3UserName() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atV4UserAccess() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atValidateInternetAddress() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atVerifyPassword() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atVersion() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atViewShowThisUnread() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atViewTitle() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atWebDbName() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atWeekday() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atWhichFolders() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atWhile() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atWide() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atWord() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atYear() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atYes() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atYesterday() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atZone() {
		throw new UnimplementedException("Not yet implemented");
	}

	public static Object atTimeZoneToText() {
		throw new UnimplementedException("Not yet implemented");
	}
}
