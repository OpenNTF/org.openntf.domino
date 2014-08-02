package org.openntf.domino.nsfdata.structs.cd;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.AbstractStruct;
import org.openntf.domino.nsfdata.structs.SIG;

public abstract class CDRecord extends AbstractStruct {
	private static final long serialVersionUID = 1L;

	public static CDRecord create(final SIG signature, final ByteBuffer data) {
		switch (signature.getSignature()) {
		case ACTION:
			return new CDACTION(signature, data);
		case ACTIONBAR:
			return new CDACTIONBAR(signature, data);
		case ACTIONBAREXT:
			return new CDACTIONBAREXT(signature, data);
		case BEGIN:
			return new CDBEGINRECORD(signature, data);
		case BORDERINFO:
			return new CDBORDERINFO(signature, data);
		case COLOR:
			return new CDCOLOR(signature, data);
		case DATAFLAGS:
			return new CDDATAFLAGS(signature, data);
		case DECSFIELD:
			return new CDDECSFIELD(signature, data);
		case DOCUMENT:
			return new CDDOCUMENT(signature, data);
		case EMBEDDEDCTL:
			return new CDEMBEDDEDCTL(signature, data);
		case END:
			return new CDENDRECORD(signature, data);
		case EVENT_LANGUAGE_ENTRY:
			return new CDEVENTENTRY(signature, data);
		case EXT_FIELD:
			return new CDEXTFIELD(signature, data);
		case EXT2_FIELD:
			return new CDEXT2FIELD(signature, data);
		case FIELD:
			return new CDFIELD(signature, data);
		case FILEHEADER:
			return new CDFILEHEADER(signature, data);
		case FILESEGMENT:
			return new CDFILESEGMENT(signature, data);
		case GRAPHIC:
			return new CDGRAPHIC(signature, data);
		case IMAGEHEADER:
			return new CDIMAGEHEADER(signature, data);
		case IMAGESEGMENT:
			return new CDIMAGESEGMENT(signature, data);
		case LARGE_PARAGRAPH:
			return new CDLARGEPARAGRAPH(signature, data);
		case LINK2:
			return new CDLINK2(signature, data);
		case PABDEFINITION:
			return new CDPABDEFINITION(signature, data);
		case PABREFERENCE:
			return new CDPABREFERENCE(signature, data);
		case PARAGRAPH:
			return new CDPARAGRAPH(signature, data);
		case PRETABLEBEGIN:
			return new CDPRETABLEBEGIN(signature, data);
		case TABLEBEGIN:
			return new CDTABLEBEGIN(signature, data);
		case TABLECELL:
			return new CDTABLECELL(signature, data);
		case TABLEDATAEXTENSION:
			return new CDTABLEDATAEXTENSION(signature, data);
		case TABLEEND:
			return new CDTABLEEND(signature, data);
		case TEXT:
			return new CDTEXT(signature, data);
		default:
			return new BasicCDRecord(signature, data);
		}
	}

	private SIG signature_;

	protected CDRecord(final SIG signature, final ByteBuffer data) {
		super(data);
		signature_ = signature;
	}

	/**
	 * @return Signature and length of this record
	 */
	public SIG getSignature() {
		return signature_;
	}

	/**
	 * @return The length (in bytes) of the data portion of this record
	 */
	public int getDataLength() {
		return signature_.getLength() - signature_.getSigLength();
	}

	/**
	 * @return Any additional bytes at the end of the record used for word alignment
	 */
	public int getExtraLength() {
		return 0;
	}

	@Override
	public int getStructSize() {
		return signature_.getLength();
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + " " + getSignature().getSignature() + "]";
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		signature_ = (SIG) in.readObject();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		super.writeExternal(out);
		// TODO change this to write the signature bytes directly
		out.writeObject(signature_);
	}
}
