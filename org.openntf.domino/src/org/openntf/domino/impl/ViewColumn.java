package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class ViewColumn extends Base<org.openntf.domino.ViewColumn, lotus.domino.ViewColumn> implements org.openntf.domino.ViewColumn {

	public ViewColumn(lotus.domino.ViewColumn delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public int getAlignment() {
		try {
			return getDelegate().getAlignment();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getColumnValuesIndex() {
		try {
			return getDelegate().getColumnValuesIndex();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getDateFmt() {
		try {
			return getDelegate().getDateFmt();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getFontColor() {
		try {
			return getDelegate().getFontColor();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public String getFontFace() {
		try {
			return getDelegate().getFontFace();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getFontPointSize() {
		try {
			return getDelegate().getFontPointSize();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getFontStyle() {
		try {
			return getDelegate().getFontStyle();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public String getFormula() {
		try {
			return getDelegate().getFormula();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getHeaderAlignment() {
		try {
			return getDelegate().getHeaderAlignment();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getHeaderFontColor() {
		try {
			return getDelegate().getHeaderFontColor();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public String getHeaderFontFace() {
		try {
			return getDelegate().getHeaderFontFace();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getHeaderFontPointSize() {
		try {
			return getDelegate().getHeaderFontPointSize();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getHeaderFontStyle() {
		try {
			return getDelegate().getHeaderFontStyle();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public String getItemName() {
		try {
			return getDelegate().getItemName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getListSep() {
		try {
			return getDelegate().getListSep();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getNumberAttrib() {
		try {
			return getDelegate().getNumberAttrib();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getNumberDigits() {
		try {
			return getDelegate().getNumberDigits();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getNumberFormat() {
		try {
			return getDelegate().getNumberFormat();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public View getParent() {
		try {
			return Factory.fromLotus(getDelegate().getParent(), View.class, this);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	@Override
	public int getPosition() {
		try {
			return getDelegate().getPosition();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public String getResortToViewName() {
		try {
			return getDelegate().getResortToViewName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getSecondaryResortColumnIndex() {
		try {
			return getDelegate().getSecondaryResortColumnIndex();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getTimeDateFmt() {
		try {
			return getDelegate().getTimeDateFmt();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getTimeFmt() {
		try {
			return getDelegate().getTimeFmt();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getTimeZoneFmt() {
		try {
			return getDelegate().getTimeZoneFmt();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public String getTitle() {
		try {
			return getDelegate().getTitle();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getWidth() {
		try {
			return getDelegate().getWidth();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public boolean isAccentSensitiveSort() {
		try {
			return getDelegate().isAccentSensitiveSort();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isCaseSensitiveSort() {
		try {
			return getDelegate().isCaseSensitiveSort();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isCategory() {
		try {
			return getDelegate().isCategory();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isConstant() {
		try {
			return getDelegate().isConstant();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isField() {
		try {
			return getDelegate().isField();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isFontBold() {
		try {
			return getDelegate().isFontBold();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isFontItalic() {
		try {
			return getDelegate().isFontItalic();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isFontStrikethrough() {
		try {
			return getDelegate().isFontStrikethrough();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isFontUnderline() {
		try {
			return getDelegate().isFontUnderline();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isFormula() {
		try {
			return getDelegate().isFormula();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isHeaderFontBold() {
		try {
			return getDelegate().isHeaderFontBold();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isHeaderFontItalic() {
		try {
			return getDelegate().isHeaderFontItalic();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isHeaderFontStrikethrough() {
		try {
			return getDelegate().isHeaderFontStrikethrough();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isHeaderFontUnderline() {
		try {
			return getDelegate().isHeaderFontUnderline();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isHidden() {
		try {
			return getDelegate().isHidden();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isHideDetail() {
		try {
			return getDelegate().isHideDetail();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isHideFormula() {
		try {
			return getDelegate().isHideFormula();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isIcon() {
		try {
			return getDelegate().isIcon();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isNumberAttribParens() {
		try {
			return getDelegate().isNumberAttribParens();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isNumberAttribPercent() {
		try {
			return getDelegate().isNumberAttribPercent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isNumberAttribPunctuated() {
		try {
			return getDelegate().isNumberAttribPunctuated();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isResize() {
		try {
			return getDelegate().isResize();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isResortAscending() {
		try {
			return getDelegate().isResortAscending();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isResortDescending() {
		try {
			return getDelegate().isResortDescending();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isResortToView() {
		try {
			return getDelegate().isResortToView();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isResponse() {
		try {
			return getDelegate().isResponse();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isSecondaryResort() {
		try {
			return getDelegate().isSecondaryResort();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isSecondaryResortDescending() {
		try {
			return getDelegate().isSecondaryResortDescending();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isShowTwistie() {
		try {
			return getDelegate().isShowTwistie();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isSortDescending() {
		try {
			return getDelegate().isSortDescending();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isSorted() {
		try {
			return getDelegate().isSorted();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public void setAccentSensitiveSort(boolean flag) {
		try {
			getDelegate().setAccentSensitiveSort(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setAlignment(int alignment) {
		try {
			getDelegate().setAlignment(alignment);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCaseSensitiveSort(boolean flag) {
		try {
			getDelegate().setCaseSensitiveSort(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setDateFmt(int format) {
		try {
			getDelegate().setDateFmt(format);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setFontBold(boolean flag) {
		try {
			getDelegate().setFontBold(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setFontColor(int color) {
		try {
			getDelegate().setFontColor(color);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setFontFace(String face) {
		try {
			getDelegate().setFontFace(face);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setFontItalic(boolean flag) {
		try {
			getDelegate().setFontItalic(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setFontPointSize(int size) {
		try {
			getDelegate().setFontPointSize(size);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setFontStrikethrough(boolean flag) {
		try {
			getDelegate().setFontStrikethrough(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setFontStyle(int style) {
		try {
			getDelegate().setFontStyle(style);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setFontUnderline(boolean flag) {
		try {
			getDelegate().setFontUnderline(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setFormula(String formula) {
		try {
			getDelegate().setFormula(formula);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setHeaderAlignment(int alignment) {
		try {
			getDelegate().setHeaderAlignment(alignment);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setHeaderFontBold(boolean flag) {
		try {
			getDelegate().setHeaderFontBold(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setHeaderFontColor(int color) {
		try {
			getDelegate().setHeaderFontColor(color);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setHeaderFontFace(String face) {
		try {
			getDelegate().setHeaderFontFace(face);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setHeaderFontItalic(boolean flag) {
		try {
			getDelegate().setHeaderFontItalic(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setHeaderFontPointSize(int size) {
		try {
			getDelegate().setHeaderFontPointSize(size);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setHeaderFontStrikethrough(boolean flag) {
		try {
			getDelegate().setHeaderFontStrikethrough(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setHeaderFontStyle(int style) {
		try {
			getDelegate().setHeaderFontStyle(style);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setHeaderFontUnderline(boolean flag) {
		try {
			getDelegate().setHeaderFontUnderline(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setHidden(boolean flag) {
		try {
			getDelegate().setHidden(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setHideDetail(boolean flag) {
		try {
			getDelegate().setHideDetail(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setHideFormula(boolean flag) {
		try {
			getDelegate().setHideFormula(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setListSep(int separator) {
		try {
			getDelegate().setListSep(separator);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setNumberAttrib(int attributes) {
		try {
			getDelegate().setNumberAttrib(attributes);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setNumberAttribParens(boolean flag) {
		try {
			getDelegate().setNumberAttribParens(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setNumberAttribPercent(boolean flag) {
		try {
			getDelegate().setNumberAttribPercent(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setNumberAttribPunctuated(boolean flag) {
		try {
			getDelegate().setNumberAttribPunctuated(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setNumberDigits(int digits) {
		try {
			getDelegate().setNumberDigits(digits);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setNumberFormat(int format) {
		try {
			getDelegate().setNumberFormat(format);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setPosition(int position) {
		try {
			getDelegate().setPosition(position);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setResize(boolean flag) {
		try {
			getDelegate().setResize(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setResortAscending(boolean flag) {
		try {
			getDelegate().setResortAscending(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setResortDescending(boolean flag) {
		try {
			getDelegate().setResortDescending(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setResortToView(boolean flag) {
		try {
			getDelegate().setResortToView(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setResortToViewName(String name) {
		try {
			getDelegate().setResortToViewName(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setSecondaryResort(boolean flag) {
		try {
			getDelegate().setSecondaryResort(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setSecondaryResortColumnIndex(int index) {
		try {
			getDelegate().setSecondaryResortColumnIndex(index);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setSecondaryResortDescending(boolean flag) {
		try {
			getDelegate().setSecondaryResortDescending(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setShowTwistie(boolean flag) {
		try {
			getDelegate().setShowTwistie(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setSortDescending(boolean flag) {
		try {
			getDelegate().setSortDescending(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setSorted(boolean flag) {
		try {
			getDelegate().setSorted(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setTimeDateFmt(int format) {
		try {
			getDelegate().setTimeDateFmt(format);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setTimeFmt(int format) {
		try {
			getDelegate().setTimeFmt(format);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setTimeZoneFmt(int format) {
		try {
			getDelegate().setTimeZoneFmt(format);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setTitle(String title) {
		try {
			getDelegate().setTitle(title);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setWidth(int width) {
		try {
			getDelegate().setWidth(width);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
