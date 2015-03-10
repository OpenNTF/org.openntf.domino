package org.openntf.domino.progress;

import java.util.Observable;
import java.util.Observer;

public abstract class ProgressObserver implements Observer {

	ProgressInfo progressInfo;
	ProgressInfo rootProgressInfo;

	public ProgressObserver(final int workTodo) {
		progressInfo = new ProgressInfo(null, 1, workTodo);
		rootProgressInfo = progressInfo;
	}

	@Override
	public void update(final Observable observable, final Object obj) {
		if (obj instanceof ProgressEvent) {
			update(observable, (ProgressEvent) obj);
		}
	}

	protected void update(final Observable observable, final ProgressEvent obj) {
		if (obj instanceof ProgressStartEvent) {
			ProgressStartEvent evt = (ProgressStartEvent) obj;
			if (progressInfo == rootProgressInfo)
				start(obj.getInfo());
			progressInfo = new ProgressInfo(progressInfo, evt.getWeight(), evt.getWork());
		} else if (obj instanceof ProgressStepEvent) {
			progressInfo.step(((ProgressStepEvent) obj).getSteps());
		} else if (obj instanceof ProgressStopEvent) {
			progressInfo.stop();
			progressInfo = progressInfo.getParent();
		}
		progress(rootProgressInfo.getProgress(), obj.getInfo());
		if (progressInfo == rootProgressInfo)
			stop(obj.getInfo());

	}

	protected abstract void stop(final String info);

	protected abstract void start(final String info);

	protected abstract void progress(final double progress, final String info);

}
