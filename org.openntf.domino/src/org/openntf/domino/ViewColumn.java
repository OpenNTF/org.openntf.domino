package org.openntf.domino;

import lotus.domino.View;

public interface ViewColumn extends Base<lotus.domino.ViewColumn>, lotus.domino.ViewColumn {
	@Override
	public int getAlignment();

	@Override
	public int getColumnValuesIndex();

	@Override
	public int getDateFmt();

	@Override
	public int getFontColor();

	@Override
	public String getFontFace();

	@Override
	public int getFontPointSize();

	@Override
	public int getFontStyle();

	@Override
	public String getFormula();

	@Override
	public int getHeaderAlignment();

	@Override
	public int getHeaderFontColor();

	@Override
	public String getHeaderFontFace();

	@Override
	public int getHeaderFontPointSize();

	@Override
	public int getHeaderFontStyle();

	@Override
	public String getItemName();

	@Override
	public int getListSep();

	@Override
	public int getNumberAttrib();

	@Override
	public int getNumberDigits();

	@Override
	public int getNumberFormat();

	@Override
	public View getParent();

	@Override
	public int getPosition();

	@Override
	public String getResortToViewName();

	@Override
	public int getSecondaryResortColumnIndex();

	@Override
	public int getTimeDateFmt();

	@Override
	public int getTimeFmt();

	@Override
	public int getTimeZoneFmt();

	@Override
	public String getTitle();

	@Override
	public int getWidth();

	@Override
	public boolean isAccentSensitiveSort();

	@Override
	public boolean isCaseSensitiveSort();

	@Override
	public boolean isCategory();

	@Override
	public boolean isConstant();

	@Override
	public boolean isField();

	@Override
	public boolean isFontBold();

	@Override
	public boolean isFontItalic();

	@Override
	public boolean isFontStrikethrough();

	@Override
	public boolean isFontUnderline();

	@Override
	public boolean isFormula();

	@Override
	public boolean isHeaderFontBold();

	@Override
	public boolean isHeaderFontItalic();

	@Override
	public boolean isHeaderFontStrikethrough();

	@Override
	public boolean isHeaderFontUnderline();

	@Override
	public boolean isHidden();

	@Override
	public boolean isHideDetail();

	@Override
	public boolean isHideFormula();

	@Override
	public boolean isIcon();

	@Override
	public boolean isNumberAttribParens();

	@Override
	public boolean isNumberAttribPercent();

	@Override
	public boolean isNumberAttribPunctuated();

	@Override
	public boolean isResize();

	@Override
	public boolean isResortAscending();

	@Override
	public boolean isResortDescending();

	@Override
	public boolean isResortToView();

	@Override
	public boolean isResponse();

	@Override
	public boolean isSecondaryResort();

	@Override
	public boolean isSecondaryResortDescending();

	@Override
	public boolean isShowTwistie();

	@Override
	public boolean isSortDescending();

	@Override
	public boolean isSorted();

	@Override
	public void setAccentSensitiveSort(boolean arg0);

	@Override
	public void setAlignment(int arg0);

	@Override
	public void setCaseSensitiveSort(boolean arg0);

	@Override
	public void setDateFmt(int arg0);

	@Override
	public void setFontBold(boolean arg0);

	@Override
	public void setFontColor(int arg0);

	@Override
	public void setFontFace(String arg0);

	@Override
	public void setFontItalic(boolean arg0);

	@Override
	public void setFontPointSize(int arg0);

	@Override
	public void setFontStrikethrough(boolean arg0);

	@Override
	public void setFontStyle(int arg0);

	@Override
	public void setFontUnderline(boolean arg0);

	@Override
	public void setFormula(String arg0);

	@Override
	public void setHeaderAlignment(int arg0);

	@Override
	public void setHeaderFontBold(boolean arg0);

	@Override
	public void setHeaderFontColor(int arg0);

	@Override
	public void setHeaderFontFace(String arg0);

	@Override
	public void setHeaderFontItalic(boolean arg0);

	@Override
	public void setHeaderFontPointSize(int arg0);

	@Override
	public void setHeaderFontStrikethrough(boolean arg0);

	@Override
	public void setHeaderFontStyle(int arg0);

	@Override
	public void setHeaderFontUnderline(boolean arg0);

	@Override
	public void setHidden(boolean arg0);

	@Override
	public void setHideDetail(boolean arg0);

	@Override
	public void setHideFormula(boolean arg0);

	@Override
	public void setListSep(int arg0);

	@Override
	public void setNumberAttrib(int arg0);

	@Override
	public void setNumberAttribParens(boolean arg0);

	@Override
	public void setNumberAttribPercent(boolean arg0);

	@Override
	public void setNumberAttribPunctuated(boolean arg0);

	@Override
	public void setNumberDigits(int arg0);

	@Override
	public void setNumberFormat(int arg0);

	@Override
	public void setPosition(int arg0);

	@Override
	public void setResize(boolean arg0);

	@Override
	public void setResortAscending(boolean arg0);

	@Override
	public void setResortDescending(boolean arg0);

	@Override
	public void setResortToView(boolean arg0);

	@Override
	public void setResortToViewName(String arg0);

	@Override
	public void setSecondaryResort(boolean arg0);

	@Override
	public void setSecondaryResortColumnIndex(int arg0);

	@Override
	public void setSecondaryResortDescending(boolean arg0);

	@Override
	public void setShowTwistie(boolean arg0);

	@Override
	public void setSortDescending(boolean arg0);

	@Override
	public void setSorted(boolean arg0);

	@Override
	public void setTimeDateFmt(int arg0);

	@Override
	public void setTimeFmt(int arg0);

	@Override
	public void setTimeZoneFmt(int arg0);

	@Override
	public void setTitle(String arg0);

	@Override
	public void setWidth(int arg0);
}
