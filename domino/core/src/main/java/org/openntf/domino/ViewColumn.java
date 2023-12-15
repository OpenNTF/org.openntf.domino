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
package org.openntf.domino;

import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.types.Design;
import org.openntf.domino.types.FactorySchema;

/**
 * A column in a view or folder. Methods inherited from {@link org.openntf.domino.types.Design} interface throw
 * {@link UnsupportedOperationException}.
 * <h3>Notable enhancements</h3>
 * <ul>
 * <li>Has an {@link org.openntf.domino.ext.ViewColumn#getIndex() index} property fixing the native columns index numbers</li>
 * </ul>
 */
public interface ViewColumn
		extends Base<lotus.domino.ViewColumn>, lotus.domino.ViewColumn, org.openntf.domino.ext.ViewColumn, Design, DatabaseDescendant {

	public static class Schema extends FactorySchema<ViewColumn, lotus.domino.ViewColumn, View> {
		@Override
		public Class<ViewColumn> typeClass() {
			return ViewColumn.class;
		}

		@Override
		public Class<lotus.domino.ViewColumn> delegateClass() {
			return lotus.domino.ViewColumn.class;
		}

		@Override
		public Class<View> parentClass() {
			return View.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getAlignment()
	 */
	@Override
	public int getAlignment();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getColumnValuesIndex()
	 */
	@Override
	public int getColumnValuesIndex();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getDateFmt()
	 */
	@Override
	public int getDateFmt();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getFontColor()
	 */
	@Override
	public int getFontColor();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getFontFace()
	 */
	@Override
	public String getFontFace();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getFontPointSize()
	 */
	@Override
	public int getFontPointSize();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getFontStyle()
	 */
	@Override
	public int getFontStyle();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getFormula()
	 */
	@Override
	public String getFormula();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getHeaderAlignment()
	 */
	@Override
	public int getHeaderAlignment();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getHeaderFontColor()
	 */
	@Override
	public int getHeaderFontColor();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getHeaderFontFace()
	 */
	@Override
	public String getHeaderFontFace();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getHeaderFontPointSize()
	 */
	@Override
	public int getHeaderFontPointSize();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getHeaderFontStyle()
	 */
	@Override
	public int getHeaderFontStyle();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getItemName()
	 */
	@Override
	public String getItemName();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getListSep()
	 */
	@Override
	public int getListSep();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getNumberAttrib()
	 */
	@Override
	public int getNumberAttrib();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getNumberDigits()
	 */
	@Override
	public int getNumberDigits();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getNumberFormat()
	 */
	@Override
	public int getNumberFormat();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getParent()
	 */
	@Override
	public View getParent();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getPosition()
	 */
	@Override
	public int getPosition();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getResortToViewName()
	 */
	@Override
	public String getResortToViewName();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getSecondaryResortColumnIndex()
	 */
	@Override
	public int getSecondaryResortColumnIndex();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getTimeDateFmt()
	 */
	@Override
	public int getTimeDateFmt();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getTimeFmt()
	 */
	@Override
	public int getTimeFmt();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getTimeZoneFmt()
	 */
	@Override
	public int getTimeZoneFmt();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getTitle()
	 */
	@Override
	public String getTitle();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#getWidth()
	 */
	@Override
	public int getWidth();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isAccentSensitiveSort()
	 */
	@Override
	public boolean isAccentSensitiveSort();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isCaseSensitiveSort()
	 */
	@Override
	public boolean isCaseSensitiveSort();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isCategory()
	 */
	@Override
	public boolean isCategory();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isConstant()
	 */
	@Override
	public boolean isConstant();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isField()
	 */
	@Override
	public boolean isField();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isFontBold()
	 */
	@Override
	public boolean isFontBold();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isFontItalic()
	 */
	@Override
	public boolean isFontItalic();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isFontStrikethrough()
	 */
	@Override
	public boolean isFontStrikethrough();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isFontUnderline()
	 */
	@Override
	public boolean isFontUnderline();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isFormula()
	 */
	@Override
	public boolean isFormula();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isHeaderFontBold()
	 */
	@Override
	public boolean isHeaderFontBold();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isHeaderFontItalic()
	 */
	@Override
	public boolean isHeaderFontItalic();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isHeaderFontStrikethrough()
	 */
	@Override
	public boolean isHeaderFontStrikethrough();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isHeaderFontUnderline()
	 */
	@Override
	public boolean isHeaderFontUnderline();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isHidden()
	 */
	@Override
	public boolean isHidden();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isHideDetail()
	 */
	@Override
	public boolean isHideDetail();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isHideFormula()
	 */
	@Override
	public boolean isHideFormula();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isIcon()
	 */
	@Override
	public boolean isIcon();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isNumberAttribParens()
	 */
	@Override
	public boolean isNumberAttribParens();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isNumberAttribPercent()
	 */
	@Override
	public boolean isNumberAttribPercent();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isNumberAttribPunctuated()
	 */
	@Override
	public boolean isNumberAttribPunctuated();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isResize()
	 */
	@Override
	public boolean isResize();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isResortAscending()
	 */
	@Override
	public boolean isResortAscending();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isResortDescending()
	 */
	@Override
	public boolean isResortDescending();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isResortToView()
	 */
	@Override
	public boolean isResortToView();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isResponse()
	 */
	@Override
	public boolean isResponse();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isSecondaryResort()
	 */
	@Override
	public boolean isSecondaryResort();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isSecondaryResortDescending()
	 */
	@Override
	public boolean isSecondaryResortDescending();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isShowTwistie()
	 */
	@Override
	public boolean isShowTwistie();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isSortDescending()
	 */
	@Override
	public boolean isSortDescending();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#isSorted()
	 */
	@Override
	public boolean isSorted();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setAccentSensitiveSort(boolean)
	 */
	@Override
	public void setAccentSensitiveSort(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setAlignment(int)
	 */
	@Override
	public void setAlignment(final int alignment);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setCaseSensitiveSort(boolean)
	 */
	@Override
	public void setCaseSensitiveSort(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setDateFmt(int)
	 */
	@Override
	public void setDateFmt(final int format);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setFontBold(boolean)
	 */
	@Override
	public void setFontBold(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setFontColor(int)
	 */
	@Override
	public void setFontColor(final int color);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setFontFace(java.lang.String)
	 */
	@Override
	public void setFontFace(final String face);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setFontItalic(boolean)
	 */
	@Override
	public void setFontItalic(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setFontPointSize(int)
	 */
	@Override
	public void setFontPointSize(final int size);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setFontStrikethrough(boolean)
	 */
	@Override
	public void setFontStrikethrough(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setFontStyle(int)
	 */
	@Override
	public void setFontStyle(final int style);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setFontUnderline(boolean)
	 */
	@Override
	public void setFontUnderline(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setFormula(java.lang.String)
	 */
	@Override
	public void setFormula(final String formula);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setHeaderAlignment(int)
	 */
	@Override
	public void setHeaderAlignment(final int alignment);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setHeaderFontBold(boolean)
	 */
	@Override
	public void setHeaderFontBold(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setHeaderFontColor(int)
	 */
	@Override
	public void setHeaderFontColor(final int color);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setHeaderFontFace(java.lang.String)
	 */
	@Override
	public void setHeaderFontFace(final String face);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setHeaderFontItalic(boolean)
	 */
	@Override
	public void setHeaderFontItalic(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setHeaderFontPointSize(int)
	 */
	@Override
	public void setHeaderFontPointSize(final int size);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setHeaderFontStrikethrough(boolean)
	 */
	@Override
	public void setHeaderFontStrikethrough(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setHeaderFontStyle(int)
	 */
	@Override
	public void setHeaderFontStyle(final int style);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setHeaderFontUnderline(boolean)
	 */
	@Override
	public void setHeaderFontUnderline(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setHidden(boolean)
	 */
	@Override
	public void setHidden(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setHideDetail(boolean)
	 */
	@Override
	public void setHideDetail(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setHideFormula(boolean)
	 */
	@Override
	public void setHideFormula(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setListSep(int)
	 */
	@Override
	public void setListSep(final int separator);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setNumberAttrib(int)
	 */
	@Override
	public void setNumberAttrib(final int attributes);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setNumberAttribParens(boolean)
	 */
	@Override
	public void setNumberAttribParens(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setNumberAttribPercent(boolean)
	 */
	@Override
	public void setNumberAttribPercent(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setNumberAttribPunctuated(boolean)
	 */
	@Override
	public void setNumberAttribPunctuated(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setNumberDigits(int)
	 */
	@Override
	public void setNumberDigits(final int digits);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setNumberFormat(int)
	 */
	@Override
	public void setNumberFormat(final int format);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setPosition(int)
	 */
	@Override
	public void setPosition(final int position);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setResize(boolean)
	 */
	@Override
	public void setResize(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setResortAscending(boolean)
	 */
	@Override
	public void setResortAscending(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setResortDescending(boolean)
	 */
	@Override
	public void setResortDescending(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setResortToView(boolean)
	 */
	@Override
	public void setResortToView(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setResortToViewName(java.lang.String)
	 */
	@Override
	public void setResortToViewName(final String name);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setSecondaryResort(boolean)
	 */
	@Override
	public void setSecondaryResort(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setSecondaryResortColumnIndex(int)
	 */
	@Override
	public void setSecondaryResortColumnIndex(final int index);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setSecondaryResortDescending(boolean)
	 */
	@Override
	public void setSecondaryResortDescending(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setShowTwistie(boolean)
	 */
	@Override
	public void setShowTwistie(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setSortDescending(boolean)
	 */
	@Override
	public void setSortDescending(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setSorted(boolean)
	 */
	@Override
	public void setSorted(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setTimeDateFmt(int)
	 */
	@Override
	public void setTimeDateFmt(final int format);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setTimeFmt(int)
	 */
	@Override
	public void setTimeFmt(final int format);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setTimeZoneFmt(int)
	 */
	@Override
	public void setTimeZoneFmt(final int format);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(final String title);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.ViewColumn#setWidth(int)
	 */
	@Override
	public void setWidth(final int width);
	
	/*
	 * Indicates whether a view column can be used in DQL 'ViewName'.column <operator> <value> terms.
	 * 
	 * @return boolean
	 * @since V11
	 */
	@Override
	public boolean isValidDominoQueryColumn();


	/*
	 * Indicates whether a view column can be used in DQL <fieldname> <operator> <value> terms.
	 * 
	 * @return boolean
	 * @since V11
	 */
	@Override
	public boolean isValidDominoQueryField();
}
