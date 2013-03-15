package org.openntf.domino.helpers;

import java.util.Date;
import java.util.HashMap;
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

	public static enum Controls {
		TARGET_SERVER, TARGET_FILEPATH, TARGET_LOOKUP_VIEW, SOURCE_KEY_FORMULA, STRATEGY, SYNC_SOURCE_FIELD, SYNC_TARGET_FIELD;
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

	public DocumentSyncHelper(Document controlDoc, Map<Controls, String> controlMap) {
		if (controlMap == null)
			controlMap = new HashMap<Controls, String>();
		if (controlMap.containsKey(Controls.TARGET_SERVER)) {
			setTargetServer(controlDoc.getItemValueString(controlMap.get(Controls.TARGET_SERVER)));
		} else if (controlDoc.hasItem(Controls.TARGET_SERVER.toString())) {
			setTargetServer(controlDoc.getItemValueString(Controls.TARGET_SERVER.toString()));
		} else {
			setTargetServer(Factory.getParentDatabase(controlDoc).getServer());
		}
		if (controlMap.containsKey(Controls.TARGET_FILEPATH)) {
			setTargetFilepath(controlDoc.getItemValueString(controlMap.get(Controls.TARGET_FILEPATH)));
		} else if (controlDoc.hasItem(Controls.TARGET_FILEPATH.toString())) {
			setTargetFilepath(controlDoc.getItemValueString(Controls.TARGET_FILEPATH.toString()));
		} else {
			setTargetFilepath(Factory.getParentDatabase(controlDoc).getFilePath());
		}
		if (controlMap.containsKey(Controls.TARGET_LOOKUP_VIEW)) {
			setTargetLookupView(controlDoc.getItemValueString(controlMap.get(Controls.TARGET_LOOKUP_VIEW)));
		} else if (controlDoc.hasItem(Controls.TARGET_LOOKUP_VIEW.toString())) {
			setTargetLookupView(controlDoc.getItemValueString(Controls.TARGET_LOOKUP_VIEW.toString()));
		} else {
			setTargetLookupView("TARGET_LOOKUP_VIEW");
		}
		if (controlMap.containsKey(Controls.SOURCE_KEY_FORMULA)) {
			setSourceKeyFormula(controlDoc.getItemValueString(controlMap.get(Controls.SOURCE_KEY_FORMULA)));
		} else if (controlDoc.hasItem(Controls.SOURCE_KEY_FORMULA.toString())) {
			setSourceKeyFormula(controlDoc.getItemValueString(Controls.SOURCE_KEY_FORMULA.toString()));
		} else {
			setSourceKeyFormula("SOURCE_KEY_FORMULA"); // this would be the name of the field on the source document used for the lookup
		}
		if (controlMap.containsKey(Controls.STRATEGY)) {
			setStrategy(Strategy.valueOf(controlDoc.getItemValueString(controlMap.get(Controls.STRATEGY))));
		} else if (controlDoc.hasItem(Controls.STRATEGY.toString())) {
			setStrategy(Strategy.valueOf(controlDoc.getItemValueString(Controls.STRATEGY.toString())));
		} else {
			setStrategy(Strategy.CREATE_AND_REPLACE);
		}
		Map<String, String> syncMap = new HashMap<String, String>();
		if (controlMap.containsKey(Controls.SYNC_SOURCE_FIELD) && controlMap.containsKey(Controls.SYNC_TARGET_FIELD)) {
			java.util.Vector<Object> keyVec = controlDoc.getItemValue(controlMap.get(Controls.SYNC_SOURCE_FIELD));
			java.util.Vector<Object> valueVec = controlDoc.getItemValue(controlMap.get(Controls.SYNC_TARGET_FIELD));
			int i = 0;
			for (Object key : keyVec) {
				syncMap.put((String) key, (String) valueVec.get(i));
				i++;
			}
		} else if (controlDoc.hasItem(Controls.SYNC_SOURCE_FIELD.toString()) && controlDoc.hasItem(Controls.SYNC_TARGET_FIELD.toString())) {
			java.util.Vector<Object> keyVec = controlDoc.getItemValue(Controls.SYNC_SOURCE_FIELD.toString());
			java.util.Vector<Object> valueVec = controlDoc.getItemValue(Controls.SYNC_TARGET_FIELD.toString());
			int i = 0;
			for (Object key : keyVec) {
				syncMap.put((String) key, (String) valueVec.get(i));
				i++;
			}
		} else {
			// TODO some default sync mappping, perhaps dynamic
		}
		setSyncMap(syncMap);
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
