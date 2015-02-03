package org.openntf.domino.design.impl;

import java.io.File;
import java.io.IOException;

public interface HasConfig {

	void writeOnDiskConfig(File file) throws IOException;

	void readOnDiskConfig(File file);
}
