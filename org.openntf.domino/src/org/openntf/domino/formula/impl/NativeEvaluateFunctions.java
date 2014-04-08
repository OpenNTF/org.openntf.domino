package org.openntf.domino.formula.impl;

import org.openntf.domino.formula.EvaluateException;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.ValueHolder;
import org.openntf.domino.formula.ValueHolder.DataType;

import com.ibm.commons.util.FastStringBuffer;

public enum NativeEvaluateFunctions {
	;
	/*============================================================================*/
	/*
	 * Text functions
	 */
	/*============================================================================*/
	/*----------------------------------------------------------------------------*/
	/*
	 * @Ascii
	 */
	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate({ "@Ascii", "Possible at all?" })
	@ParamCount({ 1, 2 })
	public static ValueHolder atAscii(final FormulaContext ctx, final ValueHolder params[]) throws EvaluateException {
		return (params.length == 2) ? ctx.evaluateNative("@Ascii(p1;" + params[1].quoteValue() + ")", params[0]) : // Force NL
				ctx.evaluateNative("@Ascii(p1)", params[0]);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Explode for DateRange
	 */
	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@Explode(DateRange)")
	static ValueHolder explodeDateRanges(final FormulaContext ctx, final ValueHolder what) {
		return ctx.evaluateNative("@Explode(p1)", what);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Like, @Matches
	 */
	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate({ "@Like", "Use @MatchesRegExp instead" })
	@ParamCount({ 2, 3 })
	public static ValueHolder atLike(final FormulaContext ctx, final ValueHolder[] params) {
		if (params.length == 2) {
			return ctx.evaluateNative("@Like(p1;p2)", params[0], params[1]);
		} else {
			return ctx.evaluateNative("@Like(p1;p2;p3)", params[0], params[1], params[2]);
		}
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate({ "@Matches", "Use @MatchesRegExp instead" })
	@ParamCount(2)
	public static ValueHolder atMatches(final FormulaContext ctx, final ValueHolder[] params) {
		return ctx.evaluateNative("@Matches(p1;p2)", params[0], params[1]);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Unique
	 */
	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@Unique(<no parameters>)")
	static ValueHolder unique(final FormulaContext ctx) {
		return ctx.evaluateNative("@Unique");
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Password, @HashPassword, @VerifyPassword, @PasswordQuality
	 */
	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@Password")
	@ParamCount(1)
	public static ValueHolder atPassword(final FormulaContext ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@Password(p1)", params[0]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@HashPassword")
	@ParamCount(1)
	public static ValueHolder atHashPassword(final FormulaContext ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@HashPassword(p1)", params[0]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@VerifyPassword")
	@ParamCount(2)
	public static ValueHolder atVerifyPassword(final FormulaContext ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@VerifyPassword(p1;p2)", params[0], params[1]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@PasswordQuality")
	@ParamCount(1)
	public static ValueHolder atPasswordQuality(final FormulaContext ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@PasswordQuality(p1)", params[0]);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Wide, @Narrow
	 */
	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@Wide")
	@ParamCount(1)
	public static ValueHolder atWide(final FormulaContext ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@Wide(p1)", params[0]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@Narrow")
	@ParamCount(1)
	public static ValueHolder atNarrow(final FormulaContext ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@Narrow(p1)", params[0]);
	}

	/*============================================================================*/
	/*
	 * Mail, Name, Certificate
	 */
	/*============================================================================*/
	/*----------------------------------------------------------------------------*/
	/*
	 * @Certificate
	 */
	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@Certificate")
	@ParamCount(2)
	public static ValueHolder atCertificate(final FormulaContext ctx, final ValueHolder params[]) throws EvaluateException {
		return ctx.evaluateNative("@Certificate(" + params[0].quoteValue() + ";p1)", params[1]);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @LDAPServer, @Name, @NameLookup, @UserName
	 */
	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@LDAPServer")
	@ParamCount(0)
	public static ValueHolder atLDAPServer(final FormulaContext ctx) {
		return ctx.evaluateNative("@LDAPServer");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate({ "@Name", "openNTF-Name doesn't yet look stable" })
	@ParamCount(2)
	public static ValueHolder atName(final FormulaContext ctx, final ValueHolder params[]) throws EvaluateException {
		return ctx.evaluateNative("@Name(" + params[0].quoteValue() + ";p1)", params[1]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@NameLookup")
	@ParamCount(3)
	public static ValueHolder atNameLookup(final FormulaContext ctx, final ValueHolder params[]) throws EvaluateException {
		return ctx.evaluateNative("@NameLookup(" + params[0].quoteValue() + ";p1;p2)", params[1], params[2]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@UserName")
	@ParamCount({ 0, 1 })
	public static ValueHolder atUserName(final FormulaContext ctx, final ValueHolder params[]) {
		return (params.length == 0) ? ctx.evaluateNative("@UserName") : ctx.evaluateNative("@UserName(p1)", params[0]);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @MailDbName, @MailEncryptSavedPreference, @MailEncryptSentPreference,
	 * @MailSavePreference, @MailSignPreference, @OptimizeMailAddress
	 */
	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@MailDbName")
	@ParamCount(0)
	public static ValueHolder atMailDbName(final FormulaContext ctx) {
		return ctx.evaluateNative("@MailDbName");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@MailEncryptSavedPreference")
	@ParamCount(0)
	public static ValueHolder atMailEncryptSavedPreference(final FormulaContext ctx) {
		return ctx.evaluateNative("@MailEncryptSavedPreference");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@MailEncryptSentPreference")
	@ParamCount(0)
	public static ValueHolder atMailEncryptSentPreference(final FormulaContext ctx) {
		return ctx.evaluateNative("@MailEncryptSentPreference");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@MailSavePreference")
	@ParamCount(0)
	public static ValueHolder atMailSavePreference(final FormulaContext ctx) {
		return ctx.evaluateNative("@MailSavePreference");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@MailSignPreference")
	@ParamCount(0)
	public static ValueHolder atMailSignPreference(final FormulaContext ctx) {
		return ctx.evaluateNative("@MailSignPreference");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@OptimizeMailAddress")
	@ParamCount(1)
	public static ValueHolder atOptimizeMailAddress(final FormulaContext ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@OptimizeMailAddress(p1)", params[0]);
	}

	/*============================================================================*/
	/*
	 * Miscellanea
	 */
	/*============================================================================*/
	@NeedsNativeEvaluate("@ConfigFile")
	@ParamCount(0)
	public static ValueHolder atConfigFile(final FormulaContext ctx) {
		return ctx.evaluateNative("@ConfigFile");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@Domain")
	@ParamCount(0)
	public static ValueHolder atDomain(final FormulaContext ctx) {
		return ctx.evaluateNative("@Domain");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@Environment")
	@ParamCount({ 1, 2 })
	public static ValueHolder atEnvironment(final FormulaContext ctx, final ValueHolder[] params) {
		if (params.length == 1)
			return ctx.evaluateNative("@Environment(p1)", params[0]);
		return ctx.evaluateNative("@Environment(p1;p2)", params[0], params[1]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@LanguagePreference")
	@ParamCount(1)
	public static ValueHolder atLanguagePreference(final FormulaContext ctx, final ValueHolder params[]) throws EvaluateException {
		return ctx.evaluateNative("@LanguagePreference(" + params[0].quoteValue() + ")");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@Locale")
	@ParamCount({ 1, 2 })
	public static ValueHolder atLocale(final FormulaContext ctx, final ValueHolder[] params) throws EvaluateException {
		if (params.length == 1)
			return ctx.evaluateNative("@Locale(" + params[0].quoteValue() + ")");
		return ctx.evaluateNative("@Locale(" + params[0].quoteValue() + ";p1)", params[1]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@OrgDir")
	@ParamCount(0)
	public static ValueHolder atOrgDir(final FormulaContext ctx) {
		return ctx.evaluateNative("@OrgDir");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@PolicyIsFieldLocked")
	@ParamCount(1)
	public static ValueHolder atPolicyIsFieldLocked(final FormulaContext ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@PolicyIsFieldLocked(p1)", params[0]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@RegQueryValue")
	@ParamCount(3)
	public static ValueHolder atRegQueryValue(final FormulaContext ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@RegQueryValue(p1;p2;p3)", params[0], params[1], params[2]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@ServerAccess")
	@ParamCount({ 2, 3 })
	public static ValueHolder atServerAccess(final FormulaContext ctx, final ValueHolder[] params) throws EvaluateException {
		if (params.length == 2) {
			return ctx.evaluateNative("@ServerAccess(" + params[0].quoteValue() + ";p1)", params[1]);
		} else {
			return ctx.evaluateNative("@ServerAccess(" + params[0].quoteValue() + ";p1;p2)", params[1], params[2]);
		}
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@AdminECLIsLocked")
	@ParamCount(0)
	public static ValueHolder atAdminECLIsLocked(final FormulaContext ctx) {
		return ctx.evaluateNative("@AdminECLIsLocked");
	}

	/*============================================================================*/
	/*
	 * Further
	 */
	/*============================================================================*/
	@NeedsNativeEvaluate("@Abstract")
	@ParamCount(4)
	public static ValueHolder atAbstract(final FormulaContext ctx, final ValueHolder[] params) throws EvaluateException {
		return ctx.evaluateNative("@Abstract(" + params[0].quoteValue() + ";" + params[1].quoteValue() + ";" + params[2].quoteValue() + ";"
				+ params[3].quoteValue() + ")");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@AbstractSimple")
	@ParamCount(1)
	public static ValueHolder atAbstractSimple(final FormulaContext ctx, final ValueHolder[] params) throws EvaluateException {
		return ctx.evaluateNative("@AbstractSimple(" + params[0].quoteValue() + ")");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@BrowserInfo")
	@ParamCount(1)
	public static ValueHolder atBrowserInfo(final FormulaContext ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@BrowserInfo(p1)", params[0]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@CheckFormulaSyntax")
	@ParamCount(1)
	public static ValueHolder atCheckFormulaSyntax(final FormulaContext ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@CheckFormulaSyntax(p1)", params[0]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@ClientType")
	@ParamCount(0)
	public static ValueHolder atClientType(final FormulaContext ctx) {
		return ctx.evaluateNative("@ClientType");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DB2Schema")
	@ParamCount(2)
	public static ValueHolder atDB2Schema(final FormulaContext ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@DB2Schema(p1;p2)", params[0], params[1]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DbColumn")
	@ParamCount(4)
	public static ValueHolder atDbColumn(final FormulaContext ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@DbColumn(p1;p2;p3;p4)", params[0], params[1], params[2], params[3]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DbCommand")
	@ParamCount({ 2, 5 })
	public static ValueHolder atDbCommand(final FormulaContext ctx, final ValueHolder params[]) {
		if (params.length == 2)
			return ctx.evaluateNative("@DbCommand(p1;p2)", params[0], params[1]);
		if (params.length == 3)
			return ctx.evaluateNative("@DbCommand(p1;p2;p3)", params[0], params[1], params[2]);
		if (params.length == 4)
			return ctx.evaluateNative("@DbCommand(p1;p2;p3;p4)", params[0], params[1], params[2], params[3]);
		return ctx.evaluateNative("@DbCommand(p1;p2;p3;p4;p5)", params[0], params[1], params[2], params[3], params[4]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DbExists")
	@ParamCount({ 1, 2 })
	public static ValueHolder atDbExists(final FormulaContext ctx, final ValueHolder[] params) {
		if (params.length == 1)
			return ctx.evaluateNative("@DbExists(p1)", params[0]);
		return ctx.evaluateNative("@DbExists(p1;p2)", params[0], params[1]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DbLookup")
	@ParamCount({ 5, 9 })
	public static ValueHolder atDbLookup(final FormulaContext ctx, final ValueHolder params[]) throws EvaluateException {
		if (params.length == 5)
			return ctx.evaluateNative("@DbLookup(p1;p2;p3;p4;p5)", params[0], params[1], params[2], params[3], params[4]);
		if (params.length == 6) {
			if (params[5].dataType == DataType.KEYWORD_STRING)
				return ctx.evaluateNative("@DbLookup(p1;p2;p3;p4;p5;" + params[5].quoteValue() + ")", params[0], params[1], params[2],
						params[3], params[4]);
			else
				return ctx.evaluateNative("@DbLookup(p1;p2;p3;p4;p5;p6)", params[0], params[1], params[2], params[3], params[4], params[5]);
		}
		if (params.length == 7)
			return ctx.evaluateNative("@DbLookup(p1;p2;p3;p4;p5;p6;" + params[6].quoteValue() + ")", params[0], params[1], params[2],
					params[3], params[4], params[5]);
		if (params.length == 8)
			return ctx.evaluateNative("@DbLookup(p1;p2;p3;p4;p5;p6;p7;p8)", params[0], params[1], params[2], params[3], params[4],
					params[5], params[6], params[7]);
		return ctx.evaluateNative("@DbLookup(p1;p2;p3;p4;p5;p6;p7;p8;p9)", params[0], params[1], params[2], params[3], params[4],
				params[5], params[6], params[7], params[8]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DbManager")
	@ParamCount(0)
	public static ValueHolder atDbManager(final FormulaContext ctx) {
		return ctx.evaluateNative("@DbManager");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DbName")
	@ParamCount(0)
	public static ValueHolder atDbName(final FormulaContext ctx) {
		return ctx.evaluateNative("@DbName");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DbTitle")
	@ParamCount(0)
	public static ValueHolder atDbTitle(final FormulaContext ctx) {
		return ctx.evaluateNative("@DbTitle");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DeleteDocument")
	@ParamCount(0)
	public static ValueHolder atDeleteDocument(final FormulaContext ctx) {
		return ctx.evaluateNative("@DeleteDocument");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DocChildren")
	@ParamCount({ 0, 3 })
	public static ValueHolder atDocChildren(final FormulaContext ctx, final ValueHolder[] params) throws EvaluateException {
		FastStringBuffer fsb = new FastStringBuffer(256);
		fsb.append("@DocChildren");
		if (params.length > 0) {
			fsb.append('(');
			fsb.append(params[0].quoteValue());
		}
		if (params.length > 1) {
			fsb.append(';');
			fsb.append(params[1].quoteValue());
		}
		if (params.length > 2) {
			fsb.append(';');
			fsb.append(params[2].quoteValue());
		}
		if (params.length > 0)
			fsb.append(')');
		return ctx.evaluateNative(fsb.toString());
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DocDescendants")
	@ParamCount({ 0, 3 })
	public static ValueHolder atDocDescendants(final FormulaContext ctx, final ValueHolder[] params) throws EvaluateException {
		FastStringBuffer fsb = new FastStringBuffer(256);
		fsb.append("@DocDescendants");
		if (params.length > 0) {
			fsb.append('(');
			fsb.append(params[0].quoteValue());
		}
		if (params.length > 1) {
			fsb.append(';');
			fsb.append(params[1].quoteValue());
		}
		if (params.length > 2) {
			fsb.append(';');
			fsb.append(params[2].quoteValue());
		}
		if (params.length > 0)
			fsb.append(')');
		return ctx.evaluateNative(fsb.toString());
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DocLevel")
	@ParamCount(0)
	public static ValueHolder atDocLevel(final FormulaContext ctx) {
		return ctx.evaluateNative("@DocLevel");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DocMark")
	@ParamCount(1)
	public static ValueHolder atDocMark(final FormulaContext ctx, final ValueHolder[] params) throws EvaluateException {
		return ctx.evaluateNative("@DocMark(" + params[0].quoteValue() + ")");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DocNumber")
	@ParamCount({ 0, 1 })
	public static ValueHolder atDocNumber(final FormulaContext ctx, final ValueHolder[] params) throws EvaluateException {
		if (params.length == 0)
			return ctx.evaluateNative("@DocNumber");
		return ctx.evaluateNative("@DocNumber(" + params[0].quoteValue() + ")");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DocOmittedLength")
	@ParamCount(0)
	public static ValueHolder atDocOmittedLength(final FormulaContext ctx) {
		return ctx.evaluateNative("@DocOmittedLength");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DocParentNumber")
	@ParamCount({ 0, 1 })
	public static ValueHolder atDocParentNumber(final FormulaContext ctx, final ValueHolder[] params) throws EvaluateException {
		if (params.length == 0)
			return ctx.evaluateNative("@DocParentNumber");
		return ctx.evaluateNative("@DocParentNumber(" + params[0].quoteValue() + ")");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DocSiblings")
	@ParamCount(0)
	public static ValueHolder atDocSiblings(final FormulaContext ctx) {
		return ctx.evaluateNative("@DocSiblings");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@EditECL")
	@ParamCount(2)
	public static ValueHolder atEditECL(final FormulaContext ctx, final ValueHolder[] params) {
		return ctx.evaluateNative("@EditECL(p1;p2)", params[0], params[1]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@EditUserECL")
	@ParamCount(0)
	public static ValueHolder atEditUserECL(final FormulaContext ctx) {
		return ctx.evaluateNative("@EditUserECL");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@FormLanguage")
	@ParamCount(0)
	public static ValueHolder atFormLanguage(final FormulaContext ctx) {
		return ctx.evaluateNative("@FormLanguage");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@GetAddressBooks")
	@ParamCount(1)
	public static ValueHolder atGetAddressBooks(final FormulaContext ctx, final ValueHolder[] params) throws EvaluateException {
		return ctx.evaluateNative("@GetAddressBooks(" + params[0].quoteValue() + ")");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@GetCurrentTimeZone")
	@ParamCount(0)
	public static ValueHolder atGetCurrentTimeZone(final FormulaContext ctx) {
		return ctx.evaluateNative("@GetCurrentTimeZone");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@GetFocusTable")
	@ParamCount(1)
	public static ValueHolder atGetFocusTable(final FormulaContext ctx, final ValueHolder[] params) throws EvaluateException {
		return ctx.evaluateNative("@GetFocusTable(" + params[0].quoteValue() + ")");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@GetHTTPHeader")
	@ParamCount(1)
	public static ValueHolder atGetHTTPHeader(final FormulaContext ctx, final ValueHolder[] params) {
		return ctx.evaluateNative("@GetHTTPHeader(p1)", params[0]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@GetPortsList")
	@ParamCount(1)
	public static ValueHolder atGetPortsList(final FormulaContext ctx, final ValueHolder[] params) throws EvaluateException {
		return ctx.evaluateNative("@GetPortsList(" + params[0].quoteValue() + ")");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@GetProfileField")
	@ParamCount({ 2, 3 })
	public static ValueHolder atGetProfileField(final FormulaContext ctx, final ValueHolder params[]) {
		if (params.length == 2)
			return ctx.evaluateNative("@GetProfileField(p1;p2)", params[0], params[1]);
		return ctx.evaluateNative("@GetProfileField(p1;p2;p3)", params[0], params[1], params[2]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@HardDeleteDocument")
	@ParamCount(0)
	public static ValueHolder atHardDeleteDocument(final FormulaContext ctx) {
		return ctx.evaluateNative("@HardDeleteDocument");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsAgentEnabled")
	@ParamCount(1)
	public static ValueHolder atIsAgentEnabled(final FormulaContext ctx, final ValueHolder[] params) {
		return ctx.evaluateNative("@IsAgentEnabled(p1)", params[0]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsAppInstalled")
	@ParamCount(1)
	public static ValueHolder atIsAppInstalled(final FormulaContext ctx, final ValueHolder[] params) {
		return ctx.evaluateNative("@IsAppInstalled(p1)", params[0]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsCategory")
	@ParamCount({ 0, 2 })
	public static ValueHolder atIsCategory(final FormulaContext ctx, final ValueHolder[] params) {
		if (params.length == 0)
			return ctx.evaluateNative("@IsCategory");
		if (params.length == 1)
			return ctx.evaluateNative("@IsCategory(p1)", params[0]);
		return ctx.evaluateNative("@IsCategory(p1;p2)", params[0], params[1]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsDocBeingEdited")
	@ParamCount(0)
	public static ValueHolder atIsDocBeingEdited(final FormulaContext ctx) {
		return ctx.evaluateNative("@IsDocBeingEdited");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsDocBeingLoaded")
	@ParamCount(0)
	public static ValueHolder atIsDocBeingLoaded(final FormulaContext ctx) {
		return ctx.evaluateNative("@IsDocBeingLoaded");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsDocBeingMailed")
	@ParamCount(0)
	public static ValueHolder atIsDocBeingMailed(final FormulaContext ctx) {
		return ctx.evaluateNative("@IsDocBeingMailed");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsDocBeingRecalculated")
	@ParamCount(0)
	public static ValueHolder atIsDocBeingRecalculated(final FormulaContext ctx) {
		return ctx.evaluateNative("@IsDocBeingRecalculated");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsDocBeingSaved")
	@ParamCount(0)
	public static ValueHolder atIsDocBeingSaved(final FormulaContext ctx) {
		return ctx.evaluateNative("@IsDocBeingSaved");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsDocTruncated")
	@ParamCount(0)
	public static ValueHolder atIsDocTruncated(final FormulaContext ctx) {
		return ctx.evaluateNative("@IsDocTruncated");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsEmbeddedInsideWCT")
	@ParamCount(0)
	public static ValueHolder atIsEmbeddedInsideWCT(final FormulaContext ctx) {
		return ctx.evaluateNative("@IsEmbeddedInsideWCT");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsExpandable")
	@ParamCount({ 0, 2 })
	public static ValueHolder atIsExpandable(final FormulaContext ctx, final ValueHolder[] params) {
		if (params.length == 0)
			return ctx.evaluateNative("@IsExpandable");
		if (params.length == 1)
			return ctx.evaluateNative("@IsExpandable(p1)", params[0]);
		return ctx.evaluateNative("@IsExpandable(p1;p2)", params[0], params[1]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsModalHelp")
	@ParamCount(0)
	public static ValueHolder atIsModalHelp(final FormulaContext ctx) {
		return ctx.evaluateNative("@IsModalHelp");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsUsingJavaElement")
	@ParamCount(0)
	public static ValueHolder atIsUsingJavaElement(final FormulaContext ctx) {
		return ctx.evaluateNative("@IsUsingJavaElement");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsVirtualizedDirectory")
	@ParamCount(0)
	public static ValueHolder atIsVirtualizedDirectory(final FormulaContext ctx) {
		return ctx.evaluateNative("@IsVirtualizedDirectory");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@LaunchApp")
	@ParamCount(1)
	public static ValueHolder atLaunchApp(final FormulaContext ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@LaunchApp(p1)", params[0]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@Platform")
	@ParamCount({ 0, 1 })
	public static ValueHolder atPlatform(final FormulaContext ctx, final ValueHolder[] params) throws EvaluateException {
		if (params.length == 0)
			return ctx.evaluateNative("@Platform");
		return ctx.evaluateNative("@IsExpandable(" + params[0].quoteValue() + ")");
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@RefreshECL")
	@ParamCount(2)
	public static ValueHolder atRefreshECL(final FormulaContext ctx, final ValueHolder[] params) {
		return ctx.evaluateNative("@RefreshECL(p1;p2)", params[0], params[1]);
	}
	/*----------------------------------------------------------------------------*/
}
