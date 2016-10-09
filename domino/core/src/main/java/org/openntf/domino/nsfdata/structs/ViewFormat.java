package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

import org.openntf.domino.nsfdata.NSFCompiledFormula;

/**
 * ViewFormat is a convenience class to wrap the components of a $VIEWFORMAT item
 * 
 * @author jgallagher
 *
 */
public class ViewFormat {
	private VIEW_TABLE_FORMAT format_;
	private VIEW_COLUMN_FORMAT[] columnFormats_;
	private String[] itemNames_;
	private NSFCompiledFormula[] formulas_;
	private NSFCompiledFormula[] constantValues_;
	private String[] columnTitles_;
	private VIEW_TABLE_FORMAT2 format2_;
	private VIEW_COLUMN_FORMAT2[] columnFormats2_;
	private NSFCompiledFormula[] hideWhenFormulas_;
	private String[] twistieResources_;
	private VIEW_TABLE_FORMAT3 format3_;

	public ViewFormat(final ByteBuffer data) {
		ByteBuffer localData = data.duplicate().order(ByteOrder.LITTLE_ENDIAN);

		// Read in the opening VIEW_TABLE_FORMAT
		format_ = new VIEW_TABLE_FORMAT();
		format_.init(localData);
		localData.position(localData.position() + format_.size());

		int columnCount = format_.Columns.get();

		// Read in the array of VIEW_COLUMN_FORMATs
		columnFormats_ = new VIEW_COLUMN_FORMAT[columnCount];
		for (int i = 0; i < columnCount; i++) {
			columnFormats_[i] = new VIEW_COLUMN_FORMAT();
			ByteBuffer columnData = localData.duplicate();
			columnData.limit(columnData.position() + columnFormats_[i].size());
			columnFormats_[i].init(columnData);

			localData.position(localData.position() + columnFormats_[i].size());
		}

		// Read in the item name, formula, and title pairs for each column
		itemNames_ = new String[columnCount];
		formulas_ = new NSFCompiledFormula[columnCount];
		constantValues_ = new NSFCompiledFormula[columnCount];
		columnTitles_ = new String[columnCount];
		for (int i = 0; i < columnCount; i++) {
			int itemNameSize = columnFormats_[i].ItemNameSize.get();
			if (itemNameSize > 0) {
				byte[] itemNameBytes = new byte[itemNameSize];
				localData.get(itemNameBytes);
				itemNames_[i] = ODSUtils.fromLMBCS(itemNameBytes);
			} else {
				itemNames_[i] = "";
			}

			int columnTitleSize = columnFormats_[i].TitleSize.get();
			if (columnTitleSize > 0) {
				byte[] columnTitleBytes = new byte[columnTitleSize];
				localData.get(columnTitleBytes);
				columnTitles_[i] = ODSUtils.fromLMBCS(columnTitleBytes);
			} else {
				columnTitles_[i] = "";
			}

			int formulaSize = columnFormats_[i].FormulaSize.get();
			if (formulaSize > 0) {
				byte[] formulaBytes = new byte[formulaSize];
				localData.get(formulaBytes);
				formulas_[i] = new NSFCompiledFormula(formulaBytes);
			} else {
				formulas_[i] = null;
			}

			int constantValueSize = columnFormats_[i].ConstantValueSize.get();
			if (constantValueSize > 0) {
				byte[] constantValueBytes = new byte[constantValueSize];
				localData.get(constantValueBytes);
				constantValues_[i] = new NSFCompiledFormula(constantValueBytes);
			} else {
				constantValues_[i] = null;
			}

		}

		// VIEW_TABLE_FORMAT2 exists for views saved in Notes 2.0 and later
		if (localData.hasRemaining()) {
			format2_ = new VIEW_TABLE_FORMAT2();
			format2_.init(localData);
			localData.position(localData.position() + format2_.size());
		} else {
			format2_ = null;
		}

		// one VIEW_COLUMN_FORMAT2 for each column saved in Notes 4.0 and later
		if (localData.hasRemaining()) {
			columnFormats2_ = new VIEW_COLUMN_FORMAT2[columnCount];
			for (int i = 0; i < columnCount; i++) {
				columnFormats2_[i] = new VIEW_COLUMN_FORMAT2();
				ByteBuffer columnData = localData.duplicate();
				columnData.limit(columnData.position() + columnFormats2_[i].size());
				columnFormats2_[i].init(columnData);

				localData.position(localData.position() + columnFormats2_[i].size());
			}

			// Now read in their variable data
			hideWhenFormulas_ = new NSFCompiledFormula[columnCount];
			twistieResources_ = new String[columnCount];
			for (int i = 0; i < columnCount; i++) {
				int hideWhenFormulaSize = columnFormats2_[i].wHideWhenFormulaSize.get();
				if (hideWhenFormulaSize > 0) {
					byte[] hideWhenFormulaBytes = new byte[hideWhenFormulaSize];
					localData.get(hideWhenFormulaBytes);
					hideWhenFormulas_[i] = new NSFCompiledFormula(hideWhenFormulaBytes);
				} else {
					hideWhenFormulas_[i] = null;
				}

				int twistieResourceSize = columnFormats2_[i].wTwistieResourceSize.get();
				if (twistieResourceSize > 0) {
					byte[] twistieResourceBytes = new byte[twistieResourceSize];
					localData.get(twistieResourceBytes);
					twistieResources_[i] = ODSUtils.fromLMBCS(twistieResourceBytes);
				} else {
					twistieResources_[i] = "";
				}
			}
		} else {
			columnFormats2_ = new VIEW_COLUMN_FORMAT2[0];
			hideWhenFormulas_ = new NSFCompiledFormula[0];
			twistieResources_ = new String[0];
		}

		// one VIEW_COLUMN_FORMAT3 for each column saved in Notes 5.0 and later
		// TODO Nifty 50 trleader.nsf view "TeamRoom Leader's Guide" has 8 bytes remaining at this point somehow
		//		if (localData.hasRemaining()) {
		//			System.out.println("remaining amount: " + localData.remaining());
		//			format3_ = new VIEW_TABLE_FORMAT3();
		//			format3_.init(localData);
		//			localData.position(localData.position() + format3_.size());
		//		}
	}

	public VIEW_TABLE_FORMAT getFormat() {
		return format_;
	}

	public List<VIEW_COLUMN_FORMAT> getColumnFormats() {
		return Arrays.asList(columnFormats_);
	}

	public List<String> getItemNames() {
		return Arrays.asList(itemNames_);
	}

	public List<NSFCompiledFormula> getFormulas() {
		return Arrays.asList(formulas_);
	}

	public List<NSFCompiledFormula> getConstantValues() {
		return Arrays.asList(constantValues_);
	}

	public List<String> getColumnTitles() {
		return Arrays.asList(columnTitles_);
	}

	public VIEW_TABLE_FORMAT2 getFormat2() {
		return format2_;
	}

	public List<VIEW_COLUMN_FORMAT2> getColumnFormats2() {
		return Arrays.asList(columnFormats2_);
	}

	public List<NSFCompiledFormula> getHideWhenFormulas() {
		return Arrays.asList(hideWhenFormulas_);
	}

	public List<String> getTwistieResources() {
		return Arrays.asList(twistieResources_);
	}

	public VIEW_TABLE_FORMAT3 getFormat3() {
		return format3_;
	}
}
