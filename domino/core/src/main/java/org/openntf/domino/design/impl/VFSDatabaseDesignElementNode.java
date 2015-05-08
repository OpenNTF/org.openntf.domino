package org.openntf.domino.design.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.design.DesignBase;
import org.openntf.domino.design.DesignBaseNamed;
import org.openntf.domino.design.DxlConverter;
import org.openntf.domino.design.VFSNode;
import org.openntf.domino.utils.Factory.SessionType;

/**
 * This node represents a DesignElement node. You can read/write it's content
 * 
 * TODO: There is ONE file representation per Design-Document. It does not yet support XSP-Config or MetaData files!
 * 
 * @author Roland Praml, FOCONIS AG
 *
 */
public class VFSDatabaseDesignElementNode extends VFSAbstractNode<Void> {

	//private SoftReference<DesignBase> designElementRef;
	DesignBase designElement = null;
	private int contentLength = -1;
	private long lastSeen = -1;
	private Class<? extends DesignBase> designClass;
	private String designUnid;

	public VFSDatabaseDesignElementNode(final VFSNode parent, final String name, final DesignBase t) {
		super(parent, name);
		//designElementRef = new SoftReference<DesignBase>(t);
		designElement = t;
		designClass = t.getClass();
		designUnid = t.getUniversalID();
	}

	protected Database getDatabase() {
		VFSNode parent = this;
		while (parent != null) {
			parent = parent.getParent();
			if (parent.isNSF()) {
				return SessionType.CURRENT.get().getDatabase(parent.getPath());
			}
		}
		return null;

	}

	protected synchronized DesignBase getDesignElement() {
		//		DesignBase designElement = designElementRef == null ? null : designElementRef.get();
		//		Database db = getDatabase();
		//		if (designElement == null) {
		//			try {
		//				designElement = designClass.newInstance();
		//			} catch (InstantiationException e) {
		//				DominoUtils.handleException(e);
		//			} catch (IllegalAccessException e) {
		//				DominoUtils.handleException(e);
		//			}
		//			designElementRef = new SoftReference<DesignBase>(designElement);
		//			System.out.println("Restoring unid " + designUnid);
		//			Document doc = db.getDocumentByUNID(designUnid);
		//			if (doc != null) {
		//				((AbstractDesignBase) designElement).init(doc);
		//				return designElement;
		//			}
		//		}
		designElement.reattach(getDatabase());
		return designElement;
	}

	@Override
	public boolean delete() {
		if (!exists())
			return false;
		System.out.println("DELETE: " + getPath());
		return getDesignElement().getDocument().remove(true);

	}

	@Override
	public boolean exists() {
		return getDesignElement().getDocument() != null;
	}

	@Override
	public boolean isDirectory() {
		return false;
	}

	@Override
	public boolean isNote() {
		return true;
	}

	@Override
	public boolean isNSF() {
		return false;
	}

	@Override
	public long lastModified() {
		Document doc = getDesignElement().getDocument();
		if (doc == null) {
			return 0;
		}
		return doc.getLastModifiedDate().getTime();
	}

	@Override
	protected void init() {

	}

	@Override
	protected VFSNode newNode(final String dir) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected VFSNode newLeaf(final String name, final Void t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte[] getContent(final DxlConverter converter) throws IOException {

		DesignBase el = getDesignElement();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		el.exportDesign(converter, bos);
		byte[] bb = bos.toByteArray();
		bos.close();
		el.flush(); // discard internal caches
		return bb;
	}

	@Override
	public long getContentLength(final DxlConverter converter) {
		long docLm = lastModified();
		if (lastSeen != docLm) {
			lastSeen = docLm;
			contentLength = -1;
		}
		if (contentLength == -1) {
			try {
				contentLength = getContent(converter).length;
			} catch (IOException e) {
				e.printStackTrace();
				contentLength = 0;
			}
		}

		return contentLength;
	}

	@Override
	public void setContent(final DxlConverter converter, final byte[] newBB) throws IOException {
		DesignBase designElement = getDesignElement();
		if (newBB.length > 0) {
			ByteArrayInputStream bis = new ByteArrayInputStream(newBB);
			designElement.importDesign(converter, bis);
			bis.close();
		}

		// Adjust name, as it might change due a raw import
		if (designElement instanceof DesignBaseNamed) {
			String fullName = getPathInNSF();
			fullName = fullName.substring(designElement.getMapping().getOnDiskFolder().length());
			if (fullName.startsWith("/"))
				fullName = fullName.substring(1);
			((DesignBaseNamed) designElement).setName(fullName);
		}
		designElement.save(converter);
		designElement.getDocument().sign();
		designElement.flush();
		contentLength = newBB.length;
		refresh();
	}

}
