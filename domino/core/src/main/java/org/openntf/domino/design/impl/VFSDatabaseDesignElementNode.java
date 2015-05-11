package org.openntf.domino.design.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.design.DesignBase;
import org.openntf.domino.design.DesignBaseNamed;
import org.openntf.domino.design.DxlConverter;
import org.openntf.domino.design.VFSNode;
import org.openntf.domino.exceptions.OpenNTFNotesException;
import org.openntf.domino.utils.Factory.SessionType;
import org.xml.sax.SAXParseException;

/**
 * This node represents a DesignElement node. You can read/write it's content
 * 
 * TODO: There is ONE file representation per Design-Document. It does not yet support XSP-Config or MetaData files!
 * 
 * @author Roland Praml, FOCONIS AG
 *
 */
public class VFSDatabaseDesignElementNode extends VFSAbstractNode<Void> {
	public static final Logger log_ = Logger.getLogger(VFSDatabaseDesignElementNode.class.getName());
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
		return SessionType.CURRENT.get().getDatabase(getVFSDatabaseNode().getPath());
	}

	protected synchronized DesignBase getDesignElement() {
		designElement.reattach(getDatabase());
		return designElement;
	}

	@Override
	public boolean delete() {
		if (!exists())
			return false;
		if (log_.isLoggable(Level.INFO)) {
			log_.info("VFS Delete: " + getPath());
		}
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
	public boolean isDatabase() {
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
	protected VFSNode newNode(final String dir) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected VFSNode newLeaf(final String name, final Void t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getContent(final DxlConverter converter, final OutputStream os) throws IOException {
		DesignBase el = getDesignElement();
		el.exportDesign(converter, os);
		el.flush(); // discard internal caches
	}

	@Override
	public long getContentLength(final DxlConverter converter) {
		long docLm = lastModified();
		if (lastSeen != docLm) {
			lastSeen = docLm;
			contentLength = -1;
		}
		if (contentLength == -1) {
			DesignBase el = getDesignElement();
			contentLength = el.getExportSize(converter);
			el.flush();
		}

		return contentLength;
	}

	@Override
	public void setContent(final DxlConverter converter, final InputStream is, final boolean sign) throws IOException {
		DesignBase designElement = getDesignElement();
		try {
			designElement.importDesign(converter, is);
		} catch (OpenNTFNotesException e) {
			Throwable cause = e.getCause();
			if (cause instanceof SAXParseException) {
				SAXParseException spe = (SAXParseException) cause;
				System.out.println(spe);
			}
		}

		// Adjust name, as it might change due a raw import
		if (designElement instanceof DesignBaseNamed) {
			String fullName = getRelativePath();
			fullName = fullName.substring(designElement.getMapping().getOnDiskFolder().length());
			if (fullName.startsWith("/"))
				fullName = fullName.substring(1);
			((DesignBaseNamed) designElement).setName(fullName);
		}
		designElement.save(converter);
		designElement.flush();
		if (sign) {
			Document doc = designElement.getDocument();
			doc.sign();
			doc.save();
		}
		contentLength = -1; // TODO set from stream!
		getVFSDatabaseNode().refresh();
	}

}
