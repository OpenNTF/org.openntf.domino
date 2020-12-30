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
package org.openntf.domino.formula;

import java.util.Map;

import org.openntf.formula.EvaluateException;
import org.openntf.formula.Function;
import org.openntf.formula.FunctionFactory;
import org.openntf.formula.FunctionSet;
import org.openntf.formula.ValueHolder;
import org.openntf.formula.ValueHolder.DataType;
import org.openntf.formula.annotation.ParamCount;
import org.openntf.formula.function.TextFunctions;

public enum NativeEvaluateFunctions {
	;

	public static class Functions extends FunctionSet {
		private static final Map<String, Function> functionSet = FunctionFactory.getFunctions(NativeEvaluateFunctions.class);

		@Override
		public Map<String, Function> getFunctions() {
			return functionSet;
		}

		@Override
		public int getPriority() {
			// TODO Auto-generated method stub
			return 40; //  @Unique is overwritten!
		}

	}

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
	public static ValueHolder atAscii(final FormulaContextNotes ctx, final ValueHolder params[]) throws EvaluateException {
		return (params.length == 2) ? ctx.evaluateNative("@Ascii(p1;" + params[1].quoteValue() + ")", params[0]) : // Force NL //$NON-NLS-1$ //$NON-NLS-2$
				ctx.evaluateNative("@Ascii(p1)", params[0]); //$NON-NLS-1$
	}

