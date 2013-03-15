package org.openntf.domino.helpers;

import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Item;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.utils.Factory;

public class DocumentSyncHelper {
	private static final Logger log_ = Logger.getLogger(DocumentSyncHelper.class.getName());

	public static enum Strategy {
		REPLACE_IF_NEWER, CREATE_AND_REPLACE, REPLACE_ONLY
	}

	private String targetServer_;
	private String targetFilepath_;
	private String targetLookupView_;
	private String sourceKeyFormula_;
	private Strategy strategy_;
	private Map<String, String> syncMap_;

	public DocumentSyncHelper() {
		// TODO allow for constructor arguments to configure
	}

	public DocumentSyncHelper(Strategy strategy, Map<String, String> syncMap, String... args) {
		setStrategy(strategy);
		setSyncMap(syncMap);
		if (args.length >= 1) {
			setTargetServer(args[0]);
		}
		if (args.length >= 2) {
			setTargetFilepath(args[1]);
		}
		if (args.length >= 3) {
			setTargetLookupView(args[2]);
		}
		if (args.length >= 4) {
			setSourceKeyFormula(args[3]);
		}

	}

	public void process(DocumentCollection coll) {
		// TODO Check to make sure properties are all set up before running
		Session session = org.openntf.domino.utils.Factory.getSession(coll);
		Database targetDb = session.getDatabase(getTargetServer(), getTargetFilepath());
		View targetView = targetDb.getView(getTargetLookupView());
		Strategy strategy = getStrategy();
		for (Document source : coll) {
			Date sourceLastMod = source.getLastModified().toJavaDate();
			Object lookupKey = Factory.wrappedEvaluate(session, getSourceKeyFormula(), source);
			DocumentCollection targetColl = targetView.getAllDocumentsByKey(lookupKey, true);
			for (Document target : targetColl) {
				boolean targetDirty = false;
				for (Map.Entry<String, String> entry : getSyncMap().entrySet()) {
					String targetItemName = entry.getValue();
					java.util.Vector<Object> sourceValue = Factory.wrappedEvaluate(session, entry.getKey(), source);
					if (strategy == Strategy.CREATE_AND_REPLACE) {
						target.replaceItemValue(targetItemName, sourceValue);
						targetDirty = true;
					} else {
						Item targetItem = target.getFirstItem(targetItemName);
						if (strategy == Strategy.REPLACE_IF_NEWER) {
							Date itemLastMod = targetItem.getLastModified().toJavaDate();
							if (sourceLastMod.after(itemLastMod)) {
								targetItem.setValues(sourceValue);
								targetDirty = true;
							}
						} else if (strategy == Strategy.REPLACE_ONLY) {
							if (targetItem != null) {
								targetItem.setValues(sourceValue);
								targetDirty = true;
							}
						}
					}
				}
				if (targetDirty) {
					target.save();
				}
			}
		}
	}

	public String getTargetServer() {
		return targetServer_;
	}

	public void setTargetServer(String targetServer) {
		targetServer_ = targetServer;
	}

	public String getTargetFilepath() {
		return targetFilepath_;
	}

	public void setTargetFilepath(String targetFilepath) {
		targetFilepath_ = targetFilepath;
	}

	public String getTargetLookupView() {
		return targetLookupView_;
	}

	public void setTargetLookupView(String targetLookupView) {
		targetLookupView_ = targetLookupView;
	}

	public String getSourceKeyFormula() {
		return sourceKeyFormula_;
	}

	public void setSourceKeyFormula(String sourceKeyFormula) {
		sourceKeyFormula_ = sourceKeyFormula;
	}

	public Strategy getStrategy() {
		return strategy_;
	}

	public void setStrategy(Strategy strategy) {
		strategy_ = strategy;
	}

	public Map<String, String> getSyncMap() {
		return syncMap_;
	}

	public void setSyncMap(Map<String, String> syncMap) {
		syncMap_ = syncMap;
	}

}
