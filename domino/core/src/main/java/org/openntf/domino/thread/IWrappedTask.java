package org.openntf.domino.thread;

import java.util.Observer;

public interface IWrappedTask {

	void addObserver(Observer o);

	void stop();

	String getDescription();

}