	// TODO RPR: move to openntf	@NeedsNativeEvaluate("@Explode(DateRange)")
	@ParamCount({ 1, 4 })
	public static ValueHolder atExplode(final FormulaContextNotes ctx, final ValueHolder[] params) {
		ValueHolder vh = params[0];
		if (vh.dataType != DataType.STRING)
			return ctx.evaluateNative("@Explode(p1)", vh); //$NON-NLS-1$
		return TextFunctions.atExplode(ctx, params);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Password, @HashPassword, @VerifyPassword, @PasswordQuality
	 */
	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@Password")
	@ParamCount(1)
	public static ValueHolder atPassword(final FormulaContextNotes ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@Password(p1)", params[0]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@HashPassword")
	@ParamCount(1)
	public static ValueHolder atHashPassword(final FormulaContextNotes ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@HashPassword(p1)", params[0]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@VerifyPassword")
	@ParamCount(2)
	public static ValueHolder atVerifyPassword(final FormulaContextNotes ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@VerifyPassword(p1;p2)", params[0], params[1]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@PasswordQuality")
	@ParamCount(1)
	public static ValueHolder atPasswordQuality(final FormulaContextNotes ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@PasswordQuality(p1)", params[0]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Wide, @Narrow
	 */
	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@Wide")
	@ParamCount(1)
	public static ValueHolder atWide(final FormulaContextNotes ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@Wide(p1)", params[0]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@Narrow")
	@ParamCount(1)
	public static ValueHolder atNarrow(final FormulaContextNotes ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@Narrow(p1)", params[0]); //$NON-NLS-1$
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
	public static ValueHolder atCertificate(final FormulaContextNotes ctx, final ValueHolder params[]) throws EvaluateException {
		return ctx.evaluateNative("@Certificate(" + params[0].quoteValue() + ";p1)", params[1]); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @LDAPServer, @Name, @NameLookup, @UserName
	 */
	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@LDAPServer")
	@ParamCount(0)
	public static ValueHolder atLDAPServer(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@LDAPServer"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate({ "@Name", "openNTF-Name doesn't yet look stable" })
	@ParamCount(2)
	public static ValueHolder atName(final FormulaContextNotes ctx, final ValueHolder params[]) throws EvaluateException {
		return ctx.evaluateNative("@Name(" + params[0].quoteValue() + ";p1)", params[1]); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@NameLookup")
	@ParamCount(3)
	public static ValueHolder atNameLookup(final FormulaContextNotes ctx, final ValueHolder params[]) throws EvaluateException {
		return ctx.evaluateNative("@NameLookup(" + params[0].quoteValue() + ";p1;p2)", params[1], params[2]); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@UserName")
	@ParamCount({ 0, 1 })
	public static ValueHolder atUserName(final FormulaContextNotes ctx, final ValueHolder params[]) {
		return (params.length == 0) ? ctx.evaluateNative("@UserName") : ctx.evaluateNative("@UserName(p1)", params[0]); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @MailDbName, @MailEncryptSavedPreference, @MailEncryptSentPreference,
	 * @MailSavePreference, @MailSignPreference, @OptimizeMailAddress
	 */
	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@MailDbName")
	@ParamCount(0)
	public static ValueHolder atMailDbName(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@MailDbName"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@MailEncryptSavedPreference")
	@ParamCount(0)
	public static ValueHolder atMailEncryptSavedPreference(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@MailEncryptSavedPreference"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@MailEncryptSentPreference")
	@ParamCount(0)
	public static ValueHolder atMailEncryptSentPreference(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@MailEncryptSentPreference"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@MailSavePreference")
	@ParamCount(0)
	public static ValueHolder atMailSavePreference(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@MailSavePreference"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@MailSignPreference")
	@ParamCount(0)
	public static ValueHolder atMailSignPreference(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@MailSignPreference"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@OptimizeMailAddress")
	@ParamCount(1)
	public static ValueHolder atOptimizeMailAddress(final FormulaContextNotes ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@OptimizeMailAddress(p1)", params[0]); //$NON-NLS-1$
	}

	/*============================================================================*/
	/*
	 * Miscellanea
	 */
	/*============================================================================*/
	@NeedsNativeEvaluate("@ConfigFile")
	@ParamCount(0)
	public static ValueHolder atConfigFile(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@ConfigFile"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@Domain")
	@ParamCount(0)
	public static ValueHolder atDomain(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@Domain"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@LanguagePreference")
	@ParamCount(1)
	public static ValueHolder atLanguagePreference(final FormulaContextNotes ctx, final ValueHolder params[]) throws EvaluateException {
		return ctx.evaluateNative("@LanguagePreference(" + params[0].quoteValue() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@Locale")
	@ParamCount({ 1, 2 })
	public static ValueHolder atLocale(final FormulaContextNotes ctx, final ValueHolder[] params) throws EvaluateException {
		if (params.length == 1)
			return ctx.evaluateNative("@Locale(" + params[0].quoteValue() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		return ctx.evaluateNative("@Locale(" + params[0].quoteValue() + ";p1)", params[1]); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@OrgDir")
	@ParamCount(0)
	public static ValueHolder atOrgDir(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@OrgDir"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@PolicyIsFieldLocked")
	@ParamCount(1)
	public static ValueHolder atPolicyIsFieldLocked(final FormulaContextNotes ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@PolicyIsFieldLocked(p1)", params[0]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@RegQueryValue")
	@ParamCount(3)
	public static ValueHolder atRegQueryValue(final FormulaContextNotes ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@RegQueryValue(p1;p2;p3)", params[0], params[1], params[2]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@ServerAccess")
	@ParamCount({ 2, 3 })
	public static ValueHolder atServerAccess(final FormulaContextNotes ctx, final ValueHolder[] params) throws EvaluateException {
		if (params.length == 2) {
			return ctx.evaluateNative("@ServerAccess(" + params[0].quoteValue() + ";p1)", params[1]); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return ctx.evaluateNative("@ServerAccess(" + params[0].quoteValue() + ";p1;p2)", params[1], params[2]); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@AdminECLIsLocked")
	@ParamCount(0)
	public static ValueHolder atAdminECLIsLocked(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@AdminECLIsLocked"); //$NON-NLS-1$
	}

	/*============================================================================*/
	/*
	 * Further
	 */
	/*============================================================================*/
	@NeedsNativeEvaluate("@Abstract")
	@ParamCount(4)
	public static ValueHolder atAbstract(final FormulaContextNotes ctx, final ValueHolder[] params) throws EvaluateException {
		return ctx.evaluateNative("@Abstract(" + params[0].quoteValue() + ";" + params[1].quoteValue() + ";" + params[2].quoteValue() + ";" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				+ params[3].quoteValue() + ")"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@AbstractSimple")
	@ParamCount(1)
	public static ValueHolder atAbstractSimple(final FormulaContextNotes ctx, final ValueHolder[] params) throws EvaluateException {
		return ctx.evaluateNative("@AbstractSimple(" + params[0].quoteValue() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@BrowserInfo")
	@ParamCount(1)
	public static ValueHolder atBrowserInfo(final FormulaContextNotes ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@BrowserInfo(p1)", params[0]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@ClientType")
	@ParamCount(0)
	public static ValueHolder atClientType(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@ClientType"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DB2Schema")
	@ParamCount(2)
	public static ValueHolder atDB2Schema(final FormulaContextNotes ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@DB2Schema(p1;p2)", params[0], params[1]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DbColumn")
	@ParamCount(4)
	public static ValueHolder atDbColumn(final FormulaContextNotes ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@DbColumn(p1;p2;p3;p4)", params[0], params[1], params[2], params[3]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DbCommand")
	@ParamCount({ 2, 5 })
	public static ValueHolder atDbCommand(final FormulaContextNotes ctx, final ValueHolder params[]) {
		if (params.length == 2)
			return ctx.evaluateNative("@DbCommand(p1;p2)", params[0], params[1]); //$NON-NLS-1$
		if (params.length == 3)
			return ctx.evaluateNative("@DbCommand(p1;p2;p3)", params[0], params[1], params[2]); //$NON-NLS-1$
		if (params.length == 4)
			return ctx.evaluateNative("@DbCommand(p1;p2;p3;p4)", params[0], params[1], params[2], params[3]); //$NON-NLS-1$
		return ctx.evaluateNative("@DbCommand(p1;p2;p3;p4;p5)", params[0], params[1], params[2], params[3], params[4]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DbExists")
	@ParamCount({ 1, 2 })
	public static ValueHolder atDbExists(final FormulaContextNotes ctx, final ValueHolder[] params) {
		if (params.length == 1)
			return ctx.evaluateNative("@DbExists(p1)", params[0]); //$NON-NLS-1$
		return ctx.evaluateNative("@DbExists(p1;p2)", params[0], params[1]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DbLookup")
	@ParamCount({ 5, 9 })
	public static ValueHolder atDbLookup(final FormulaContextNotes ctx, final ValueHolder params[]) throws EvaluateException {
		if (params.length == 5)
			return ctx.evaluateNative("@DbLookup(p1;p2;p3;p4;p5)", params[0], params[1], params[2], params[3], params[4]); //$NON-NLS-1$
		if (params.length == 6) {
			if (params[5].dataType == DataType.KEYWORD_STRING)
				return ctx.evaluateNative("@DbLookup(p1;p2;p3;p4;p5;" + params[5].quoteValue() + ")", params[0], params[1], params[2], //$NON-NLS-1$ //$NON-NLS-2$
						params[3], params[4]);
			else
				return ctx.evaluateNative("@DbLookup(p1;p2;p3;p4;p5;p6)", params[0], params[1], params[2], params[3], params[4], params[5]); //$NON-NLS-1$
		}
		if (params.length == 7)
			return ctx.evaluateNative("@DbLookup(p1;p2;p3;p4;p5;p6;" + params[6].quoteValue() + ")", params[0], params[1], params[2], //$NON-NLS-1$ //$NON-NLS-2$
					params[3], params[4], params[5]);
		if (params.length == 8)
			return ctx.evaluateNative("@DbLookup(p1;p2;p3;p4;p5;p6;p7;p8)", params[0], params[1], params[2], params[3], params[4], //$NON-NLS-1$
					params[5], params[6], params[7]);
		return ctx.evaluateNative("@DbLookup(p1;p2;p3;p4;p5;p6;p7;p8;p9)", params[0], params[1], params[2], params[3], params[4], //$NON-NLS-1$
				params[5], params[6], params[7], params[8]);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DbManager")
	@ParamCount(0)
	public static ValueHolder atDbManager(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@DbManager"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DbName")
	@ParamCount(0)
	public static ValueHolder atDbName(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@DbName"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DbTitle")
	@ParamCount(0)
	public static ValueHolder atDbTitle(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@DbTitle"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DeleteDocument")
	@ParamCount(0)
	public static ValueHolder atDeleteDocument(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@DeleteDocument"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DocChildren")
	@ParamCount({ 0, 3 })
	public static ValueHolder atDocChildren(final FormulaContextNotes ctx, final ValueHolder[] params) throws EvaluateException {
		StringBuilder sb = new StringBuilder(256);
		sb.append("@DocChildren"); //$NON-NLS-1$
		if (params.length > 0) {
			sb.append('(');
			sb.append(params[0].quoteValue());
		}
		if (params.length > 1) {
			sb.append(';');
			sb.append(params[1].quoteValue());
		}
		if (params.length > 2) {
			sb.append(';');
			sb.append(params[2].quoteValue());
		}
		if (params.length > 0)
			sb.append(')');
		return ctx.evaluateNative(sb.toString());
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DocDescendants")
	@ParamCount({ 0, 3 })
	public static ValueHolder atDocDescendants(final FormulaContextNotes ctx, final ValueHolder[] params) throws EvaluateException {
		StringBuilder sb = new StringBuilder(256);
		sb.append("@DocDescendants"); //$NON-NLS-1$
		for (int i = 0; i < 3; i++)
			if (params.length > i) {
				sb.append((i == 0) ? '(' : ';');
				sb.append(params[i].quoteValue());
			}
		if (params.length > 0)
			sb.append(')');
		return ctx.evaluateNative(sb.toString());
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DocLevel")
	@ParamCount(0)
	public static ValueHolder atDocLevel(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@DocLevel"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DocMark")
	@ParamCount(1)
	public static ValueHolder atDocMark(final FormulaContextNotes ctx, final ValueHolder[] params) throws EvaluateException {
		return ctx.evaluateNative("@DocMark(" + params[0].quoteValue() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DocNumber")
	@ParamCount({ 0, 1 })
	public static ValueHolder atDocNumber(final FormulaContextNotes ctx, final ValueHolder[] params) throws EvaluateException {
		if (params.length == 0)
			return ctx.evaluateNative("@DocNumber"); //$NON-NLS-1$
		return ctx.evaluateNative("@DocNumber(" + params[0].quoteValue() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DocOmittedLength")
	@ParamCount(0)
	public static ValueHolder atDocOmittedLength(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@DocOmittedLength"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DocParentNumber")
	@ParamCount({ 0, 1 })
	public static ValueHolder atDocParentNumber(final FormulaContextNotes ctx, final ValueHolder[] params) throws EvaluateException {
		if (params.length == 0)
			return ctx.evaluateNative("@DocParentNumber"); //$NON-NLS-1$
		return ctx.evaluateNative("@DocParentNumber(" + params[0].quoteValue() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@DocSiblings")
	@ParamCount(0)
	public static ValueHolder atDocSiblings(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@DocSiblings"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@EditECL")
	@ParamCount(2)
	public static ValueHolder atEditECL(final FormulaContextNotes ctx, final ValueHolder[] params) {
		return ctx.evaluateNative("@EditECL(p1;p2)", params[0], params[1]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@EditUserECL")
	@ParamCount(0)
	public static ValueHolder atEditUserECL(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@EditUserECL"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@FormLanguage")
	@ParamCount(0)
	public static ValueHolder atFormLanguage(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@FormLanguage"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@GetAddressBooks")
	@ParamCount(1)
	public static ValueHolder atGetAddressBooks(final FormulaContextNotes ctx, final ValueHolder[] params) throws EvaluateException {
		return ctx.evaluateNative("@GetAddressBooks(" + params[0].quoteValue() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@GetCurrentTimeZone")
	@ParamCount(0)
	public static ValueHolder atGetCurrentTimeZone(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@GetCurrentTimeZone"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@GetFocusTable")
	@ParamCount(1)
	public static ValueHolder atGetFocusTable(final FormulaContextNotes ctx, final ValueHolder[] params) throws EvaluateException {
		return ctx.evaluateNative("@GetFocusTable(" + params[0].quoteValue() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@GetHTTPHeader")
	@ParamCount(1)
	public static ValueHolder atGetHTTPHeader(final FormulaContextNotes ctx, final ValueHolder[] params) {
		return ctx.evaluateNative("@GetHTTPHeader(p1)", params[0]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@GetPortsList")
	@ParamCount(1)
	public static ValueHolder atGetPortsList(final FormulaContextNotes ctx, final ValueHolder[] params) throws EvaluateException {
		return ctx.evaluateNative("@GetPortsList(" + params[0].quoteValue() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@GetProfileField")
	@ParamCount({ 2, 3 })
	public static ValueHolder atGetProfileField(final FormulaContextNotes ctx, final ValueHolder params[]) {
		if (params.length == 2)
			return ctx.evaluateNative("@GetProfileField(p1;p2)", params[0], params[1]); //$NON-NLS-1$
		return ctx.evaluateNative("@GetProfileField(p1;p2;p3)", params[0], params[1], params[2]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@HardDeleteDocument")
	@ParamCount(0)
	public static ValueHolder atHardDeleteDocument(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@HardDeleteDocument"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsAgentEnabled")
	@ParamCount(1)
	public static ValueHolder atIsAgentEnabled(final FormulaContextNotes ctx, final ValueHolder[] params) {
		return ctx.evaluateNative("@IsAgentEnabled(p1)", params[0]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsAppInstalled")
	@ParamCount(1)
	public static ValueHolder atIsAppInstalled(final FormulaContextNotes ctx, final ValueHolder[] params) {
		return ctx.evaluateNative("@IsAppInstalled(p1)", params[0]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsCategory")
	@ParamCount({ 0, 2 })
	public static ValueHolder atIsCategory(final FormulaContextNotes ctx, final ValueHolder[] params) {
		if (params.length == 0)
			return ctx.evaluateNative("@IsCategory"); //$NON-NLS-1$
		if (params.length == 1)
			return ctx.evaluateNative("@IsCategory(p1)", params[0]); //$NON-NLS-1$
		return ctx.evaluateNative("@IsCategory(p1;p2)", params[0], params[1]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsDocBeingEdited")
	@ParamCount(0)
	public static ValueHolder atIsDocBeingEdited(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@IsDocBeingEdited"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsDocBeingLoaded")
	@ParamCount(0)
	public static ValueHolder atIsDocBeingLoaded(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@IsDocBeingLoaded"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsDocBeingMailed")
	@ParamCount(0)
	public static ValueHolder atIsDocBeingMailed(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@IsDocBeingMailed"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsDocBeingRecalculated")
	@ParamCount(0)
	public static ValueHolder atIsDocBeingRecalculated(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@IsDocBeingRecalculated"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsDocBeingSaved")
	@ParamCount(0)
	public static ValueHolder atIsDocBeingSaved(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@IsDocBeingSaved"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsDocTruncated")
	@ParamCount(0)
	public static ValueHolder atIsDocTruncated(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@IsDocTruncated"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsEmbeddedInsideWCT")
	@ParamCount(0)
	public static ValueHolder atIsEmbeddedInsideWCT(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@IsEmbeddedInsideWCT"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsExpandable")
	@ParamCount({ 0, 2 })
	public static ValueHolder atIsExpandable(final FormulaContextNotes ctx, final ValueHolder[] params) {
		if (params.length == 0)
			return ctx.evaluateNative("@IsExpandable"); //$NON-NLS-1$
		if (params.length == 1)
			return ctx.evaluateNative("@IsExpandable(p1)", params[0]); //$NON-NLS-1$
		return ctx.evaluateNative("@IsExpandable(p1;p2)", params[0], params[1]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsModalHelp")
	@ParamCount(0)
	public static ValueHolder atIsModalHelp(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@IsModalHelp"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsUsingJavaElement")
	@ParamCount(0)
	public static ValueHolder atIsUsingJavaElement(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@IsUsingJavaElement"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@IsVirtualizedDirectory")
	@ParamCount(0)
	public static ValueHolder atIsVirtualizedDirectory(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@IsVirtualizedDirectory"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@LaunchApp")
	@ParamCount(1)
	public static ValueHolder atLaunchApp(final FormulaContextNotes ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@LaunchApp(p1)", params[0]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@MailSend")
	@ParamCount({ 0, 7 })
	public static ValueHolder atMailSend(final FormulaContextNotes ctx, final ValueHolder[] params) throws EvaluateException {
		StringBuilder sb = new StringBuilder(256);
		sb.append("@MailSend"); //$NON-NLS-1$
		for (int i = 0; i < 7; i++)
			if (params.length > i) {
				sb.append((i == 0) ? '(' : ';');
				sb.append(params[i].quoteValue());
			}
		if (params.length > 0)
			sb.append(')');
		return ctx.evaluateNative(sb.toString());
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@Platform")
	@ParamCount({ 0, 1 })
	public static ValueHolder atPlatform(final FormulaContextNotes ctx, final ValueHolder[] params) throws EvaluateException {
		if (params.length == 0)
			return ctx.evaluateNative("@Platform"); //$NON-NLS-1$
		return ctx.evaluateNative("@Platform(" + params[0].quoteValue() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@RefreshECL")
	@ParamCount(2)
	public static ValueHolder atRefreshECL(final FormulaContextNotes ctx, final ValueHolder[] params) {
		return ctx.evaluateNative("@RefreshECL(p1;p2)", params[0], params[1]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@ServerName")
	@ParamCount(0)
	public static ValueHolder atServerName(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@ServerName"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@SetHTTPHeader")
	@ParamCount(2)
	public static ValueHolder atSetHTTPHeader(final FormulaContextNotes ctx, final ValueHolder[] params) {
		return ctx.evaluateNative("@SetHTTPHeader(p1;p2)", params[0], params[1]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@SetProfileField")
	@ParamCount({ 2, 3 })
	public static ValueHolder atSetProfileField(final FormulaContextNotes ctx, final ValueHolder[] params) {
		if (params.length == 2)
			return ctx.evaluateNative("@SetProfileField(p1;p2)", params[0], params[1]); //$NON-NLS-1$
		return ctx.evaluateNative("@SetProfileField(p1;p2;p3)", params[0], params[1], params[2]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@ShowParentPreview")
	@ParamCount(0)
	public static ValueHolder atShowParentPreview(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@ShowParentPreview"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@SoundEx")
	@ParamCount(1)
	public static ValueHolder atSoundEx(final FormulaContextNotes ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@SoundEx(p1)", params[0]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@StatusBar")
	@ParamCount(1)
	public static ValueHolder atStatusBar(final FormulaContextNotes ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@StatusBar(p1)", params[0]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@ThisName")
	@ParamCount(0)
	public static ValueHolder atThisName(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@ThisName"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@ThisValue")
	@ParamCount(0)
	public static ValueHolder atThisValue(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@ThisValue"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@TimeToTextInZone")
	@ParamCount({ 2, 3 })
	public static ValueHolder atTimeToTextInZone(final FormulaContextNotes ctx, final ValueHolder params[]) {
		if (params.length == 2)
			return ctx.evaluateNative("@TimeToTextInZone(p1;p2)", params[0], params[1]); //$NON-NLS-1$
		return ctx.evaluateNative("@TimeToTextInZone(p1;p2;p3)", params[0], params[1], params[2]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@TimeZoneToText")
	@ParamCount({ 1, 2 })
	public static ValueHolder atTimeZoneToText(final FormulaContextNotes ctx, final ValueHolder params[]) {
		if (params.length == 1)
			return ctx.evaluateNative("@TimeZoneToText(p1)", params[0]); //$NON-NLS-1$
		return ctx.evaluateNative("@TimeZoneToText(p1;p2)", params[0], params[1]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@UndeleteDocument")
	@ParamCount(0)
	public static ValueHolder atUndeleteDocument(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@UndeleteDocument"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@UpdateFormulaContext")
	@ParamCount(0)
	public static ValueHolder atUpdateFormulaContext(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@UpdateFormulaContext"); //$NON-NLS-1$
	}

	@ParamCount({ 0, 1 })
	public static ValueHolder atUnique(final FormulaContextNotes ctx, final ValueHolder[] params) {
		if (params == null || params.length == 0)
			return ctx.evaluateNative("@Unique"); //$NON-NLS-1$
		return TextFunctions.atUnique(ctx, params);
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@URLQueryString")
	@ParamCount({ 0, 1 })
	public static ValueHolder atURLQueryString(final FormulaContextNotes ctx, final ValueHolder params[]) {
		if (params.length == 0)
			return ctx.evaluateNative("@URLQueryString"); //$NON-NLS-1$
		return ctx.evaluateNative("@URLQueryString(p1)", params[0]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@UserNameLanguage")
	@ParamCount(1)
	public static ValueHolder atUserNameLanguage(final FormulaContextNotes ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@UserNameLanguage(p1)", params[0]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@UserNamesList")
	@ParamCount(0)
	public static ValueHolder atUserNamesList(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@UserNamesList"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@UserPrivileges")
	@ParamCount(0)
	public static ValueHolder atUserPrivileges(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@UserPrivileges"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@UserRoles")
	@ParamCount(0)
	public static ValueHolder atUserRoles(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@UserRoles"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@V3UserName")
	@ParamCount(0)
	public static ValueHolder atV3UserName(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@V3UserName"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@V4UserAccess")
	@ParamCount(1)
	public static ValueHolder atV4UserAccess(final FormulaContextNotes ctx, final ValueHolder params[]) {
		return ctx.evaluateNative("@V4UserAccess(p1)", params[0]); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@ValidateInternetAddress")
	@ParamCount(2)
	public static ValueHolder atValidateInternetAddress(final FormulaContextNotes ctx, final ValueHolder params[]) throws EvaluateException {
		return ctx.evaluateNative("@ValidateInternetAddress(" + params[0].quoteValue() + ";p1)", params[1]); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@Version")
	@ParamCount(0)
	public static ValueHolder atVersion(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@Version"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@ViewTitle")
	@ParamCount(0)
	public static ValueHolder atViewTitle(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@ViewTitle"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@WebDbName")
	@ParamCount(0)
	public static ValueHolder atWebDbName(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@WebDbName"); //$NON-NLS-1$
	}

	/*----------------------------------------------------------------------------*/
	@NeedsNativeEvaluate("@Zone")
	@ParamCount({ 0, 1 })
	public static ValueHolder atZone(final FormulaContextNotes ctx, final ValueHolder params[]) {
		if (params.length == 0)
			return ctx.evaluateNative("@Zone"); //$NON-NLS-1$
		return ctx.evaluateNative("@Zone(p1)", params[0]); //$NON-NLS-1$
	}

}
