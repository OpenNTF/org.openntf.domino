package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.LENGTH_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record defines the Action Bar attributes. It is an extension of the CDACTIONBAR record. It is found within a $V5ACTIONS item and
 * is preceded by a CDACTIONBAR record. (actods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 */
public class CDACTIONBAREXT extends CDRecord {
	public static enum BackgroundRepeat {
		DEFAULT,
		/**
		 * Image repeats once in upper left of action bar
		 */
		REPEATONCE,
		/**
		 * Image repeats vertically along left of action bar
		 */
		REPEATVERT,
		/**
		 * Image repeats horizontally along top of action bar
		 */
		REPEATHORIZ,
		/**
		 * Image "tiles" (repeats) to fit action bar
		 */
		TILE,
		/**
		 * Image is divided and "tiled" (repeated) to fit action bar
		 */
		CENTER_TILE,
		/**
		 * Image is sized to fit action bar
		 */
		REPEATSIZE,
		/**
		 * Image is centered in action bar
		 */
		REPEATCENTER
	}

	public static enum ButtonWidth {
		/**
		 * Width is calculated based on text length and image width
		 */
		DEFAULT,
		/**
		 * Width is at least button background image width or wider if needed to fit text and image
		 */
		BACKGROUND,
		/**
		 * Width is set to value in wBtnWidthAbsolute
		 */
		ABSOLUTE
	}

	public static enum Justify {
		LEFT, CENTER, RIGHT;
	}

	public static enum BorderDisplay {
		ONMOUSEOVER, ALWAYS, NEVER, NOTES
	}

	public static enum Flag {
		/**
		 * Width style is valid
		 */
		WIDTH_STYLE_VALID(0x00000001);

		private final int value_;

		private Flag(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static Set<Flag> valuesOf(final int flags) {
			Set<Flag> result = EnumSet.noneOf(Flag.class);
			for (Flag flag : values()) {
				if ((flag.getValue() & flags) > 0) {
					result.add(flag);
				}
			}
			return result;
		}
	}

	public final COLOR_VALUE BackColor = inner(new COLOR_VALUE());
	public final COLOR_VALUE LineColor = inner(new COLOR_VALUE());
	public final COLOR_VALUE FontColor = inner(new COLOR_VALUE());
	public final COLOR_VALUE ButtonColor = inner(new COLOR_VALUE());
	public final Enum16<BorderDisplay> BtnBorderDisplay = new Enum16<BorderDisplay>(BorderDisplay.values());
	public final Unsigned16 wAppletHeight = new Unsigned16();
	public final Enum16<BackgroundRepeat> wBarBackgroundRepeat = new Enum16<BackgroundRepeat>(BackgroundRepeat.values());
	public final Enum8<ButtonWidth> BtnWidthStyle = new Enum8<ButtonWidth>(ButtonWidth.values());
	public final Enum8<Justify> BtnTextJustify = new Enum8<Justify>(Justify.values());
	public final Unsigned16 wBtnWidthAbsolute = new Unsigned16();
	public final Unsigned16 wBtnInternalMargin = new Unsigned16();
	/**
	 * Use getFlags for access.
	 */
	@Deprecated
	public final Unsigned32 dwFlags = new Unsigned32();
	public final FONTID barFontID = inner(new FONTID());
	public final LENGTH_VALUE barHeight = inner(new LENGTH_VALUE());
	public final Unsigned32[] Spare = array(new Unsigned32[12]);

	public CDACTIONBAREXT(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDACTIONBAREXT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return See ACTIONBAREXT_xxx flags
	 */
	public Set<Flag> getFlags() {
		return Flag.valuesOf((int) dwFlags.get());
	}

}
