package org.openntf.domino.thread;

import java.util.concurrent.Callable;

public interface IWrappedCallable<V> extends IWrappedTask, Callable<V> {

}