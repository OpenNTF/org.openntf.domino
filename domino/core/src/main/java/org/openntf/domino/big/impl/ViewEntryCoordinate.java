package org.openntf.domino.big.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;

public class ViewEntryCoordinate implements org.openntf.domino.big.ViewEntryCoordinate {
	public static final char POS_SEP = '.';
	private NoteCoordinate parentViewCoordinate_;
	private String position_;
	private transient ViewNavigator sourceNav_;

	public ViewEntryCoordinate(final ViewEntry viewEntry) {
		parentViewCoordinate_ = new NoteCoordinate(viewEntry.getParentView());
	}

	@Override
	public void readExternal(final ObjectInput arg0) throws IOException, ClassNotFoundException {
		parentViewCoordinate_ = (NoteCoordinate) arg0.readObject();
		position_ = arg0.readUTF();
	}

	@Override
	public void writeExternal(final ObjectOutput arg0) throws IOException {
		arg0.writeObject(parentViewCoordinate_);
		arg0.writeUTF(position_);
	}

	@Override
	public String getReplicaId() {
		return parentViewCoordinate_.getReplicaId();
	}

	@Override
	public Long getReplicaLong() {
		return parentViewCoordinate_.getReplicaLong();
	}

	@Override
	public String getViewUNID() {
		return parentViewCoordinate_.getUNID();
	}

	@Override
	public String getPosition() {
		return position_;
	}

	@Override
	public ViewEntry getViewEntry() {
		if (sourceNav_ == null) {
			View view = parentViewCoordinate_.getView();
			return view.getEntryAtPosition(getPosition());
		} else {
			return sourceNav_.getPos(getPosition(), ViewNavigator.DEFAULT_SEPARATOR);
		}
	}

}
