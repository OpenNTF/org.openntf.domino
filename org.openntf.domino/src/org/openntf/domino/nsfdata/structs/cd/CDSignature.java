package org.openntf.domino.nsfdata.structs.cd;

import static org.openntf.domino.nsfdata.structs.ODSConstants.RecordLength.BYTE;
import static org.openntf.domino.nsfdata.structs.ODSConstants.RecordLength.LONG;
import static org.openntf.domino.nsfdata.structs.ODSConstants.RecordLength.WORD;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.LSIG;
import org.openntf.domino.nsfdata.structs.ODSConstants.RecordLength;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

public enum CDSignature {
	PDEF_MAIN(WORD, 83, CDPDEF_MAIN.class),
	PDEF_TYPE(WORD, 84, CDPDEF_TYPE.class),
	PDEF_PROPERTY(WORD, 85, CDPDEF_PROPERTY.class),
	PDEF_ACTION(WORD, 86, CDPDEF_ACTION.class),
	TABLECELL_DATAFLAGS(BYTE, 87, null),
	EMBEDDEDCONTACTLIST(WORD, 88, null),
	IGNORE(BYTE, 89, null),
	TABLECELL_HREF2(WORD, 90, CDRESOURCE.class), // Note: this structure is shared among several records
	HREFBORDER(WORD, 91, null),
	TABLEDATAEXTENSION(WORD, 92, CDTABLEDATAEXTENSION.class),
	EMBEDDEDCALCTL(WORD, 93, null),
	ACTIONEXT(WORD, 94, CDACTIONEXT.class),
	EVENT_LANGUAGE_ENTRY(WORD, 95, CDEVENTENTRY.class),
	FILESEGMENT(LONG, 96, CDFILESEGMENT.class),
	FILEHEADER(LONG, 97, CDFILEHEADER.class),
	DATAFLAGS(BYTE, 98, CDDATAFLAGS.class),
	BACKGROUNDPROPERTIES(BYTE, 98, null),
	EMBEDEXTRA_INFO(WORD, 100, null),
	CLIENT_BLOBPART(WORD, 101, CDBLOBPART.class), // Note: this structure is shared with BLOBPART
	CLIENT_EVENT(WORD, 102, CDEVENT.class), // Note: this structure is shared with EVENT
	BORDERINFO_HS(WORD, 103, null),
	LARGE_PARAGRAPH(WORD, 104, CDLARGEPARAGRAPH.class),
	EXT_EMBEDDEDSCHED(WORD, 105, null),
	BOXSIZE(BYTE, 106, null),
	POSITIONING(BYTE, 107, null),
	LAYER(BYTE, 108, CDLAYER.class),
	DECSFIELD(WORD, 109, CDDECSFIELD.class),
	SPAN_END(BYTE, 110, null),
	SPAN_BEGIN(BYTE, 111, null),
	TEXTPROPERTIESTABLE(WORD, 112, null),
	HREF2(WORD, 113, CDRESOURCE.class), // Note: this structure is shared among several records
	BACKGROUNDCOLOR(BYTE, 114, CDCOLOR.class), // Note: this structure is shared with COLOR
	INLINE(WORD, 115, null), V6HOTSPOTBEGIN_CONTINUATION(WORD, 116, null), TARGET_DBLCLK(WORD, 117, CDTARGET.class),
	CAPTION(WORD, 118, CDCAPTION.class), LINKCOLORS(WORD, 119, null), TABLECELL_HREF(WORD, 120, CDRESOURCE.class),
	ACTIONBAREXT(WORD, 121, CDACTIONBAREXT.class), IDNAME(WORD, 122, CDIDNAME.class), TABLECELL_IDNAME(WORD, 123, CDIDNAME.class),
	IMAGESEGMENT(LONG, 124, CDIMAGESEGMENT.class), IMAGEHEADER(LONG, 125, CDIMAGEHEADER.class), V5HOTSPOTBEGIN(WORD, 126, null),
	V5HOTSPOTEND(BYTE, 127, null), TEXTPROPERTY(WORD, 128, null),
	PARAGRAPH(BYTE, 129, CDPARAGRAPH.class),
	PABDEFINITION(WORD, 130, CDPABDEFINITION.class),
	PABREFERENCE(BYTE, 131, CDPABREFERENCE.class),
	TEXT(WORD, 133, CDTEXT.class),
	HEADER(WORD, 142, null),
	LINKEXPORT2(WORD, 146, null),
	BITMAPHEADER(LONG, 149, CDBITMAPHEADER.class),
	BITMAPSEGMENT(LONG, 150, CDBITMAPSEGMENT.class),
	COLORTABLE(LONG, 151, CDCOLORTABLE.class),
	GRAPHIC(LONG, 153, CDGRAPHIC.class),
	PMMETASEC(LONG, 154, null),
	WINMETASEG(LONG, 155, CDWINMETASEG.class),
	MACMETASEG(LONG, 156, null),
	CGMETA(LONG, 157, null),
	PMMETAHEADER(LONG, 158, null),
	WINMETAHEADER(LONG, 159, CDWINMETAHEADER.class),
	MACMETAHEADER(LONG, 160, null),
	TABLEBEGIN(BYTE, 163, CDTABLEBEGIN.class),
	TABLECELL(BYTE, 164, CDTABLECELL.class),
	TABLEEND(BYTE, 165, CDTABLEEND.class),
	STYLENAME(BYTE, 166, null),
	STORAGELINK(WORD, 196, null),
	TRANSPARENTTABLE(LONG, 197, null),
	HORIZONTALRULE(WORD, 201, CDHRULE.class),
	ALTTEXT(WORD, 202, null),
	ANCHOR(WORD, 203, null),
	HTMLBEGIN(WORD, 204, CDHTMLBEGIN.class),
	HTMLEND(WORD, 205, CDHTMLEND.class),
	HTMLFORMULA(WORD, 206, null),
	NESTEDTABLEBEGIN(BYTE, 207, CDTABLEBEGIN.class),
	NESTEDTABLECELL(BYTE, 208, CDTABLECELL.class),
	NESTEDTABLEEND(BYTE, 209, CDTABLEEND.class),
	COLOR(BYTE, 210, CDCOLOR.class),
	TABLECELL_COLOR(BYTE, 211, CDCOLOR.class), // Note: this structure is shared with COLOR
	BLOBPART(WORD, 220, CDBLOBPART.class),
	BEGIN(BYTE, 221, CDBEGINRECORD.class),
	END(BYTE, 222, CDENDRECORD.class),
	VERTICALALIGN(BYTE, 223, null),
	FLOATPOSITION(BYTE, 224, null),
	TIMERINFO(BYTE, 225, null),
	TABLEROWHEIGHT(BYTE, 226, CDTABLEROWHEIGHT.class),
	TABLELABEL(WORD, 227, CDTABLELABEL.class),
	BIDI_TEXT(WORD, 228, null),
	BIDI_TEXTEFFECT(WORD, 229, null),
	REGIONBEGIN(WORD, 230, CDREGIONBEGIN.class),
	REGIONEND(WORD, 231, CDREGIONEND.class),
	TRANSITION(WORD, 232, null),
	FIELDHINT(WORD, 233, null),
	PLACEHOLDER(WORD, 234, null),
	EMBEDDEDOUTLINE(WORD, 236, null),
	EMBEDDEDVIEW(WORD, 237, null),
	CELLBACKGROUNDDATA(WORD, 238, CDCELLBACKGROUNDDATA.class),
	FRAMESETHEADER(WORD, 239, CDFRAMESETHEADER.class),
	FRAMESET(WORD, 240, CDFRAMESET.class),
	FRAME(WORD, 241, CDFRAME.class),
	TARGET(WORD, 242, CDTARGET.class),
	MAPELEMENT(WORD, 244, null),
	AREAELEMENT(WORD, 245, null),
	HREF(WORD, 246, CDRESOURCE.class), // Note: this structure is shared among several records
	EMBEDDEDCTL(WORD, 247, CDEMBEDDEDCTL.class), HTML_ALTTEXT(WORD, 248, null), EVENT(WORD, 249, CDEVENT.class),
	PRETABLEBEGIN(WORD, 251, CDPRETABLEBEGIN.class), BORDERINFO(WORD, 252, CDBORDERINFO.class), EMBEDDEDSCHEDCTL(WORD, 253, null),
	EXT2_FIELD(WORD, 254, CDEXT2FIELD.class), EMBEDDEDEDITCTL(WORD, 255, null),

