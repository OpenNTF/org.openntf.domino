/*
 * Copyright OpenNTF 2013
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
package org.openntf.domino;

// TODO: Auto-generated Javadoc
/**
 * The Interface ViewColumn.
 */
public interface ViewColumn extends Base<lotus.domino.ViewColumn>, lotus.domino.ViewColumn {
	
	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getAlignment()
	 */
	@Override
	public int getAlignment();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getColumnValuesIndex()
	 */
	@Override
	public int getColumnValuesIndex();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getDateFmt()
	 */
	@Override
	public int getDateFmt();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getFontColor()
	 */
	@Override
	public int getFontColor();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getFontFace()
	 */
	@Override
	public String getFontFace();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getFontPointSize()
	 */
	@Override
	public int getFontPointSize();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getFontStyle()
	 */
	@Override
	public int getFontStyle();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getFormula()
	 */
	@Override
	public String getFormula();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getHeaderAlignment()
	 */
	@Override
	public int getHeaderAlignment();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getHeaderFontColor()
	 */
	@Override
	public int getHeaderFontColor();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getHeaderFontFace()
	 */
	@Override
	public String getHeaderFontFace();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getHeaderFontPointSize()
	 */
	@Override
	public int getHeaderFontPointSize();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getHeaderFontStyle()
	 */
	@Override
	public int getHeaderFontStyle();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getItemName()
	 */
	@Override
	public String getItemName();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getListSep()
	 */
	@Override
	public int getListSep();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getNumberAttrib()
	 */
	@Override
	public int getNumberAttrib();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getNumberDigits()
	 */
	@Override
	public int getNumberDigits();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getNumberFormat()
	 */
	@Override
	public int getNumberFormat();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getParent()
	 */
	@Override
	public View getParent();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getPosition()
	 */
	@Override
	public int getPosition();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getResortToViewName()
	 */
	@Override
	public String getResortToViewName();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getSecondaryResortColumnIndex()
	 */
	@Override
	public int getSecondaryResortColumnIndex();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getTimeDateFmt()
	 */
	@Override
	public int getTimeDateFmt();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getTimeFmt()
	 */
	@Override
	public int getTimeFmt();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getTimeZoneFmt()
	 */
	@Override
	public int getTimeZoneFmt();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getTitle()
	 */
	@Override
	public String getTitle();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#getWidth()
	 */
	@Override
	public int getWidth();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isAccentSensitiveSort()
	 */
	@Override
	public boolean isAccentSensitiveSort();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isCaseSensitiveSort()
	 */
	@Override
	public boolean isCaseSensitiveSort();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isCategory()
	 */
	@Override
	public boolean isCategory();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isConstant()
	 */
	@Override
	public boolean isConstant();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isField()
	 */
	@Override
	public boolean isField();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isFontBold()
	 */
	@Override
	public boolean isFontBold();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isFontItalic()
	 */
	@Override
	public boolean isFontItalic();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isFontStrikethrough()
	 */
	@Override
	public boolean isFontStrikethrough();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isFontUnderline()
	 */
	@Override
	public boolean isFontUnderline();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isFormula()
	 */
	@Override
	public boolean isFormula();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isHeaderFontBold()
	 */
	@Override
	public boolean isHeaderFontBold();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isHeaderFontItalic()
	 */
	@Override
	public boolean isHeaderFontItalic();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isHeaderFontStrikethrough()
	 */
	@Override
	public boolean isHeaderFontStrikethrough();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isHeaderFontUnderline()
	 */
	@Override
	public boolean isHeaderFontUnderline();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isHidden()
	 */
	@Override
	public boolean isHidden();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isHideDetail()
	 */
	@Override
	public boolean isHideDetail();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isHideFormula()
	 */
	@Override
	public boolean isHideFormula();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isIcon()
	 */
	@Override
	public boolean isIcon();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isNumberAttribParens()
	 */
	@Override
	public boolean isNumberAttribParens();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isNumberAttribPercent()
	 */
	@Override
	public boolean isNumberAttribPercent();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isNumberAttribPunctuated()
	 */
	@Override
	public boolean isNumberAttribPunctuated();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isResize()
	 */
	@Override
	public boolean isResize();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isResortAscending()
	 */
	@Override
	public boolean isResortAscending();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isResortDescending()
	 */
	@Override
	public boolean isResortDescending();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isResortToView()
	 */
	@Override
	public boolean isResortToView();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isResponse()
	 */
	@Override
	public boolean isResponse();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isSecondaryResort()
	 */
	@Override
	public boolean isSecondaryResort();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isSecondaryResortDescending()
	 */
	@Override
	public boolean isSecondaryResortDescending();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isShowTwistie()
	 */
	@Override
	public boolean isShowTwistie();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isSortDescending()
	 */
	@Override
	public boolean isSortDescending();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#isSorted()
	 */
	@Override
	public boolean isSorted();

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setAccentSensitiveSort(boolean)
	 */
	@Override
	public void setAccentSensitiveSort(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setAlignment(int)
	 */
	@Override
	public void setAlignment(int alignment);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setCaseSensitiveSort(boolean)
	 */
	@Override
	public void setCaseSensitiveSort(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setDateFmt(int)
	 */
	@Override
	public void setDateFmt(int format);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setFontBold(boolean)
	 */
	@Override
	public void setFontBold(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setFontColor(int)
	 */
	@Override
	public void setFontColor(int color);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setFontFace(java.lang.String)
	 */
	@Override
	public void setFontFace(String face);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setFontItalic(boolean)
	 */
	@Override
	public void setFontItalic(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setFontPointSize(int)
	 */
	@Override
	public void setFontPointSize(int size);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setFontStrikethrough(boolean)
	 */
	@Override
	public void setFontStrikethrough(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setFontStyle(int)
	 */
	@Override
	public void setFontStyle(int style);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setFontUnderline(boolean)
	 */
	@Override
	public void setFontUnderline(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setFormula(java.lang.String)
	 */
	@Override
	public void setFormula(String formula);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setHeaderAlignment(int)
	 */
	@Override
	public void setHeaderAlignment(int alignment);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setHeaderFontBold(boolean)
	 */
	@Override
	public void setHeaderFontBold(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setHeaderFontColor(int)
	 */
	@Override
	public void setHeaderFontColor(int color);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setHeaderFontFace(java.lang.String)
	 */
	@Override
	public void setHeaderFontFace(String face);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setHeaderFontItalic(boolean)
	 */
	@Override
	public void setHeaderFontItalic(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setHeaderFontPointSize(int)
	 */
	@Override
	public void setHeaderFontPointSize(int size);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setHeaderFontStrikethrough(boolean)
	 */
	@Override
	public void setHeaderFontStrikethrough(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setHeaderFontStyle(int)
	 */
	@Override
	public void setHeaderFontStyle(int style);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setHeaderFontUnderline(boolean)
	 */
	@Override
	public void setHeaderFontUnderline(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setHidden(boolean)
	 */
	@Override
	public void setHidden(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setHideDetail(boolean)
	 */
	@Override
	public void setHideDetail(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setHideFormula(boolean)
	 */
	@Override
	public void setHideFormula(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setListSep(int)
	 */
	@Override
	public void setListSep(int separator);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setNumberAttrib(int)
	 */
	@Override
	public void setNumberAttrib(int attributes);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setNumberAttribParens(boolean)
	 */
	@Override
	public void setNumberAttribParens(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setNumberAttribPercent(boolean)
	 */
	@Override
	public void setNumberAttribPercent(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setNumberAttribPunctuated(boolean)
	 */
	@Override
	public void setNumberAttribPunctuated(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setNumberDigits(int)
	 */
	@Override
	public void setNumberDigits(int digits);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setNumberFormat(int)
	 */
	@Override
	public void setNumberFormat(int format);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setPosition(int)
	 */
	@Override
	public void setPosition(int position);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setResize(boolean)
	 */
	@Override
	public void setResize(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setResortAscending(boolean)
	 */
	@Override
	public void setResortAscending(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setResortDescending(boolean)
	 */
	@Override
	public void setResortDescending(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setResortToView(boolean)
	 */
	@Override
	public void setResortToView(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setResortToViewName(java.lang.String)
	 */
	@Override
	public void setResortToViewName(String name);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setSecondaryResort(boolean)
	 */
	@Override
	public void setSecondaryResort(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setSecondaryResortColumnIndex(int)
	 */
	@Override
	public void setSecondaryResortColumnIndex(int index);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setSecondaryResortDescending(boolean)
	 */
	@Override
	public void setSecondaryResortDescending(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setShowTwistie(boolean)
	 */
	@Override
	public void setShowTwistie(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setSortDescending(boolean)
	 */
	@Override
	public void setSortDescending(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setSorted(boolean)
	 */
	@Override
	public void setSorted(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setTimeDateFmt(int)
	 */
	@Override
	public void setTimeDateFmt(int format);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setTimeFmt(int)
	 */
	@Override
	public void setTimeFmt(int format);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setTimeZoneFmt(int)
	 */
	@Override
	public void setTimeZoneFmt(int format);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title);

	/* (non-Javadoc)
	 * @see lotus.domino.ViewColumn#setWidth(int)
	 */
	@Override
	public void setWidth(int width);
}
