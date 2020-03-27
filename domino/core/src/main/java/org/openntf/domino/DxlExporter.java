/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino;

import java.util.Vector;

import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface DxlExporter.
 */
public interface DxlExporter extends Base<lotus.domino.DxlExporter>, lotus.domino.DxlExporter, org.openntf.domino.ext.DxlExporter,
		SessionDescendant {

	public static class Schema extends FactorySchema<DxlExporter, lotus.domino.DxlExporter, Session> {
		@Override
		public Class<DxlExporter> typeClass() {
			return DxlExporter.class;
		}

		@Override
		public Class<lotus.domino.DxlExporter> delegateClass() {
			return lotus.domino.DxlExporter.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Enum to allow easy access to Rich Text options.
	 * 
	 * @since org.openntf.domino 4.5.0
	 */
	public static enum RichTextOption {
		DXL(DxlExporter.DXLRICHTEXTOPTION_DXL), RAW(DxlExporter.DXLRICHTEXTOPTION_RAW);

		/** The value_. */
		private final int value_;

		/**
		 * Instantiates a new dB option.
		 * 
		 * @param value
		 *            the value
		 */
		private RichTextOption(final int value) {
			value_ = value;
		}

		/**
		 * Gets the value.
		 * 
		 * @return the value
		 */
		public int getValue() {
			return value_;
		}

		public static RichTextOption valueOf(final int value) {
			for (RichTextOption opt : values()) {
				if (opt.getValue() == value) {
					return opt;
				}
			}
			return null;
		}
	}

	/**
	 * Enum to allow easy access to MIME options
	 * 
	 * @since org.openntf.domino 4.5.0
	 */
	public static enum MIMEOption {
		DXL(DxlExporter.DXLMIMEOPTION_DXL), RAW(DxlExporter.DXLMIMEOPTION_RAW);

		/** The value_. */
		private final int value_;

		/**
		 * Instantiates a new dB option.
		 * 
		 * @param value
		 *            the value
		 */
		private MIMEOption(final int value) {
			value_ = value;
		}

		/**
		 * Gets the value.
		 * 
		 * @return the value
		 */
		public int getValue() {
			return value_;
		}

		public static MIMEOption valueOf(final int value) {
			for (MIMEOption opt : values()) {
				if (opt.getValue() == value) {
					return opt;
				}
			}
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#exportDxl(lotus.domino.Database)
	 */
	@Override
	public String exportDxl(final lotus.domino.Database database);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#exportDxl(lotus.domino.Document)
	 */
	@Override
	public String exportDxl(final lotus.domino.Document doc);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#exportDxl(lotus.domino.DocumentCollection)
	 */
	@Override
	public String exportDxl(final lotus.domino.DocumentCollection docs);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#exportDxl(lotus.domino.NoteCollection)
	 */
	@Override
	public String exportDxl(final lotus.domino.NoteCollection notes);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getAttachmentOmittedText()
	 */
	@Override
	public String getAttachmentOmittedText();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getConvertNotesBitmapsToGIF()
	 */
	@Override
	public boolean getConvertNotesBitmapsToGIF();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getDoctypeSYSTEM()
	 */
	@Override
	public String getDoctypeSYSTEM();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getExitOnFirstFatalError()
	 */
	@Override
	public boolean getExitOnFirstFatalError();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getForceNoteFormat()
	 */
	@Override
	public boolean getForceNoteFormat();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getLog()
	 */
	@Override
	public String getLog();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getLogComment()
	 */
	@Override
	public String getLogComment();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getMIMEOption()
	 */
	@Override
	public int getMIMEOption();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getOLEObjectOmittedText()
	 */
	@Override
	public String getOLEObjectOmittedText();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getOmitItemNames()
	 */
	@Override
	public Vector<String> getOmitItemNames();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getOmitMiscFileObjects()
	 */
	@Override
	public boolean getOmitMiscFileObjects();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getOmitOLEObjects()
	 */
	@Override
	public boolean getOmitOLEObjects();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getOmitRichtextAttachments()
	 */
	@Override
	public boolean getOmitRichtextAttachments();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getOmitRichtextPictures()
	 */
	@Override
	public boolean getOmitRichtextPictures();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getOutputDOCTYPE()
	 */
	@Override
	public boolean getOutputDOCTYPE();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getPictureOmittedText()
	 */
	@Override
	public String getPictureOmittedText();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getRestrictToItemNames()
	 */
	@Override
	public Vector<String> getRestrictToItemNames();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getRichTextOption()
	 */
	@Override
	public int getRichTextOption();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#getUncompressAttachments()
	 */
	@Override
	public boolean getUncompressAttachments();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setAttachmentOmittedText(java.lang.String)
	 */
	@Override
	public void setAttachmentOmittedText(final String replacementText);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setConvertNotesBitmapsToGIF(boolean)
	 */
	@Override
	public void setConvertNotesBitmapsToGIF(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setDoctypeSYSTEM(java.lang.String)
	 */
	@Override
	public void setDoctypeSYSTEM(final String system);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setExitOnFirstFatalError(boolean)
	 */
	@Override
	public void setExitOnFirstFatalError(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setForceNoteFormat(boolean)
	 */
	@Override
	public void setForceNoteFormat(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setLogComment(java.lang.String)
	 */
	@Override
	public void setLogComment(final String comment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setMIMEOption(int)
	 */
	@Override
	@Deprecated
	public void setMIMEOption(final int option);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setOLEObjectOmittedText(java.lang.String)
	 */
	@Override
	public void setOLEObjectOmittedText(final String replacementText);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setOmitItemNames(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void setOmitItemNames(final Vector names);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setOmitMiscFileObjects(boolean)
	 */
	@Override
	public void setOmitMiscFileObjects(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setOmitOLEObjects(boolean)
	 */
	@Override
	public void setOmitOLEObjects(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setOmitRichtextAttachments(boolean)
	 */
	@Override
	public void setOmitRichtextAttachments(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setOmitRichtextPictures(boolean)
	 */
	@Override
	public void setOmitRichtextPictures(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setOutputDOCTYPE(boolean)
	 */
	@Override
	public void setOutputDOCTYPE(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setPictureOmittedText(java.lang.String)
	 */
	@Override
	public void setPictureOmittedText(final String replacementText);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setRestrictToItemNames(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void setRestrictToItemNames(final Vector names);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setRichTextOption(int)
	 */
	@Override
	@Deprecated
	public void setRichTextOption(final int option);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DxlExporter#setUncompressAttachments(boolean)
	 */
	@Override
	public void setUncompressAttachments(final boolean flag);

}