	DOCUMENT_PRE_26(BYTE, 128, null), FIELD_PRE_36(WORD, 132, null), FIELD(WORD, 138, CDFIELD.class),
	DOCUMENT(BYTE, 134, CDDOCUMENT.class), METAFILE(WORD, 135, null), BITMAP(WORD, 136, null), FONTTABLE(WORD, 139, CDFONTTABLE.class),
	LINK(BYTE, 140, null), LINKEXPORT(BYTE, 141, null), KEYWORD(WORD, 143, null), LINK2(WORD, 145, CDLINK2.class), CGM(WORD, 147, null),
	TIFF(LONG, 148, null), PATTERNTABLE(LONG, 152, CDPATTERNTABLE.class), DDEBEGIN(WORD, 161, null), DDEEND(WORD, 162, null),
	OLEBEGIN(WORD, 167, CDOLEBEGIN.class), OLEEND(WORD, 168, CDOLEEND.class), HOTSPOTBEGIN(WORD, 169, CDHOTSPOTBEGIN.class),
	HOTSPOTEND(BYTE, 170, CDHOTSPOTEND.class), BUTTON(WORD, 171, CDBUTTON.class), BAR(WORD, 172, CDBAR.class),
	V4HOTSPOTBEGIN(WORD, 173, CDHOTSPOTBEGIN.class), V4HOTSPOTEND(BYTE, 174, CDHOTSPOTEND.class), EXT_FIELD(WORD, 176, CDEXTFIELD.class),
	LSOBJECT(WORD, 177, CDLSOBJECT.class), HTMLHEADER(WORD, 178, null), HTMLSEGMENT(WORD, 179, null), LAYOUT(BYTE, 183, CDLAYOUT.class),
	LAYOUTTEXT(BYTE, 184, CDLAYOUTTEXT.class), LAYOUTEND(BYTE, 185, CDLAYOUTEND.class), LAYOUTFIELD(BYTE, 186, CDLAYOUTFIELD.class),
	PABHIDE(WORD, 187, CDPABHIDE.class), PABFORMREF(BYTE, 188, CDPABFORMULAREF.class), ACTIONBAR(BYTE, 189, CDACTIONBAR.class),
	ACTION(WORD, 190, LONG, CDACTION.class), DOCAUTOLAUNCH(WORD, 191, CDDOCAUTOLAUNCH.class),
	LAYOUTGRAPHIC(BYTE, 192, CDLAYOUTGRAPHIC.class), OLEOBJINFO(WORD, 193, CDOLEOBJ_INFO.class),
	LAYOUTBUTTON(BYTE, 194, CDLAYOUTBUTTON.class), TEXTEFFECT(WORD, 195, null),

