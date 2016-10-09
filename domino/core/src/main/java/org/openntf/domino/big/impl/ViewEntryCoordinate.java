package org.openntf.domino.big.impl;

import static org.openntf.domino.big.NoteCoordinate.Utils.insertByteArray;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.openntf.domino.Document;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;
import org.openntf.domino.exceptions.UnimplementedException;

public class ViewEntryCoordinate implements org.openntf.domino.big.ViewEntryCoordinate {
	public static final char POS_SEP = '.';
	private NoteCoordinate parentViewCoordinate_;
	private String position_;
	private String entryType_;
	private transient ViewNavigator sourceNav_;

	//	private transient ViewEntry sourceEntry_;

	public ViewEntryCoordinate(final ViewEntry viewEntry) {
		//		System.out.println("A ViewEntryCoordinate is being created with a viewentry");
		//		new Throwable().printStackTrace();

		parentViewCoordinate_ = new NoteCoordinate(viewEntry.getParentView());
		position_ = viewEntry.getPosition();
		if (viewEntry.isCategory()) {
			entryType_ = "EC";
		} else if (viewEntry.isDocument()) {
			entryType_ = "ED";
		} else if (viewEntry.isTotal()) {
			entryType_ = "ET";
		} else {
			entryType_ = "EU";
		}
	}

	public ViewEntryCoordinate(final CharSequence metaversalid) {
		//		if (metaversalid.length() < 50) {
		//			System.out.println("A ViewEntryCoordinate is being created with a bogus metaversalid: " + metaversalid);
		//			new Throwable().printStackTrace();
		//		}
		entryType_ = metaversalid.subSequence(0, 2).toString();
		parentViewCoordinate_ = new NoteCoordinate(metaversalid.subSequence(2, 50));
		position_ = metaversalid.subSequence(50, metaversalid.length()).toString();
	}

	@Override
	public void setSourceNav(final ViewNavigator nav) {
		sourceNav_ = nav;
	}

	@Override
	public void readExternal(final ObjectInput arg0) throws IOException, ClassNotFoundException {
		parentViewCoordinate_ = (NoteCoordinate) arg0.readObject();
		position_ = arg0.readUTF();
		entryType_ = arg0.readUTF();
		//		System.out.println("TEMP DEBUG deserializing a ViewEntryCoordinate");
	}

	@Override
	public void writeExternal(final ObjectOutput arg0) throws IOException {
		arg0.writeObject(parentViewCoordinate_);
		arg0.writeUTF(position_);
		arg0.writeUTF(entryType_);
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
	public String getUNID() {
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

	@Override
	public Document getDocument() {
		ViewEntry entry = getViewEntry();
		if (entry.isDocument()) {
			return entry.getDocument();
		}
		return null;
	}

	@Override
	public Document getDocument(final String serverName) {
		return getDocument();
	}

	@Override
	public Object get(final String key) {
		return getViewEntry().get(key);
	}

	@Override
	public boolean isView() {
		return false;
	}

	@Override
	public boolean isIcon() {
		return false;
	}

	@Override
	public int compareTo(final org.openntf.domino.big.NoteCoordinate arg0) {
		throw new UnimplementedException("compareTo has not yet been implemented in ViewEntryCoordinate");
	}

	@Override
	public byte[] toByteArray() {
		byte[] result = new byte[24];
		insertByteArray(parentViewCoordinate_.db, result, 0);
		insertByteArray(parentViewCoordinate_.x, result, 8);
		insertByteArray(parentViewCoordinate_.y, result, 16);
		return result;
	}

	@Override
	public int insertToByteArray(final byte[] bytes, final int pos) {
		insertByteArray(parentViewCoordinate_.db, bytes, pos + 0);
		insertByteArray(parentViewCoordinate_.x, bytes, pos + 8);
		insertByteArray(parentViewCoordinate_.y, bytes, pos + 16);
		return pos + 24;
	}

	@Override
	public String toString() {
		return entryType_ + parentViewCoordinate_.toString() + getPosition();
	}

	@Override
	public View getView() {
		return parentViewCoordinate_.getView();
	}

	@Override
	public Document getViewDocument() {
		return parentViewCoordinate_.getDocument();
	}

	@Override
	public String getEntryType() {
		return entryType_;
	}

	@Override
	public long getX() {
		throw new UnimplementedException("X can't be exported for ViewEntryCoordinates");
	}

	@Override
	public long getY() {
		throw new UnimplementedException("Y can't be exported for ViewEntryCoordinates");
	}

}
