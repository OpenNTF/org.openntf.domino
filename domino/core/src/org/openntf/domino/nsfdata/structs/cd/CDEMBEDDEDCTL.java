package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record may further define attributes within a CDFIELD such as tab order.
 * 
 * @since Lotus Notes/Domino 5.0
 */
public class CDEMBEDDEDCTL extends CDRecord {
	public static enum CtlVersion {
		VERSION1
	}

	/**
	 * Use getCtlStyle for access.
	 */
	@Deprecated
	public final Unsigned32 CtlStyle = new Unsigned32();
	/**
	 * Use getFlags for access.
	 */
	@Deprecated
	public final Unsigned16 Flags = new Unsigned16();
	public final Unsigned16 Width = new Unsigned16();
	public final Unsigned16 Height = new Unsigned16();
	public final Enum16<CtlVersion> Version = new Enum16<CtlVersion>(CtlVersion.values());
	/**
	 * Use getCtlType for access.
	 */
	@Deprecated
	public final Unsigned16 CtlType = new Unsigned16();
	public final Unsigned16 MaxChars = new Unsigned16();
	public final Unsigned16 MaxLines = new Unsigned16();
	public final Unsigned16 Percentage = new Unsigned16();
	public final Unsigned32[] Spare = array(new Unsigned32[3]);

	public CDEMBEDDEDCTL(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDEMBEDDEDCTL(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Embedded control Style, see EC_STYLE_xxx
	 */
	public int getCtlStyle() {
		// TODO make enum
		return (int) CtlStyle.get();
	}

	/**
	 * @return Embedded control Flags, see EC_FLAG_xxx
	 */
	public short getFlags() {
		// TODO make enum
		return (short) Flags.get();
	}

	/**
	 * @return Embedded control type, see EMBEDDEDCTL_xxx
	 */
	public short getCtlType() {
		// TODO make enum
		return (short) CtlType.get();
	}
}
