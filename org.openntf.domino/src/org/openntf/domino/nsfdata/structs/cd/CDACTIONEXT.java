package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.NSFCompiledFormula;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * New field attributes have been added in Notes/Domino 6. To preserve compatibility with existing applications, the new attributes have
 * been placed in this extension to the CDACTION record. This record is optional, and may not be present in the $Body item of the form note.
 * 
 * In R5, a view action's hide whens, and hence state, is only evaluated once when the view is loaded. Notes/Domino 6 allows the action's
 * state to be evaluated each time a different document becomes current. A checkbox in the action bar and/or a checked item on the action
 * menu can change to reflect a value in the currently selected document. The control formula value evaluates to true (checked) or false
 * (unchecked). The wControlType member of CDACTIONEXT must be ACTION_CONTROL_TYPE_CHECKBOX and the view must allow for evaluating actions
 * for every document changed. (actods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDACTIONEXT extends CDRecord {
	public static enum Type {
		BUTTON((short) 0), CHECKBOX((short) 1), MENU_SEPARATOR((short) 2);

		private final short value_;

		private Type(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static Type valueOf(final short typeCode) {
			for (Type type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching Type found for type code " + typeCode);
		}
	}

	static {
		addFixed("dwFlags", Integer.class);
		addFixed("wControlType", Short.class);
		addFixedUnsigned("wControlFormulaLen", Short.class);
		addFixedUnsigned("wLabelFormulaLen", Short.class);
		addFixedUnsigned("wParentLabelFormulaLen", Short.class);
		addFixedUnsigned("wCompActionIDLen", Short.class);
		addFixedUnsigned("wProgrammaticUseTxtLen", Short.class);
		addFixedArray("dwExtra", Integer.class, 2);

		addVariableData("ControlFormula", "wControlFormulaLen");
		addVariableData("LabelFormula", "wLabelFormulaLen");
		addVariableData("ParentLabelFormula", "wParentLabelFormulaLen");
		addVariableString("CompActionID", "wCompActionIDLen");
		addVariableString("ProgrammaticUseTxt", "wProgrammaticUseTxtLen");
	}

	public static final int SIZE = getFixedStructSize();

	public CDACTIONEXT(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDACTIONEXT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public Type getControlType() {
		return Type.valueOf((Short) getStructElement("wControlType"));
	}

	public NSFCompiledFormula getControlFormula() {
		return new NSFCompiledFormula((byte[]) getStructElement("ControlFormula"));
	}

	/**
	 * @return Formula used for control's/menu's label.
	 */
	public NSFCompiledFormula getLabelFormula() {
		return new NSFCompiledFormula((byte[]) getStructElement("LabelFormula"));
	}

	/**
	 * @return Formula used for control's/menu's "parent" label when action is first in a group.
	 */
	public NSFCompiledFormula getParentLabelFormula() {
		return new NSFCompiledFormula((byte[]) getStructElement("ParentLabelFormula"));
	}

	public String getCompActionId() {
		return (String) getStructElement("CompActionID");
	}

	public String getProgrammaticUseText() {
		return (String) getStructElement("ProgrammaticUseText");
	}
}
