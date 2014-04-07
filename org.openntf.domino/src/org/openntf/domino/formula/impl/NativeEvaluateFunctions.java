package org.openntf.domino.formula.impl;

import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.ValueHolder;

public enum NativeEvaluateFunctions {
	;
	/*============================================================================*/
	/*
	 * Part I: Text functions
	 */
	/*============================================================================*/
	/*----------------------------------------------------------------------------*/
	/*
	 * @Ascii
	 */
	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate({ "@Ascii", "Possible at all?" })
	@ParamCount({ 1, 2 })
	public static ValueHolder atAscii(final FormulaContext ctx, final ValueHolder params[]) {
		return (params.length == 2) ? ctx.evaluateNative("@Ascii(p1;p2)", params[0], params[1]) : ctx.evaluateNative("@Ascii(p1)",
				params[0]);
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
	 * Part II: Mail, Name, Certificate
	 */
	/*============================================================================*/
	/*----------------------------------------------------------------------------*/
	/*
	 * @Certificate
	 */
	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@Certificate")
	@ParamCount(2)
	public static ValueHolder atCertificate(final FormulaContext ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@Certificate(p1;p2)", params[0], params[1]);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @LDAPServer, @Name, @NameLookup
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
	public static ValueHolder atName(final FormulaContext ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@Name(p1;p2)", params[0], params[1]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@NameLookup")
	@ParamCount(3)
	public static ValueHolder atNameLookup(final FormulaContext ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@NameLookup(p1;p2;p3)", params[0], params[1], params[2]);
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
	 * Part III: Others
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
		if (params.length == 1) {
			return ctx.evaluateNative("@Environment(p1)", params[0]);
		} else {
			return ctx.evaluateNative("@Environment(p1;p2)", params[0], params[1]);
		}
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@LanguagePreference")
	@ParamCount(1)
	public static ValueHolder atLanguagePreference(final FormulaContext ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@LanguagePreference(p1)", params[0]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@Locale")
	@ParamCount({ 1, 2 })
	public static ValueHolder atLocale(final FormulaContext ctx, final ValueHolder[] params) {
		if (params.length == 1) {
			return ctx.evaluateNative("@Locale(p1)", params[0]);
		} else {
			return ctx.evaluateNative("@Locale(p1;p2)", params[0], params[1]);
		}
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
	public static ValueHolder atServerAccess(final FormulaContext ctx, final ValueHolder[] params) {
		if (params.length == 2) {
			return ctx.evaluateNative("@ServerAccess(p1;p2)", params[0], params[1]);
		} else {
			return ctx.evaluateNative("@ServerAccess(p1;p2;p3)", params[0], params[1], params[2]);
		}
	}
	/*----------------------------------------------------------------------------*/
}
