package org.openntf.domino.design.impl;

import java.io.File;
import java.io.IOException;

public interface HasMetadata {

	void writeOnDiskMeta(File file) throws IOException;

	void readOnDiskMeta(File file);
}