	VMHEADER(BYTE, 175, null), VMBITMAP(BYTE, 176, null), BMRECT(BYTE, 177, null), VMPOLYGON_BYTE(BYTE, 178, null),
	VMPOLYLINE_BYTE(BYTE, 179, null), VMREGION(BYTE, 180, null), VMACTION(BYTE, 181, null), VMELLIPSE(BYTE, 182, null),
	VMRNDRECT(BYTE, 184, null), VMBUTTON(BYTE, 185, null), VMACTION_2(WORD, 186, null), VMTEXTBOX(WORD, 197, null),
	VMPOLYGON(WORD, 188, null), VMPOLYLINE(WORD, 190, null), VMCIRCLE(BYTE, 191, null), VMPOLYRGN_BYTE(BYTE, 192, null),

	ALTERNATEBEGIN(WORD, 198, null), ALTERNATEEND(BYTE, 199, null), OLERTMARKER(WORD, 200, null);

	private final RecordLength recordLength_;
	private final int baseValue_;
	private final RecordLength trueRecordLength_;
	private final Class<? extends CDRecord> clazz_;

	private CDSignature(final RecordLength recordLength, final int baseValue, final Class<? extends CDRecord> clazz) {
		recordLength_ = recordLength;
		baseValue_ = baseValue;
		trueRecordLength_ = null;
		clazz_ = clazz;
	}

	/**
	 * This exists because the ACTION record type has a signature defined with WORD but actually uses an LSIG
	 */
	private CDSignature(final RecordLength recordLength, final int baseValue, final RecordLength trueRecordLength,
			final Class<? extends CDRecord> clazz) {
		recordLength_ = recordLength;
		baseValue_ = baseValue;
		trueRecordLength_ = trueRecordLength;
		clazz_ = clazz;
	}

	public int getBaseValue() {
		return baseValue_;
	}

	public RecordLength getRecordLength() {
		return recordLength_;
	}

	public RecordLength getEffectiveRecordLength() {
		return trueRecordLength_ != null ? trueRecordLength_ : recordLength_;
	}

	public Class<? extends CDRecord> getInstanceClass() {
		return clazz_;
	}

	public int getSize() {
		if (trueRecordLength_ != null) {
			switch (trueRecordLength_) {
			case BYTE:
				return 2;
			case LONG:
				return 6;
			case WORD:
				return 4;
			default:
				return 0;
			}
		} else {
			switch (recordLength_) {
			case BYTE:
				return 2;
			case LONG:
				return 6;
			case WORD:
				return 4;
			default:
				return 0;
			}
		}
	}

	public static SIG sigForData(final ByteBuffer data) {
		data.order(ByteOrder.LITTLE_ENDIAN);
		//		System.out.println("reading sig at position: " + data.position());
		byte lowOrderByte = data.get(data.position());
		int lowOrder = lowOrderByte & 0xFF;
		int highOrder = data.get(data.position() + 1) & 0xFF;
		//		System.out.println("low order: " + lowOrder);
		//		System.out.println("low order byte: " + lowOrderByte);
		//		System.out.println("high order: " + highOrder);

		RecordLength length = RecordLength.valueOf(highOrder);

		//		int value = lowOrder | highOrder;
		for (CDSignature cdSig : values()) {
			if (lowOrder == cdSig.getBaseValue() && length == cdSig.getRecordLength()) {
				//				System.out.println("matched signature " + cdSig);
				// Now determine which SIG to return based on the record length
				switch (cdSig.getEffectiveRecordLength()) {
				case BYTE:
					return new BSIG(cdSig, highOrder);
				case WORD:
					return new WSIG(cdSig, data.getShort(data.position() + 2) & 0xFFFF);
				case LONG:
					return new LSIG(cdSig, data.getInt(data.position() + 2) & 0xFFFFFFFFL);
				default:
					break;
				}
			}
		}
		throw new IllegalArgumentException("Unknown sig value " + lowOrder + " for length " + length);
	}
}