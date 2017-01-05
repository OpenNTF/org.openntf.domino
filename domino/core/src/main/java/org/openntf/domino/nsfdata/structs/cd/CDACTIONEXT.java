package org.openntf.domino.nsfdata.structs.cd;

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
		BUTTON, CHECKBOX, MENU_SEPARATOR;
	}

	public final WSIG Header = inner(new WSIG());
	public final Unsigned32 dwFlags = new Unsigned32();
	public final Enum16<Type> wControlType = new Enum16<Type>(Type.values());
	public final Unsigned16 wControlFormulaLen = new Unsigned16();
	public final Unsigned16 wLabelFormulaLen = new Unsigned16();
	public final Unsigned16 wParentLabelFormulaLen = new Unsigned16();
	public final Unsigned16 wCompActionIDLen = new Unsigned16();
	public final Unsigned16 wProgrammaticUseTxtLen = new Unsigned16();
	public final Unsigned32[] dwExtra = array(new Unsigned32[2]);

	static {
		addVariableData("ControlFormula", "wControlFormulaLen");
		addVariableData("LabelFormula", "wLabelFormulaLen");
		addVariableData("ParentLabelFormula", "wParentLabelFormulaLen");
		addVariableString("CompActionID", "wCompActionIDLen");
		addVariableString("ProgrammaticUseTxt", "wProgrammaticUseTxtLen");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public NSFCompiledFormula getControlFormula() {
		return new NSFCompiledFormula((byte[]) getVariableElement("ControlFormula"));
	}

	/**
	 * @return Formula used for control's/menu's label.
	 */
	public NSFCompiledFormula getLabelFormula() {
		return new NSFCompiledFormula((byte[]) getVariableElement("LabelFormula"));
	}

	/**
	 * @return Formula used for control's/menu's "parent" label when action is first in a group.
	 */
	public NSFCompiledFormula getParentLabelFormula() {
		return new NSFCompiledFormula((byte[]) getVariableElement("ParentLabelFormula"));
	}

	public String getCompActionId() {
		return (String) getVariableElement("CompActionID");
	}

	public String getProgrammaticUseText() {
		return (String) getVariableElement("ProgrammaticUseText");
	}
}
